package me.smartco.akstore.store.rest.event

import akka.actor.{ActorContext, Props, ActorSystem}
import me.smartco.akstore.common.event.Message


/**
 * Created by libin on 14-11-20.
 */
class Sender {
  def pushEvent(message: Message)(implicit context: ActorContext): Unit = {
    val taskActor = context.actorSelection("/user/task")
    taskActor ! message
  }
}

object Sender extends Sender
