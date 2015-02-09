package me.smartco.akstore.store.spring

import akka.actor.{Props, Actor}
import me.smartco.akstore.biz.spring.SpringUtil
import me.smartco.akstore.common.spring.MongoConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * Created by libin on 14-11-7.
 */

case class Start()

case class Stop()

case class GetBean(value: Class[_])

case class LookupBean(value: Class[_])

class SpringActor extends Actor{
  //protected ApplicationContext context= new AnnotationConfigApplicationContext(ApplicationConfig.class);
  var applicationContext:ApplicationContext = _
  def receive = {
    case Start() =>
      applicationContext = SpringUtil.getInstance().getApplicationContext
      var beanLookup=context.actorOf(
        props = Props(new BeanLookupActor(applicationContext)),
        name = "beanLookup"
      )
      println(beanLookup)
    case Stop() =>

  }
}

class BeanLookupActor(applicationContext: ApplicationContext)
  extends Actor {

  def receive = {
    case LookupBean(beanType) =>
      sender ! applicationContext.getBean(beanType)
  }
}


