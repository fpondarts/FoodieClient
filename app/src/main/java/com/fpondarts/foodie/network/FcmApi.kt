package com.fpondarts.foodie.network

import com.fpondarts.foodie.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface FcmApi {

    companion object {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY

        }

        val httpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(logging)
        }



        operator fun invoke() : DirectionsApi {
            return Retrofit
                .Builder()
                .baseUrl("https://maps.googleapis.com/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DirectionsApi::class.java)
        }

        const val USERS_PREFIX="users"
        const val SHOPS_PREFIX="shops"
        const val API_PREFIX = "maps/api/directions/json"
        const val SERVER_KEY = BuildConfig.fcm_server_key

    }
}