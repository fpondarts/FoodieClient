package com.fpondarts.foodie.network

import android.content.res.Resources
import com.fpondarts.foodie.BuildConfig
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.User
import com.fpondarts.foodie.model.Directions
import com.google.android.gms.maps.model.LatLng
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import com.google.gson.JsonObject

interface DirectionsApi {




    @GET(API_PREFIX)
    suspend fun getRoute(@Query("origin")origin:String
                         , @Query("destination")dest:String
                         , @Query("waypoints") wayPoint:String
                         , @Query("key")key:String = API_KEY): Response<Directions>


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
        const val API_KEY = "AIzaSyDfTfWIPJRWTHeYn89oMhiPi4LYn7IjKRI"
    }

}