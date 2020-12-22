package models

case class Bill(fromId: Int, amount: Int, category: String, comment: String, groupId: Int, id: Int = 0)
