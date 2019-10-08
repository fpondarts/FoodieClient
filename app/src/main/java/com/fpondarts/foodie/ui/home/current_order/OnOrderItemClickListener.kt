package com.fpondarts.foodie.ui.home.current_order

import com.fpondarts.foodie.model.OrderItem

interface OnOrderItemClickListener {

    fun onItemClick(item: OrderItem)
}