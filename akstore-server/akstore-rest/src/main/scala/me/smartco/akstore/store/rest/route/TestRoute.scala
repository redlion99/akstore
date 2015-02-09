package me.smartco.akstore.store.rest.route


import akka.actor.ActorContext
import com.fasterxml.jackson.databind.ObjectMapper
import me.smartco.akstore.common.model.{Shipping, Address, PaymentType}
import me.smartco.akstore.exception.CodeValidateFailedException
import me.smartco.akstore.biz.service.{TransactionService, MallService}
import me.smartco.akstore.integration.ServiceFacade
import me.smartco.akstore.store.mongodb.mall.{Cart, Customer, Product, ProductRepository}
import me.smartco.akstore.store.spring.Bean
import spray.routing.Directives._
import spray.routing.PathMatchers.Segment
import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext

import me.smartco.akstore.store.rest.json.Resources._
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._

import scala.util.Random

/**
 * Created by libin on 14-12-27.
 */
trait TestRoute {
  def testRoute(implicit context: ActorContext, mapper: ObjectMapper, executor: ExecutionContext) = {
    val facade=Bean[ServiceFacade]
    path("test"/"orders"){
      val mallManager=facade.getMallService
      val compositeService=facade.getCompositeService
      val transactionService=Bean[TransactionService]
      post{
        complete{
            val customer = compositeService.register("13333333333", "13333333333", null, "13333333333", mallManager.getValidateCode("13333333333"))
            val cart: Cart = mallManager.getCart(customer)
            cart.clearItems()
            mallManager.getProductRepository.findAll().asScala.map{
              product =>
                val s=Random.nextInt%2
                if(s==0){
                  cart.addItem(product, 1+Math.abs(Random.nextInt%3),null)
                }
            }
            mallManager.getCartRepository.save(cart)
            val shipping: Shipping = new Shipping(new Address("罗山路50号", "浦东","上海" ), "里斯", "13333333333", Shipping.ShippingMethod.express)
            val orders=transactionService.initOrderGroupFromCart(cart, shipping, PaymentType.cash)
            mallManager.getCartRepository.delete(cart)
            orders.toBriefJson
        }
      }
    }
  }
}
