package com.fpondarts.foodie.ui.home.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.Repository

class ProfileViewModel (repository: Repository): ViewModel() {

    var name:String?=null
    var email:String?=null

}