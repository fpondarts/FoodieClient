package com.fpondarts.foodie.ui.home.ui.home.shop_menu

import androidx.lifecycle.*
import com.fpondarts.foodie.data.db.entity.MenuItem
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.ui.auth.AuthListener
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException

class ShopViewModel (private val repository: Repository ) : ViewModel() {

    var liveMenu = MutableLiveData<ArrayList<MenuItem>>().apply {
        value = ArrayList()
    }


    var listener : AuthListener? = null

    val menu = ArrayList<MenuItem>()

    fun setShop(shopId: Long){
        repository.newOrder(shopId)
    }

    fun getMenu(shopId: Long): LiveData<List<MenuItem>>{
        var liveData: LiveData<List<MenuItem>>? = null
        try {
            return repository.getShopMenu(shopId)
        } catch (e: FoodieApiException){
            listener!!.onFailure(e.message!!)
        }
        return liveData!!
    }


}
