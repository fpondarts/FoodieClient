package com.fpondarts.foodie.ui.auth2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fpondarts.foodie.data.repository.AuthRepository

class AuthViewModelFactory(
    private val repository: AuthRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AuthRepository::class.java).newInstance(repository)
    }

}