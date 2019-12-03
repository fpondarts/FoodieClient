package com.fpondarts.foodie.data.repository.interfaces

import androidx.lifecycle.LiveData
import com.fpondarts.foodie.data.db.entity.MenuItem
import com.fpondarts.foodie.data.db.entity.Shop

interface ShopRepository {

    fun getShop(id:Long):LiveData<Shop>

    fun getMenu(id:Long):LiveData<List<MenuItem>>

    fun getMenuItem(id:Long):LiveData<MenuItem>
}