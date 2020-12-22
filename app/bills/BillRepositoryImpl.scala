package bills

import models.Bill
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

class BillRepository () extends BillRepo {
  val db = Database.forConfig("postgres")

  val bills = BillsTable.bills;

  override def get(): Future[Seq[Bill]] = {
    db.run(bills.result)
  }

  override def add(bill: Bill): Future[Int] = db.run(bills returning bills.map(_.id) += bill)
}

object BillsTable {
  val bills = TableQuery[Bills]

  class Bills(tag: Tag) extends Table[Bill](tag, "bills") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def fromId = column[Int]("fromid")

    def amount = column[Int]("amount")

    def description = column[String]("description")

    def category = column[String]("category")

    def tabId = column[Int]("tabid")

    def * = (fromId, amount, description, category, tabId, id) <> ((Bill.apply _).tupled, Bill.unapply)
  }

}
