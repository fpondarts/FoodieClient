package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fpondarts.foodie.model.OrderState
import java.time.LocalDateTime
import java.util.*

@Entity
data class Order(
    @PrimaryKey
    val id: Long,
    val dateTime: String,
    val price: Float,
    val userId: Long,
    val shopId:Long,
    val state: OrderState,
    val deliveryId: Long
    ) {
}