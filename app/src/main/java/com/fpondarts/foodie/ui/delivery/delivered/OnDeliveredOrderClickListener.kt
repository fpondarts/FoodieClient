package com.fpondarts.foodie.ui.delivery.delivered

interface OnDeliveredOrderClickListener {

    fun onOrderClick(order_id: Long, shop_id: Long, user_id: Long)
}