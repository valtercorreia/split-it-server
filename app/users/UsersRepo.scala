package users

import models.User

import scala.concurrent.Future

trait UsersRepo {
  def getByEmail(email: String): Future[Option[User]]
}
