package authentication

import com.google.inject.{Inject, Singleton}
import org.mindrot.jbcrypt.BCrypt
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import users.UsersRepo

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthController @Inject()(val controllerComponents: ControllerComponents, val usersRepo: UsersRepo) (
  implicit ec: ExecutionContext
) extends BaseController {

  implicit val authenticationRequestJson = Json.format[AuthenticationRequest]

  def authenticate(): Action[AnyContent] = Action.async { implicit request =>
    val requestBodyJson = request.body.asJson
    val authJsonBody: Option[AuthenticationRequest] = requestBodyJson.flatMap(Json.fromJson[AuthenticationRequest](_).asOpt)

    authJsonBody match {
      case Some(authBody) =>
        usersRepo.getByEmail(authBody.email)
          .map {
            case Some(user) => {
              if (passwordsMatch(authBody.password, user.passHash)) {
                Ok.withSession("uId" -> user.id.toString)
              } else {
                Unauthorized
              }
            }
            case None => Unauthorized
          }
      case None =>
        Future(BadRequest)
    }

  }

  private def passwordsMatch(givenPassword: String, dbPasswordHash: String): Boolean = BCrypt.checkpw(givenPassword, dbPasswordHash)

}
