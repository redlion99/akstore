package me.smartco.akstore.store.rest.route

import java.util

import akka.actor.ActorContext
import com.fasterxml.jackson.databind.ObjectMapper
import me.smartco.akstore.common.model.{OrderStatus, AbstractDocument}
import me.smartco.akstore.integration.ServiceFacade
import me.smartco.akstore.store.mongodb.partner.Partner
import me.smartco.akstore.store.rest.json.Resources._
import me.smartco.akstore.store.mongodb.core.AttachmentsRepository
import me.smartco.akstore.store.mongodb.mall._
import me.smartco.akstore.store.service.PartnerService
import me.smartco.akstore.store.spring.Bean
import me.smartco.akstore.common.util.Converters._
import me.smartco.akstore.transaction.service.TransactionService
import me.smartco.akstore.user.model.User
import org.springframework.data.domain.PageRequest
import spray.http.StatusCodes
import spray.routing.Directives._
import spray.routing.PathMatchers.LongNumber

import scala.concurrent.ExecutionContext

/**
 * Created by libin on 14-11-12.
 */
trait PartnerRoute {
  def partnerRoute(user: User)(implicit context: ActorContext, mapper: ObjectMapper, executor: ExecutionContext) = {
    val facade = Bean[ServiceFacade]
    pathPrefix("partners" / Segment) { partnerId =>

      val partnerManager = facade.getPartnerService
      val partnerRepo = partnerManager.getPartnerRepository
      val partnerStaff = partnerManager.getPartnerStaffByUserId(user.getId)
      authorize(user.hasRole("Staff") || partnerId.equals(partnerStaff.getPartner.getId)) {
        val partner = partnerRepo.findOne(partnerId)

        pathEnd {
          complete {
            partner.toJson
          }
        } ~
          path("shops") {
            val shopRepo = partnerManager.getShopRepository
            parameters('page.as[Int] ? 0) { (page) =>
              complete {
                shopRepo.findByPartner(partner, new PageRequest(page, 50))
              }
            }
          } ~
          path("shops" / Segment) { shopId =>
            val shopRepo = partnerManager.getShopRepository
            complete {
              shopRepo.findOne(shopId).toJson
            }
          } ~
          path("products") {
            val productRepo = partnerManager.getProductRepository
            get {
              parameters('shop, 'page.as[Int]) { (shop, page) =>
                val shopRepo = partnerManager.getShopRepository
                complete {
                  productRepo.findByShop(shopRepo.findOne(shop), AbstractDocument.pageRequest(page))
                }
              } ~
                parameters('name, 'page.as[Int]) { (name, page) =>
                  complete {
                    productRepo.findByPartnerAndNameLike(partner, name, AbstractDocument.pageRequest(page))
                  }
                }
            } ~
              //create edit
              post {
                formFields('_id.?, 'shop_id, '_name, '_price.as[Double], '_discount.as[Double] ? 1.0, '_shortDescription, '_description,
                  'cat0_name.?, 'cat1_name.?, 'cat2_name.?, 'picture_id.?, '_remark.?, "_dispatchSet".?) {
                  (productId, shopId, name, price, discount, _shortDescription, description, cat0, cat1, cat2, picture, remark, _dispatchSet) =>
                    val shopRepo = partnerManager.getShopRepository
                    val shop = shopRepo.findOne(shopId)
                    complete {
                      import java.math.BigDecimal
                      import me.smartco.akstore.store.mongodb.mall.Product

                      val product: Product = productId match {
                        case Some(pid) =>
                          var m = productRepo.findOne(pid)
                          if (m != null) {
                            m.setName(name)
                            m.setPrice(BigDecimal.valueOf(price))
                            m.setDescription(description)
                          } else {
                            m = new Product(shop, name, BigDecimal.valueOf(price), description)
                          }
                          m
                        case None => new Product(shop, name, BigDecimal.valueOf(price), description)
                      }

                      picture match {
                        case Some(attachmentId) =>
                          var repo = Bean[AttachmentsRepository]
                          if (attachmentId.isEmpty == false)
                            product.setPicture(repo.findOne(attachmentId))
                        case None =>
                      }
                      product.setCat0(Category.get(cat0 getOrElse "生鲜"))
                      product.setCat1(Category.get(cat1 getOrElse null))
                      product.setCat2(Category.get(cat2 getOrElse null))
                      product.setShortDescription(_shortDescription)
                      product.setRemark(remark.getOrElse(""))
                      product.setLocation(product.shop.getLocation)

                      val dispatchSet = mapper.readTree(_dispatchSet.getOrElse("[]"))
                      if (dispatchSet.isArray) {
                        val it = dispatchSet.elements()
                        if (it.hasNext) {
                          product.clearElement()
                        }
                        while (it.hasNext) {
                          val node = it.next()
                          if (node.has("product_id")) {
                            val id = node.get("product_id").asText()
                            if (null != id && id.isEmpty == false) {
                              val quantity = node.get("_quantity").asInt(1)
                              val dp = partnerManager.getDispatchProductRepository.findOne(id)
                              product.addElement(dp, quantity)
                            }
                          }
                        }
                      }
                      if (discount < 1.0) {
                        product.setFeature(true)
                      } else {
                        product.setFeature(false)
                      }
                      product.setDiscount(discount.toFloat)
                      productRepo.save(product).toBriefJson
                    }
                }
              }
          } ~
          path("products" / Segment) { productId =>
            val productRepo = partnerManager.getProductRepository
            get {
              complete {
                productRepo.findOne(productId).toJson
              }
            }
          } ~
          path("dispatch" / "products" / Segment) { id =>
            val partnerManager = Bean[PartnerService]
            complete {
              partnerManager.getDispatchProductRepository.findOne(id).toJson
            }
          } ~
          path("dispatch" / "products") {
            val partnerManager = Bean[PartnerService]
            get {
              parameters('page.as[Int] ? 0) { (page) =>
                complete {
                  partnerManager.getDispatchProductRepository.findAll(AbstractDocument.pageRequest(page))
                }
              }
            }
          } ~
          path("dispatch" / "orders") {
            get {
              parameters('page.as[Int] ? 0) { (page) =>
                complete {
                  partnerManager.getDispatchOrderRepository.findByPartner(partner, AbstractDocument.pageRequest(page))
                }
              }
            }
          } ~
          transactionRoute(user,partner)
      }
    }
  }

