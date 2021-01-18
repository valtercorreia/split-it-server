package tabs

import models.Tab
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

class TabsRepoImpl extends TabsRepo {
  val db = Database.forConfig("postgres")

  val tabs = TabsTable.tabs;

  override def getById(id: Int): Future[Option[Tab]] = {
    db.run(tabs.filter(_.id === id).result.headOption)
  }

  override def create(tab: Tab): Future[Int] = {
    db.run(tabs += tab)
  }

  override def getAll(): Future[Seq[Tab]] = {
    db.run(tabs.result)
  }
}

object TabsTable {
  val tabs = TableQuery[Tabs]

  class Tabs(tag: Tag) extends Table[Tab](tag, "tabs") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def title = column[String]("title")

    def * = (title, id) <> ((Tab.apply _).tupled, Tab.unapply)
  }

}