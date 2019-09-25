package com.fpondarts.foodie.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fpondarts.foodie.data.repository.UserRepository

class SignUpViewModelFactory(
    private val repository: UserRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel?> create(modelClass: Class<T>): T{
        return SignUpViewModel(repository) as T
    }



}