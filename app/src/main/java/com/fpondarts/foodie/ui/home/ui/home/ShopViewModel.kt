package com.fpondarts.foodie.ui.home.ui.home

import androidx.lifecycle.*
import com.fpondarts.foodie.data.db.entity.Menu
import com.fpondarts.foodie.data.db.entity.MenuItem
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.ui.auth.AuthListener
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException
import java.lang.Exception

class ShopViewModel (private val repository: Repository ) : ViewModel() {

    var liveMenu = MutableLiveData<ArrayList<MenuItem>>().apply {
        value = ArrayList()
    }

    var listener : AuthListener? = null

    val menu = ArrayList<MenuItem>()

    fun setShop(shopId:Int){
        Coroutines.main {
            try{
                liveMenu.value =  repository.getShopMenu(shopId).items
            } catch (e:FoodieApiException){
                listener!!.onFailure("FoodieApiException")
            }
        }
    }
}
