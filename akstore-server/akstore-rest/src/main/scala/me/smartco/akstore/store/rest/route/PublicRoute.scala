package me.smartco.akstore.store.rest.route

import akka.actor.ActorContext
import com.fasterxml.jackson.databind.ObjectMapper
import me.smartco.akstore.biz.conf.Configuration
import me.smartco.akstore.biz.service.MallService
import me.smartco.akstore.common.model.{OrderStatus, AbstractDocument}
import me.smartco.akstore.store.mongodb.mall.{Product, ProductRepository}
import me.smartco.akstore.store.mongodb.partner.Shop
import me.smartco.akstore.store.spring.Bean
import spray.routing.Directives._
import spray.routing.PathMatchers.Segment

import scala.concurrent.ExecutionContext

import me.smartco.akstore.store.rest.json.Resources._
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._

/**
 * Created by libin on 14-12-18.
 */
trait PublicRoute {

  def publicRoute(implicit context: ActorContext, mapper: ObjectMapper, executor: ExecutionContext) = {

      pathPrefix("products") {
        val mallManager = Bean[MallService]
        path("near"/"location") {
          get{
            parameters('lat.as[Double], 'lng.as[Double],'feature.as[Boolean] ? false, 'page.as[Int] ? 0) { (lat, lng,feature, page) =>
              complete {
                mallManager.findProductByLocationWithin(feature,lat, lng, 50.0, page)
              }
            }
          }

        } ~
          path(Segment) { productId =>
            complete {
              val repo = mallManager.getProductRepository
              repo.findOne(productId).toJson
            }
          }
      } ~
        pathPrefix("shops") {
          val mallManager = Bean[MallService]
          pathEnd {
            complete {
              ""
            }
          } ~
            path("near"/"location") {
              get{
                parameters('lat.as[Double], 'lng.as[Double], 'page.as[Int] ? 0) { (lat, lng, page) =>
                  complete {
                    mallManager.findShopByLocationWithin(lat, lng, 50.0, page)
                  }
                }
              }
            }
        } ~
        path("orders"/"status"/"map"){
          get{
            complete{
              OrderStatus.textMap.toJson
            }
          }
        } ~
        pathPrefix("shops"/Segment) { shopId =>
          val mallManager = Bean[MallService]
          var shop:Shop=null
          val config=Bean[Configuration]
          if(shopId.equals("_ps_")){
            shop=config.getPrimaryShop
          }else{
            shop = mallManager.getShopRepository.findOne(shopId)
          }

          pathEnd{
              complete {
                shop.toJson
              }
            } ~
            path( "comments") {
              get {
                parameters('page.as[Int] ? 0) { (page) => {
                  complete {
                    mallManager.getCommentRepository.findByShop(shop, AbstractDocument.pageRequest(page))
                  }
                }
                }
              }
            } ~
            path( "products") {
              get{
                parameters('page.as[Int] ? 0,'feature.as[Int] ? ) { (page,feature) =>
                  complete {
                    feature match{
                      case Some(1) =>
                        mallManager.getProductRepository.findByShopAndActiveAndFeature(shop,true,true,Product.getDefaultPageable(page))
                      case None =>
                        mallManager.getProductRepository.findByShopAndActive(shop,true, Product.getDefaultPageable(page))
                    }
                  }
                }
              }

            } ~
            path( "products"/"feature") {
              get{
                parameters('page.as[Int] ? 0) { (page) =>
                  complete {

                    mallManager.getProductRepository.findByShopAndActiveAndFeature(shop,true,true,Product.getDefaultPageable(page))
                  }
                }
              }
            }
        }


  }
}
