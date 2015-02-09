package me.smartco.akstore.store.rest

import akka.actor.Props
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import me.smartco.akstore.common.actor.ActorSystemFactory
import me.smartco.akstore.store.spring.{SpringActor, Start}
import spray.can.Http
import spray.servlet.WebBoot

import scala.concurrent.duration._

/**
 * Created by libin on 14-11-7.
 */
trait Boot {
  // we need an ActorSystem to host our application in
  implicit val system = ActorSystemFactory.getActorSystem

  system.registerOnTermination {
    // put additional cleanup code here
    system.log.info("Application shut down")
  }



  // create and start our service actor
  val serviceActor = system.actorOf(Props[RestServiceActor], "rest-service")

  println("start actor:")
  println(serviceActor)

  val springActor = system.actorOf(
    props = Props[SpringActor],
    name = "spring"
  )
  println("start actor:")
  println(springActor)




  springActor ! Start()

}

class ServletBoot extends Boot with WebBoot{

}

object AppBoot extends App with Boot{
  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ? Http.Bind(serviceActor, interface = "localhost", port = 9090)
}