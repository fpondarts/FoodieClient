package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderItem(

    @PrimaryKey
    val id:Long,
    val order_id:Long,
    val product_id:Long,
    val units:Int
) {
}