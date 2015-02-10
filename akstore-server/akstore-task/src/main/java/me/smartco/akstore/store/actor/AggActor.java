package me.smartco.akstore.store.actor;

import akka.actor.UntypedActor;
import me.smartco.akstore.integration.CompositeService;
import me.smartco.akstore.integration.ServiceFacade;
import me.smartco.akstore.store.message.AggCommand;
import me.smartco.akstore.biz.spring.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by libin on 15-1-5.
 */
public class AggActor extends UntypedActor {
    private Logger logger= LoggerFactory.getLogger(AggActor.class);

    @Override
    public void onReceive(Object message) throws Exception {
        ServiceFacade serviceFacade=SpringUtil.getInstance().getBean(ServiceFacade.class);
        CompositeService compositeService=serviceFacade.getCompositeService();
        if(message instanceof AggCommand){
            logger.info("run Aggregation");
            compositeService.aggComments();
            compositeService.aggOrders();
        }

    }
}
