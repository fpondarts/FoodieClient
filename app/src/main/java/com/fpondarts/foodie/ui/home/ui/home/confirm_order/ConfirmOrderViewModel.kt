package com.fpondarts.foodie.ui.home.ui.home.confirm_order

import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.ui.auth.AuthListener

class ConfirmOrderViewModel(val repository: Repository) : ViewModel() {

    var points: EditText? = null
    val pointsStr = String()
    val priceStr = "$" + repository.currentOrder?.price.toString()
    var checkedButton = R.id.rb_money_price

    var listener: AuthListener? = null

    fun onRadioButtonClicked(view: View){
        points!!.isEnabled = (view.id == R.id.rb_favor)
    }

    fun onConfirmOrder(view:View){

    }

    fun onCancel(view:View){

    }
}
