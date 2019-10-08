package com.fpondarts.foodie.ui.home.ui.home.delivery_address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException

class DeliveryAddressViewModel(val repository: Repository): ViewModel() {

    val price: MutableLiveData<Float?> = MutableLiveData<Float?>().apply {
        value = null
    }

    fun getDeliveryPrice(lat:Double,long:Double):LiveData<Float?>{
        Coroutines.io{
            try{
                price.postValue(repository.askDeliveryPrice(lat,long))
            } catch (e:FoodieApiException){

            }
        }
        return price
    }


}