package com.fpondarts.foodie.ui.my_orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fpondarts.foodie.R
import com.fpondarts.foodie.databinding.FragmentMyOrdersBinding
import com.fpondarts.foodie.ui.auth.FoodieViewModelFactory
import kotlinx.android.synthetic.main.fragment_my_orders.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class MyOrdersFragment : Fragment(), KodeinAware, OnActiveOrderClickListener, OnPastOrderClickListener {

    override val kodein by kodein()

    private var viewModel : MyOrdersViewModel? = null

    val factory: FoodieViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentMyOrdersBinding = DataBindingUtil.inflate(inflater,R.layout.current_order_fragment,container,false)
        viewModel = ViewModelProviders.of(this,factory).get(MyOrdersViewModel::class.java)
        binding.viewModel = viewModel
        return inflater.inflate(R.layout.current_order_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActiveOrderClick() {



    }

    override fun onPastOrderClick() {



    }
}