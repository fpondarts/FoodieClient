package com.fpondarts.foodie.ui.my_orders


import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.model.OrderPricedItem
import com.fpondarts.foodie.ui.delivery.offers.OrderAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_prices.view.*
import kotlinx.android.synthetic.main.card_shop.*
import kotlinx.android.synthetic.main.card_to_rate.view.*
import kotlinx.android.synthetic.main.card_user.*
import kotlinx.android.synthetic.main.content_order.*
import kotlinx.android.synthetic.main.fragment_delivery_option.*
import kotlinx.android.synthetic.main.fragment_past_order.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class PastOrderFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    var progressDialog: ProgressDialog? = null

    val repository: Repository by instance()

    private var orderId:Long? = null

    private var shopId:Long? = null

    private var deliveryId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        orderId = arguments!!.getLong("orderId")
        return inflater.inflate(R.layout.fragment_past_order, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        items_recycler_view?.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        val order = repository.getOrder(orderId!!)
        order.observe(this, Observer {
            it?.let {
                shopId = it.shop_id
                deliveryId = it.delivery_id
                val menu = repository.getShopMenu(it.shop_id!!)
                menu.observe(this, Observer {
                    it?.let {
                        val orderItems = repository.getOrderItems(orderId!!)
                        menu.removeObservers(this)
                        orderItems.observe(this, Observer {
                            it?.let {
                                val recyclerList = ArrayList<OrderPricedItem>()
                                items_recycler_view.adapter = OrderAdapter(recyclerList)
                                for (item in it) {
                                    val menuItem = repository.getMenuItem(item.product_id)
                                    menuItem.observe(this, Observer {
                                        it?.let {
                                            recyclerList.add(
                                                OrderPricedItem(
                                                    it.name,
                                                    item.units,
                                                    it.price
                                                )
                                            )
                                            menuItem.removeObservers(this)
                                            items_recycler_view.adapter!!.notifyItemInserted(
                                                recyclerList.size - 1
                                            )
                                        }
                                    })
                                }

                            }
                        })
                    }
                })
                repository.getShop(it.shop_id).observe(this, Observer {
                    it?.let{
                        tv_shop_name.text = it.name
                        Picasso.get().load(it.photoUrl).rotate(0.0.toFloat()).into(shopPic)
                        tv_shop_address.text = it.address
                        shop_rating_card.card_rating_name.text = it.name

                    }
                })
                repository.getDelivery(it.delivery_id).observe(this, Observer {
                    it?.let{
                        tv_user_name.text = it.name
                        tv_email.text = it.email
                        tv_phone.text = it.phone_number
                        delivery_rating_card.card_rating_name.text = it.name
                        Picasso.get().load(it.picture).rotate(90.0.toFloat()).into(profilePic)
                    }
                })
                order.removeObservers(this)

                price_card.order_price.text = "$${it.price}"
            }

            delivery_rating_card.card_rating_title.text = "Calificación del envío"
            shop_rating_card.card_rating_title.text = "Calificación de la tienda"


            val rate_delivery = delivery_rating_card.rate_button
            val rate_shop = shop_rating_card.rate_button

            if (it.delivery_review == null || it.delivery_review == (0.0).toFloat()){

                rate_delivery.isEnabled = true

                rate_delivery.setOnClickListener(View.OnClickListener {
                    this.rateDelivery()
                })

            } else {
                rate_delivery.isEnabled = false
                delivery_rating_card.card_rating_rating.rating = it.delivery_review!!.toFloat()
                delivery_rating_card.card_rating_rating.isEnabled = false

            }

            if (it.shop_review == null || it.shop_review == (0.0).toFloat()){

                rate_shop.isEnabled = true

                rate_shop.setOnClickListener(View.OnClickListener {
                    rateShop()
                })

            } else {
                rate_shop.isEnabled = false
                shop_rating_card.card_rating_rating.rating = it.shop_review!!.toFloat()
                shop_rating_card.card_rating_rating.isEnabled = false
            }
        })
    }


    fun rateShop(){
        progressDialog = ProgressDialog.show(context,"Enviando calificacion","Espere")
        val rating = shop_rating_card.card_rating_rating.rating
        if ( rating > 0) {
            repository.rateShop(orderId!!,rating).observe(this, Observer {
                it?.let{
                    if (it.code in 199..299){
                          shop_rating_card.card_rating_rating.isEnabled = false
                          shop_rating_card.rate_button.isEnabled = false
                          Toast.makeText(activity,"Tu calificación se ha procesado con exito",Toast.LENGTH_SHORT).show()
                          progressDialog?.dismiss()
                    } else {
                          Toast.makeText(activity,"Hubo un error en el proceso, intente nuevamente",Toast.LENGTH_LONG).show()
                          progressDialog?.dismiss()
                    }
                }
            })
        } else {
            progressDialog?.dismiss()
        }
    }

    fun rateDelivery(){
        progressDialog = ProgressDialog.show(context,"Enviando calificacion","Espere")
        val rating = delivery_rating_card.card_rating_rating.rating
        if ( rating > 0) {
            repository.rateDelivery(orderId!!,rating).observe(this, Observer {
                it?.let{
                    if (it.code in 199..299){
                        delivery_rating_card.card_rating_rating.isEnabled = false
                        delivery_rating_card.rate_button.isEnabled = false
                        Toast.makeText(activity,"Tu calificación se ha procesado con exito",Toast.LENGTH_SHORT).show()
                        progressDialog?.dismiss()
                    } else {
                        Toast.makeText(activity,"Hubo un error en el proceso, intente nuevamente",Toast.LENGTH_LONG).show()
                        progressDialog?.dismiss()
                    }
                }
            })

        } else {
            progressDialog?.dismiss()
        }
    }

}
