package com.fpondarts.foodie.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fpondarts.foodie.data.db.FoodieDatabase
import com.fpondarts.foodie.data.db.entity.Menu
import com.fpondarts.foodie.data.db.entity.MenuItem
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


class Repository(
    private val api: FoodieApi,
    private val db : FoodieDatabase
):SafeApiRequest() {

    val topRanked = db.getShopDao().getAllOrdered()

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

    suspend fun getShops():LiveData<List<Shop>>{
        return db.getShopDao().loadShops()
    }

    fun getTopShops():LiveData<List<Shop>>{
        val ans = db.getShopDao().getAllOrdered()
        if (ans.value==null || ans.value!!.isEmpty()){
            Coroutines.io{
                try {
                    val top = api.getTopShops(currentUser.value!!.sessionToken!!)
                    if (top.isSuccessful){
                        db.getShopDao().upsertBatch((top.body()!!))
                    }
                } catch (e:FoodieApiException) {
                    throw e
                }
            }
        }

        return ans
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

    suspend fun getShopMenu(shopId:Int): Menu{
        val liveMenu = db.getMenuItemDao().loadMenu(shopId)

        liveMenu.value?.let{
            return Menu(shopId,ArrayList<MenuItem>(liveMenu.value!!))
        } ?: run{
           return api.getMenu(currentUser.value!!.sessionToken!!,shopId).body()!!
        }
    }

    fun getItemName(itemId:Long):String?{
        return db.getMenuItemDao().loadItem(itemId).value?.let {
            return it.name
        }
        return null
    }


    fun addItemToOrder(itemId:Int,number:Int,unitPrice:Float) {
        currentOrder!!.addItems(itemId,number,unitPrice)
    }
    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)




}