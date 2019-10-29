package com.fpondarts.foodie.ui.my_orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.db.entity.Order
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.model.OrderState

class MyOrdersViewModel(val repository: Repository): ViewModel() {

    fun getActiveOrders():LiveData<List<Order>>{
        return repository.getOrdersByState(OrderState.ON_WAY)
    }


    fun getDeliveredOrders():LiveData<List<Order>>{
        return repository.getOrdersByState(OrderState.DELIVERED)
    }
}