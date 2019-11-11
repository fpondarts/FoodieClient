package com.fpondarts.foodie.ui.auth2

import android.view.View
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.AuthRepository
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.IncompleteDataException

class RegisterDataViewModel(val authRepository: AuthRepository) : ViewModel() {

    @Bindable
    val phone = MutableLiveData<String>()

    @Bindable
    val creditCard = MutableLiveData<String>()

    @Bindable
    val cvv = MutableLiveData<String>()

    val registered = MutableLiveData<Boolean?>().apply {
        value = null
    }

    var name : String? = null
    var email: String? = null
    var password: String? = null
    var uid: String? = null
    var photo: String? = null

    fun signUpUser():LiveData<Boolean?>{
        if (phone.value.isNullOrEmpty() || creditCard.value.isNullOrEmpty() || cvv.value.isNullOrEmpty()){
            throw IncompleteDataException("Todos los campos son obligatorios")
        }
        return authRepository.registerUser(name!!,email!!,password,uid!!,photo,phone.value!!)
    }

    fun signUpDelivery():LiveData<Boolean?>{
        if (phone.value.isNullOrEmpty() || creditCard.value.isNullOrEmpty() || cvv.value.isNullOrEmpty()){
            throw IncompleteDataException("Todos los campos son obligatorios")
        } else if (photo.isNullOrEmpty()){
            throw IncompleteDataException("La foto es obligatoria para los deliveries")
        }
        return authRepository.registerDelivery(name!!,email!!,password,uid!!,photo!!,phone.value!!)

    }


}
