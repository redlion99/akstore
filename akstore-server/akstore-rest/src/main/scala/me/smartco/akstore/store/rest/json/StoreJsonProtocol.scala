package me.smartco.akstore.store.rest.json

import spray.httpx.SprayJsonSupport
import spray.json._
import DefaultJsonProtocol._
/**
 * Created by libin on 14-11-8.
 */
object StoreJsonProtocol {
  //implicit val userFormat = jsonFormat4(Customer)
}

object JsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val cartItemFormat=jsonFormat2(CartItem)
}

