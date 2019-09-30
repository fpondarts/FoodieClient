package com.fpondarts.foodie.data.db.entity

import androidx.room.PrimaryKey

data class MenuItem (
    @PrimaryKey
    val itemId: Int,
    val name: String,
    val description: String,
    val price: Float
)