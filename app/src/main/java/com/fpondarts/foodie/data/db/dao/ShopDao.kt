package com.fpondarts.foodie.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fpondarts.foodie.data.db.entity.Shop

@Dao
interface ShopDao {

    @Query("Select * from shop where id = :shopId")
    fun loadShop(shopId:Int):LiveData<Shop>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(shop:Shop)

}