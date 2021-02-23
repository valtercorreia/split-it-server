package tabs_users

import models.TabUser
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

class TabsUsersRepoImpl extends TabsUsersRepo {
  val db = Database.forConfig("postgres")

  val tabsUsers = TabsTable.tabsUsers;

  override def getByUserId(userId: Int): Future[Seq[Int]] = {
    db.run(tabsUsers.filter(_.userId === userId).map(_.tabId).result)
  }

  override def create(tabUser: TabUser): Future[Int] = {
    db.run(tabsUsers += tabUser)
  }
}

object TabsTable {
  val tabsUsers = TableQuery[TabsUsers]

  class TabsUsers(tag: Tag) extends Table[TabUser](tag, "tabs_users") {
    def tabId = column[Int]("tab_id")

    def userId = column[Int]("user_id")

    def * = (tabId, userId) <> ((TabUser.apply _).tupled, TabUser.unapply)
  }

}