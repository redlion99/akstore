package me.smartco.akstore.biz;

import com.mongodb.*;
import me.smartco.akstore.biz.spring.BizConfiguration;
import me.smartco.akstore.common.spring.MongoConfiguration;
import me.smartco.akstore.transaction.spring.JpaConfiguration;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertThat;

/**
 * Created by libin on 14-11-7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MongoConfiguration.class, JpaConfiguration.class, BizConfiguration.class})
public abstract class AbstractIntegrationTest {

    //protected ApplicationContext context= new AnnotationConfigApplicationContext(ApplicationConfig.class);
    //protected ApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");

    @Autowired
    Mongo mongo;

    @Before
    public void setUp() {

        //MongoDbFactory factory=(MongoDbFactory)context.getBean("mongoDbFactory");

        //DB database = factory.getDb();
        DB database=mongo.getDB("estore");

        // Customers

        DBCollection customers = database.getCollection("customer");


        // Products

        DBCollection products = database.getCollection("product");


        // Orders

        DBCollection orders = database.getCollection("order");
        //orders.drop();

        // Line items


    }
}
