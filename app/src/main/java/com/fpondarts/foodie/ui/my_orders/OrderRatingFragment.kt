package com.fpondarts.foodie.ui.my_orders

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.fpondarts.foodie.R

class OrderRatingFragment : Fragment() {

    companion object {
        fun newInstance() = OrderRatingFragment()
    }

    private lateinit var viewModel: OrderRatingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_rating, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OrderRatingViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
