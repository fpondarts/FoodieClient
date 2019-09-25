package com.fpondarts.foodie.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fpondarts.foodie.data.repository.UserRepository

class SignInViewModelFactory(
    private val repository: UserRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel?> create(modelClass: Class<T>): T{
        return SignInViewModel(repository) as T
    }



}