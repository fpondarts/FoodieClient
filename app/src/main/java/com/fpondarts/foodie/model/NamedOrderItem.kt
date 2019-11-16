package com.fpondarts.foodie.model

data class NamedOrderItem(
    val id:Long,
    val units:Int,
    val name:String,
    val price:Float?=0.0.toFloat()
) {
}