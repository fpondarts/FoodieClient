package com.fpondarts.foodie.ui.home


import android.app.Activity
import android.app.Dialog
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.fpondarts.foodie.FoodieApp

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.Delivery
import com.fpondarts.foodie.data.db.entity.Order
import com.fpondarts.foodie.data.repository.Repository
import com.squareup.picasso.Picasso
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.LazyKodein

/**
 * A simple [Fragment] subclass.
 */
class DeliveryDialog(
    val observer:LifecycleOwner,
    val offer_id: MutableLiveData<Long>,
    val order:Order,
    val delivery: Delivery,
    val price:Double=0.0,
    val pay:Double=0.0
): KodeinAware {

    override lateinit var kodein :LazyKodein

    fun showDialog(activity:Activity){
        val dialog = Dialog(activity)

        kodein = (activity.applicationContext as FoodieApp).kodein

        val repository : Repository by instance()

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.fragment_delivery_option);

        dialog.findViewById<TextView>(R.id.delivery_name).text = delivery.name
        dialog.findViewById<RatingBar>(R.id.delivery_rating).rating = delivery.rating.toFloat()
        Picasso.get().load(delivery.picture).rotate(90.0.toFloat()).into(dialog.findViewById<ImageView>(R.id.delivery_pic))

        if (order.payWithPoints) {
            dialog.findViewById<EditText>(R.id.et_favour_points).isEnabled = true
        }

        val cancel_button = dialog.findViewById<Button>(R.id.button_cancel_offer)
        cancel_button.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        val offer_button = dialog.findViewById<Button>(R.id.button_offer)

        offer_button.setOnClickListener(View.OnClickListener {
            dialog.findViewById<ProgressBar>(R.id.waitin_offer).visibility = View.VISIBLE
            cancel_button.isEnabled = false
            it.isEnabled = false
            repository.postOffer(delivery.user_id,order.order_id,price,pay).observe(
                observer, Observer {
                    it?.let {
                        if (!it.equals(-1)){
                            offer_id.postValue(it)
                            Toast.makeText(activity,"Esperando respuesta del delivery",Toast.LENGTH_LONG).show()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(activity,"No se pudo realizar la oferta",Toast.LENGTH_LONG).show()
                            offer_button.isEnabled = true
                            cancel_button.isEnabled = true
                        }
                        dialog.findViewById<ProgressBar>(R.id.waitin_offer).visibility = View.GONE
                    }
                }
            )
        })

        dialog.show()

    }

}
