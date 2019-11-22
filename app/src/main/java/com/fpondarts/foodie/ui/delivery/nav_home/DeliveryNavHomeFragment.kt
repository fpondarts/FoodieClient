package com.fpondarts.foodie.ui.delivery.nav_home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.fpondarts.foodie.R
import org.kodein.di.KodeinAware

/**
 * A simple [Fragment] subclass.
 */
class DeliveryNavHomeFragment : Fragment(), KodeinAware {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_delivery_nav_home, container, false)
    }


}
