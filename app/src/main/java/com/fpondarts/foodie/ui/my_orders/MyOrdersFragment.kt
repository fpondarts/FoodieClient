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
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class MyOrdersFragment : Fragment(), KodeinAware {

    private lateinit var myOrdersViewModel: MyOrdersViewModel

    override val kodein by kodein()

    val factory: FoodieViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentMyOrdersBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_orders,container,false)
        val viewModel = ViewModelProviders.of(this,factory).get(MyOrdersViewModel::class.java)
        binding.viewModel = viewModel


        val root = inflater.inflate(R.layout.fragment_my_orders, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        myOrdersViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}