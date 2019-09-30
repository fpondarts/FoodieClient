package com.fpondarts.foodie.ui.home.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.fpondarts.foodie.R
import com.fpondarts.foodie.databinding.CurrentOrderFragmentBinding
import com.fpondarts.foodie.ui.auth.FoodieViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.Kodein
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class CurrentOrderFragment : Fragment(), KodeinAware {


    override val kodein by kodein()

    val factory: FoodieViewModelFactory by instance()

    companion object {
        fun newInstance() = CurrentOrderFragment()
    }




    private lateinit var viewModel: CurrentOrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: CurrentOrderFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.current_order_fragment,container,false)
        viewModel = ViewModelProviders.of(this,factory).get(CurrentOrderViewModel::class.java)

        return inflater.inflate(R.layout.current_order_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CurrentOrderViewModel::class.java)
    }

}
