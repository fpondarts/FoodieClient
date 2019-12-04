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
import com.fpondarts.foodie.data.repository.UserRepository
import com.fpondarts.foodie.data.repository.interfaces.OrderRepository
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


    override fun onOrderClick(order_id: Long, isFavour: Boolean) {
        val bundle = bundleOf().apply {
            putLong("order_id", order_id)
            putBoolean("is_favour",isFavour)
        }
        findNavController().navigate(R.id.action_delivered_orders_to_delivered_order,bundle)
    }

    override val kodein by kodein()

    private lateinit var repository: OrderRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val isDelivery = arguments!!.getBoolean("is_delivery",false)

        if (isDelivery){
            val delRepo : DeliveryRepository by instance()
            repository = delRepo
        } else {
            val userRepo : UserRepository by instance()
            repository = userRepo

        }

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
