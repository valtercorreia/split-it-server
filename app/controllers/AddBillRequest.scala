package controllers

case class AddBillRequest(amount: Int, category: String, comment: String, groupId: Int)
