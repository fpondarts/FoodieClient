package com.fpondarts.foodie.ui.profile

import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.UserRepository

class ProfileViewModel (repository: UserRepository): ViewModel() {

    var name:String?=null
    var email:String?=null

}