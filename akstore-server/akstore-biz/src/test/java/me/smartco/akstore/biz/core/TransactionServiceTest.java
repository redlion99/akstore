package me.smartco.akstore.biz.core;

import me.smartco.akstore.biz.AbstractIntegrationTest;
import me.smartco.akstore.biz.service.MallService;
import me.smartco.akstore.biz.service.TransactionService;
import me.smartco.akstore.common.model.PaymentType;
import me.smartco.akstore.exception.CodeValidateFailedException;
import me.smartco.akstore.exception.ShopTooFarException;
import me.smartco.akstore.common.model.Address;
import me.smartco.akstore.common.model.Shipping;
import me.smartco.akstore.integration.CompositeService;
import me.smartco.akstore.store.mongodb.mall.Cart;
import me.smartco.akstore.store.mongodb.mall.Customer;
import me.smartco.akstore.store.mongodb.mall.Product;
import me.smartco.akstore.user.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by libin on 15-1-9.
 */
public class TransactionServiceTest extends AbstractIntegrationTest {
    @Autowired
    private TransactionService transactionService;


    @Autowired
    private MallService mallManager;

    @Autowired
    private CompositeService compositeService;

    @Test
    public void testCreateOrder() throws ShopTooFarException {
        Product product= mallManager.getProductRepository().findAll().get(0);
        Assert.assertNotNull(product);
        Customer customer= null;
        try {
            customer = compositeService.register("testCreateOrder","b","a@111.com","133", mallManager.getValidateCode("testCreateOrder"));
        } catch (CodeValidateFailedException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(customer);
        Cart cart= mallManager.getCart(customer);
        cart.setItemAmount(product,1,null);
        cart.addItem(product, 1,null);
        mallManager.getCartRepository().save(cart);
        Assert.assertTrue(cart.getGroups().size()>0);
        Assert.assertTrue(cart.getTotal().doubleValue()>0);
        Shipping shipping=new Shipping(new Address("1","2","3"),"zhangshan","1234", Shipping.ShippingMethod.express);
        transactionService.initOrderGroupFromCart(cart, shipping, PaymentType.cash);
        mallManager.getCartRepository().delete(cart);
    }
}
