package com.fpondarts.foodie.ui.delivery.delivered


import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.Order
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.data.db.entity.User
import com.fpondarts.foodie.data.repository.DeliveryRepository
import com.fpondarts.foodie.model.OrderPricedItem
import com.fpondarts.foodie.ui.delivery.offers.OrderAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_shop.*
import kotlinx.android.synthetic.main.card_user.*
import kotlinx.android.synthetic.main.content_order.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class DeliveredOrderFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    val repository: DeliveryRepository by instance()

    private var order_id: Long = -1

    private lateinit var order: Order
    private lateinit var shop: Shop
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this f
        //fragment

        order_id = arguments!!.getLong("order_id")

        return inflater.inflate(R.layout.fragment_delivered_order, container, false)
    }

    var dialog: ProgressDialog? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        items_recycler_view?.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        dialog = ProgressDialog.show(activity,"Espere","Cargando datos de orden")

        val order = repository.getOrder(order_id!!)
        order.observe(this, Observer {
            it?.let {
                this.order = it
                val menu = repository.getMenu(it.shop_id!!)
                menu.observe(this, Observer {
                    it?.let {
                        val orderItems = repository.getOrderItems(order_id!!)
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
                        shop = it
                    }
                })

                repository.getUser(it.user_id).observe(this, Observer {
                    it?.let{
                        tv_user_name.text = it.name
                        tv_email.text = it.email
                        tv_phone.text = it.phone_number
                        Picasso.get().load(it.picture).rotate(90.0.toFloat()).into(profilePic)
                    }
                })

                repository.getUser(it.user_id).observe(this, Observer {
                    it?.let{
                        tv_user_name.text = it.name
                        tv_email.text = it.email
                        tv_phone.text = it.phone_number
                        Picasso.get().load(it.picture).rotate(90.0.toFloat()).into(profilePic)
                    }
                })

                order.removeObservers(this)
            }
        })
    }

}
