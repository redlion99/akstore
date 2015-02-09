package me.smartco.akstore.store.rest.route

import akka.actor.ActorContext
import com.fasterxml.jackson.databind.ObjectMapper
import me.smartco.akstore.common.Constants
import me.smartco.akstore.common.model.{Payment, Address, AbstractDocument}
import me.smartco.akstore.integration.ServiceFacade
import me.smartco.akstore.store.mongodb.mall.{Advertisement, Category}
import me.smartco.akstore.store.mongodb.market.DispatchProduct
import me.smartco.akstore.store.spring.Bean
import me.smartco.akstore.biz.service.PartnerService
import me.smartco.akstore.store.mongodb.core.AttachmentsRepository
import me.smartco.akstore.store.mongodb.partner.{DispatchOptions, PartnerStaff}
import me.smartco.akstore.common.util.ImgUtil
import me.smartco.akstore.user.model.User
import org.springframework.data.domain.PageRequest
import spray.http.{StatusCodes, BodyPart}
import spray.routing.{Directives, MalformedQueryParamRejection}

import scala.collection.mutable
import scala.concurrent.ExecutionContext

import spray.routing.Directives._
import me.smartco.akstore.store.rest.json.Resources._
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._
import scala.collection.JavaConverters._
import StatusCodes._

/**
 * Created by libin on 14-11-14.
 */
