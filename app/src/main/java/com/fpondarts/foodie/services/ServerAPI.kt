package com.fpondarts.foodie.services

import com.fpondarts.foodie.model.FoodieUser

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ServerAPI {

    @HEAD("/user/email/{email}")
    fun checkEmailIsAvailable(@Path("email") email: String): Call<Void>

    @GET("/user/{email}")
    fun signIn(@Path("email") email: String, @Body password: String): Call<Void>

    @POST("/user")
    fun signUpUser(@Body user: FoodieUser): Call<FoodieUser>

}
