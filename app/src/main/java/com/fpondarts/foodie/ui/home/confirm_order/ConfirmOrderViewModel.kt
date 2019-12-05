package com.fpondarts.foodie.ui.home.confirm_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.UserRepository
import com.fpondarts.foodie.ui.auth.AuthListener
import kotlin.math.round

class ConfirmOrderViewModel(val repository: UserRepository) : ViewModel() {

    val priceStr = "$${(round(repository.currentOrder?.price!! * 100.0) / 100.0).toString()}"



    var listener: AuthListener? = null



    fun confirmOrder(discount:Boolean,favour:Boolean = false):LiveData<Boolean>{
        return repository.confirmOrder(discount,favour)
    }

}
