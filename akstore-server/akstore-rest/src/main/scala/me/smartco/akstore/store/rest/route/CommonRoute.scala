package me.smartco.akstore.store.rest.route


import java.util

import akka.actor.ActorContext
import com.fasterxml.jackson.databind.ObjectMapper
import me.smartco.akstore.common.model.Attachment
import me.smartco.akstore.integration.ServiceFacade
import me.smartco.akstore.store.mongodb.mall.{Advertisement, AdvertisementRepository}
import me.smartco.akstore.store.service.MallService
import me.smartco.akstore.store.spring.Bean
import me.smartco.akstore.biz.conf.Configuration
import me.smartco.akstore.store.mongodb.core.AttachmentsRepository
import me.smartco.akstore.common.util.{MD5Util, ImgUtil}
import spray.http.{HttpCookie, StatusCodes, BodyPart}
import spray.routing.{AuthenticationFailedRejection, Directives, MalformedQueryParamRejection}

import scala.collection.mutable
import scala.concurrent.ExecutionContext

import spray.routing.Directives._
import me.smartco.akstore.store.rest.json.Resources._
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._

import StatusCodes._


/**
 * Created by libin on 14-11-12.
 */
trait CommonRoute {



  def commonRoute(implicit context: ActorContext, mapper: ObjectMapper, executor: ExecutionContext) = {
    val facade=Bean[ServiceFacade]
    val manager = facade.getMallService
    val userService=facade.getUserService
    path("users" / "login") {
      post {
        formFields('username, 'password) { (username, password) =>

          val user = userService.findByUsernameAndPassword(username, password)
          if (null != user) {
            val configuration = Bean[Configuration]
            val token = userService.getAvailableToken(user)
            setCookie(HttpCookie("ut", content=token,path=Some("/")),HttpCookie("uid", content=user.getId,path=Some("/"))){
              complete {
                val d = new util.HashMap[String, Object]
                d.put("token", token)
                d.put("tokenExpiredAt", Long.box(user.tokenExpiredAt.getTime))
                d.put("primaryShop", configuration.getPrimaryShop)
                if (user.hasRole("Customer")) {
                  val customer = manager.getCustomerByUserID(user.getId)
                  d.put("customer", customer)
                  d.put("shippingAddress",customer.shippingAddress())
                  d.put("shippingSet",customer.shippingSet())
                }
                if (user.hasRole("Staff")) {
                  d.put("staff", manager.getStaffByUserId(user.getId))
                  d.put("primaryPartner", configuration.getPrimaryPartner)
                }
                if (user.hasRole("PartnerStaff"))
                  d.put("partnerStaff", manager.getPartnerStaffByUserId(user.getId))
                d.toJson
              }
            }
          }
          else
            complete(Unauthorized,"username or password incorrect")

        }
      }
    } ~
    path("token"/Segment/"refresh"){ token=>
      val manager = Bean[MallService]
      get{
        val user=userService.getUserRepository.findByTokenAndActive(token,true)
        if(null==user){
          reject(AuthenticationFailedRejection(AuthenticationFailedRejection.CredentialsRejected,null))
        }else{
          complete{
            val fore=user.tokenExpiredAt.getTime-System.currentTimeMillis()<3600000*24*7
            val refreshedToken = userService.getAvailableToken(user,fore)
            val d = new util.HashMap[String, Object]
            d.put("username", user.getUsername)
            d.put("token", refreshedToken)
            d.put("tokenExpiredAt", Long.box(user.tokenExpiredAt.getTime))
            d.toJson
          }
        }
      }
    } ~
      path("customers") {
        post{
          formFields('username, 'password, 'mobile, 'code, 'email.?) { (username, password, mobile,code, email) =>
            complete {
              val compositeService=facade.getCompositeService
              val userService=facade.getUserService
                var customer = compositeService.register(username, password, email.getOrElse(null), mobile,code)
                if(null!=customer){
                  val d = new util.HashMap[String,Object]()
                  val user=userService.findById(customer.getId)
                  d.put("token",userService.getAvailableToken(user))
                  d.put("tokenExpiredAt",Long.box(user.tokenExpiredAt.getTime))
                  d.put("customer",customer)
                  customer.toJson
                }else{
                  (StatusCodes.Forbidden,"""{"error":"registerStaff failed"}""")
                }
            }
          }
        }
      } ~
      path("password"/"reset") {
        val mallManager: MallService = Bean[MallService]
        post {
          formFields('mobile, 'code,'new_pwd) { (mobile, code,new_pwd) =>
            authorize(userService.getValidateCode(mobile).equals(code)) {
              complete {
                val user=userService.getUserRepository.findByUsername(mobile)
                user.setPassword(MD5Util.MD5(new_pwd))
                userService.getUserRepository.save(user).toBriefJson
              }
            }
          }
        }
      } ~
      path("attachments") {
        post {
          formFields('file.as[BodyPart], 'base_size.as[Int]?52, 'extra_size.as[Int]?0) { (file,baseSize,extraSize) =>
            complete {
              val repo = Bean[AttachmentsRepository]
              var attr = repo.save(new Attachment(file.entity.data.toByteArray,Attachment.Type.jpg,baseSize,extraSize))
              repo.save(attr).toJson
            }
          }
        }
      } ~
      path("validate"/"code"/Segment) {
        mobile =>
        complete{
          val manager = Bean[MallService]
          manager.getValidateCode(mobile)
          """{"result":true}"""
        }
      } ~
    path("settings"/"primary_shop"){
      get{
        complete{
          val configuration = Bean[Configuration]
          configuration.getPrimaryShop.toBriefJson
        }
      }
    } ~
    path("advertisements"){
      get{
        complete{
          val repo = Bean[AdvertisementRepository]
          repo.findByActive(true,Advertisement.getDefaultPageable(0))
        }
      }
    }


  }
}
