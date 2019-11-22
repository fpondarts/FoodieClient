package com.fpondarts.foodie.ui.delivery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fpondarts.foodie.data.repository.DeliveryRepository

class DeliveryViewModelFactory(val repository: DeliveryRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel?> create(modelClass: Class<T>): T{
        return modelClass.getConstructor(DeliveryRepository::class.java).newInstance(repository)
    }
}