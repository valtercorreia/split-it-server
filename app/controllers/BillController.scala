package controllers

import com.google.inject.{Inject, Singleton}
import models.Bill
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BillController @Inject()(val controllerComponents: ControllerComponents,
                               val billRepo: BillRepo)(
                                       implicit ec: ExecutionContext
                                     )
extends BaseController {

  implicit val transactionListJson = Json.format[Bill]
  implicit val transactionRequestJson = Json.format[AddBillRequest]

  def getAll: Action[AnyContent] = Action.async {
    billRepo.get().map(users => Ok(Json.toJson(users)))
  }

  def addBill(): Action[AnyContent] = Action.async { implicit request =>
    val content = request.body
    val jsonObject = content.asJson
    val transaction: Option[AddBillRequest] = jsonObject.flatMap(Json.fromJson[AddBillRequest](_).asOpt)

    transaction match {
      case Some(newItem) =>
        val toBeAdded = Bill(1, newItem.amount, newItem.category, newItem.comment, newItem.groupId)
        billRepo.add(toBeAdded).map(a => Created(Json.toJson(toBeAdded)))
      case None =>
        Future(BadRequest)
    }
  }

}
