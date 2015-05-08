package me.smartco.akstore.biz.spring;

import me.smartco.akstore.common.spring.MongoConfiguration;
import me.smartco.akstore.transaction.spring.JpaConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by libin on 14-11-20.
 */
public class SpringUtil {
    private static SpringUtil ourInstance = new SpringUtil();

    public static SpringUtil getInstance() {
        return ourInstance;
    }

    private ApplicationContext applicationContext;
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private SpringUtil() {
        try {
            applicationContext = new AnnotationConfigApplicationContext(MongoConfiguration.class, JpaConfiguration.class, BizConfiguration.class);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    public <T> T getBean(java.lang.Class<T> aClass){
        return applicationContext.getBean(aClass);
    }
}
