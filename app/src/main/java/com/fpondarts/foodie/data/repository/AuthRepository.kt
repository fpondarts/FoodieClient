package com.fpondarts.foodie.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fpondarts.foodie.network.FoodieApi
import com.fpondarts.foodie.network.request.UserRegisterRequest
import com.fpondarts.foodie.util.Coroutines

class AuthRepository(private val api:FoodieApi) {

    fun registerUser(name:String,email:String,password:String?,fbUid:String,photoUri:String?,phone:String): LiveData<Boolean?> {

        val registered = MutableLiveData<Boolean?>().apply {
            value = null
        }

        Coroutines.io{
            val requestBody = UserRegisterRequest(name,email,password,fbUid,phone,"usuario","flat",photoUri)
            val response = api.registerUser(requestBody)
            if (response.isSuccessful){
                registered.postValue(true)
            } else {
                registered.postValue(false)
            }
        }

        return registered

    }

    fun registerDelivery(name:String,email:String,password:String?,fbUid:String,photoUri:String,phone:String):LiveData<Boolean?>{
        val registered = MutableLiveData<Boolean?>().apply {
            value = null
        }

        Coroutines.io{
            val requestBody = UserRegisterRequest(name,email,password,fbUid,phone,"usuario","flat",photoUri)
            val response = api.registerUser(requestBody)
            if (response.isSuccessful){
                registered.postValue(true)
            } else {
                registered.postValue(false)
            }
        }
        return registered
    }
}