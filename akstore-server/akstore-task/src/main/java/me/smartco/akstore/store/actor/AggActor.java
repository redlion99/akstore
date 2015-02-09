package me.smartco.akstore.store.actor;

import akka.actor.UntypedActor;
import me.smartco.akstore.biz.service.MallService;
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
        if(message instanceof AggCommand){
            logger.info("run Aggregation");
            MallService mallManager=SpringUtil.getInstance().getBean(MallService.class);
            mallManager.aggComments();
            mallManager.aggOrders();
        }

    }
}
