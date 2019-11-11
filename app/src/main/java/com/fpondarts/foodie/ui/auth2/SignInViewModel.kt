package com.fpondarts.foodie.ui.auth2

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.AuthRepository
import com.fpondarts.foodie.network.response.SignInResponse
import com.fpondarts.foodie.util.exception.IncompleteDataException

class SignInViewModel (val repository: AuthRepository) : ViewModel() {

    @Bindable
    val userName = MutableLiveData<String>()

    @Bindable
    val password = MutableLiveData<String>()

    var googleAuthHandler: GoogleAuthHandler? = null


    fun signIn(): LiveData<SignInResponse?> {
        if (userName.value.isNullOrBlank() || password.value.isNullOrBlank()){
            throw IncompleteDataException("Usuario y contraseña son obligatorios")
        }
        return repository.passwordLogin(userName.value!!,password.value!!)
    }

    fun tokenSignIn(token:String):LiveData<SignInResponse?>{
        return repository.tokenLogin(token)
    }



}
