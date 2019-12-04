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
import com.fpondarts.foodie.data.repository.UserRepository
import com.fpondarts.foodie.data.repository.interfaces.RepositoryInterface
import com.fpondarts.foodie.model.OrderPricedItem
import com.fpondarts.foodie.ui.delivery.offers.OrderAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_prices.*
import kotlinx.android.synthetic.main.card_rated.*
import kotlinx.android.synthetic.main.card_shop.*
import kotlinx.android.synthetic.main.card_user.*
import kotlinx.android.synthetic.main.content_order.*
import kotlinx.android.synthetic.main.recycler_order_items.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import kotlin.math.round

/**
 * A simple [Fragment] subclass.
 */
class DeliveredOrderFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private lateinit var repository: RepositoryInterface

    private var order_id: Long = -1
    private var favour = false

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
        favour = arguments!!.getBoolean("is_favour")

        if (favour){
            val userRepository: UserRepository by instance()
            repository = userRepository
        } else {
            val delRepository: DeliveryRepository by instance()
            repository = delRepository
        }



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
                            dialog?.dismiss()
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

                delivery_price_title.text = "Tu ganancia"
                if (it.payWithPoints){
                    delivery_price.text = "${it.favourPoints} puntos"
                } else {
                    val roundedPay = round(it.delivery_pay!! * 100.0) / 100.0
                    delivery_price.text = "$${roundedPay}"
                }

                val roundedPrice = round(it.price * 100.0)/ 100.0
                order_price.text = "$$roundedPrice"

                repository.getUser(it.user_id).observe(this, Observer {
                    it?.let{
                        user = it
                        tv_user_name.text = it.name
                        tv_email.text = it.email
                        tv_phone.text = it.phone_number
                        Picasso.get().load(it.picture).rotate(90.0.toFloat()).into(profilePic)
                    }
                })

                rating.isEnabled = false
                rating_caption.visibility = View.INVISIBLE

                it.delivery_review?.let{
                    rating.rating = it
                } ?.run {
                    rating_caption.visibility = View.VISIBLE
                }

                order.removeObservers(this)
            }
        })
    }

}
