package com.fpondarts.foodie.ui.home.delivery_map

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import com.fpondarts.foodie.R
import com.fpondarts.foodie.ui.FoodieViewModelFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import android.widget.Toast
import com.fpondarts.foodie.model.Coordinates
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*




class DeliveryMapFragment : Fragment()
    , OnMapReadyCallback
    , GoogleMap.OnMarkerClickListener
    , KodeinAware{


    override fun onMarkerClick(p0: Marker?): Boolean {
        Toast.makeText(activity,"Marker: "+p0?.tag.toString(),Toast.LENGTH_LONG).show()
        return true
    }

    override val kodein by kodein()

    val factory: FoodieViewModelFactory by instance()

    companion object {
        fun newInstance() = DeliveryMapFragment()
    }


    private lateinit var mMap:GoogleMap
    private lateinit var viewModel: DeliveryMapViewModel

    private var order_id:Long = 0

    private var user_lat:Double? = null
    private var user_lon:Double? = null
    private var shop_lat:Float? = null
    private var shop_lon:Float? = null
    private var shop_id:Long? = null

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
        user_lat = arguments!!.getDouble("user_lat")
        user_lon = arguments!!.getDouble("user_lon")
        shop_id = arguments!!.getLong("shop_id")

    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0 as GoogleMap

        mMap.setMinZoomPreference(15.0.toFloat())
        mMap.setMaxZoomPreference(20.0.toFloat())

        viewModel.repository.getShop(shop_id!!).observe(this, Observer {
            it?.let {
                coordinates = Coordinates(it.latitude,it.longitude)
                Toast.makeText(activity,it.latitude.toString() +" ::: "+it.longitude.toString(),Toast.LENGTH_LONG).show()
                mMap.addMarker(MarkerOptions()
                    .position(LatLng(it.latitude,it.longitude))
                    .title(it.name))

            }
        })

      /*  viewModel.repository.availableDeliveries.observe(this, Observer {
            mMap?.apply {
                this.clear()
                it.forEach {
                    val marker = mMap.addMarker(MarkerOptions().draggable(false).position(LatLng(it.latitude,it.longitude)))
                    marker.tag = it.user_id
                }
            }
        }) */

        viewModel.repository.getOrder(order_id).observe(this, Observer {
            Log.d("Map fragment","Observing order in map")

            it?.let {

                Log.d("Map fragment","Order not null ")
                val coordinates = LatLng(user_lat!!,user_lon!!)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15.0.toFloat()))
                mMap.addMarker(MarkerOptions().position(coordinates).title("Tu posicion"))
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
