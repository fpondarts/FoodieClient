package com.fpondarts.foodie.ui.home.menu_item_sheet


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.fpondarts.foodie.R
import com.fpondarts.foodie.databinding.FragmentMenuItemBottomSheetBinding
import com.fpondarts.foodie.ui.auth.AuthListener
import com.fpondarts.foodie.ui.FoodieViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_menu_item_bottom_sheet.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class MenuItemBottomSheet : BottomSheetDialogFragment(), KodeinAware, AuthListener {


    override val kodein by kodein()

    private val factory: FoodieViewModelFactory by instance()

    private var mViewModel: MIBottomSheetViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMenuItemBottomSheetBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_menu_item_bottom_sheet,container,false)
        mViewModel = ViewModelProviders.of(this,factory).get(MIBottomSheetViewModel::class.java)
        mViewModel!!.listener = this

        binding.viewModel = mViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bs_number_picker.minValue = 1
        bs_number_picker.maxValue = 99
        bs_price.text = "- $"+arguments?.getFloat("price").toString()
        bs_cancel.setOnClickListener(View.OnClickListener {
            childFragmentManager.popBackStack()
        })

        bs_cancel.setOnClickListener(View.OnClickListener {
            this.dismiss()
        })

        arguments?.let{
            mViewModel!!.itemId = it.getLong("id")
            mViewModel!!.itemPrice = it.getFloat("price")
            mViewModel!!.name = it.getString("name")
        } ?: kotlin.run {
            childFragmentManager.popBackStack()
        }
    }

    override fun onStarted() {
    }

    override fun onSuccess() {
        Toast.makeText(context,"Agregado al pedido",Toast.LENGTH_SHORT).show()
        this.dismiss()
    }

    override fun onFailure(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        this.dismiss()
    }


}
