package com.fpondarts.foodie.ui.home.ui.home

import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.ui.auth.AuthListener
import java.lang.NullPointerException

class MIBottomSheetViewModel(val repository:Repository): ViewModel(){

    var itemId:Int?=null
    var itemPrice:Float?=null
    var number = 0

    var listener:AuthListener? = null

    fun onAddClick(view: View){
        try{
            repository.addItemToOrder(itemId!!,number,itemPrice!!)
            listener?.onSuccess()
        } catch( e: NullPointerException){
            listener?.onFailure("No se pudo agregar al pedido")
        }

    }

    fun onNumberChange(picker:NumberPicker){
        number = picker.value
    }
}