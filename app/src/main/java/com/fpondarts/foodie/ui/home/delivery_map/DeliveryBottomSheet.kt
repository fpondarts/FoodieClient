package com.fpondarts.foodie.ui.home.delivery_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.fpondarts.foodie.R
import com.fpondarts.foodie.model.OfferState
import com.fpondarts.foodie.ui.FoodieViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_delivery_bottom_sheet.*
import kotlinx.android.synthetic.main.item_delivery.delivery_name
import kotlinx.android.synthetic.main.item_delivery.delivery_rating
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class DeliveryBottomSheet : BottomSheetDialogFragment(), KodeinAware{

    var mViewModel: DeliveryViewModel? = null

    override val kodein by kodein()
    val factory: FoodieViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewModel = ViewModelProviders.of(this,factory).get(DeliveryViewModel::class.java)


        return inflater.inflate(R.layout.fragment_delivery_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments!!.getString("name")
        val rating = arguments!!.getFloat("rating")

        delivery_name.text = name
        delivery_rating.rating = rating

        mViewModel!!.offerState.observe(this, Observer {
            it?.let{
                if (it == OfferState.WAITING){

                } else if (it == OfferState.ACCEPTED){

                } else if (it == OfferState.REJECTED){

                }
            }
        })

        delivery_offer_button.setOnClickListener {

        }





    }


}
