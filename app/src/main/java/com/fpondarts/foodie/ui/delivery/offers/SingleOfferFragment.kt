package com.fpondarts.foodie.ui.delivery.offers


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
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.DeliveryRepository
import com.fpondarts.foodie.model.OrderItem
import com.fpondarts.foodie.model.OrderPricedItem
import kotlinx.android.synthetic.main.content_order.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_single_offer.*
import kotlinx.android.synthetic.main.fragment_single_offer.button_accept
import kotlinx.android.synthetic.main.item_offer.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.android.x.kodein


/**
 * A simple [Fragment] subclass.
 */
class SingleOfferFragment : Fragment(),KodeinAware{

    var offer_id:Long? = null
    var order_id:Long? = null
    var shop_id:Long? = null
    var user_id:Long? = null

    override val kodein by kodein()

    val repository:DeliveryRepository by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        offer_id = arguments!!.getLong("offer_id")
        order_id = arguments!!.getLong("order_id")

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
                        repository.isWorking.postValue(true)
                        findNavController().navigate(R.id.action_singleOfferFragment_to_workingFragment
                            ,bundle
                            ,NavOptions.Builder().setPopUpTo(R.id.offersFragment,true).build())
                    } else {
                        Toast.makeText(activity,"No se pudo aceptar la oferta",Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }
                }
            })
        })


        button_reject_offer.setOnClickListener(View.OnClickListener {
            repository.rejectOffer(offer_id!!).observe(this,Observer{
                it?.let {
                    if (it){
                        Toast.makeText(activity,"Oferta rechazada",Toast.LENGTH_SHORT).show()
                        fragmentManager!!.popBackStack()
                    }
                }
            })
        })



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

                val menu = repository.getMenu(shop_id!!)
                menu.observe(this@SingleOfferFragment, Observer {
                    it?.let{
                        val orderItems = repository.getOrderItems(order_id!!)
                        menu.removeObservers(this)
                        orderItems.observe(this@SingleOfferFragment, Observer {
                            it?.let {
                                val recyclerList = ArrayList<OrderPricedItem>()
                                items_recycler_view.adapter = OrderAdapter(recyclerList)
                                for (item in it){
                                    val menuItem = repository.getMenuItem(item.product_id)
                                    menuItem.observe(this@SingleOfferFragment, Observer {
                                        it?.let{
                                            recyclerList.add(OrderPricedItem(it.name,item.units,it.price))
                                            menuItem.removeObservers(this@SingleOfferFragment)
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



    }




}
