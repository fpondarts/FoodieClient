package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Shop(
    @PrimaryKey
    val shopId:Int,
    val name: String,
    val rating: Float,
    val photoUrl: String
){
}