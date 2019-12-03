package com.fpondarts.foodie.ui.delivery.delivered

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.DeliveryRepository
import com.fpondarts.foodie.ui.my_orders.OnMyOrderClickListener
import kotlinx.android.synthetic.main.fragment_delivered_orders.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DeliveredOrdersFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class DeliveredOrdersFragment : Fragment(), KodeinAware, OnDeliveredOrderClickListener {


    override fun onOrderClick(order_id: Long, shop_id: Long, user_id: Long) {
        val bundle = bundleOf().apply {
            putLong("order_id", order_id)
            putLong("shop_id",shop_id)
            putLong("user_id",user_id)
        }
        findNavController().navigate(R.id.action_delivered_orders_to_delivered_order,bundle)
    }

    override val kodein by kodein()

    val repository : DeliveryRepository by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_delivered_orders,container,false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        delivered_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        repository.getDeliveredByMe().observe(this, Observer {
            it?.let{
                if (! it.isNullOrEmpty() ){
                    delivered_recycler_view.adapter = DeliveredOrdersAdapter(it,this)
                    delivered_recycler_view.adapter!!.notifyDataSetChanged()
                }
            }
        })

    }



}
