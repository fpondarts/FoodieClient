package com.fpondarts.foodie.network

import androidx.lifecycle.LiveData
import com.fpondarts.foodie.data.db.entity.Menu
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.model.FoodieUser
import com.fpondarts.foodie.network.response.AvailabilityResponse
import com.fpondarts.foodie.network.response.SignInResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface FoodieApi {

    @GET("/user/availability/{email}")
    suspend fun checkEmailIsAvailable(@Path("email") email: String):Response<AvailabilityResponse>

    @POST
    suspend fun signIn(email: String, password: String?, fbToken: String):Response<SignInResponse>

    @GET("shops/{id}")
    suspend fun getShop(@Header("FOODIE-API-KEY") token:String, @Path("id") id:Int):Response<Shop>

    @GET("shops/{id}/menu")
    suspend fun getMenu(@Header("FOODIE-API-KEY") token:String, @Path("id") id:Int):Response<Menu>


    companion object {
        operator fun invoke() : FoodieApi {
            return Retrofit
                .Builder()
                .baseUrl("https://foodie-node.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FoodieApi::class.java)
        }
    }

}