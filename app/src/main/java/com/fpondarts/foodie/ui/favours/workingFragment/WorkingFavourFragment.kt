package com.fpondarts.foodie.ui.favours.workingFragment


import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.Order
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.data.repository.UserRepository
import com.fpondarts.foodie.model.OrderPricedItem
import com.fpondarts.foodie.ui.delivery.offers.OrderAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_pick_up.*
import kotlinx.android.synthetic.main.card_prices.*
import kotlinx.android.synthetic.main.card_shop.*
import kotlinx.android.synthetic.main.card_user.*
import kotlinx.android.synthetic.main.content_active_order.*
import kotlinx.android.synthetic.main.content_order.*
import kotlinx.android.synthetic.main.content_order.choose_location_card
import kotlinx.android.synthetic.main.fragment_working.*
import kotlinx.android.synthetic.main.recycler_order_items.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import kotlin.math.round

/**
 * A simple [Fragment] subclass.
 */
class WorkingFavourFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    val repository: UserRepository by instance()

    var order_id:Long? = null
    var shop_id: Long? = null
    var user_id: Long? = null

    private lateinit var shop: Shop
    private lateinit var order: Order
    private lateinit var theirFbId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //order_id = arguments!!.getLong("order_id")

        order_id = arguments!!.getLong("order_id")

        return inflater.inflate(R.layout.fragment_working, container, false)
    }

    private var dialog: ProgressDialog? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        items_recycler_view?.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        dialog = ProgressDialog.show(activity,"Espere","Cargando orden")

        val order = repository.getOrder(order_id!!)
        order.observe(this, Observer {
            it?.let{
                this.order = it
                shop_id = it.shop_id
                user_id = it.user_id

                repository.getShop(shop_id!!).observe(this, Observer {
                    it?.let{
                        this.shop = it
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
                        theirFbId= it.firebase_uid!!
                    }
                })

                delivery_price.text = "${it.favourPoints!!.toString()} puntos"
                order_price.text = "$${(round(it.price * 100.0) / 100.0).toString()}"
                delivery_price_title.text = "Tu ganancia"

                if (it.state == "pickedUp")
                    finish_order_tv.text = "Finalizar pedido"


                val menu = repository.getMenu(shop_id!!)
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
                                dialog?.dismiss()

                            }
                        })
                    }
                })

                chat_card.setOnClickListener {
                    val bundle = Bundle().apply {
                        putLong("order_id",order_id!!)
                        putString("my_id",repository.currentUser.value!!.firebase_uid)
                        putString("their_id",theirFbId)
                    }
                    findNavController().navigate(R.id.action_workingFavourFragment_to_conversationFragment,bundle)
                }

                order.removeObservers(this)
            }
        })



        finish_order_card.setOnClickListener(View.OnClickListener {
            if (this.order.state == "pickedUp"){
                repository.changeOrderState(order_id!!,"delivered").observe(this, Observer {
                    it?.let{
                        if (it){
                            repository.isWorking.postValue(false)
                            repository.current_order = -1
                            findNavController().navigate(R.id.action_workingFavourFragment_to_favourOffersFragment,null,
                                NavOptions.Builder().setPopUpTo(R.id.workingFragment,true).build())
                        } else {
                            Toast.makeText(activity,"Error en la entrega del pedido",Toast.LENGTH_LONG).show()
                        }
                    }
                })
            } else if (this.order.state == "onWay"){
                repository.changeOrderState(order_id!!,"pickedUp").observe(this, Observer {
                    it?.let{
                        if (it){
                            this.order.state = "pickedUp"
                            finish_order_tv.text = "Finalizar pedido"
                        } else {
                            Toast.makeText(activity,"Error en la entrega del pedido",Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }

        })


        choose_location_card.setOnClickListener {
            val bundle = bundleOf(
                "shop_lat" to this.shop.latitude.toFloat(),
                "shop_lon" to this.shop.longitude.toFloat(),
                "dest_lat" to this.order.latitud.toFloat(),
                "dest_lon" to this.order.longitud.toFloat(),
                "pickedUp" to (this.order.state == "pickedUp"),
                "isFavour" to this.order.payWithPoints
            )

            findNavController().navigate(R.id.action_workingFavourFragment_to_workingMapFragment2,bundle)

        }

    }

}
