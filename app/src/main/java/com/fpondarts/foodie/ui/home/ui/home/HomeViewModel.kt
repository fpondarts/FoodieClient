package com.fpondarts.foodie.ui.home.ui.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException

class HomeViewModel (
    private val repository: Repository
): ViewModel() {

    val shops = ArrayList<Shop>()
    val searchText : String? = null


    val shopsLiveData = MutableLiveData<List<Shop>>().apply {
        value = shops
    }

    fun onSearchClick(view: View?){
        Coroutines.main{
            try{

            } catch(e:FoodieApiException) {

            }
        }
    }

    private fun addNewShops(newShops:List<Shop>){
        shops.addAll(newShops)
        shopsLiveData.value = shops
    }

}