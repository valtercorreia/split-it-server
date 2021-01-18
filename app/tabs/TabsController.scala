package tabs

import com.google.inject.{Inject, Singleton}
import models.Tab
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TabsController @Inject()(val controllerComponents: ControllerComponents,
                               val tabsRepo: TabsRepo)
                              (implicit ec: ExecutionContext)
extends BaseController {

  implicit val tabListJson = Json.format[Tab]
  implicit val addTabRequestJson = Json.format[AddTabRequest]

  def getAll: Action[AnyContent] = Action.async {
    tabsRepo.getAll().map(tabs => Ok(Json.toJson(tabs)))
  }

  /*
  def getById: Action[AnyContent] = Action.async { implicit request =>
    tabsRepo.getById(request.).map(tabs => Ok(Json.toJson(tabs)))
  }*/

  def createTab(): Action[AnyContent] = Action.async { implicit request =>
    val content = request.body
    val jsonObject = content.asJson
    val tab: Option[AddTabRequest] = jsonObject.flatMap(Json.fromJson[AddTabRequest](_).asOpt)

    tab match {
      case Some(newItem) =>
        val toBeAdded = Tab(newItem.title)
        tabsRepo.create(toBeAdded).map(_ => Created)
      case None =>
        Future(BadRequest)
    }
  }

}
