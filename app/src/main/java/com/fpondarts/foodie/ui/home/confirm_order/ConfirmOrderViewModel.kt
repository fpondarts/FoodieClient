package com.fpondarts.foodie.ui.home.confirm_order

import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.ui.auth.AuthListener
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException

class ConfirmOrderViewModel(val repository: Repository) : ViewModel() {

    var points: EditText? = null
    var pointsStr = String()
    var totalPrice = repository.currentOrder?.price!! + repository.currentOrder?.getDeliveryPrice()!!
    val priceStr = "$" + repository.currentOrder?.price.toString()
    val deliveryPriceStr="$" + repository.currentOrder?.getDeliveryPrice()?.toString()
    val totalPriceStr = MutableLiveData<String>().apply {
        value = "$" + totalPrice.toString()
    }

    val confirmed = MutableLiveData<Boolean>().apply {
        value = false
    }

    var checkedButton = R.id.rb_money_price

    var listener: AuthListener? = null

    fun onRadioButtonClicked(view: View){
        if (view.id == R.id.rb_favor){
            points!!.isEnabled = true
            totalPrice = repository.currentOrder!!.price
        } else {
            totalPrice = repository.currentOrder!!.price + repository.currentOrder!!.getDeliveryPrice()!!
        }
        totalPriceStr.postValue("$" + totalPrice.toBigDecimal().toString())
    }

    fun confirmOrder():LiveData<Boolean>{
        if (checkedButton == R.id.rb_favor) {
            if (pointsStr.toInt() == 0){
                listener?.onFailure("Debes ofrecer al menos 1 punto de favor")
            }
            if(pointsStr.toInt() > repository.getUserPoints()){
                listener?.onFailure("No tienes puntos suficientes")
            }
        }
        return repository.confirmOrder()

    }

}
