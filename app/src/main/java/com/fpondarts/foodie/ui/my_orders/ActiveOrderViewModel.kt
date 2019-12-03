package com.fpondarts.foodie.ui.my_orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.db.entity.Delivery
import com.fpondarts.foodie.data.db.entity.Order
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.data.repository.UserRepository

class ActiveOrderViewModel(val repository: UserRepository) : ViewModel() {


    fun getOrder(id:Long): LiveData<Order>{
        return repository.getOrder(id)
    }

    fun getDelivery(id:Long): LiveData<Delivery>{
        return repository.getDelivery(id)
    }

    fun getShop(id:Long): LiveData<Shop>{
        return repository.getShop(id)
    }



}
