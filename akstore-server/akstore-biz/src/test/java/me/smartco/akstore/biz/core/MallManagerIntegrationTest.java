package me.smartco.akstore.biz.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.smartco.akstore.common.model.Views;
import me.smartco.akstore.integration.CompositeService;
import me.smartco.akstore.common.spring.BriefContentPage;
import me.smartco.akstore.biz.conf.Configuration;
import me.smartco.akstore.exception.CodeValidateFailedException;
import me.smartco.akstore.exception.ShopTooFarException;
import me.smartco.akstore.store.service.MallService;
import me.smartco.akstore.biz.AbstractIntegrationTest;
import me.smartco.akstore.store.mongodb.mall.*;
import me.smartco.akstore.user.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by libin on 14-11-7.
 */
public class MallManagerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    CustomerRepository repository;


    @Autowired
    private MallService mallManager;

    @Autowired
    private Configuration configuration;

    @Autowired
    private UserService userService;

    @Autowired
    private CompositeService compositeService;


    public void testRegister(){
        try {
            userService.register("a","b",null,"13598765678");
        } catch (CodeValidateFailedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateComment() throws ShopTooFarException {
        Product product= mallManager.getProductRepository().findAll().get(0);
        Assert.assertNotNull(product);
        Customer customer= null;
        try {
            customer = compositeService.register("testCreateOrder","b","a@111.com","133", userService.getValidateCode("testCreateOrder"));
        } catch (CodeValidateFailedException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(customer);
//        List<Order> orderList=mallManager.getOrderRepository().findByCustomer(customer, AbstractDocument.pageRequest(1)).getContent();
//        //Assert.assertTrue(orderList.size()>0);
//          //  Order order=orderList.get(0);
//        Order order=mallManager.getOrderRepository().findOne("201501055156453453831818");
//                Iterator<LineItem> it= order.lineItems().iterator();
//                while (it.hasNext()){
//                    Comment comment=new Comment(customer,it.next().product(),new Random().nextInt(4)+2,"test");
//                    mallManager.getCommentRepository().save(comment);
//                }

    }

    @Test
    public void findProducts() throws IOException {

        Page<Product> productShorts= new BriefContentPage<Product>(mallManager.getProductRepository().findByActive(true,Product.getDefaultPageable(0)));
        Assert.assertNotNull(productShorts.getContent());
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.writerWithView(Views.Brief.class).writeValue(System.out, productShorts);

    }

    @Test
    public void testGroup() throws SQLException {
        mallManager.aggComments();
        compositeService.aggOrders();
    }

}
