package com.fpondarts.foodie.data.repository.interfaces

import androidx.lifecycle.LiveData
import com.fpondarts.foodie.data.db.entity.User

interface FoodieUsersRepository {

    fun getUser(id:Long):LiveData<User>
}