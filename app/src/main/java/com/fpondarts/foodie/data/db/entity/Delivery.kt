package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
@Entity
data class Delivery(
    @PrimaryKey
    val user_id:Long,
    val name:String,
    val latitude: Double,
    val longitude: Double,
    val rating: Double,
    val reviews: Int,
    val phone_number:String,
    val picture:String,
    val firebase_uid:String
) {
}