package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["shopId","itemId"])
data class MenuItem (
    val shopId:Int,
    val itemId: Long,
    val name: String,
    val description: String,
    val price: Float
)