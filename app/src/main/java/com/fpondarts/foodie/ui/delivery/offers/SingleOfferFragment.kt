package com.fpondarts.foodie.ui.delivery.offers


import android.os.Bundle
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

        return inflater.inflate(R.layout.fragment_single_offer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        offer_id = arguments!!.getLong("offer_id")
        order_id = arguments!!.getLong("order_id")


        button_accept.setOnClickListener(View.OnClickListener {
            repository.acceptOffer(offer_id!!).observe(this, Observer {
                it?.let {
                    if (it){
                        val bundle = bundleOf("order_id" to order_id)
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


        button_reject.setOnClickListener(View.OnClickListener {
            repository.rejectOffer(offer_id!!).observe(this,Observer{

            })
        })



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        items_recycler_view?.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        repository.getOrder(order_id!!).observe(this, Observer {
            it?.let{

            }
        })

        repository.getMenu(shop_id!!).observe(this, Observer {
            it?.let{
                repository.getOrderItems(order_id!!).observe(this, Observer {
                    it?.let {
                        val recyclerList = ArrayList<OrderPricedItem>()
                        for (item in it){
                            val menuItem = repository.getMenuItem(item.product_id).value
                            recyclerList.add(OrderPricedItem(menuItem!!.name,item.units,menuItem!!.price))
                        }

                        items_recycler_view.adapter = OrderAdapter()
                    }
                })
            }
        })

    }




}
