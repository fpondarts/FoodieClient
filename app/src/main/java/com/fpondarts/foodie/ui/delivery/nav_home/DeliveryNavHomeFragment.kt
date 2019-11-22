package com.fpondarts.foodie.ui.delivery.nav_home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.DeliveryRepository
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.KodeinAware

/**
 * A simple [Fragment] subclass.
 */
class DeliveryNavHomeFragment : Fragment(), KodeinAware {


    override val kodein by kodein()

    val repository: DeliveryRepository by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivery_nav_home, container, false)
    }


}
