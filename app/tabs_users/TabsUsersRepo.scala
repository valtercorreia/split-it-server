package tabs_users

import models.TabUser

import scala.concurrent.Future

trait TabsUsersRepo {
  def getByUserId(id: Int): Future[Seq[Int]]

  def create(tabUser: TabUser): Future[Int]
}