trait StaffRoute {
  def staffRoute(user: User)(implicit context: ActorContext, mapper: ObjectMapper, executor: ExecutionContext) = {
    val facade=Bean[ServiceFacade]
    pathPrefix("admin") {
      path("partners") {
        post {
          formFields('partner_name, 'partner_mobile, 'partner_password, '_name, 'address_province, 'address_city, 'address_street, 'location_lat.as[Double], 'location_lng.as[Double]) {
            (name, mobile, password, shopName, province, city, street, lat, lng) =>
              val partnerManager = facade.getPartnerService
              val userService=facade.getUserService
              complete {
                val user = userService.register(mobile, password,Constants.partner_staff_role, partnerManager.getValidateCode(mobile))
                val staff: PartnerStaff = partnerManager.createPartnerFromUser(user.getId,user.getUsername, name, mobile)
                var shop = partnerManager.createShop4Partner(staff.getPartner, shopName)
                shop.setAddress(new Address(street, city, province))
                shop.setLocation(lat, lng)
                partnerManager.getShopRepository.save(shop).toJson
              }
          }
        } ~
          get {

            complete {
              "xxxxx"
            }
          }
      } ~
        path("shops") {
          get {
            parameters('name, 'page.as[Int] ? 0) { (name, page) =>
              complete {
                val partnerManager = Bean[PartnerService]
                partnerManager.getShopRepository.findByNameLike(name, new PageRequest(page, 50))
              }
            } ~
              parameters('page.as[Int] ? 0) { (page) =>
                complete {
                  val partnerManager = Bean[PartnerService]
                  partnerManager.getShopRepository.findAll(new PageRequest(page, 50))
                }
              }
          } ~
            post {
              formFields('_id, 'partner_name, 'partner_account_payment_accountId.?, 'partner_account_payment_accountType
                ,'morning.as[Boolean]?true,'afternoon.as[Boolean]?false,'contact_phone,'_minFare.as[Int]?20
                , '_name, 'address_province, 'address_city, 'address_street, 'location_lat.as[Double], 'location_lng.as[Double], 'picture_id.?) {
                (id, name, accountId, accountType, morning,afternoon,contact_phone,_minFare,shopName, province, city, street, lat, lng, picture) =>
                  complete {
                    val partnerManager = Bean[PartnerService]
                    var shop = partnerManager.getShopRepository.findOne(id)
                    if (null != shop) {
                      shop.setName(shopName)
                      shop.setAddress(new Address(street, city, province))
                      shop.setLocation(lat, lng)
                      shop.setDispatchOptions(new DispatchOptions(morning,false,afternoon))
                      shop.getContact.setPhone(contact_phone)
                      shop.setMinFare(_minFare)
                      picture match {
                        case Some(attachmentId) =>
                          var repo = Bean[AttachmentsRepository]
                          shop.setPicture(repo.findOne(attachmentId))
                        case None =>
                      }
                      partnerManager.getShopRepository.save(shop)
                      var partner = shop.partner
                      partner.setName(name)
                      partnerManager.getPartnerRepository.save(partner)
                      accountId match {
                        case Some(id) =>
                          var account = partner.account
                          account.setPayment(new Payment(accountType, id))
                          partnerManager.getAccountRepository.save(account)
                        case None =>
                      }
                      shop.toJson
                    } else {
                      BadRequest
                    }

                  }
              }
            }
        } ~
        path("shops" / Segment) { shopId =>
          complete {
            val partnerManager = Bean[PartnerService]
            partnerManager.getShopRepository.findOne(shopId).toJson
          }
        } ~
        path("dispatch" / "products") {
          val partnerManager = Bean[PartnerService]
          post {
            formFields('_id.?, '_name, '_description, '_unitPrice.as[Double],'_atomWeight.as[Float] ? 1.0f, '_unitWeight.as[Float] ? 1.0f, '_stock.as[Int],
              'cat0_name.?, 'cat1_name.?, 'cat2_name.?,'_origin,'picture_id.?) {
              (_id, name, description, unitPrice,_atomWeight, unitWeight, stock,cat0,cat1,cat2, origin,picture_id) =>
                complete {
                  val id = _id.getOrElse("")
                  var product: DispatchProduct = null
                  if (id.isEmpty) {
                    product = new DispatchProduct(name, stock, java.math.BigDecimal.valueOf(unitPrice))
                  } else {
                    product = partnerManager.getDispatchProductRepository.findOne(id)
                    product.setName(name)
                    product.setStock(stock)
                    product.setUnitPrice(java.math.BigDecimal.valueOf(unitPrice))
                  }
                  picture_id match {
                    case Some(attachmentId) =>
                      var repo = Bean[AttachmentsRepository]
                      if (attachmentId.isEmpty == false)
                        product.setPicture(repo.findOne(attachmentId))
                    case None =>
                  }
                  product.setCat0(Category.get(cat0 getOrElse "生鲜"))
                  product.setCat1(Category.get(cat1 getOrElse null))
                  product.setCat2(Category.get(cat2 getOrElse null))
                  product.setOrigin(origin)
                  product.setDescription(description)
                  product.setUnitWeight(unitWeight)
                  product.setAtomWeight(_atomWeight)
                  partnerManager.getDispatchProductRepository.save(product).toJson
                }
            }
          } ~
            get {
              parameters('page.as[Int] ? 0) { (page) =>
                complete {
                  partnerManager.getDispatchProductRepository.findAll(AbstractDocument.pageRequest(page))
                }
              }
            }
        } ~
        path("dispatch" / "products" / Segment) { id =>
          val partnerManager = Bean[PartnerService]
          complete {
            partnerManager.getDispatchProductRepository.findOne(id).toJson
          }
        } ~
        path("dispatch" / "orders") {
          val partnerManager = Bean[PartnerService]
          get {
            parameters('page.as[Int] ? 0) { (page) =>
              complete {
                partnerManager.getDispatchOrderRepository.findAll(AbstractDocument.pageRequest(page))
              }
            }
          }
        } ~
        path("dispatch" / "orders" / Segment) { id =>
          val partnerManager = Bean[PartnerService]
          complete {
            partnerManager.getDispatchOrderRepository.findOne(id).toJson
          }
        }~
        path("advertisements"){
          val partnerManager = Bean[PartnerService]
          post{
            formFields('picture_id,'shop_id.?,'product_id.?){ (picture_id,shop_id,product_id) =>
              complete{
                val picture=partnerManager.getAttachmentsRepository.findOne(picture_id)
                if(null!=picture){
                  val shop=partnerManager.getShopRepository.findOne(shop_id getOrElse "")
                  val product=partnerManager.getProductRepository.findOne(product_id getOrElse "")
                  val advertisement=new Advertisement
                  advertisement.setPicture(picture)
                  advertisement.setRefProduct(product)
                  advertisement.setRefShop(shop)
                  partnerManager.getAdvertisementRepository.save(advertisement).toBriefJson
                }else{
                  (StatusCodes.BadRequest,"")
                }
              }
            }
          }
        }~
      path("advertisements"/Segment){ adv_id =>
        val partnerManager = Bean[PartnerService]
        delete{
            complete{
              partnerManager.getAdvertisementRepository.delete(adv_id)
              partnerManager.getAdvertisementRepository.findByActive(true,Advertisement.getDefaultPageable(0)).toBriefJson
            }
        }
      }
    }

  }
}
