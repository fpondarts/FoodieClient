package com.fpondarts.foodie.ui.home.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.MenuItem
import com.fpondarts.foodie.ui.auth.FoodieViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.shop_menu_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ShopMenuFragment : Fragment(), KodeinAware, OnMenuItemClickListener {

    override val kodein by kodein()

    val factory: FoodieViewModelFactory by instance()
    private val shopId:Int = 0
    companion object {
        fun newInstance() = ShopMenuFragment()
    }

    private lateinit var viewModel: ShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shop_menu_fragment, container, false)
        shopId = arguments!!.getInt("shopId")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this,factory).get(ShopViewModel::class.java)

        shop_menu_recycler_view.apply {

            layoutManager = LinearLayoutManager(activity)

        }

        shop_menu_recycler_view.adapter = ShopMenuAdapter(ArrayList<MenuItem>(),this)


        viewModel.setShop(shopId).observe(this, Observer {
            it?.let{
                viewModel.updateMenu(it)
                shop_menu_recycler_view.adapter?.notifyDataSetChanged()
            }
        })

    }

    override fun onItemClick(item: MenuItem) {
        val dialog = MenuItemBottomSheet().apply{
            arguments = Bundle().apply {
                putInt("itemId",item.itemId)
                putString("name",item.name)
            }
        }

        dialog.show(childFragmentManager,"MenuItemBottom")
    }



}
