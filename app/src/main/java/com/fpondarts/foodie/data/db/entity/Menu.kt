package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Menu (
    @PrimaryKey
    val shopId:Long,
    val items:ArrayList<MenuItem>
){}