package me.smartco.akstore.store.rest

import akka.actor._
import me.smartco.akstore.integration.ServiceFacade
import me.smartco.akstore.store.service.MallService
import me.smartco.akstore.store.spring.Bean
import me.smartco.akstore.user.model.User
import spray.http.HttpCookie
import spray.routing.authentication.UserPass

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by libin on 14-11-10.
 */
object Authenticator extends App {
  def userPassAuthenticator(userPass: Option[UserPass])(implicit context: ActorContext,ec: ExecutionContext,tokenCookie:Option[HttpCookie]): Future[Option[User]] =
    Future {
      val facade=Bean[ServiceFacade]
      val userService=facade.getUserService
      tokenCookie match {
        case Some(HttpCookie(_,content,_,_,_,_,_,_,_)) =>
          Option(userService.getUserRepository.findByTokenAndActive(content, true))
        case None =>
          userPass.map(up=> userService.getUserRepository.findByUsernameAndTokenAndActive(up.user,up.pass,true))
      }
    }
}

class Authenticator[String] {

}

