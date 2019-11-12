package com.fpondarts.foodie.data.repository

import androidx.lifecycle.MutableLiveData
import com.fpondarts.foodie.data.db.FoodieDatabase
import com.fpondarts.foodie.network.FoodieApi
import com.fpondarts.foodie.util.exception.FoodieApiException

class DeliveryRepository(
    private val api:FoodieApi,
    private val db:FoodieDatabase
) {

    val apiError = MutableLiveData<FoodieApiException>().apply {
        value = null
    }





}