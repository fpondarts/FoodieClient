package com.fpondarts.foodie.ui.home.confirm_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.UserRepository
import com.fpondarts.foodie.ui.auth.AuthListener

class ConfirmOrderViewModel(val repository: UserRepository) : ViewModel() {

    val priceStr = "$" + repository.currentOrder?.price!!.toString()



    var listener: AuthListener? = null



    fun confirmOrder(favour:Boolean = false):LiveData<Boolean>{
        return repository.confirmOrder(favour)
    }

}
