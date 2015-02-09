package me.smartco.akstore.store.rest

import me.smartco.akstore.exception.{ShopTooFarException, OrderConfirmFailedException, CodeValidateFailedException}
import org.springframework.dao.{DataAccessException, DataIntegrityViolationException}
import spray.http.StatusCodes
import spray.http.StatusCodes._
import spray.routing.Directives._
import spray.routing._
import spray.util.LoggingContext

/**
 * Created by libin on 14-12-9.
 */
object RestExceptionHandler {
  implicit def restExceptionHandler(implicit log: LoggingContext) = ExceptionHandler {
    case e: OrderConfirmFailedException =>
      complete(BadRequest, """{"error":"订单金额于元素数据不匹配"} """ )
    case e:CodeValidateFailedException=>ctx=>
      complete(BadRequest, """{"error":"验证码不正确"} """ )

    case e:ShopTooFarException =>
      complete(BadRequest, """{"error":"商户距离送货地点太远"} """ )
    case e: DataIntegrityViolationException =>
      log.error(e,"unhandled server error")
      complete(BadRequest, """{"error":"输入的内容和已有的数据冲突"} """ )
    case e: DataAccessException =>
      log.error(e,"unhandled server error")
      complete(BadRequest, """{"error":"数据库错误"} """ )
    case e=>
      log.error(e,"unhandled server error")
      complete(InternalServerError, """{"error":"服务器错误"} """)
  }
  implicit val restRejectionHandler = RejectionHandler {
    case Nil =>
      complete(StatusCodes.NotFound, "{\"error\":\"path not found\"}")
    case MissingFormFieldRejection(name) :: _ =>
      complete(StatusCodes.BadRequest, "{\"error\":\"missing field " + name + " \"}")
    case AuthenticationFailedRejection(m,challengeHeaders):: _ =>
      println(m)
      complete(StatusCodes.Unauthorized,challengeHeaders,"""{"error":"登录状态无效，请重新登录"} """)
    case MissingQueryParamRejection(name)::_ =>
      complete(StatusCodes.BadRequest, "{\"error\":\"missing field " + name + " \"}")
    case rej =>
      println(rej)
      complete("{\"error\":\"unknown\"}")
  }
}
