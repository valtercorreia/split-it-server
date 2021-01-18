package tabs

import models.Tab

import scala.concurrent.Future

trait TabsRepo {
  def getAll(): Future[Seq[Tab]]

  def getById(id: Int): Future[Option[Tab]]

  def create(tab: Tab): Future[Int]
}
