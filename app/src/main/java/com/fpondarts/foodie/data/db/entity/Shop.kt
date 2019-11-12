package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties

@Entity
@IgnoreExtraProperties
data class Shop (
    @PrimaryKey
    val id:Long,
    val name: String,
    val photoUrl: String?,
    val latitude: Double,
    val longitude: Double,
    val rating: Float
){}