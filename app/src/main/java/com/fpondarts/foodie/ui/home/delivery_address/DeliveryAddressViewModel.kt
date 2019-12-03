package com.fpondarts.foodie.ui.home.delivery_address

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.UserRepository

class DeliveryAddressViewModel(val repository: UserRepository): ViewModel() {

    val price: MutableLiveData<Float?> = MutableLiveData<Float?>().apply {
        value = null
    }


}