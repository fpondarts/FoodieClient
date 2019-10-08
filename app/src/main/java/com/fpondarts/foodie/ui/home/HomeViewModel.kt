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

    val shops = ArrayList<Shop>()
    val searchText : String? = null
    var authListener: AuthListener? = null


    fun onSearchClick(view: View?){
        Coroutines.main{
            try{
                repository.getShops()
            } catch(e:FoodieApiException) {

            }
        }
    }

    fun getTopShops():LiveData<List<Shop>>{
        var live:LiveData<List<Shop>>? = null
        try {
            live = repository.getTopShops()
        } catch (e:FoodieApiException){
            authListener!!.onFailure("APIFAIL")
        }
        return live!!
    }

}