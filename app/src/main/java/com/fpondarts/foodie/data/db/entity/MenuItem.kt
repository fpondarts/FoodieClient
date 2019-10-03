package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity

@Entity(primaryKeys = ["shopId","id"])
data class MenuItem (
    val shopId:Long,
    val id: Long,
    val name: String,
    val description: String,
    val price: Float
)