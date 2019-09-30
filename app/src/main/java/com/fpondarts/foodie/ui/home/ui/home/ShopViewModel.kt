package com.fpondarts.foodie.ui.home.ui.home

import androidx.lifecycle.*
import com.fpondarts.foodie.data.db.entity.Menu
import com.fpondarts.foodie.data.db.entity.MenuItem
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.data.repository.Repository

class ShopViewModel (private val repository: Repository ) : ViewModel() {

    var liveShop: LiveData<Shop>? = null

    val menu = ArrayList<MenuItem>()

    fun updateMenu(shop: Shop){
        shop.menu?.let{
            menu.clear()
            menu.addAll(shop.menu.items)
        }
    }

    fun setShop(shopId:Int):LiveData<Shop>{
        liveShop = repository.getShop(shopId)
        return liveShop!!
    }
}
