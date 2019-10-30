package com.fpondarts.foodie.ui.my_orders

interface OnMyOrderClickListener {

    fun onActiveOrderClick(active:Boolean, orderId:Long, shopId: Long?, deliveryId:Long?)
}