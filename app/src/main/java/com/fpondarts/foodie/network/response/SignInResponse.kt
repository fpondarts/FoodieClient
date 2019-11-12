package com.fpondarts.foodie.network.response

data class SignInResponse(
   val token:String,
   val role: String,
   val user_id:Long
){}