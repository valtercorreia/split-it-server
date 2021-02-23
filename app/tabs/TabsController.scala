package tabs

import com.google.inject.{Inject, Singleton}
import models.{Tab, TabUser}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import tabs_users.TabsUsersRepo

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TabsController @Inject()(val controllerComponents: ControllerComponents,
                               val tabsRepo: TabsRepo,
                               val tabsUsersRepo: TabsUsersRepo)
                              (implicit ec: ExecutionContext)
extends BaseController {

  implicit val tabListJson = Json.format[Tab]
  implicit val addTabRequestJson = Json.format[AddTabRequest]

  def getAll(): Action[AnyContent] = Action.async {
    tabsRepo.getAll().map(tabs => Ok(Json.toJson(tabs)))
  }

  def postTab(): Action[AnyContent] = Action.async { implicit request =>
    val sessionUserId = request.session.data("uId")

    val content = request.body
    val jsonObject = content.asJson
    val tab: Option[AddTabRequest] = jsonObject.flatMap(Json.fromJson[AddTabRequest](_).asOpt)

    tab match {
      case Some(newItem) =>
        val toBeAdded = Tab(newItem.title)
        tabsRepo.create(toBeAdded).flatMap(tabId => tabsUsersRepo.create(TabUser(tabId, sessionUserId.toInt))).map(_ => Created)
      case None =>
        Future(BadRequest)
    }
  }

  def getTab(tabId: Long): Action[AnyContent] = Action.async {
    tabsRepo.getById(tabId.toInt).map(tab => Ok(Json.toJson(tab)))
  }

}
