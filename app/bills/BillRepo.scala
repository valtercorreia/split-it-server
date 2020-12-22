package bills

import models.Bill

import scala.concurrent.Future

trait BillRepo {
  def get(): Future[Seq[Bill]]
  def add(bill: Bill): Future[Int]
}
