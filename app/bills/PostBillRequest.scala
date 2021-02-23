package bills

case class PostBillRequest(amount: BigDecimal, category: String, comment: String)
