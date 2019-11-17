package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fpondarts.foodie.model.OrderState
import com.google.firebase.database.IgnoreExtraProperties
import java.time.LocalDateTime
import java.util.*

@IgnoreExtraProperties
@Entity
data class Order(
    @PrimaryKey
    val order_id: Long,
    val created_at: String,
    val price: Float,
    val user_id: Long,
    val shop_id:Long,
    val state: String,
    val delivery_id: Long,
    val deliveryRating:Float?,
    val shopRating:Float?,
    val latitud:Double,
    val longitud:Double,
    val payWithPoints:Boolean
    ) {
}