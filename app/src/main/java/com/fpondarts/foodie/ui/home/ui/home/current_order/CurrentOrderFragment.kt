package com.fpondarts.foodie.ui.home.ui.home.current_order

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.fpondarts.foodie.R
import com.fpondarts.foodie.databinding.CurrentOrderFragmentBinding
import com.fpondarts.foodie.model.OrderItem
import com.fpondarts.foodie.ui.auth.FoodieViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.current_order_fragment.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class CurrentOrderFragment : BottomSheetDialogFragment(), KodeinAware, OnOrderItemClickListener{


    override fun onItemClick(item: OrderItem) {
        viewModel?.removeFromOrder(item.id)
        current_order_recycler_view.adapter!!.notifyDataSetChanged()
    }


    override val kodein by kodein()

    val factory: FoodieViewModelFactory by instance()

    companion object {
        fun newInstance() = CurrentOrderFragment()
    }




    private var viewModel: CurrentOrderViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: CurrentOrderFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.current_order_fragment,container,false)
        viewModel = ViewModelProviders.of(this,factory).get(CurrentOrderViewModel::class.java)
        binding.viewModel = viewModel
        return inflater.inflate(R.layout.current_order_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel!!.getCurrentOrder()

        current_order_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        MutableLiveData<MutableCollection<OrderItem>>().apply {
            value = viewModel!!.getCurrentOrder().items!!.values
        }.observe(this, Observer{
            it?.let{
                current_order_recycler_view.adapter = CurrentOrderAdapter(it,this)
                current_order_recycler_view.adapter!!.notifyDataSetChanged()
            }

        })
    }

}
