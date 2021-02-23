package models

case class Bill(fromId: Int, amount: BigDecimal, category: String, comment: String, tabId: Int, id: Int = 0)
