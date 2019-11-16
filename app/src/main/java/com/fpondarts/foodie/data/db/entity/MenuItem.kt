package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity

@Entity(primaryKeys = ["shop_id","id"])
data class MenuItem (
    val shop_id:Long,
    val id: Long,
    val name: String,
    val description: String,
    val price: Float
)