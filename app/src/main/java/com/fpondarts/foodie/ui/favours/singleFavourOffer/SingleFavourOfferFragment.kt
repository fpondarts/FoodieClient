package com.fpondarts.foodie.ui.favours.singleFavourOffer


import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
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
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.model.OrderPricedItem
import com.fpondarts.foodie.ui.delivery.offers.OrderAdapter
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_prices.*
import kotlinx.android.synthetic.main.card_shop.*
import kotlinx.android.synthetic.main.card_user.*
import kotlinx.android.synthetic.main.content_order.*
import kotlinx.android.synthetic.main.fragment_single_offer.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import kotlin.math.round

/**
 * A simple [Fragment] subclass.
 */
class SingleFavourOfferFragment : Fragment() , KodeinAware {

    var offer_id:Long? = null
    var order_id:Long? = null
    var shop_id:Long? = null
    var user_id:Long? = null
    var points: Int? = null
    override val kodein by kodein()


    val repository: Repository by instance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        offer_id = arguments!!.getLong("offer_id")
        order_id = arguments!!.getLong("order_id")
        points = arguments!!.getInt("points")

        Log.d("SingleOffer","Offer_id:${offer_id}, Order_id:${order_id}")


        return inflater.inflate(R.layout.fragment_single_offer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_accept.setOnClickListener(View.OnClickListener {
            repository.acceptOffer(offer_id!!).observe(this, Observer {
                it?.let {
                    if (it){
                        val bundle = bundleOf("order_id" to order_id)
                        repository.current_order = order_id!!
                        repository.isWorking.postValue(true)
                        findNavController().navigate(R.id.action_singleFavourOfferFragment_to_workingFavourFragment
                            ,bundle
                            , NavOptions.Builder().setPopUpTo(R.id.offersFragment,true).build())
                    } else {
                        Toast.makeText(activity,"No se pudo aceptar la oferta", Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }
                }
            })
        })


        button_reject_offer.setOnClickListener(View.OnClickListener {
            repository.rejectOffer(offer_id!!).observe(this,Observer{
                it?.let {
                    if (it){
                        Toast.makeText(activity,"Oferta rechazada", Toast.LENGTH_SHORT).show()
                        fragmentManager!!.popBackStack()
                    }
                }
            })
        })



    }

    var dialog: ProgressDialog? = null

    private lateinit var destLatLng: LatLng
    private lateinit var shopLatLng: LatLng


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        items_recycler_view?.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        dialog = ProgressDialog.show(activity,"Cargando datos",null)

        val order = repository.getOrder(order_id!!)
        order.observe(this, Observer {
            it?.let{
                shop_id = it.shop_id
                user_id = it.user_id

                destLatLng = LatLng(it.latitud,it.longitud)

                repository.getShop(shop_id!!).observe(this, Observer {
                    it?.let{
                        shopLatLng = LatLng(it.latitude,it.longitude)

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



                delivery_price.text = "${points.toString()} puntos"
                order_price.text = "$${(round(it.price * 100.0) / 100.0).toString()}"
                delivery_price_title.text = "Tu ganancia"

                val menu = repository.getShopMenu(shop_id!!)
                menu.observe(this@SingleFavourOfferFragment, Observer {
                    it?.let{
                        val orderItems = repository.getOrderItems(order_id!!)
                        menu.removeObservers(this)
                        orderItems.observe(this@SingleFavourOfferFragment, Observer {
                            it?.let {
                                val recyclerList = ArrayList<OrderPricedItem>()
                                items_recycler_view.adapter = OrderAdapter(recyclerList)
                                for (item in it){
                                    val menuItem = repository.getMenuItem(item.product_id)
                                    menuItem.observe(this@SingleFavourOfferFragment, Observer {
                                        it?.let{
                                            recyclerList.add(OrderPricedItem(it.name,item.units,it.price))
                                            menuItem.removeObservers(this@SingleFavourOfferFragment)
                                            items_recycler_view.adapter!!.notifyItemInserted(recyclerList.size-1)
                                            dialog?.dismiss()
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

        choose_location_card.setOnClickListener {
            val bundle = bundleOf("shop_lat" to shopLatLng.latitude.toFloat(),
                "shop_lon" to shopLatLng.longitude.toFloat(),
                "dest_lat" to destLatLng.latitude.toFloat(),
                "dest_lon" to destLatLng.longitude.toFloat())

            findNavController().navigate(R.id.action_singleFavourOfferFragment_to_offerMapFragment2,bundle)

        }

    }




}
