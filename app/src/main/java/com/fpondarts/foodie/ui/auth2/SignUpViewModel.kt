package com.fpondarts.foodie.ui.auth2

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {

    @Bindable
    val email = MutableLiveData<String>()

    @Bindable
    val name = MutableLiveData<String>()

    @Bindable
    val password = MutableLiveData<String>()

    var handler: GoogleAuthHandler? = null

    fun onGoogleSignUp(){
        handler?.handleAuth()
    }




}
