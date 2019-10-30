package com.fpondarts.foodie.ui.my_orders

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.db.entity.Delivery
import com.fpondarts.foodie.data.db.entity.Order
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.data.repository.Repository

class OrderRatingViewModel (val repository: Repository): ViewModel() {

    var shopName: String = ""
    var shopRating: Float = 0.0.toFloat()
    var deliveryName: String = ""
    var deliveryRating: Float = 0.0.toFloat()

    var rateShopEnabled: Boolean = false
    var rateDeliveryEnabled: Boolean = false

    fun getOrder(orderId:Long):LiveData<Order>{

       return repository.getOrder(orderId)
    }

    fun getDelivery(deliveryId:Long): LiveData<Delivery>{
        return repository.getDelivery(deliveryId)
    }

    fun getShop(shopId:Long): LiveData<Shop> {
        return repository.getShop(shopId)
    }


}
