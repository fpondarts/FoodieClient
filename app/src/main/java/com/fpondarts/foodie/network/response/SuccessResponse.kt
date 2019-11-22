package com.fpondarts.foodie.network.response

data class SuccessResponse(
    val msg:String?,
    val code: Int = 200
) {
}