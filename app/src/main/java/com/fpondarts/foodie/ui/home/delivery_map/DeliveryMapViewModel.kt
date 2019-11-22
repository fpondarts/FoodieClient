package com.fpondarts.foodie.ui.home.delivery_map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.db.entity.Delivery
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.util.Coroutines
import com.google.android.gms.maps.model.LatLng

class DeliveryMapViewModel (val repository: Repository) : ViewModel() {

    fun getCurrentShop():LiveData<Shop>{
        return repository.getCurrentShop()
    }

    fun getAvailableDeliveries():LiveData<List<Delivery>>{
        return repository.availableDeliveries
    }
}
