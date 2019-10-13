package com.fpondarts.foodie.network.request

import com.fpondarts.foodie.model.Coordinates
import com.fpondarts.foodie.model.OrderItem

data class OrderRequest (
    val shopId:Long,
    val orderItems: Collection<OrderItem>,
    val latitude: Double,
    val longitude: Double,
    val payWithPoints:Boolean,
    val favourPoints:Int
){

}