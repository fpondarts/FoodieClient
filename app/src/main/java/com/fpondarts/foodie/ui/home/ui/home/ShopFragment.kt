package com.fpondarts.foodie.ui.home.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.fpondarts.foodie.R
import com.fpondarts.foodie.ui.auth.FoodieViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ShopFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    val factory: FoodieViewModelFactory by instance()
    private val shopId:Int = 0
    companion object {
        fun newInstance() = ShopFragment()
    }

    private lateinit var viewModel: ShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shop_fragment, container, false)
        shopId = arguments!!.getInt("shopId")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this,factory).get(ShopViewModel::class.java)
        viewModel.setShop(shopId)
    }

}
