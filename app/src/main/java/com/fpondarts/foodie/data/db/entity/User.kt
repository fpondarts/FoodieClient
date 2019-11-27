package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties


const val CURRENT_USER_ID = 0

@Entity
@IgnoreExtraProperties
data class User(
    @PrimaryKey
    var user_id: Long,
    var name:String,
    var email:String,
    val latitude: Double,
    val longitude: Double,
    var password:String?,
    var firebase_uid:String?,
    var picture:String?,
    val phone_number:String,
    var registered:Boolean?,
    var sessionToken:String?,
    var suscripcion: String?,
    var state:String,
    var current_order:Long,
    val make_favours: Boolean,
    val reviews: Int,
    val rating: Float,
    var favourPoints: Int = 0
){
}
