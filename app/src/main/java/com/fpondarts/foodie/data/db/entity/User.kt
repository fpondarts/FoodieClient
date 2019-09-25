package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


const val CURRENT_USER_ID = 0

@Entity
data class User(
    var name:String,
    var email:String,
    var password:String?,
    var uid:String?,
    var photoUrl:String?,
    var registered:Boolean?,
    var sessionToken:String?
){
    @PrimaryKey(autoGenerate = false)
    var appUserId: Int = CURRENT_USER_ID

}
