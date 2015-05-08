package me.smartco.akstore.store.spring

/**
 * Created by libin on 14-11-7.
 */

import akka.actor.ActorContext
import akka.pattern.ask
import akka.util.Timeout
import me.smartco.akstore.biz.spring.SpringUtil
import scala.concurrent.Await
import scala.concurrent.duration._

object Bean {
  private implicit val timeout = Timeout(10.seconds)
  def apply[T](implicit manifest: Manifest[T],
               context: ActorContext) = {
    val applicationContext = SpringUtil.getInstance().getApplicationContext
    applicationContext.getBean(manifest.runtimeClass).asInstanceOf[T]
//    val beanLookup =
//      context.actorSelection("/user/spring/beanLookup")
//    Await.result(
//      (beanLookup ? LookupBean(manifest.runtimeClass)).mapTo[T](manifest),
//      timeout.duration)
  }
}
