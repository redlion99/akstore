package me.smartco.akstore.web;

import me.smartco.akstore.common.actor.ActorSystemFactory;
import me.smartco.akstore.store.task.TaskActorBooter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by libin on 14-11-21.
 */
public class TaskHandlerInitializer implements ServletContextListener {
    private TaskActorBooter taskActorStarter=new TaskActorBooter(ActorSystemFactory.getActorSystem());
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        taskActorStarter.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        taskActorStarter.stop();
    }
}
