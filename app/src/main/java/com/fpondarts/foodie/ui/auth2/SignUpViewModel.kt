package com.fpondarts.foodie.ui.auth2

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.AuthRepository
import com.fpondarts.foodie.util.exception.IncompleteDataException
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel(val repository: AuthRepository) : ViewModel() {

    val email = MutableLiveData<String>()

    val name = MutableLiveData<String>()

    val password = MutableLiveData<String>()

    var handler: GoogleAuthHandler? = null

    fun emailSignUp(){
        if (email.value.isNullOrBlank() || password.value.isNullOrBlank() || name.value.isNullOrBlank()){
            throw IncompleteDataException("Los campos son obligatorios")
        }

    }




}
