package com.fpondarts.foodie.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fpondarts.foodie.data.repository.Repository

class FoodieViewModelFactory(
    private val repository: Repository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel?> create(modelClass: Class<T>): T{
        return modelClass.getConstructor(Repository::class.java).newInstance(repository)
    }



}