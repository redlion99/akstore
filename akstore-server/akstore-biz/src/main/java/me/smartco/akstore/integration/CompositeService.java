package me.smartco.akstore.integration;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import me.smartco.akstore.common.actor.ActorSystemFactory;
import me.smartco.akstore.common.event.order.CompletedEvent;
import me.smartco.akstore.common.event.order.OrderEvent;
import me.smartco.akstore.common.model.*;
import me.smartco.akstore.common.util.GPSUtil;
import me.smartco.akstore.exception.ShopTooFarException;
import me.smartco.akstore.store.mongodb.mall.*;
import me.smartco.akstore.store.mongodb.mall.promotion.Discount;
import me.smartco.akstore.store.mongodb.market.DispatchProduct;
import me.smartco.akstore.store.mongodb.partner.Shop;
import me.smartco.akstore.store.service.MallService;
import me.smartco.akstore.store.service.PartnerService;
import me.smartco.akstore.transaction.service.TransactionService;
import me.smartco.akstore.exception.CodeValidateFailedException;
import me.smartco.akstore.transaction.model.LineItem;
import me.smartco.akstore.transaction.model.OrderEntity;
import me.smartco.akstore.transaction.model.OrderGroup;
import me.smartco.akstore.transaction.model.OrderHistory;
import me.smartco.akstore.transaction.repository.BillDAO;
import me.smartco.akstore.transaction.repository.BillFlowDAO;
import me.smartco.akstore.user.model.User;
import me.smartco.akstore.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by libin on 15-2-9.
 */

public class CompositeService {
    private UserService userService;
    private MallService mallService;
    private PartnerService partnerService;
    private TransactionService transactionService;


    private Logger logger= LoggerFactory.getLogger(CompositeService.class);

    public CompositeService(UserService userService, MallService mallService, PartnerService partnerService, TransactionService transactionService) {
        this.userService = userService;
        this.mallService = mallService;
        this.partnerService = partnerService;
        this.transactionService = transactionService;
    }

    public Customer register(String username,String password,String email,String mobile,String validateCode) throws CodeValidateFailedException {
        validateCode=userService.getValidateCode(username);
        User user=userService.register(username, password, "Customer", validateCode);
        if(null!=user){
            Customer customer =mallService.getCustomerByUserID(user.getId());
            if(null==customer) {
                customer = new Customer(user.getId(),user.getUsername());
                customer.contact().setPhone(mobile);
                if (null != email) {
                    customer.contact().setEmail(new EmailAddress(email));
                }
                mallService.getCustomerRepository().save(customer);
            }
            userService.getAvailableToken(user);
            return customer;
        }else {
            return null;
        }
    }


    public User registerStaff(String username, String password, String validateCode) throws CodeValidateFailedException {
        return userService.register(username, password, "Staff", validateCode);
    }

