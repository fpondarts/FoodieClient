package com.fpondarts.foodie.ui.favours.workingFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.model.OrderPricedItem
import com.fpondarts.foodie.ui.delivery.offers.OrderAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_prices.*
import kotlinx.android.synthetic.main.card_shop.*
import kotlinx.android.synthetic.main.card_user.*
import kotlinx.android.synthetic.main.content_order.*
import kotlinx.android.synthetic.main.fragment_working.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import kotlin.math.round

/**
 * A simple [Fragment] subclass.
 */
class WorkingFavourFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    val repository: Repository by instance()

    var order_id:Long? = null
    var shop_id: Long? = null
    var user_id: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //order_id = arguments!!.getLong("order_id")

        order_id = arguments!!.getLong("order_id")

        return inflater.inflate(R.layout.fragment_working, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        items_recycler_view?.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        val order = repository.getOrder(order_id!!)
        order.observe(this, Observer {
            it?.let{

                shop_id = it.shop_id
                user_id = it.user_id

                repository.getShop(shop_id!!).observe(this, Observer {
                    it?.let{
                        tv_shop_name.text = it.name
                        tv_shop_address.text = it.address
                        Picasso.get().load(it.photoUrl).resize(64,64).into(shopPic)
                    }
                })

                repository.getUser(user_id!!).observe(this, Observer {
                    it?.let{
                        Picasso.get().load(it.picture).resize(64,64).into(profilePic)
                        tv_user_name.text = it.name
                        tv_email.text = it.email
                        tv_phone.text = it.phone_number
                    }
                })

                repository.getOrder(order_id!!).observe(this, Observer {
                    it?.let{
                        delivery_price.text = "${it.favourPoints!!.toString()} puntos"
                        order_price.text = "$${(round(it.price * 100.0) / 100.0).toString()}"
                        delivery_price_title.text = "Tu ganancia"
                    }
                })

                val menu = repository.getShopMenu(shop_id!!)
                menu.observe(this, Observer {
                    it?.let{
                        val orderItems = repository.getOrderItems(order_id!!)
                        menu.removeObservers(this)
                        orderItems.observe(this, Observer {
                            it?.let {
                                val recyclerList = ArrayList<OrderPricedItem>()
                                items_recycler_view.adapter = OrderAdapter(recyclerList)
                                for (item in it){
                                    val menuItem = repository.getMenuItem(item.product_id)
                                    menuItem.observe(this, Observer {
                                        it?.let{
                                            recyclerList.add(OrderPricedItem(it.name,item.units,it.price))
                                            menuItem.removeObservers(this)
                                            items_recycler_view.adapter!!.notifyItemInserted(recyclerList.size-1)
                                        }
                                    })
                                }

                            }
                        })
                    }
                })
                order.removeObservers(this)
            }
        })

        finish_order_card.setOnClickListener(View.OnClickListener {
            Toast.makeText(context,"Click", Toast.LENGTH_LONG).show()
            repository.finishDelivery(order_id!!).observe(this, Observer {
                it?.let{
                    if (it){
                        repository.isWorking.postValue(false)
                        repository.current_order = -1
                        findNavController().navigate(R.id.action_workingFragment_to_offersFragment,null,
                            NavOptions.Builder().setPopUpTo(R.id.workingFragment,true).build())
                    } else {
                        Toast.makeText(activity,"Error en la entrega del pedido", Toast.LENGTH_LONG).show()
                    }
                }
            })
        })


    }

}
