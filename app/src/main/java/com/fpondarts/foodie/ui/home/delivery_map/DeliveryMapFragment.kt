package com.fpondarts.foodie.ui.home.delivery_map

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import com.fpondarts.foodie.R
import com.fpondarts.foodie.ui.FoodieViewModelFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import android.widget.Toast
import com.fpondarts.foodie.model.Coordinates


class DeliveryMapFragment : Fragment()
    , OnMapReadyCallback
    , KodeinAware{

    override val kodein by kodein()

    val factory: FoodieViewModelFactory by instance()

    companion object {
        fun newInstance() = DeliveryMapFragment()
    }


    private lateinit var mMap:GoogleMap
    private lateinit var viewModel: DeliveryMapViewModel

    private var order_id:Long = 0
    private lateinit var coordinates:Coordinates

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

        order_id = arguments!!.getLong("order_id")

        viewModel.repository.availableDeliveries.observe(this, Observer {
            mMap?.apply {
                this.clear()
                it.forEach {
                    val marker = mMap.addMarker(MarkerOptions().draggable(false).position(LatLng(it.latitude,it.longitude)))
                    marker.tag = it.user_id
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
                coordinates = Coordinates(it.latitude,it.longitude)
                Toast.makeText(activity,it.latitude.toString() +"-"+it.longitude.toString(),Toast.LENGTH_LONG).show()
                mMap.addMarker(MarkerOptions().position(LatLng(it.latitude,it.longitude)).title(it.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(120.00.toFloat())
                    ))

            }
        })

        useHandler()
    }

    var mHandler: Handler? = null

    fun useHandler() {
        mHandler = Handler()
        mHandler!!.postDelayed(mRunnable, 5000)
    }

    private val mRunnable = object : Runnable {

        override fun run() {

            viewModel.repository.refreshDeliveries(coordinates.latitude,coordinates.longitude)

            mHandler!!.postDelayed(this, 5000)
        }
    }

}
