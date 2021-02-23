package bills

import com.google.inject.{Inject, Singleton}
import models.Bill
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import tabs_users.TabsUsersRepo

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BillController @Inject()(val controllerComponents: ControllerComponents,
                               val billRepo: BillRepo,
                               val tabsUsersRepo: TabsUsersRepo)(
                                       implicit ec: ExecutionContext
                                     )
extends BaseController {

  implicit val getBillResponseJson = Json.format[BillResponseModel]
  implicit val postBillRequestJson = Json.format[PostBillRequest]

  def getBills(tabId: Long): Action[AnyContent] = Action.async { implicit request =>
    val sessionUserIdOpt = request.session.data.get("uId")
    if (sessionUserIdOpt.isEmpty) {
      Future(Unauthorized)
    } else {
      val sessionUserId = sessionUserIdOpt.get.toInt;

      userHasPermissions(sessionUserId, tabId)
        .flatMap(hasPermission => if (!hasPermission) {
          Future(Forbidden)
        } else {
          billRepo.getWithTabId(tabId)
            .map(bills => bills.map(bill => convertToBillResponse(bill, sessionUserId)))
            .map(billsList => Ok(Json.toJson(billsList)))
        })
    }
  }

  def postBill(tabId: Long): Action[AnyContent] = Action.async { implicit request =>
    val sessionUserIdOpt = request.session.data.get("uId")
    if (sessionUserIdOpt.isEmpty) {
      Future(Unauthorized)
    } else {
      val sessionUserId = sessionUserIdOpt.get.toInt;

      userHasPermissions(sessionUserId, tabId)
        .flatMap(hasPermission => if (!hasPermission) {
          Future(Forbidden)
        } else {
          val content = request.body
          val jsonObject = content.asJson
          val transaction: Option[PostBillRequest] = jsonObject.flatMap(Json.fromJson[PostBillRequest](_).asOpt)

          transaction match {
            case Some(newItem) =>

              val toBeAdded = Bill(sessionUserId, newItem.amount, newItem.category, newItem.comment, tabId.toInt)
              billRepo.add(toBeAdded).map(_ => Created)
            case None =>
              Future(BadRequest)
          }
        })
    }
  }

  def userHasPermissions(userId: Int, tabId: Long): Future[Boolean] = {
    tabsUsersRepo.getByUserId(userId).map(_.contains(tabId))
  }

  def convertToBillResponse(bill: Bill, userId: Int): BillResponseModel = {
    val isCredit = bill.fromId != userId

    BillResponseModel(bill.amount, isCredit, bill.category, bill.comment, bill.id)
  }
}