  def transactionRoute(user: User,partner:Partner)(implicit context: ActorContext, mapper: ObjectMapper, executor: ExecutionContext) = {
    val facade = Bean[ServiceFacade]
    val transactionService = facade.getTransactionService
    val partnerService=facade.getPartnerService
    val compsiteService=facade.getCompositeService
    path("account" / "balance") {
      get {
        complete {
          val map = new util.HashMap[String, AnyRef]()
          map.put("accountId", partner.account().getId)
          map.put("balance", transactionService.getAccountBalance(partner.account().getId))
          map.toBriefJson
        }
      }
    } ~
      path("orders") {
        val itemRepo = transactionService.getLineItemDAO
        parameters('shop_id.?, 'dateStart.?, 'dateEnd.?, 'page.as[Int]) { (shop, dateStart, dateEnd, page) =>
          val shopRepo = partnerService.getShopRepository
          complete {
            val start = dateStart getOrElse null
            val end = dateEnd getOrElse null
            shop match {
              case Some(shop_id) =>
                compsiteService.searchOrder(shopRepo.findOne(shop_id), start, end, AbstractDocument.pageRequest(page))
              case None =>
                transactionService.getOrderDAO.findByPartnerId(partner.getId, AbstractDocument.pageRequest(page))
            }

          }
        }
      } ~
      path("orders" / Segment) { orderId =>
        get {
          complete {
            transactionService.getOrderDAO.findOne(orderId).toJson
          }
        }
      } ~
      path("orders" / Segment / "status") { orderId =>
        post {
          formFields('_status.as[Int]) {
            (status) =>
              complete {
                val order = transactionService.getOrderDAO.findOne(orderId)
                if (OrderStatus.hasFlag(status, order.getStatus)) {
                  order.setStatus(status)
                  compsiteService.createOrderHistory(order)
                  transactionService.getOrderDAO.save(order).toBriefJson
                }
                order.toBriefJson
              }
          }
        }
      } ~
      path("orders" / "items" / Segment) { itemId =>
        val itemRepo = transactionService.getLineItemDAO
        get {
          complete {
            itemRepo.findOne(itemId).toJson
          }
        }
      } ~
      path("bills") {
        val billRepo = transactionService.getBillDAO
        parameter('page.as[Int]) { (page) =>
          complete {
            billRepo.findByAccountId(partner.account.getId, AbstractDocument.pageRequest(page))
          }
        }
      } ~
      path("bills" / Segment) { billId =>
        val billRepo = transactionService.getBillDAO
        get {
          complete {
            billRepo.findOne(billId).toJson
          }
        }
      } ~
      path("billflows") {
        val billflowRepo = transactionService.getBillFlowRepository
        parameters('page.as[Int], 'dateStart.?, 'dateEnd.?) { (page, dateStart, dateEnd) =>
          complete {
            val start = dateStart getOrElse null
            val end = dateEnd getOrElse null
            billflowRepo.findByAccountIdAndCreateTimeBetween(partner.account.getId, start, end, AbstractDocument.pageRequest(page))
          }
        }
      } ~
      path("billflows" / Segment) { billFlowId =>
        get {
          val billflowRepo = transactionService.getBillFlowRepository
          complete {
            billflowRepo.findOne(billFlowId).toJson
          }
        }
      }
  }
}

object PartnerRoute extends PartnerRoute