package com.fpondarts.foodie.ui.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.ui.auth.AuthListener
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException

class HomeViewModel (
    private val repository: Repository
): ViewModel() {

    val searchText : String? = null
    var authListener: AuthListener? = null



    fun getAllShops():LiveData<List<Shop>>{
        var live:LiveData<List<Shop>>? = null
        try {
            live = repository.getAllShops()
        } catch (e:FoodieApiException){
            authListener!!.onFailure("APIFAIL")
        }
        return live!!
    }

    fun getMoreShops(){
        repository.getMoreShops()
    }

}