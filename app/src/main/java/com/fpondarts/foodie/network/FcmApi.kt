package com.fpondarts.foodie.network

import com.fpondarts.foodie.BuildConfig
import com.fpondarts.foodie.network.fcm_data.FcmMessageData
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FcmApi {

    @POST("fcm/send")
    suspend fun pushNotification(@Body json:JsonObject,
                                 @Header("Content-Type")contentType:String = CONTENT_TYPE
                                 ,@Header("Authorization")authKey:String = AUTH_HEADER):Response<JsonObject>





    companion object {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY

        }

        val httpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(logging)
        }

        operator fun invoke() : FcmApi {
            return Retrofit
                .Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FcmApi::class.java)
        }
        const val AUTH_HEADER = "key=${BuildConfig.fcm_server_key}"
        const val CONTENT_TYPE = "application/json"

    }
}