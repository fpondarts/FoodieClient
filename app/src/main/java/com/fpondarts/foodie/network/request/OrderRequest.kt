package com.fpondarts.foodie.network.request

import com.fpondarts.foodie.model.Coordinates
import com.fpondarts.foodie.model.OrderItem

data class OrderRequest (
    val shop_id:Long,
    val products: Collection<OrderItem>,
    val coordinates:Coordinates,
    val payWithPoints:Boolean,
    val favourPoints:Int?,
    val price:Float?,
    val user_id:Long,
    val state:String = "created"
){

}