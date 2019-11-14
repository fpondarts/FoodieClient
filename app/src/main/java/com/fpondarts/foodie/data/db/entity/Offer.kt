package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fpondarts.foodie.data.db.Converters
import com.fpondarts.foodie.model.OfferState

@Entity
data class Offer(
    val orderId:Long,
    val delivery_id:Long,
    @TypeConverters(Converters::class)
    val state: OfferState,
    val created_at_seconds:Long?,
    val delivery_price:Float
)