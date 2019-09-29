package com.fpondarts.foodie.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fpondarts.foodie.data.db.FoodieDatabase
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.data.db.entity.User
import com.fpondarts.foodie.network.FoodieApi
import com.fpondarts.foodie.network.SafeApiRequest
import com.fpondarts.foodie.network.response.AvailabilityResponse
import com.fpondarts.foodie.network.response.SignInResponse
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException
import okhttp3.Call
import okhttp3.ResponseBody
import retrofit2.Response
import java.security.PrivateKey
import javax.security.auth.callback.Callback


class Repository(
    private val api: FoodieApi,
    private val db : FoodieDatabase
):SafeApiRequest() {




    val AVAILABLE = "Available"
    val UNAVAILABLE = "Unavailable"
    val ERROR = "Error"

    suspend fun checkAvailability(email:String):AvailabilityResponse{
        return apiRequest{ api.checkEmailIsAvailable(email) }
    }

    suspend fun foodieSignIn(email: String, password:String?, fbToken:String):SignInResponse{
        return apiRequest { api.signIn(email,password, fbToken) }
    }

    suspend fun getShop(shopId:Int):LiveData<Shop>{
        val shop = db.getShopDao().loadShop(shopId)
        Coroutines.main{
            try{
                val apiShop = api.getShop(shopId).body()
                apiShop?.let {
                    db.getShopDao().upsert(apiShop)
                }
            } catch (e:FoodieApiException){

            }
        }
        return shop
    }

    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)




}