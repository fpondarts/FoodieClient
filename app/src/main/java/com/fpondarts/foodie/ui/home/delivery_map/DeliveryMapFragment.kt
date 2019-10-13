package com.fpondarts.foodie.ui.home.delivery_map

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import com.fpondarts.foodie.R
import com.fpondarts.foodie.ui.auth.FoodieViewModelFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import android.content.Context.LOCATION_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.location.LocationManager
import android.widget.Toast


class DeliveryMapFragment : Fragment()
    , OnMapReadyCallback
    , GoogleMap.OnCameraIdleListener,
    KodeinAware{

    override val kodein by kodein()

    val factory: FoodieViewModelFactory by instance()

    companion object {
        fun newInstance() = DeliveryMapFragment()
    }


    private lateinit var mMap:GoogleMap
    private lateinit var viewModel: DeliveryMapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this,factory).get(DeliveryMapViewModel::class.java)

        return inflater.inflate(R.layout.delivery_map_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val map = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)

        viewModel.availableDeliveries.observe(this, Observer {
            mMap?.apply {
                this.clear()
                it.forEach {
                    val marker = mMap.addMarker(MarkerOptions().position(LatLng(it.latitude,it.longitude)))
                    marker.tag = it.id
                }
            }
        })



    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0 as GoogleMap

        mMap.setMinZoomPreference(15.0.toFloat())
        mMap.setMaxZoomPreference(20.0.toFloat())

        viewModel.getCurrentShop().observe(this, Observer {
            it?.let {
                Toast.makeText(activity,it.latitude.toString() +"-"+it.longitude.toString(),Toast.LENGTH_LONG).show()
                mMap.addMarker(MarkerOptions().position(LatLng(it.latitude,it.longitude)).title(it.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(120.00.toFloat())
                    ))

            }
        })
    }

    override fun onCameraIdle() {
        viewModel.updateMarkers(mMap.cameraPosition.target.latitude,mMap.cameraPosition.target.longitude)
    }
}
