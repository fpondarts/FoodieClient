package com.fpondarts.foodie.ui.chat

import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.Repository

class ConversationViewModel(val repository: Repository) : ViewModel() {

    val message: String? = null

    fun sendMessage(){
        message?.let{
            repository
        }
    }

}
