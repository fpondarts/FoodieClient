package com.fpondarts.foodie.ui.home.ui.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.databinding.FragmentMenuItemBottomSheetBinding
import com.fpondarts.foodie.ui.auth.AuthListener
import com.fpondarts.foodie.ui.auth.FoodieViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_menu_item_bottom_sheet.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class MenuItemBottomSheet : BottomSheetDialogFragment(), KodeinAware, AuthListener {


    override val kodein by kodein()

    private val factory: FoodieViewModelFactory by instance()

    private var mViewModel:MIBottomSheetViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding: FragmentMenuItemBottomSheetBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_menu_item_bottom_sheet,container,false)
        mViewModel = ViewModelProviders.of(this,factory).get(MIBottomSheetViewModel::class.java)
        mViewModel!!.listener = this


        binding.viewModel = mViewModel

        bs_number_picker.minValue = 1
        bs_number_picker.maxValue = 99

        bs_item_name.text = arguments?.getString("itemName")
        bs_price.text = "- $"+arguments?.getFloat("price").toString()

        bs_cancel.setOnClickListener(View.OnClickListener {
            childFragmentManager.popBackStack()
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let{
            mViewModel!!.itemId = it.getInt("itemId")
            mViewModel!!.itemPrice = it.getFloat("price")
        } ?: kotlin.run {
            childFragmentManager.popBackStack()
        }
    }

    override fun onStarted() {
    }

    override fun onSuccess() {
        Toast.makeText(context,"Agregado al pedido",Toast.LENGTH_SHORT).show()
        childFragmentManager.popBackStack()
    }

    override fun onFailure(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        childFragmentManager.popBackStack()
    }


}
