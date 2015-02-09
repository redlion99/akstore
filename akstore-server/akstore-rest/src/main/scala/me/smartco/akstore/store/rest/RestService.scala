package me.smartco.akstore.store.rest

import akka.actor.{Actor, ActorContext}
import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import me.smartco.akstore.store.rest.route._
import me.smartco.akstore.store.rest.RestExceptionHandler._
import spray.http.MediaTypes._
import spray.routing.{RequestContext, HttpService}
import spray.routing.authentication.BasicAuth
import spray.routing.directives.AuthMagnet

/**
 * Created by libin on 14-11-7.
 */
class RestServiceActor extends Actor with RestService {

  def actorRefFactory = context

  def receive = runRoute(myRoute)

}

trait RestService extends HttpService with CustomerRoute with PartnerRoute with CommonRoute with PublicRoute with StaffRoute with TestRoute{

  //val simpleCache = routeCache(maxCapacity = 1000, timeToIdle = Duration("30 min"))
  implicit def executionContext = actorRefFactory.dispatcher

  implicit def mapper = new ObjectMapper

  mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)



  def myRoute(implicit context: ActorContext) = {

    pathPrefix("api") {
      optionalCookie("ut") { implicit tokenCookie =>
        val auth = BasicAuth(Authenticator.userPassAuthenticator _, realm = "secure site")
        respondWithMediaType(`application/json`) {
          handleRejections(restRejectionHandler) {
            commonRoute ~ publicRoute ~ testRoute ~
              authenticate(AuthMagnet.fromContextAuthenticator(auth)) {
                user =>
                  authorize(user!=null){
                    customerRoute(user) ~
                      authorize(user.hasRole("Staff") || user.hasRole("PartnerStaff")) {
                        partnerRoute(user)
                      } ~
                      authorize(user.hasRole("Staff")) {
                        staffRoute(user)
                      }
                  }

              }
          }
        }
      }
    } ~
      pathPrefix("static") {
        getFromResourceDirectory("src/main/webapp/static")
      }

  }
}