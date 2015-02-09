package me.smartco.akstore.biz.service;

import me.smartco.akstore.common.model.Location;
import me.smartco.akstore.common.model.PaymentType;
import me.smartco.akstore.common.util.GPSUtil;
import me.smartco.akstore.exception.ShopTooFarException;
import me.smartco.akstore.common.model.Shipping;
import me.smartco.akstore.store.mongodb.mall.*;
import me.smartco.akstore.store.mongodb.mall.promotion.Discount;
import me.smartco.akstore.store.mongodb.partner.Shop;
import me.smartco.akstore.transaction.model.*;
import me.smartco.akstore.transaction.model.LineItem;
import me.smartco.akstore.transaction.model.OrderGroup;
import me.smartco.akstore.transaction.model.OrderHistory;
import me.smartco.akstore.transaction.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by libin on 15-1-9.
 */
@Service
@Transactional(readOnly = true)
public class TransactionService  {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderGroupDAO orderGroupDAO;

    @Autowired
    private OrderHistoryDAO orderHistoryDAO;


    @Autowired
    private LineItemDAO lineItemDAO;


    @Autowired
    private BillFlowDAO billFlowRepository;

    public OrderDAO getOrderDAO() {
        return orderDAO;
    }

    public OrderGroupDAO getOrderGroupDAO() {
        return orderGroupDAO;
    }

    public OrderHistoryDAO getOrderHistoryDAO() {
        return orderHistoryDAO;
    }

    public LineItemDAO getLineItemDAO() {
        return lineItemDAO;
    }

    private Logger logger= LoggerFactory.getLogger(MallService.class);

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
        orderGroupDAO.save(orderGroup);

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
            orderDAO.save(orderEntity);
            OrderHistory orderHistory=new OrderHistory(orderEntity);
            orderHistoryDAO.save(orderHistory);
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
                lineItemDAO.save(lineItem);
            }


            //orderEntity =orderDAO.getOne(orderEntity.getId());
            orderEntity.setTotal(calTotalForOrder(orderEntity,shipping));
            orderDAO.save(orderEntity);
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

    public BigDecimal getAccountBalance(String accountId){
        Page<BillFlow> page=billFlowRepository.findByAccountId(accountId, new PageRequest(0, 1, Sort.Direction.DESC, "createTime"));
        if(page.hasContent()){
            return page.getContent().get(0).getBalance();
        }
        return BigDecimal.ZERO;
    }


    public synchronized BillFlow calculateBalanceAndSaveBillFlow(BillFlow billFlow){
        BigDecimal balance=getAccountBalance(billFlow.getAccountId());
        billFlow.setBalance(balance.add(billFlow.getAmount()));
        return billFlowRepository.save(billFlow);
    }

}
