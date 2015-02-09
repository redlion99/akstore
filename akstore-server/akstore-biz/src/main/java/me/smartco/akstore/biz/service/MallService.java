package me.smartco.akstore.biz.service;

import me.smartco.akstore.common.model.*;
import me.smartco.akstore.common.event.order.CompletedEvent;
import me.smartco.akstore.transaction.model.OrderHistory;
import me.smartco.akstore.common.model.OrderStatus;
import me.smartco.akstore.transaction.model.*;
import me.smartco.akstore.store.mongodb.core.*;
import me.smartco.akstore.store.mongodb.mall.*;
import me.smartco.akstore.store.mongodb.partner.Shop;
import me.smartco.akstore.store.mongodb.partner.ShopRepository;
import me.smartco.akstore.transaction.model.LineItem;
import me.smartco.akstore.transaction.repository.LineItemDAO;
import me.smartco.akstore.transaction.repository.OrderDAO;
import me.smartco.akstore.transaction.repository.OrderHistoryDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by libin on 14-11-10.
 */
@Component
public class MallService extends AbstractService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private DeliverTaskRepository deliverTaskRepository;


    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CommentRepository commentRepository;


    @Autowired
    private LineItemDAO lineItemDAO;

    @Autowired
    private RefereeRepository refereeRepository;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderHistoryDAO orderHistoryDAO;


    private Logger logger= LoggerFactory.getLogger(MallService.class);



    public Page<Shop> findShopByLocationWithin(Double lat, Double lng, Double distance, int page){
        Circle circle=new Circle(new Point(lat,lng), new Distance(distance, Metrics.KILOMETERS));
        return shopRepository.findByLocationWithinAndActive(circle, true, Shop.getDefaultPageable(page));
    }

    public Cart getCart(Customer customer){
        Cart cart=cartRepository.findOneByCustomer(customer);
        if(null==cart){
            cart=new Cart(customer);
            cartRepository.save(cart);
        }
        return cart;
    }


//    public DeliverTask createDeliverTaskForOrder(Order order,Staff staff){
//        DeliverTask deliverTask= new DeliverTask(order,order.getLocation());
//        deliverTask.setAssignee(staff);
//        return deliverTaskRepository.save(deliverTask);
//    }
//
//    public DeliverTask assignDeliverTask(Order order){
//        Location location=order.getLocation();
//        Date date=new Date(System.currentTimeMillis()-60000);
//        List<Staff> staffList= getStaffRepository().findByLocationNearAndUpdatedAtGreaterThan(new Point(location.getLat(),location.getLng()),date);
//        if(staffList!=null&&staffList.size()>0){
//           return createDeliverTaskForOrder(order,staffList.get(0));
//        }
//        return null;
//    }

    public Page<Product> findProductByLocationWithin(Boolean feature,Double lat,Double lng,Double distance,int page){

        Circle circle=new Circle(new Point(lat,lng), new Distance(distance, Metrics.KILOMETERS));
        if(feature==true)
            return productRepository.findByActiveAndFeatureAndLocationWithin(true, true, circle, Product.getDefaultPageable(page));
        else
            return productRepository.findByActiveAndLocationWithin(true, circle, Product.getDefaultPageable(page));

    }

    public Page<Product> findProductByShop(String shopId, int page){
        return productRepository.findByShopAndActive(shopRepository.findOne(shopId),true,Product.getDefaultPageable(page));
    }

    public CartRepository getCartRepository() {
        return cartRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }


    public DeliverTaskRepository getDeliverTaskRepository() {
        return deliverTaskRepository;
    }

    public CommentRepository getCommentRepository() {
        return commentRepository;
    }

    public ShopRepository getShopRepository() {
        return shopRepository;
    }


    public void aggComments(){
        logger.info("Aggregation for comments");
        GroupByResults<GroupResult> results=getMongoTemplate().group("comment", GroupBy.key("shop").initialDocument("{ count: 0,total:0,avg:0,positive:0 }").reduceFunction("function(doc, out) { out.count += 1;out.total+=doc.rate;if(doc.rate>3)out.positive+=1;}").finalizeFunction("function(out){ out.avg = out.total / out.count }"),GroupResult.class);
        results.getRawResults();
        Iterator<GroupResult> it=results.iterator();
        while (it.hasNext()){
            GroupResult gr=it.next();
            Shop shop=gr.getShop();
            shop.setCommentCount(gr.getCount());
            shop.setRate((int)Math.round(gr.getAvg()));
            shop.setPositiveComments(gr.getPositive());
            getShopRepository().save(shop);
        }
    }

    public void aggOrders() throws SQLException {
        logger.info("Aggregation for orders");
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH,-1);
        List results=lineItemDAO.aggOrdersBefore(calendar.getTime());
        Iterator<ResultSet> it=results.iterator();
        while (it.hasNext()){
            ResultSet gr=it.next();
            Product product=productRepository.findOne(gr.getString("productId"));
            product.setSold(gr.getInt("sold"));
            getProductRepository().save(product);
        }
    }

    public void aggDeliveredOrders(){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-7);
        Page<OrderEntity> orderEntities=orderDAO.findByStatusAndUpdateTimeLessThan(OrderStatus.delivered,calendar.getTime(), AbstractDocument.pageRequest(0));
        Iterator<OrderEntity> it =orderEntities.iterator();
        while (it.hasNext()){
            OrderEntity orderEntity=it.next();
            for(LineItem lineItem:orderEntity.lineItems()){
                Comment comment=new Comment(customerRepository.findOne(orderEntity.getCustomerId()),productRepository.findOne(lineItem.getProductId()),5,"");
                commentRepository.save(comment);
            }
            orderEntity.setStatus(OrderStatus.completed);
            OrderHistory orderHistory=new OrderHistory(orderEntity);
            orderHistoryDAO.save(orderHistory);
            sendTaskMessage(new CompletedEvent(orderEntity.getId()));
        }
    }

    public Referee getReferee(String id){
        return refereeRepository.findOne(id);
    }


}
