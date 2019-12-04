package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties

@Entity
@IgnoreExtraProperties

data class OrderItem(

    @PrimaryKey
    val id:Long,
    val order_id:Long,
    val product_id:Long,
    val units:Int
) {
}