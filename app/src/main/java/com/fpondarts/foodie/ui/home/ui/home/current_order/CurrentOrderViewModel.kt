package com.fpondarts.foodie.ui.home.ui.home.current_order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.model.Order
import com.fpondarts.foodie.model.OrderItem
import com.fpondarts.foodie.util.Coroutines

class CurrentOrderViewModel(val repository: Repository) : ViewModel() {


    fun getCurrentOrder():Order{
       return repository.currentOrder!!
    }

    fun removeFromOrder(itemId:Long){
        repository.currentOrder?.removeItem(itemId)
    }

}
