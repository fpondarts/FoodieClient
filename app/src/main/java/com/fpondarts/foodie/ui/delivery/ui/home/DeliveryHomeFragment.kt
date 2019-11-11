package com.fpondarts.foodie.ui.delivery.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.fpondarts.foodie.R

class DeliveryHomeFragment : Fragment() {

    companion object {
        fun newInstance() = DeliveryHomeFragment()
    }

    private lateinit var viewModel: DeliveryHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_delivery_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DeliveryHomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
