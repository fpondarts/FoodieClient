package com.fpondarts.foodie.ui.home.delivery_map

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpondarts.foodie.R

class CancelOrderDialog: DialogFragment() {


    internal lateinit var listener: CancelDialogListener

    interface CancelDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    companion object {
        fun newInstance(mensaje:String):CancelOrderDialog{
            val bundle = bundleOf("mensaje" to mensaje)
            val f = CancelOrderDialog()
            f.arguments = bundle
            return f
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction

            val msj = arguments!!.getString("mensaje","Si elige salir, la orden se cancelará")
            val builder = AlertDialog.Builder(it)
            builder.setMessage(msj)
                .setPositiveButton("Salir",
                    DialogInterface.OnClickListener { dialog, id ->
                        val options = NavOptions.Builder().setPopUpTo(R.id.deliveryMapFragment,true).build()
                        findNavController().navigate(R.id.action_deliveryMapFragment_to_nav_home,null,options)
                    })
                .setNegativeButton("Quedarme",
                    DialogInterface.OnClickListener { dialog, id ->
                        dismiss()
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}