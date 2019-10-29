package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fpondarts.foodie.data.db.Converters
import com.fpondarts.foodie.model.OfferState

@Entity
data class Offer(
    @PrimaryKey
    val offerId:Long,
    val orderId:Long,
    val deliveryId:Long,
    @TypeConverters(Converters::class)
    val offerState: OfferState
)