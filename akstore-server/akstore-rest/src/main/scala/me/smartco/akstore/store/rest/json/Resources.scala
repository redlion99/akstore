package me.smartco.akstore.store.rest.json

import java.io.StringWriter

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import me.smartco.akstore.common.model.{AbstractDocument, Views, BriefResult}
import me.smartco.akstore.common.spring.BriefContentPage
import org.springframework.data.domain.Page
import spray.httpx.marshalling.ToResponseMarshallable

/**
 * Created by libin on 14-11-8.
 */
object Resources {
  implicit def convertAny2Json[T](any: T) = new Json4dAny(any)
  class Json4dAny[T](any: T) {
    def toJson(implicit mapper: ObjectMapper): ToResponseMarshallable = {
      convertAny2Response(any)
    }
    def toBriefJson(implicit mapper: ObjectMapper): ToResponseMarshallable = {
      convertAny2BriefResponse(any)
    }
  }
  implicit def convertPage2Json[T](page: Page[T]) = new Json4dPage(page)
  class Json4dPage[T](page:Page[T]){
    def page2Json()(implicit mapper: ObjectMapper): ToResponseMarshallable = {
      convertPage2Response(page)
    }
  }
  implicit def convertPage2Response[T](page:Page[T])(implicit mapper: ObjectMapper):ToResponseMarshallable={
    val sw = new StringWriter()
    val gen = new JsonFactory().createGenerator(sw)
    val briefPage=new BriefContentPage[T](page)
    mapper.writerWithView(classOf[Views.Brief]).writeValue(gen,briefPage)
    gen.close()
    sw.toString
  }

  implicit def convertDocument2ResponseWithBrief(any:BriefResult)(implicit mapper: ObjectMapper):ToResponseMarshallable={
    convertAny2BriefResponse(any)
  }

  implicit def convertCollection2ResponseWithBrief[T](any:java.util.Collection[T])(implicit mapper: ObjectMapper):ToResponseMarshallable={
    convertAny2BriefResponse(any)
  }

  def convertAny2Response[T](any:T)(implicit mapper: ObjectMapper):ToResponseMarshallable={
    if(null==any)
      "{}"
    else{
      val sw = new StringWriter()
      val gen = new JsonFactory().createGenerator(sw)
      mapper.writeValue(gen,any)
      gen.close()
      sw.toString
    }
  }
  def convertAny2BriefResponse[T](any:T)(implicit mapper: ObjectMapper):ToResponseMarshallable={
    if(null==any)
      "{}"
    else{
      val sw = new StringWriter()
      val gen = new JsonFactory().createGenerator(sw)
      mapper.writerWithView(classOf[Views.Brief]).writeValue(gen,any)
      gen.close()
      sw.toString
    }
  }

}

case class Customer(username:String,name:String,email:String,mobile:String)

case class CartItem(product_id:String,amount:Int)



