package com.fpondarts.foodie.ui.my_orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.db.entity.Order
import com.fpondarts.foodie.data.repository.UserRepository
import com.fpondarts.foodie.model.OrderState

class MyOrdersViewModel(val repository: UserRepository): ViewModel() {

    fun getActiveOrders():LiveData<List<Order>>{
        return repository.getOrdersByState(OrderState.ON_WAY)
    }


    fun getDeliveredOrders():LiveData<List<Order>>{
        return repository.getOrdersByState(OrderState.DELIVERED)
    }
}