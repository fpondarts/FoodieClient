package com.fpondarts.foodie.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fpondarts.foodie.data.db.entity.OrderItem

@Dao
interface OrderItemDao {

    @Query("Select * from 'OrderItem' where order_id = :order_id")
    fun getOrderItems(order_id:Long):LiveData<List<OrderItem>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(items:List<OrderItem>)


}