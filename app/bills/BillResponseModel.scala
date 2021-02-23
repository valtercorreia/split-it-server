package bills

case class BillResponseModel(amount: BigDecimal, isCredit: Boolean, category: String, comment: String, id: Int)
