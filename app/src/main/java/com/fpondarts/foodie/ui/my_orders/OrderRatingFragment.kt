package com.fpondarts.foodie.ui.my_orders

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.fpondarts.foodie.R
import com.fpondarts.foodie.databinding.FragmentOrderRatingBinding
import com.fpondarts.foodie.ui.FoodieViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class OrderRatingFragment : Fragment(), KodeinAware {


    override val kodein by kodein()

    val factory: FoodieViewModelFactory by instance()

    var mViewModel: OrderRatingViewModel? = null

    companion object {
        fun newInstance() = OrderRatingFragment()
    }

    private lateinit var viewModel: OrderRatingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewModel = ViewModelProviders.of(this, factory).get(OrderRatingViewModel::class.java)
        val binding: FragmentOrderRatingBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_order_rating, container, false)

        binding.viewModel = mViewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val orderId = arguments!!.getLong("orderId")
        val shopId = arguments!!.getLong("shop_id")
        val deliveryId = arguments!!.getLong("deliveryId")

        viewModel.getOrder(orderId).observe(this, Observer {
            it?.let{
                it.shopRating?.let{
                    viewModel.shopRating = it
                } ?.run{
                    viewModel.rateShopEnabled = true
                }

                it.deliveryRating?.let{
                    viewModel.deliveryRating = it
                } ?.run {
                    viewModel.rateDeliveryEnabled = true
                }
            }

        })

        viewModel.getDelivery(deliveryId).observe(this, Observer {

            it?.let{

                viewModel.deliveryName = it.name

            }

        })

        viewModel.getShop(shopId).observe(this, Observer {

            it.let{

                viewModel.shopName = it.name

            }
        })


    }

}
