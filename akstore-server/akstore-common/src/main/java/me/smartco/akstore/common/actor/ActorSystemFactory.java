package me.smartco.akstore.common.actor;

import akka.actor.ActorSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by libin on 14-11-22.
 */
@Component
public class ActorSystemFactory {
    private static ActorSystem actorSystem;
    private static Logger logger= LoggerFactory.getLogger(ActorSystemFactory.class);

    @Bean
    public synchronized static ActorSystem getActorSystem(){
        if(null==actorSystem){
            logger.info("creating actor system!");
            actorSystem=ActorSystem.create("bigwin");
        }
        return actorSystem;
    }

}
