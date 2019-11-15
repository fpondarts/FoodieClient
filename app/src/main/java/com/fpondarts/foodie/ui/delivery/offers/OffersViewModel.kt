package com.fpondarts.foodie.ui.delivery.offers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.db.entity.Offer
import com.fpondarts.foodie.data.repository.DeliveryRepository
import com.fpondarts.foodie.util.Coroutines

class OffersViewModel(val repository: DeliveryRepository) : ViewModel() {


    fun getOffers():LiveData<List<Offer>>{

        return repository.getCurrentOffers()
    }

}
