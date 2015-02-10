package me.smartco.akstore.store.rest.route

import akka.actor.ActorContext
import com.fasterxml.jackson.databind.ObjectMapper
import me.smartco.akstore.common.event.order.CreatedEvent
import me.smartco.akstore.common.model._
import me.smartco.akstore.common.spring.BriefContentPage
import me.smartco.akstore.integration.ServiceFacade
import me.smartco.akstore.exception.OrderConfirmFailedException
import me.smartco.akstore.store.mongodb.core._
import Shipping.ShippingMethod
import me.smartco.akstore.store.rest.event.Sender
import me.smartco.akstore.store.rest.json.CartItem
import me.smartco.akstore.store.spring.Bean
import me.smartco.akstore.store.mongodb.mall._
import me.smartco.akstore.common.util.MD5Util
import me.smartco.akstore.transaction.model.OrderEntity
import me.smartco.akstore.transaction.service.TransactionService
import me.smartco.akstore.user.model.User
import org.springframework.data.domain.{PageImpl, PageRequest}
import spray.http.StatusCodes
import spray.routing.PathMatchers.Segment

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import spray.routing.{Directives}

import spray.httpx.encoding._
import Directives._
import me.smartco.akstore.store.rest.json.Resources._

import spray.routing.authentication._
import StatusCodes._
import Sender._
import me.smartco.akstore.store.rest.json.JsonSupport._

/**
 * Created by libin on 14-11-12.
 */
trait CustomerRoute {


  def jsonEntity(implicit context: ActorContext, mapper: ObjectMapper, executor: ExecutionContext) = extract {
    req =>
      mapper.readTree(req.request.entity.asString)
  }

