package com.fpondarts.foodie.ui.home.confirm_order

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation

import com.fpondarts.foodie.R
import com.fpondarts.foodie.databinding.ConfirmOrderFragmentBinding
import com.fpondarts.foodie.ui.auth.AuthListener
import com.fpondarts.foodie.ui.FoodieViewModelFactory
import kotlinx.android.synthetic.main.confirm_order_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ConfirmOrderFragment : Fragment(), AuthListener, KodeinAware {



    override fun onStarted() {


    }

    override fun onSuccess() {


    }

    override fun onFailure(message: String) {


    }

    companion object {
        fun newInstance() =
            ConfirmOrderFragment()
    }


    override val kodein by kodein()

    val factory: FoodieViewModelFactory by instance()

    private var viewModel: ConfirmOrderViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ConfirmOrderFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.confirm_order_fragment,container,false)
        val viewModel = ViewModelProviders.of(this,factory).get(ConfirmOrderViewModel::class.java)
        binding.viewModel = viewModel
        this.viewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        et_points.isEnabled = false
        viewModel!!.points = et_points

        viewModel!!.totalPriceStr.observe(this, Observer {
            total_price.text = it!!
        })

        viewModel!!.confirmed.observe(this, Observer {
            if (it) {
                Navigation.findNavController(parentFragment!!.view!!).navigate(R.id.deliveryMapFragment)
            }
        })


    }


}
