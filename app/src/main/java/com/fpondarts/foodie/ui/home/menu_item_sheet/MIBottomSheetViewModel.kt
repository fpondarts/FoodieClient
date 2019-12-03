package com.fpondarts.foodie.ui.home.menu_item_sheet

import android.view.View
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.UserRepository
import com.fpondarts.foodie.model.OrderItem
import com.fpondarts.foodie.ui.auth.AuthListener
import java.lang.NullPointerException

class MIBottomSheetViewModel(val repository:UserRepository): ViewModel(){

    var itemId:Long?=null
    var itemPrice:Float?=null
    var name:String?=null
    var number = 1

    var listener:AuthListener? = null

    fun onAddClick(view: View){
        try{
            repository.addItemToOrder(OrderItem(itemId!!,number),name!!,itemPrice!!)
            listener!!.onSuccess()
        } catch( e: NullPointerException){
            listener?.onFailure("No se pudo agregar al pedido")
        }

    }

    fun onNumberChange(newval:Int){
        number = newval
    }
}