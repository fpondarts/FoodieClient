package com.fpondarts.foodie.network.request

data class PasswordLoginRequest(
    val email:String,
    val password:String
) {
}