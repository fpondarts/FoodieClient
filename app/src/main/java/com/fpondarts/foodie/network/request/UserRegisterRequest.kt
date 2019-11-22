package com.fpondarts.foodie.network.request

data class UserRegisterRequest(
    val name:String,
    val email:String,
    val password:String?,
    val firebase_uid:String,
    val phone_number:String,
    val role:String,
    val suscripcion:String?,
    val picture:String?
) {
}