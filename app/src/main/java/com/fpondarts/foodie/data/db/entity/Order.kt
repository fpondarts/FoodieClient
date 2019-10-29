package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Order(
    @PrimaryKey
    val id: Long,
    val dateTime: Date,
    val price: Float,
    val deliveryId: Long
    ) {
}