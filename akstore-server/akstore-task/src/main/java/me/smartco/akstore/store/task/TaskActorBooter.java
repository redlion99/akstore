package me.smartco.akstore.store.task;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import me.smartco.akstore.store.actor.TaskActor;
import me.smartco.akstore.common.actor.ActorSystemFactory;
import me.smartco.akstore.common.task.BackgroundTaskStarter;
import me.smartco.akstore.store.message.AggCommand;
import me.smartco.akstore.store.message.BuildDispatchOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.FiniteDuration;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by libin on 14-11-21.
 */
@Component
public class TaskActorBooter implements BackgroundTaskStarter,InitializingBean {
    private Logger logger= LoggerFactory.getLogger(TaskActorBooter.class);
    private ActorRef taskActor;

    public TaskActorBooter(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    public TaskActorBooter() {
        this(ActorSystemFactory.getActorSystem());
    }

    private ActorSystem actorSystem;

    public void start(){

        taskActor = actorSystem.actorOf(
                Props.create(TaskActor.class),"task"
        );
        Calendar calendar = Calendar.getInstance();
        int delay_morning=24-calendar.get(Calendar.HOUR_OF_DAY);
        int delay_afternoon=12-calendar.get(Calendar.HOUR_OF_DAY);
        if(delay_afternoon<0)
            delay_afternoon=delay_afternoon+24;

        actorSystem.scheduler().schedule(FiniteDuration.apply(delay_morning, TimeUnit.HOURS), FiniteDuration.apply(24, TimeUnit.HOURS), taskActor, new AggCommand(), actorSystem.dispatcher(), ActorRef.noSender());

        actorSystem.scheduler().schedule(FiniteDuration.apply(delay_morning, TimeUnit.HOURS), FiniteDuration.apply(24, TimeUnit.HOURS), taskActor, BuildDispatchOrder.morning(), actorSystem.dispatcher(), ActorRef.noSender());
        actorSystem.scheduler().schedule(FiniteDuration.apply(delay_afternoon, TimeUnit.HOURS), FiniteDuration.apply(24, TimeUnit.HOURS), taskActor, BuildDispatchOrder.afternoon(), actorSystem.dispatcher(), ActorRef.noSender());
        logger.info("start actor:"+taskActor.toString());
    }

    public static void main(String[] args){
        ActorSystem system = ActorSystem.create("MySystem");
        new TaskActorBooter(system).start();
    }

    public void stop() {
        if(null!=taskActor){
            //taskActor.tell(new PoisonPill());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }
}
