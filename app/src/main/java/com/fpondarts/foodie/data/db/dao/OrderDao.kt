package com.fpondarts.foodie.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fpondarts.foodie.data.db.entity.Order
import com.fpondarts.foodie.model.OrderState


@Dao
interface OrderDao {

    @Query("Select * from `order` where product_id=:product_id")
    fun getOrder(id:Long): LiveData<Order>

    @Query("Select * from `order` where state = :state")
    fun getOrdersByState(state:OrderState): LiveData<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(order:Order)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(orders:Collection<Order>)

    @Query("Select COUNT(product_id) from `order`")
    fun getCount():LiveData<Int>


}