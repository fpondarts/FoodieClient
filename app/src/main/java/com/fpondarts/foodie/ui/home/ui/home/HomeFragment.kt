package com.fpondarts.foodie.ui.home.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.fpondarts.foodie.R
import com.fpondarts.foodie.databinding.ActivitySignInBinding
import com.fpondarts.foodie.databinding.FragmentHomeBinding
import com.fpondarts.foodie.ui.auth.FoodieViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class HomeFragment : Fragment(), KodeinAware{

    override val kodein: Kodein by kodein()

    private val factory: FoodieViewModelFactory by instance()
    private lateinit var homeViewModel: HomeViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel =
            ViewModelProviders.of(this,factory).get(HomeViewModel::class.java)

        val binding : FragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
        binding.viewModel = homeViewModel
        shop_recycler_view.apply{

            layoutManager = LinearLayoutManager(activity)

            adapter = ShopAdapter(homeViewModel.shops)

        }


        homeViewModel.shopsLiveData.observe(this, Observer {
            shop_recycler_view.adapter?.notifyDataSetChanged()
        })

        homeViewModel.shops
        return binding.root
    }
}