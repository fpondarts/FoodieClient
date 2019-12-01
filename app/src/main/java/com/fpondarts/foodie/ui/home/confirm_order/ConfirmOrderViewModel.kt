package com.fpondarts.foodie.ui.home.confirm_order

import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.ui.auth.AuthListener
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException

class ConfirmOrderViewModel(val repository: Repository) : ViewModel() {

    val priceStr = "$" + repository.currentOrder?.price!!.toString()



    var listener: AuthListener? = null



    fun confirmOrder(favour:Boolean = false):LiveData<Boolean>{
        return repository.confirmOrder(favour)
    }

}
