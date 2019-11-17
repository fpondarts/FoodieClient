package com.fpondarts.foodie.network.request

data class PostOfferRequest(
    val delivery_price:Double,
    val delivery_pay:Double,
    val order_id:Long,
    val delivery_id:Long
) {
}