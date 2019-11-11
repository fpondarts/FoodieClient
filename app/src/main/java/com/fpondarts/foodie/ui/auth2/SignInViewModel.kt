package com.fpondarts.foodie.ui.auth2

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignInViewModel : ViewModel() {

    @Bindable
    val userName = MutableLiveData<String>()

    @Bindable
    val password = MutableLiveData<String>()



}
