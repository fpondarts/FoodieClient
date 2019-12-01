package com.fpondarts.foodie.ui.auth2

import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.AuthRepository
import com.fpondarts.foodie.network.response.SignInResponse
import com.fpondarts.foodie.util.exception.IncompleteDataException

class SignInViewModel (val repository: AuthRepository) : ViewModel() {

    val userName = MutableLiveData<String>().apply{
        value = "normal2@gmail.com"}

    val password = MutableLiveData<String>().apply {
        value = "taller2"
    }


    fun signIn(): LiveData<SignInResponse?> {
        if (userName.value.isNullOrBlank() || password.value.isNullOrBlank()){
            throw IncompleteDataException("Usuario y contraseña son obligatorios")
        }
        return repository.passwordLogin(userName.value!!,password.value!!)
    }

    fun tokenSignIn(token:String):LiveData<SignInResponse?>{
        Log.d("idToken: ",token)
        return repository.tokenLogin(token)
    }


}
