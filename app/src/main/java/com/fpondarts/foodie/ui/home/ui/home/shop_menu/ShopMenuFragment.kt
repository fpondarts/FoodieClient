package com.fpondarts.foodie.ui.home.ui.home.shop_menu

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.MenuItem
import com.fpondarts.foodie.databinding.FragmentShopMenuBinding
import com.fpondarts.foodie.ui.auth.FoodieViewModelFactory
import com.fpondarts.foodie.ui.home.ui.home.menu_item_sheet.MenuItemBottomSheet
import com.fpondarts.foodie.ui.home.ui.home.OnMenuItemClickListener
import kotlinx.android.synthetic.main.fragment_shop_menu.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ShopMenuFragment : Fragment(), KodeinAware,
    OnMenuItemClickListener {

    override val kodein by kodein()

    private val factory: FoodieViewModelFactory by instance()

    companion object {
        fun newInstance() = ShopMenuFragment()
    }

    private lateinit var viewModel: ShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentShopMenuBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_shop_menu,container,false)
        viewModel = ViewModelProviders.of(this,factory).get(ShopViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val shopId = arguments!!.getInt("shopId")

        shop_menu_recycler_view.adapter =
            ShopMenuAdapter(
                viewModel.liveMenu.value!!,
                this
            )

        shop_menu_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.liveMenu.observe(this, Observer {
            shop_menu_recycler_view.adapter!!.notifyDataSetChanged()
        })


    }

    override fun onItemClick(item: MenuItem) {
        val dialog = MenuItemBottomSheet().apply{
            arguments = Bundle().apply {
                putLong("itemId",item.itemId)
                putString("name",item.name)
            }
        }
        dialog.show(childFragmentManager,"MenuItemBottom")
    }



}
