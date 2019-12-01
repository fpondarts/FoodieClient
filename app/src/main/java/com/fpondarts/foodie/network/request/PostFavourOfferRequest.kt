package com.fpondarts.foodie.network.request

data class PostFavourOfferRequest(
    val user_id: Long,
    val order_id: Long,
    val points: Int
) {
}