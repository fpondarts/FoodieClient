package com.fpondarts.foodie.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.os.bundleOf
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.databinding.FragmentHomeBinding
import com.fpondarts.foodie.ui.auth.AuthListener
import com.fpondarts.foodie.ui.FoodieViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class HomeFragment : Fragment(), KodeinAware, OnShopClickListener, AuthListener {
//
//    class OnBottomFetcher(val viewModel: HomeViewModel): RecyclerView.OnScrollListener(){
//        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//            super.onScrollStateChanged(recyclerView, newState)
//
//            if (!recyclerView.canScrollVertically(1)){
//                viewModel.getMoreShops();
//            }
//        }
//    }

    override fun onStarted() {
        Toast.makeText(activity,"Started api call",Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess() {
        Toast.makeText(activity,"Succeded api call",Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(message: String) {
        Toast.makeText(activity,"Failed API CALL",Toast.LENGTH_SHORT).show()
    }

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


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        shop_recycler_view?.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        homeViewModel.authListener = this

        homeViewModel.getAllShops()
            .observe(this, Observer {
            it?.let{
                if (it.isEmpty()){

                }
                shop_recycler_view.adapter = ShopAdapter(it!!, this)
                shop_recycler_view.adapter?.notifyDataSetChanged()
            }
        })

        choose_location_card.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_shopsMapFragment)
        }

//        shop_recycler_view.addOnScrollListener(OnBottomFetcher(homeViewModel))

    }

    override fun onItemClick(shop:Shop){
        val bundle = bundleOf("shop_id" to shop.id)
        navController!!.navigate(R.id.action_nav_home_to_shopFragment,bundle)
    }

}