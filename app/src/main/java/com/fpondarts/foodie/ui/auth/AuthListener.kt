package com.fpondarts.foodie.ui.auth

interface AuthListener {

    fun onStarted()

    fun onSuccess()

    fun onFailure(message:String)
}