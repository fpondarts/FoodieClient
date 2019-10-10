package com.fpondarts.foodie.network.request

import com.fpondarts.foodie.model.Coordinates
import com.fpondarts.foodie.model.OrderItem

data class OrderRequest (
    val shopId:Long,
    val orderItems: Collection<OrderItem>,
    val coordinates: Coordinates,
    val payWithPoints:Boolean,
    val favourPoints:Int
){

}