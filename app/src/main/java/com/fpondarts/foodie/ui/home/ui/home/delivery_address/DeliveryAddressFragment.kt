package com.fpondarts.foodie.ui.home.ui.home.delivery_address

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation

import com.fpondarts.foodie.R
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.delivery_address_fragment.*

class DeliveryAddressFragment : Fragment() {

    companion object {
        fun newInstance() = DeliveryAddressFragment()
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewModel: DeliveryAddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.delivery_address_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DeliveryAddressViewModel::class.java)

        choose_location_card.isClickable = true
        current_location_card.isClickable = true

        viewModel.price.observe(this, Observer {
            it?.let {
                Navigation.findNavController(parentFragment!!.view!!).navigate(R.id.confirmOrderFragment)
            }
        })

        fusedLocationProviderClient = FusedLocationProviderClient(context!!)

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {

        }

    }

    fun onCurrentLocationClick(){
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            viewModel.getDeliveryPrice(it.latitude,it.longitude)
        }.addOnFailureListener{
            Toast.makeText(activity,it.message,Toast.LENGTH_LONG).show()
        }
    }

/*    fun onChooseLocationClick(){
        val dialog = MapBottomSheetFragment().apply {

        }
        dialog.show(childFragmentManager,"GoogleMap")
    }
*/

}
