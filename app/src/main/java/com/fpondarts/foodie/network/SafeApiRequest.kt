package com.fpondarts.foodie.network

import com.fpondarts.foodie.util.exception.FoodieApiException
import com.google.android.gms.common.api.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.StringBuilder

abstract class SafeApiRequest {
    suspend fun <T: Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful){
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()
            val message = StringBuilder()
            error?.let{
                try {
                    message.append(JSONObject(it).getString("message"))
                } catch (e: JSONException){
                    message.append("Error code: "+response.code().toString())
                }
            }
            throw FoodieApiException(message.toString())
        }
    }
}