package com.fpondarts.foodie.data.repository

import androidx.lifecycle.MutableLiveData
import com.fpondarts.foodie.data.db.FoodieDatabase
import com.fpondarts.foodie.data.db.entity.User
import com.fpondarts.foodie.network.FoodieApi
import com.fpondarts.foodie.network.SafeApiRequest
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException
import java.lang.Exception

class DeliveryRepository(
    private val api:FoodieApi,
    private val db:FoodieDatabase
): SafeApiRequest() {

    val apiError = MutableLiveData<FoodieApiException>().apply {
        value = null
    }

    var token:String? = null
    var userId:Long? = null

    var currentUser = MutableLiveData<User>().apply {
        value = null
    }

    fun initUser(token:String, id:Long){
        this.token = token
        userId = id
        Coroutines.io{
            try {
                currentUser.postValue(apiRequest{ api.getUserById(token,id) })
            } catch (e:FoodieApiException) {
                apiError.postValue(e)
            }
        }

    }




}