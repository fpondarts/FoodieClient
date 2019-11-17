package com.fpondarts.foodie.ui.home.delivery_address

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.navigation.Navigation

import com.fpondarts.foodie.R
import com.fpondarts.foodie.ui.FoodieViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.delivery_address_fragment.*
import org.kodein.di.android.x.kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class DeliveryAddressFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory: FoodieViewModelFactory by instance()

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
        viewModel = ViewModelProviders.of(this,factory).get(DeliveryAddressViewModel::class.java)


        current_location_card.setOnClickListener(View.OnClickListener {
            onCurrentLocationClick()
        })

        fusedLocationProviderClient = FusedLocationProviderClient(context!!)


    }

    fun onCurrentLocationClick(){
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {

            it?.let{
                viewModel.repository.setOrderCoordinates(it.latitude,it.longitude)
                Navigation.findNavController(parentFragment!!.view!!).navigate(R.id.confirmOrderFragment)
            } ?.run {
                Toast.makeText(activity,"No se pudo obtener las coordenaddas del dispositivo",Toast.LENGTH_LONG).show()
            }

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
