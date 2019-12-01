package com.fpondarts.foodie.ui.my_orders

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.Order
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.model.OrderPricedItem
import com.fpondarts.foodie.ui.FoodieViewModelFactory
import com.fpondarts.foodie.ui.delivery.offers.OrderAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_prices.*
import kotlinx.android.synthetic.main.card_shop.*
import kotlinx.android.synthetic.main.card_user.*
import kotlinx.android.synthetic.main.content_order.*
import kotlinx.android.synthetic.main.fragment_active_order.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

open class ActiveOrderFragment : Fragment(), KodeinAware {

    companion object {
        fun newInstance() = ActiveOrderFragment()
    }

    override val kodein by kodein()

    val factory: FoodieViewModelFactory by instance()

    val repository: Repository by instance()

    private lateinit var mMap:GoogleMap

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var orderId: Long? = null

    private lateinit var order: Order

    private lateinit var shop: Shop

    private var deliveryMarker:Marker? = null

    private lateinit var viewModel: ActiveOrderViewModel

    private var dialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this,factory).get(ActiveOrderViewModel::class.java)

        orderId = arguments!!.getLong("orderId")
        return inflater.inflate(R.layout.fragment_active_order, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        items_recycler_view?.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        dialog = ProgressDialog.show(activity,"Espere","Cargando datos de orden")

        val order = repository.getOrder(orderId!!)
        order.observe(this, Observer {
            it?.let {
                this.order = it
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
                        shop = it
                    }
                })

                if (it.payWithPoints){

                    repository.getUser(it.delivery_id).observe(this, Observer {
                        it?.let{
                            tv_user_name.text = it.name
                            tv_email.text = it.email
                            tv_phone.text = it.phone_number
                            Picasso.get().load(it.picture).rotate(90.0.toFloat()).into(profilePic)
                        }
                    })
                } else {
                    repository.getDelivery(it.delivery_id).observe(this, Observer {
                        it?.let{
                            tv_user_name.text = it.name
                            tv_email.text = it.email
                            tv_phone.text = it.phone_number
                            Picasso.get().load(it.picture).rotate(90.0.toFloat()).into(profilePic)
                        }
                    })
                }


                choose_location_card.setOnClickListener {
                    val bundle = bundleOf(
                        "shop_lat" to shop.latitude.toFloat()
                        ,"shop_lon" to shop.longitude.toFloat()
                        , "dest_lat" to this.order.latitud.toFloat()
                        , "dest_lon" to this.order.longitud.toFloat()
                        , "pickedUp" to (this.order.state == "pickedUp")
                        , "isFavour" to (this.order.payWithPoints)
                        , "order_id" to this.order.order_id)
                    findNavController().navigate(R.id.action_activeOrderFragment_to_waitingOrderMapFragment,bundle)
                }
                order_price.text = "$${(Math.round(it.price!! * 100.00 )/ 100.00).toString()}"

                order.removeObservers(this)
            }
        })
    }

}
