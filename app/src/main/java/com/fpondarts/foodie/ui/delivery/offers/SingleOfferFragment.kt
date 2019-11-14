package com.fpondarts.foodie.ui.delivery.offers


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.DeliveryRepository
import kotlinx.android.synthetic.main.fragment_single_offer.*
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

        repository.getOrderItems(order_id!!).observe(this, Observer {
            it?.let{
                items_recycler_view
            }
        })

    }



}
