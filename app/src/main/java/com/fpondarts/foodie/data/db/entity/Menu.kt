package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Menu (
    @PrimaryKey
    val shopId:Int,
    val items:ArrayList<MenuItem>
){}