package com.fpondarts.foodie.ui.home


import android.app.Activity
import android.app.Dialog
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.fpondarts.foodie.FoodieApp

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.Delivery
import com.fpondarts.foodie.data.db.entity.Order
import com.fpondarts.foodie.data.repository.Repository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_delivery_option.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.android.x.kodein
import java.lang.Math.round

/**
 * A simple [Fragment] subclass.
 */
class DeliveryDialog(

): DialogFragment(), KodeinAware {

    override val kodein by kodein()

    val repository: Repository by instance()

    var imageView : ImageView? = null

    var url :String? = null

    companion object {


        /**
         * Create a new instance of CustomDialogFragment, providing "num" as an
         * argument.
         */
        fun newInstance(withPoints:Boolean,
                        delivery_id:Long,
                        order_id:Long,
                        price:Double,
                        pay:Double,
                        rating:Double,
                        pic_url:String?): DeliveryDialog {
            val f = DeliveryDialog()

            // Supply num input as an argument.
            val args = Bundle()
            args.putBoolean("withPoints",withPoints)
            args.putLong("order_id",order_id)
            args.putLong("delivery_id",delivery_id)
            args.putDouble("price",price)
            args.putDouble("pay",pay)
            args.putDouble("rating",rating)
            args.putString("pic",pic_url)


            f.arguments = args

            return f
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var withPoints: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_delivery_option,container,false)


        var offer_button = rootView.findViewById<Button>(R.id.button_offer)
        var cancel_button = rootView.findViewById<Button>(R.id.button_cancel_offer)

        var points = rootView.findViewById<EditText>(R.id.et_favour_points)

        withPoints = arguments!!.getBoolean("withPoints")

        cancel_button.setOnClickListener(View.OnClickListener {
            dismiss()
        })

        points.isEnabled = withPoints


        var rating = arguments!!.getDouble("rating")
        var ratingBar = rootView.findViewById<RatingBar>(R.id.delivery_rating)
        ratingBar.rating = rating.toFloat()

        val delivery_id = arguments!!.getLong("delivery_id")
        val order_id = arguments!!.getLong("order_id")
        val price = arguments!!.getDouble("price",0.0)
        val pay = arguments!!.getDouble("pay",0.0)

        val progressBar = rootView.findViewById<ProgressBar>(R.id.waitin_offer)

        val priceText = rootView.findViewById<TextView>(R.id.price_tv)

        url = arguments!!.getString("pic")

        progressBar.visibility = View.GONE

        imageView = rootView.findViewById(R.id.delivery_pic)



        offer_button.setOnClickListener(View.OnClickListener {
            progressBar.visibility = View.VISIBLE
            cancel_button.isEnabled = false
            it.isEnabled = false

            if (withPoints){
                if (points.text.isNullOrBlank()){
                    Toast.makeText(activity,"Debe ingresar los puntos a ofrecer",Toast.LENGTH_LONG).show()
                }
                else {
                    val number = points.text.toString().toInt()
                    if (number <= 0){
                        Toast.makeText(activity,"Debe ofrecer al menos 1 punto",Toast.LENGTH_LONG).show()
                    } else if (number > repository.currentUser.value!!.favourPoints){
                        Toast.makeText(activity,"No tienes suficientes puntos",Toast.LENGTH_LONG).show()
                    } else {
                        repository.postFavourOffer(delivery_id,order_id,number)
                    }
                }
            } else {
                repository.postOffer(delivery_id,order_id,price,pay).observe(
                    this, Observer {
                        it?.let {
                            if (it > 0){
                                repository.currentOfferId.postValue(it)
                                Toast.makeText(activity,"Esperando respuesta del delivery",Toast.LENGTH_LONG).show()
                                dismiss()
                            } else {
                                Toast.makeText(activity,"No se pudo realizar la oferta",Toast.LENGTH_LONG).show()
                                offer_button.isEnabled = true
                                cancel_button.isEnabled = true
                            }
                            progressBar.visibility = View.GONE
                        }
                    }
                )
            }

        })
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val price = arguments!!.getDouble("price")
        val delivery_id = arguments!!.getLong("delivery_id")


        if (withPoints){
            price_tv.text = "No aplica"

            repository.getUser(delivery_id).observe(this, Observer {

                delivery_rating.rating = it.rating.toFloat()
                delivery_name.text = it.name

                Picasso.get().load(it.picture)
                    .resize(80,80)
                    .rotate(270.0.toFloat()).into(delivery_pic)

            })
        }


        repository.getDelivery(delivery_id).observe(this, Observer {
            it?.let{
                delivery_rating.rating = it.rating.toFloat()
                delivery_name.text = it.name

                val price = arguments!!.getDouble("price",0.0)

                if (withPoints){
                    price_tv.text = "No aplica"
                } else {
                    if (price == 0.0)
                        price_tv.text = "Gratis!"
                    else
                        price_tv.text = "$${(round(price * 100.0) / 100.0)}}"
                }

                Picasso.get().load(it.picture)
                    .resize(80,80)
                    .rotate(270.0.toFloat()).into(delivery_pic)
            }
        })

    }

    override fun onResume() {
        super.onResume()

        url?.let{
            Picasso.get().load(url)
                .resize(80,80)
                .rotate(270.0.toFloat()).into(delivery_pic)
        }


    }

}
