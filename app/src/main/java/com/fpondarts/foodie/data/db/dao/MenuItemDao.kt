package com.fpondarts.foodie.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.fpondarts.foodie.data.db.entity.Menu
import com.fpondarts.foodie.data.db.entity.MenuItem

@Dao
interface MenuItemDao {

    @Query("Select * from menuitem where shopId = :shopId")
    fun loadMenu(shopId:Int): LiveData<List<MenuItem>>

    @Query("Select * from MenuItem where itemId = :id")
    fun loadItem(id:Long): LiveData<MenuItem>

}