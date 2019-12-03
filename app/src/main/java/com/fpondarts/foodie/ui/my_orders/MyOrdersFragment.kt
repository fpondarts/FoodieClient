package com.fpondarts.foodie.ui.my_orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.UserRepository
import com.fpondarts.foodie.databinding.FragmentMyOrdersBinding
import com.fpondarts.foodie.ui.FoodieViewModelFactory
import kotlinx.android.synthetic.main.fragment_my_orders.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class MyOrdersFragment : Fragment(), KodeinAware, OnMyOrderClickListener{

    override val kodein by kodein()

    private var viewModel : MyOrdersViewModel? = null

    val repository: UserRepository by instance()

    val factory: FoodieViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentMyOrdersBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_orders,container,false)
        viewModel = ViewModelProviders.of(this,factory).get(MyOrdersViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_active_orders.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        recycler_past_orders.apply{
            layoutManager = LinearLayoutManager(activity)
        }

        repository.getActiveOrders().observe(this, Observer {
            it?.let{
                recycler_active_orders.adapter = ActiveOrderAdapter(it,true,this)
                if (it.size > 0)
                    recycler_active_orders.adapter!!.notifyItemInserted(it.size-1)

            }
        })

        viewModel!!.getDeliveredOrders().observe(this, Observer {
            it?.let{
                recycler_past_orders.adapter = ActiveOrderAdapter(it,false,this)
                if (it.size > 0)
                    recycler_past_orders.adapter!!.notifyItemInserted(it.size-1)
            }
        })
        viewModel!!.getDeliveredOrders()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onActiveOrderClick(active:Boolean,orderId:Long,shopId:Long?,deliveryId:Long?){
        if (active){
            val bundle = bundleOf("orderId" to orderId)
            findNavController().navigate(R.id.activeOrderFragment,bundle)
        } else {
            val bundle = bundleOf("orderId" to orderId, "shop_id" to shopId, "delivery_id" to deliveryId)
            findNavController().navigate(R.id.pastOrderFragment,bundle)
        }
    }

}