package com.fpondarts.foodie.ui.home.current_order

import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.UserRepository
import com.fpondarts.foodie.model.OrderModel

class CurrentOrderViewModel(val repository: UserRepository) : ViewModel() {


    fun getCurrentOrder():OrderModel{
       return repository.currentOrder!!
    }

    fun removeFromOrder(itemId:Long){
        repository.currentOrder?.removeItem(itemId)
    }

}
