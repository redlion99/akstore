package me.smartco.akstore.store.task;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Created by libin on 14-12-26.
 */
@Component
public class SingleQuartzStarter implements InitializingBean {

    private Logger logger= LoggerFactory.getLogger(SingleQuartzStarter.class);
    private Scheduler scheduler ;

    public void start() {

    }



    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }
}
