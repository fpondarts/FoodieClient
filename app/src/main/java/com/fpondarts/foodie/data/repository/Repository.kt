package com.fpondarts.foodie.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fpondarts.foodie.data.db.FoodieDatabase
import com.fpondarts.foodie.data.db.entity.Menu
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.data.db.entity.User
import com.fpondarts.foodie.model.Order
import com.fpondarts.foodie.network.FoodieApi
import com.fpondarts.foodie.network.SafeApiRequest
import com.fpondarts.foodie.network.response.AvailabilityResponse
import com.fpondarts.foodie.network.response.SignInResponse
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException
import com.google.android.gms.common.api.ApiException
import okhttp3.Call
import okhttp3.ResponseBody
import retrofit2.Response
import java.security.PrivateKey
import javax.security.auth.callback.Callback


class Repository(
    private val api: FoodieApi,
    private val db : FoodieDatabase
):SafeApiRequest() {

    val currentUser = MutableLiveData<User>().apply {
        value = User(0,"Flavio Perez"
            ,"perezflavio94@gmail.com"
            ,"1234","1234",null
            ,true,"myToken")
    };

    var currentOrder: Order? = null
    

    val AVAILABLE = "Available"
    val UNAVAILABLE = "Unavailable"
    val ERROR = "Error"

    suspend fun checkAvailability(email:String):AvailabilityResponse{
        return apiRequest{ api.checkEmailIsAvailable(email) }
    }

    suspend fun foodieSignInLive(email:String, password: String?, fbToken: String):LiveData<User>{
        Coroutines.main{
            try {
                val response = foodieSignIn(email, password, fbToken)
            } catch (e: ApiException){

            }

        }
        return currentUser
    }

    suspend fun foodieSignIn(email: String, password:String?, fbToken:String):SignInResponse{
        return apiRequest { api.signIn(email,password, fbToken) }
    }

    fun getShop(shopId:Int):LiveData<Shop> {
        val shop = db.getShopDao().loadShop(shopId)
        Coroutines.main{
            try{
                val apiShop = api.getShop(currentUser.value!!.sessionToken!!,shopId).body()
                apiShop?.let {
                    db.getShopDao().upsert(apiShop)
                }
            } catch (e:FoodieApiException){

            }
        }
        return shop
    }

    fun newOrder(shopId:Int){
        currentOrder = Order(currentUser.value!!.uId,shopId)
    }

    suspend fun getShopMenu(shopId:Int):Menu{
        val liveMenu = db.getMenuDao().loadMenu(shopId)
        liveMenu.value?.let{
            return it
        } ?: run{
           return api.getMenu(currentUser.value!!.sessionToken!!,shopId).body()!!
        }
    }


    fun addItemToOrder(itemId:Int,number:Int,unitPrice:Float) {
        currentOrder!!.addItems(itemId,number,unitPrice)
    }
    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)




}