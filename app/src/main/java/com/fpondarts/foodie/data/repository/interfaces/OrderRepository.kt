package com.fpondarts.foodie.data.repository.interfaces

import androidx.lifecycle.LiveData
import com.fpondarts.foodie.data.db.entity.Order
import com.fpondarts.foodie.data.db.entity.OrderItem

interface OrderRepository {

    fun getOrder(id:Long):LiveData<Order>

    fun getOrderItems(order_id:Long):LiveData<List<OrderItem>>
}