package com.fpondarts.foodie.ui.home.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.databinding.ActivitySignInBinding
import com.fpondarts.foodie.databinding.FragmentHomeBinding
import com.fpondarts.foodie.ui.auth.FoodieViewModelFactory
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class HomeFragment : Fragment(), KodeinAware, OnShopClickListener {

    override val kodein: Kodein by kodein()

    private val factory: FoodieViewModelFactory by instance()
    private lateinit var homeViewModel: HomeViewModel
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel =
            ViewModelProviders.of(this,factory).get(HomeViewModel::class.java)

        val binding : FragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
        binding.viewModel = homeViewModel
        shop_recycler_view.apply {

            layoutManager = LinearLayoutManager(activity)

        }

        shop_recycler_view.adapter = ShopAdapter(homeViewModel.shops,this)




        homeViewModel.shopsLiveData.observe(this, Observer {
            shop_recycler_view.adapter?.notifyDataSetChanged()
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onItemClick(shop:Shop){
        val bundle = bundleOf("shopId" to shop.id)
        navController!!.navigate(R.id.action_nav_home_to_shopFragment,bundle)
    }

}