    public void aggOrders() throws SQLException {
        logger.info("Aggregation for orders");
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH,-1);
        List results=transactionService.aggOrdersBefore(calendar.getTime());
        Iterator<ResultSet> it=results.iterator();
        while (it.hasNext()){
            ResultSet gr=it.next();
            Product product= productRepository().findOne(gr.getString("productId"));
            product.setSold(gr.getInt("sold"));
            productRepository().save(product);
        }
    }

    public void aggDeliveredOrders(){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-7);
        Page<OrderEntity> orderEntities=transactionService.findByStatusAndUpdateTimeLessThan(OrderStatus.delivered,calendar.getTime(), AbstractDocument.pageRequest(0));
        Iterator<OrderEntity> it =orderEntities.iterator();
        while (it.hasNext()){
            OrderEntity orderEntity=it.next();
            for(LineItem lineItem:orderEntity.lineItems()){
                Comment comment=new Comment(customerRepository().findOne(orderEntity.getCustomerId()),productRepository().findOne(lineItem.getProductId()),5,"");
                commentRepository().save(comment);
            }
            orderEntity.setStatus(OrderStatus.completed);
            OrderHistory orderHistory=new OrderHistory(orderEntity);
            transactionService.getOrderHistoryDAO().save(orderHistory);
            sendTaskMessage(new CompletedEvent(orderEntity.getId()));
        }
    }

    public Page<OrderEntity> searchOrder(Shop shop,Date dateStart,Date dateEnd,Pageable page){
        if(null!=shop&&null!=dateStart&&null!=dateEnd){
            return transactionService.getOrderDAO().findByShopIdAndCreateTimeBetween(shop.getId(), dateStart, dateEnd, page);
        } else if(null!=shop&&null!=dateStart){
            return transactionService.getOrderDAO().findByShopIdAndCreateTimeGreaterThan(shop.getId(), dateStart, page);
        }else if(null!=shop&&null!=dateEnd){
            return transactionService.getOrderDAO().findByShopIdAndCreateTimeLessThan(shop.getId(), dateEnd, page);
        }else if(null!=shop){
            return transactionService.getOrderDAO().findByShopId(shop.getId(), page);
        }
        return new PageImpl<OrderEntity>(new ArrayList<OrderEntity>());
    }

    public OrderHistory createOrderHistory(OrderEntity orderEntity){
        OrderHistory orderHistory=new OrderHistory(orderEntity);
        sendTaskMessage(OrderEvent.create(orderEntity.getStatus(), orderEntity.getId(), null));
        return transactionService.getOrderHistoryDAO().save(orderHistory);
    }



    public Map<DispatchProduct,Integer> elementOfOrder(OrderEntity orderEntity) {
        Map<DispatchProduct,Integer> elementSet=new HashMap<DispatchProduct, Integer>();
        for(LineItem lineItem:orderEntity.lineItems()){
            int amount=lineItem.getAmount();
            Product product=productRepository().findOne(lineItem.getProductId());
            for(ProductElement productElement:product.elementSet()){
                DispatchProduct dispatchProduct=productElement.getProduct();
                if(elementSet.containsKey(dispatchProduct)){
                    elementSet.put(dispatchProduct,elementSet.get(dispatchProduct)+productElement.getQuantity()*amount);
                }else {
                    elementSet.put(dispatchProduct,productElement.getQuantity()*amount);
                }
                //elementSet.add(new ProductElement(productElement.product(),productElement.getQuantity()*amount));
            }
        }
        return elementSet;
    }

    public BigDecimal getOrderPriceFromCart(Cart cart,Shipping shipping){
        BigDecimal total = BigDecimal.ZERO;
        for(CartItemGroup group:cart.getGroups()){
            for(CartItem item:group.getCartItems()){
                total.add(item.getTotal());
            }
        }
        return total;
    }

    @Transactional
    public Map<String,OrderEntity> initOrderGroupFromCart(Cart cart, Shipping shipping,PaymentType paymentType) throws ShopTooFarException {
        Customer customer=cart.getCustomer();
        //check the distance between the shop and the customer
        for(CartItemGroup group:cart.getGroups()){
            for(CartItem item:group.getCartItems()){
                Location a=group.shop().getLocation();
                if(null!=a&&null!=shipping.getLocation()){
                    double distance= GPSUtil.distanceBetween(a, shipping.getLocation());
                    logger.info("distance: "+distance);
                    if(distance>group.shop().getMaxServeDistance()*10000){
                        logger.error("distance: "+distance+shipping.getLocation()+a);
                        throw new ShopTooFarException(group.shop().getName()+" : "+distance);
                    }
                }else{
                    //throw new ShopTooFarException(group.shop().getName());
                }
            }
        }
        OrderGroup orderGroup=new OrderGroup(cart.getCustomer().getId());
        transactionService.getOrderGroupDAO().save(orderGroup);

        Map<String,OrderEntity> ordersByShop=new HashMap<String, OrderEntity>();
        for(CartItemGroup group:cart.getGroups()){
            Shop shop=group.shop();
            OrderEntity orderEntity =new OrderEntity();
            orderEntity.setCustomerId(customer.getId());
            orderEntity.setOrderGroup(orderGroup);
            orderEntity.setShopId(group.getShopId());
            orderEntity.setPartnerId(shop.partner().getId());
            orderEntity.setShippingId(shipping.getId());
            orderEntity.setShopPhone(shop.getContact().getPhone());
            orderEntity.setShopName(shop.getName());
            orderEntity.setReceiver(shipping.getReceiver());
            orderEntity.setReceiverPhone(shipping.getPhone());
            if(null!=shop.getAddress())
                orderEntity.setShopAddress(shop.getAddress().toString());
            orderEntity.setShippingAddress(shipping.getShippingAddress().getString());
            orderEntity.setPaymentType(paymentType);
            transactionService.getOrderDAO().save(orderEntity);
            OrderHistory orderHistory=new OrderHistory(orderEntity);
            transactionService.getOrderHistoryDAO().save(orderHistory);
            for(CartItem item:group.getCartItems()){
                Product product=item.getProduct();
                LineItem lineItem=new LineItem(orderEntity);
                lineItem.setProductId(product.getId());
                lineItem.setProductName(product.getName());
                lineItem.setProductPrice(product.getSalePrice());
                lineItem.setProductRemark(product.getRemark());
                lineItem.setAmount(item.getAmount());
                lineItem.setPictureUrl(product.getPictureUrl());
                if(null!=item.getReferee())
                    lineItem.setRefereeId(item.getReferee().getId());
                transactionService.getLineItemDAO().save(lineItem);
            }


            //orderEntity =orderDAO.getOne(orderEntity.getId());
            orderEntity.setTotal(calTotalForOrder(orderEntity,shipping));
            transactionService.getOrderDAO().save(orderEntity);
            ordersByShop.put(group.shop().getId(), orderEntity);
        }
        return ordersByShop;
    }

    public Set<Discount> getDiscountSetForOrder(OrderEntity orderEntity){
        return new HashSet<Discount>();
    }

    public BigDecimal calTotalForOrder(OrderEntity orderEntity,Shipping shipping){
        BigDecimal total = BigDecimal.ZERO;

        for (LineItem item : orderEntity.lineItems()) {
            total = total.add(item.getTotal());
        }

        for(Discount discount : getDiscountSetForOrder(orderEntity)){
            total=total.subtract(discount.getAmount());
        }

        if(total.doubleValue()<0.0){
            total=BigDecimal.ZERO;
        }
        total.add(shipping.getShippingFee());

        return total;
    }


    private CommentRepository commentRepository() {
        return mallService.getCommentRepository();
    }

    private CustomerRepository customerRepository() {
        return mallService.getCustomerRepository();
    }

    public ProductRepository productRepository() {
        return mallService.getProductRepository();
    }

    public BillDAO getBillRepository() {
        return transactionService.getBillDAO();
    }


    public BillFlowDAO getBillFlowRepository() {
        return transactionService.getBillFlowRepository();
    }

    public void sendTaskMessage(Object message){
        ActorSelection taskActor= ActorSystemFactory.getActorSystem().actorSelection("/user/task");
        taskActor.tell(message, ActorRef.noSender());
    }


    public void aggComments() {
        mallService.aggComments();
    }
}
