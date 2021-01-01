package users

import models.User
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

class UsersRepoImpl extends UsersRepo {
  val db = Database.forConfig("postgres")

  val users = UsersTable.users;

  override def getByEmail(email: String): Future[Option[User]] = {
    db.run(users.filter(x => x.email === email).result.headOption)
  }
}

object UsersTable {
  val users = TableQuery[Users]

  class Users(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def firstName = column[String]("first_name")

    def email = column[String]("email")

    def passHash = column[String]("pass_hash")

    def * = (email, passHash, firstName, id) <> ((User.apply _).tupled, User.unapply)
  }

}