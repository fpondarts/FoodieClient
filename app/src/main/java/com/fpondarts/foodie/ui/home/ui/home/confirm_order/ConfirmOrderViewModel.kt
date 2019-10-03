package com.fpondarts.foodie.ui.home.ui.home.confirm_order

import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.Repository

class ConfirmOrderViewModel(val repository: Repository) : ViewModel() {

    var points: EditText? = null
    val pointsStr = String()
    var checkedButton = R.id.rb_money_price

    fun onRadioButtonClicked(view: View){
        points!!.isEnabled = (view.id == R.id.rb_favor)
    }

    fun onConfirmOrder(view:View){

    }

    fun onCancel(view:View){

    }
}
