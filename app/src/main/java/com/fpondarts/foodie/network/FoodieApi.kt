package com.fpondarts.foodie.network

import androidx.lifecycle.LiveData
import com.fpondarts.foodie.data.db.entity.Delivery
import com.fpondarts.foodie.data.db.entity.Menu
import com.fpondarts.foodie.data.db.entity.Offer
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.model.FoodieUser
import com.fpondarts.foodie.network.FoodieApi.Companion.API_PREFIX
import com.fpondarts.foodie.network.request.OrderRequest
import com.fpondarts.foodie.network.response.AvailabilityResponse
import com.fpondarts.foodie.network.response.ConfirmOrderResponse
import com.fpondarts.foodie.network.response.DeliveryPriceResponse
import com.fpondarts.foodie.network.response.SignInResponse
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface FoodieApi {


    @GET(API_PREFIX+"user/availability/{email}")
    suspend fun checkEmailIsAvailable(@Path("email") email: String):Response<AvailabilityResponse>

    @POST
    suspend fun signIn(email: String, password: String?, fbToken: String):Response<SignInResponse>

    @GET(API_PREFIX+"shops/{id}")
    suspend fun getShop(@Header(API_KEY_HEADER) token:String, @Path("id") id:Long):Response<Shop>

    @GET(API_PREFIX+"shops/{id}/menu")
    suspend fun getMenu(@Header(API_KEY_HEADER) token:String, @Path("id") id:Long):Response<Menu>

    @GET(API_PREFIX+"shops/top")
    suspend fun getTopShops(@Header(API_KEY_HEADER) token: String):Response<List<Shop>>

    @GET(API_PREFIX+"shops/{id}/deliveryPrice")
    suspend fun getDeliveryPrice(@Header(API_KEY_HEADER) token:String,
                                 @Path("id")id:Long,
                                 @Query("latitude")lat:Double, @Query("longitude")long:Double):Response<DeliveryPriceResponse>


    @POST(API_PREFIX+"orders")
    suspend fun confirmOrder(@Header(API_KEY_HEADER)token:String, @Body order:OrderRequest):Response<ConfirmOrderResponse>

    @GET(API_PREFIX+"deliveries")
    suspend fun getDeliveries(@Header(API_KEY_HEADER)token: String, @Query("latitude") lat:Double, @Query("longitude") long:Double):Response<List<Delivery>>

    @POST(API_PREFIX+"offer")
    suspend fun postOffer(@Header(API_KEY_HEADER)token:String, @Body deliveryId:Long, @Body orderId: Long):Response<Offer>

    companion object {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY

        }

        val httpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(logging)
        }



        operator fun invoke() : FoodieApi {
            return Retrofit
                .Builder()
                .baseUrl("https://virtserver.swaggerhub.com/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FoodieApi::class.java)
        }

         const val API_PREFIX = "FoodieAPI/FoodieAPI/1.0.0-oas3/"
        const val API_KEY_HEADER = "FOODIE-API-KEY"
    }

}