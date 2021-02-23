package bills

import models.Bill

import scala.concurrent.Future

trait BillRepo {
  def getWithTabId(tabId: Long): Future[Seq[Bill]]
  def add(bill: Bill): Future[Int]
}
