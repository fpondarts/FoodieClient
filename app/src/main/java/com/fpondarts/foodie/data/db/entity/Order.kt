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
    var state: String,
    val delivery_id: Long,
    val latitud:Double,
    val longitud:Double,
    val payWithPoints:Boolean,
    val shop_review:Float?,
    val delivery_review:Float?,
    val delivery_pay:Float?,
    val delivery_price: Float?,
    val favourPoints:Int?
    ) {
}