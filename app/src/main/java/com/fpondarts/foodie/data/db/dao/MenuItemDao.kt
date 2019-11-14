package com.fpondarts.foodie.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fpondarts.foodie.data.db.entity.Menu
import com.fpondarts.foodie.data.db.entity.MenuItem

@Dao
interface MenuItemDao {

    @Query("Select * from menuitem where shop_id = :shop_id")
    fun loadMenu(shopId:Long): LiveData<List<MenuItem>>

    @Query("Select * from menuitem")
    fun loadAll(): LiveData<List<MenuItem>>

    @Query("Select * from MenuItem where product_id = :product_id")
    fun loadItem(id:Long): LiveData<MenuItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(items:List<MenuItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(item:MenuItem)

}