package com.fpondarts.foodie.ui.home.confirm_order

import android.view.View
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.ui.auth.AuthListener
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException

class ConfirmOrderViewModel(val repository: Repository) : ViewModel() {

    var points: EditText? = null
    val pointsStr = String()
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

    fun onConfirmOrder(view:View){
        if (checkedButton == R.id.rb_favor) {
            if ((pointsStr.toInt() == 0) || (pointsStr.toInt() > repository.getUserPoints())){
                //TODO manejar error de
            }
        }
        Coroutines.io {
            try{
                confirmed.postValue(repository.confirmOrder())
            } catch(e:FoodieApiException) {

            }
        }

    }

    fun onCancel(view:View){

    }

}
