package com.fpondarts.foodie.ui.home.shop_menu

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.MenuItem
import com.fpondarts.foodie.databinding.FragmentShopMenuBinding
import com.fpondarts.foodie.ui.auth.AuthListener
import com.fpondarts.foodie.ui.FoodieViewModelFactory
import com.fpondarts.foodie.ui.home.menu_item_sheet.MenuItemBottomSheet
import com.fpondarts.foodie.ui.home.OnMenuItemClickListener
import com.fpondarts.foodie.ui.home.current_order.CurrentOrderFragment
import kotlinx.android.synthetic.main.fragment_shop_menu.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ShopMenuFragment : Fragment(), KodeinAware, AuthListener,
    OnMenuItemClickListener {
    override fun onStarted() {
    }

    override fun onSuccess() {

    }

    override fun onFailure(message: String) {
        Toast.makeText(activity,message,Toast.LENGTH_SHORT).show()
    }

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
        viewModel.listener = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val shopId = arguments!!.getLong("shop_id")

        shop_menu_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        button_pedido_actual.setOnClickListener(View.OnClickListener {
            this.onViewOrder(it)
        })

        viewModel.setShop(shopId)

        viewModel.getMenu(shopId).observe(this, Observer {
            it?.let{
                shop_menu_recycler_view.adapter =
                    ShopMenuAdapter(it, this)
                shop_menu_recycler_view.adapter!!.notifyDataSetChanged()
            } ?: run {

            }
        })

        button_fin_pedido.setOnClickListener(View.OnClickListener {
            if (viewModel.repository.currentOrder!!.isEmpty()){
                Toast.makeText(activity,"El pedido está vacío",Toast.LENGTH_LONG).show()
            } else {
                findNavController().navigate(R.id.action_shopFragment_to_deliveryAddressFragment)
            }
        })

    }

    override fun onItemClick(item: MenuItem) {
        val dialog = MenuItemBottomSheet().apply{
            arguments = Bundle().apply {
                putLong("product_id",item.id)
                putFloat("price",item.price)
                putString("name",item.name)
            }
        }
        dialog.show(childFragmentManager,"MenuItemBottom")
    }

    fun onViewOrder(view:View){
        val dialog = CurrentOrderFragment()
        dialog.show(childFragmentManager,"ViewOrderBottom")
    }


}
