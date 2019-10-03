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

    @Query("Select * from shop Limit 10")
    fun loadShops():LiveData<List<Shop>>

    @Query("Select * from shop order by rating")
    fun getAllOrdered():LiveData<List<Shop>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(shop:Shop)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertBatch(shop:List<Shop>)

}