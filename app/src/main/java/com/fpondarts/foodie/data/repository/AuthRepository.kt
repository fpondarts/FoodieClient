package com.fpondarts.foodie.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fpondarts.foodie.network.FoodieApi
import com.fpondarts.foodie.network.SafeApiRequest
import com.fpondarts.foodie.network.request.PasswordLoginRequest
import com.fpondarts.foodie.network.request.TokenLoginRequest
import com.fpondarts.foodie.network.request.UserRegisterRequest
import com.fpondarts.foodie.network.response.SignInResponse
import com.fpondarts.foodie.util.ApiExceptionHandler
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException
import retrofit2.Response
import java.lang.Exception

class AuthRepository(private val api:FoodieApi):SafeApiRequest() {

    var apiErrorHandler: ApiExceptionHandler? = null

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
                apiErrorHandler?.handle(response.message())
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

    fun passwordLogin(email:String,password:String): LiveData<SignInResponse?>{
        val request = PasswordLoginRequest(email,password)
        val response = MutableLiveData<SignInResponse?>().apply { value = null }
        Coroutines.io{
            try {
                val foodieResponse = apiRequest{ api.passwordLogin(request) }
                response.postValue(foodieResponse)
            } catch (e:FoodieApiException){
                e.message?.let {
                    apiErrorHandler?.handle(it)
                }
            }
        }
        return response
    }

    fun tokenLogin(idToken:String):LiveData<SignInResponse?>{
        val request = TokenLoginRequest(idToken)
        val response = MutableLiveData<SignInResponse?>().apply { value = null }
        Coroutines.io{
            try {
                val foodieResponse = apiRequest{ api.tokenLogin(request) }
                response.postValue(foodieResponse)
            } catch (e:FoodieApiException){
                e.message?.let {
                    apiErrorHandler?.handle(it)
                }
            }
        }
        return response
    }

}