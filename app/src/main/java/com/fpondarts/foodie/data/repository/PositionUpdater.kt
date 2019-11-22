package com.fpondarts.foodie.data.repository

import androidx.lifecycle.LiveData

interface PositionUpdater {

    fun updatePosition(latitude:Double, longitude:Double):LiveData<Boolean>
}