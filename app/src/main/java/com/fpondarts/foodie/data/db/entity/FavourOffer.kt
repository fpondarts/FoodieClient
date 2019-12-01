package com.fpondarts.foodie.data.db.entity

data class FavourOffer(
    val id:Long,
    val order_id: Long,
    val user_id: Long,
    val state: String,
    val points:Int,
    val created_at_seconds: Long
) {

}