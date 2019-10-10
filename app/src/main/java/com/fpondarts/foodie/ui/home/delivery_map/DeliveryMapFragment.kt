package com.fpondarts.foodie.ui.home.delivery_map

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.fpondarts.foodie.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DeliveryMapFragment : SupportMapFragment()
    , OnMapReadyCallback
    , GoogleMap.OnCameraIdleListener {

    companion object {
        fun newInstance() = DeliveryMapFragment()
    }


    private lateinit var mMap:GoogleMap
    private lateinit var viewModel: DeliveryMapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.delivery_map_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DeliveryMapViewModel::class.java)

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
