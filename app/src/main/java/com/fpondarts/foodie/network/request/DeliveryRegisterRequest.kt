package com.fpondarts.foodie.network.request

data class DeliveryRegisterRequest(
    val name:String,
    val email:String,
    val password:String?,
    val firebase_uid:String,
    val phone_number:String,
    val role:String,
    val balance:Int,
    val picture:String) {

}