  def customerRoute(user: User)(implicit context: ActorContext, mapper: ObjectMapper, executor: ExecutionContext) = {
    val facade=Bean[ServiceFacade]
    pathPrefix("customers" / Segment) { customerId =>
      val mallManager = facade.getMallService
      val customerRepo = mallManager.getCustomerRepository

      val customer = customerRepo.findOne(customerId)
      authorize(null != user && user.getId.equals(customer.getId)) {
        pathEnd {
          get {
            complete {
              customer.toJson
            }
          } ~
            post {
              formFields('user_realname.?, 'contact_phone.?, 'avatar_id.?) {
                (_realname, contact_phone, _avatar) =>
                  complete {
                    customer.setRealname(_realname.getOrElse(customer.getRealname))
                    _avatar match {
                      case Some(avatar_id) =>
                        val avatar = mallManager.getAttachmentsRepository.findOne(avatar_id)
                        if (null != avatar)
                          customer.setAvatar(avatar)
                      case None =>
                    }
                    contact_phone match {
                      case Some(phone) =>
                        customer.contact.setPhone(phone)
                      case None =>
                    }
                    mallManager.getCustomerRepository.save(customer).toJson
                  }
              }
            }

        } ~
          path("password") {
            post {
              formFields('old_pwd, 'new_pwd) { (oldPwd, newPwd) =>
                val userService=facade.getUserService
                val u = userService.findByUsernameAndPassword(user.getUsername, oldPwd)
                authorize(null != u) {
                  complete {
                    u.setPassword(MD5Util.MD5(newPwd))
                    userService.getUserRepository.save(u).toBriefJson
                  }
                }
              }
            }
          } ~
          path("mobile") {
            post {
              formFields('mobile, 'code) { (mobile, code) =>
                authorize(mallManager.getValidateCode(user.getUsername).equals(code)) {
                  complete {
                    customer.contact.setPhone(mobile)
                    mallManager.getCustomerRepository.save(customer).toBriefJson
                  }
                }
              }
            }
          } ~
          path("address") {
            get {
              complete {
                new ShippingObject(customer.shippingAddress(), customer.shippingSet).toJson
              }
            } ~
              post {
                formFields('shipping_address_province, 'shipping_address_city, 'shipping_address_street
                  , 'shipping_receiver, 'shipping_phone, 'location_lat.as[Double], 'location_lng.as[Double]) {
                  (shipping_address_province, shipping_address_city, shipping_address_street, shipping_receiver, shipping_phone, location_lat, location_lng) =>
                    complete {
                      val address = new Address(shipping_address_street, shipping_address_city, shipping_address_province)
                      val shipping = new Shipping(address, shipping_receiver, shipping_phone, ShippingMethod.normal)
                      shipping.setLocation(new Location(location_lat, location_lng))
                      if (null == customer.shippingAddress || !customer.shippingAddress.toString.equals(shipping.toString)) {
                        customer.setShippingAddress(shipping)
                        customer.addAddress(shipping)
                        customerRepo.save(customer)
                      }
                      new ShippingObject(customer.shippingAddress(), customer.shippingSet).toBriefJson
                    }
                }
              }
          } ~
          path("follow" / "shops") {
            post {
              formFields('_shop) { (shop_id) =>
                complete {
                  val shop = mallManager.getShopRepository.findOne(shop_id)
                  customer.addFavoriteShop(shop)
                  mallManager.getCustomerRepository.save(customer)
                  new BriefContentPage(customer.favoriteShops).toBriefJson
                }
              }
            } ~
              get {
                complete {
                  new BriefContentPage(customer.favoriteShops).toBriefJson
                }
              }
          } ~
          path("unfollow" / "shops") {
            post {
              formFields('_shop) { (shop_id) =>
                complete {
                  val shop = mallManager.getShopRepository.findOne(shop_id)
                  customer.removeFavoriteShop(shop)
                  mallManager.getCustomerRepository.save(customer)
                  new BriefContentPage(customer.favoriteShops).toBriefJson
                }
              }
            }
          } ~
          path("orders") {
            val transactionService=Bean[TransactionService]
            get {
              parameters('page.as[Int] ? 0, 'ongoing.as[Boolean] ?) { (page, ongoing) =>
                val repo = transactionService.getOrderDAO
                complete {
                  ongoing match {
                    case Some(true) =>
                      repo.findByCustomerIdAndStatusLessThan(customerId, 1000, OrderEntity.getDefaultPageable(page))
                    case Some(false) =>
                      repo.findByCustomerIdAndStatusGreaterThan(customerId, 1000, OrderEntity.getDefaultPageable(page))
                    case None =>
                      repo.findByCustomerId(customerId, OrderEntity.getDefaultPageable(page))
                  }

                }
              }
            } ~
              post {
                formFields('_total.as[Double], 'shipping_address_province, 'shipping_address_city, 'shipping_address_street
                  , 'shipping_receiver, 'shipping_phone, 'location_lat.as[Double], 'location_lng.as[Double], 'payment.?) {
                  (total, shipping_address_province, shipping_address_city, shipping_address_street, shipping_receiver, shipping_phone, location_lat, location_lng, paymentOption) =>
                    val compositeService=facade.getCompositeService
                    complete {
                      val address = new Address(shipping_address_street, shipping_address_city, shipping_address_province)
                      val shipping = new Shipping(address, shipping_receiver, shipping_phone, ShippingMethod.normal)
                      shipping.setLocation(new Location(location_lat, location_lng))
                      if (null == customer.shippingAddress || !customer.shippingAddress.toString.equals(shipping.toString)) {
                        customer.setShippingAddress(shipping)
                        customer.addAddress(shipping)
                        customerRepo.save(customer)
                      }
                      val cart = mallManager.getCart(customer)

                      if (cart.getTotal.doubleValue().equals(total)) {
                        val orderGroup = compositeService.initOrderGroupFromCart(cart, shipping, PaymentType.valueOf(paymentOption.getOrElse("cash")))
                        mallManager.getCartRepository.delete(cart)
                        val it = orderGroup.values().iterator()
                        while (it.hasNext) {
                          pushEvent(new CreatedEvent(it.next().getId))
                        }
                        orderGroup.toJson
                      } else {
                        throw new OrderConfirmFailedException("")
                      }
                    }
                }
              }
          } ~
          path("orders" / Segment) { orderId =>
            val transactionService=Bean[TransactionService]
            val orderReop = transactionService.getOrderDAO
            var order = orderReop.findOne(orderId)
            authorize(order.getCustomerId == customerId){
              get{
                complete {
                  order.toJson
                }
              } ~
                delete{
                  authorize(OrderStatus.hasFlag(OrderStatus.canceled,order.getStatus)){
                    complete{
                      order.setStatus(OrderStatus.canceled)
                      orderReop.save(order).toBriefJson
                    }
                  }
                }
            }
          } ~
          path("comments") {
            post {
              extract(_.request.entity.asString) {
                bodyString =>
                  complete {
                    val it = mapper.readTree(bodyString).elements()
                    while (it.hasNext) {
                      val item = it.next()
                      val lineItem = mallManager.getProductRepository.findOne(item.get("product_id").asText())
                      val comment = new Comment(customer, lineItem, item.get("rate").asInt(), item.get("content").asText())
                      mallManager.getCommentRepository.save(comment)
                    }
                    """{"result":"OK"}"""
                  }
              }
            }
          } ~
          path("shops" / Segment / "comments") { shopId =>
            post {
              formFields('_content, '_rate.as[Int] ? 5) { (content, rate) =>
                val shop = mallManager.getShopRepository.findOne(shopId)
                complete {
                  val comment = new Comment(customer, shop, rate, content)
                  mallManager.getCommentRepository.save(comment).toBriefJson
                }
              }
            }
          } ~
          path("cart") {
            get {
              complete {
                mallManager.getCart(customer)
              }
            } ~
              post {

                extract(_.request.entity.asString) {
                  bodyString =>
                    val it = mapper.readTree(bodyString).elements()
                    val cart = mallManager.getCart(customer)
                    cart.clearItems()
                    while (it.hasNext) {
                      val item = it.next()
                      val product = mallManager.getProductRepository.findOne(item.get("product_id").asText())
                      if(item.has("referee")){
                        val referee=mallManager.getReferee(item.get("referee").asText)
                        cart.addItem(product, item.get("amount").asInt(1),referee)
                      }else{
                        cart.addItem(product, item.get("amount").asInt(1),null)
                      }
                    }
                    complete {
                      mallManager.getCartRepository.save(cart).toBriefJson
                    }
                }
              }

          } ~
          path("cart" / "add") {
            //val mallManager=Bean[CustomerManager]
            post {
              formFields('product_id, '_amount.as[Int]) { (productId, amount) =>
                val product = mallManager.getProductRepository.findOne(productId)
                complete {
                  val cart = mallManager.getCart(customer)
                  cart.addItem(product, amount,null)
                  mallManager.getCartRepository.save(cart).toBriefJson
                }
              }
            }
          } ~
          path("cart" / "set") {
            post {
              //val mallManager=Bean[CustomerManager]
              formFields('product_id, '_amount.as[Int]) { (productId, amount) =>
                val product = mallManager.getProductRepository.findOne(productId)
                complete {
                  val cart = mallManager.getCart(customer)
                  cart.setItemAmount(product, amount,null)
                  mallManager.getCartRepository.save(cart).toBriefJson
                }
              }
            }

          } ~
          path("advisory") {
            post {
              formFields('content) { (content) =>
                val repo = Bean[AdvisoryRepository]
                complete {
                  repo.save(new Advisory(customer, content)).toBriefJson
                }
              }
            }
          }

      }
    }

  }
}

object CustomerRoute extends CustomerRoute
