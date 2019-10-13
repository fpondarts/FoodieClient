package com.fpondarts.foodie.ui.profile

import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.Repository

class ProfileViewModel (repository: Repository): ViewModel() {

    var name:String?=null
    var email:String?=null

}