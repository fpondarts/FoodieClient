package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Delivery(
    @PrimaryKey
    val id:Long,
    val name:String,
    val latitude: Double,
    val longitude: Double,
    val available:Boolean,
    val rating: Float
) {
}