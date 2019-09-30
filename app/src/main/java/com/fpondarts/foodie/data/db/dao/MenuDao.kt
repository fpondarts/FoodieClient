package com.fpondarts.foodie.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.fpondarts.foodie.data.db.entity.Menu

@Dao
interface MenuDao {

    @Query("Select * from menu where shopId = :shopId")
    fun loadMenu(shopId:Int): LiveData<Menu>


}