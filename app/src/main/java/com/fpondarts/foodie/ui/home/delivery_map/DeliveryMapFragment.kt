package com.fpondarts.foodie.ui.home.delivery_map

import android.app.ProgressDialog
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
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.findNavController
import com.fpondarts.foodie.data.db.entity.Order
import com.fpondarts.foodie.model.Coordinates
import com.fpondarts.foodie.ui.home.DeliveryDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*






class DeliveryMapFragment : Fragment()
    , OnMapReadyCallback
    , GoogleMap.OnMarkerClickListener
    , KodeinAware{


    override fun onMarkerClick(p0: Marker?): Boolean {
        if (p0!!.title == "Delivery"){
            val delivery_id = p0!!.tag as Long
            Log.d("MapFragment","Click on delivery: ${p0.tag}")
            val liveDel = viewModel.repository.getDelivery(delivery_id)
            liveDel.removeObservers(this)

            liveDel.observe(
                this, Observer {
                    it?.let{
                        val delivery= it
                        viewModel.repository.askDeliveryPrice(user_lat!!,user_lon!!,shop_id!!,it.user_id).observe(
                            this, Observer {
                                it?.let {
                                    val dialog = DeliveryDialog.newInstance(
                                        order.payWithPoints
                                        ,delivery.user_id
                                        ,order_id
                                        ,it.price
                                        ,it.pay
                                        ,delivery.rating
                                        ,delivery.picture)
                                    dialog.show(childFragmentManager,"Ofrece tu pedido")
                                }
                            }
                        )
                        liveDel.removeObservers(this)
                    }
                }
            )
        }

        return true
    }

    private var shop_init: Boolean = false 
    private lateinit var shop_title: String

    override val kodein by kodein()

    val factory: FoodieViewModelFactory by instance()

    companion object {
        fun newInstance() = DeliveryMapFragment()
    }


    private lateinit var mMap:GoogleMap
    private lateinit var viewModel: DeliveryMapViewModel

    private var progressDialog : ProgressDialog? = null

    private var order_id:Long = 0

    private var user_lat:Double? = null
    private var user_lon:Double? = null
    private var shop_lat:Double? = null
    private var shop_lon:Double? = null
    private var shop_id:Long? = null
    private lateinit var order: Order

    private val currentOfferId = MutableLiveData<Long>().apply {
        value = null
    }

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

        viewModel.repository.currentOfferId.postValue(-1)


    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0 as GoogleMap

        mMap.setMinZoomPreference(5.0.toFloat())
        mMap.setMaxZoomPreference(20.0.toFloat())

        mMap.setOnMarkerClickListener(this)
        viewModel.repository.getShop(shop_id!!).observe(this, Observer {
            it?.let {
                shop_init = true
                this.shop_lat = it.latitude
                this.shop_lon = it.longitude
                this.shop_title = it.name
            }
        })

        viewModel.repository.availableDeliveries.observe(this, Observer {
            mMap?.apply {
                Log.d("MapFragment","Se observaron cambios en deliveries")
                val list = it
                this.clear()
                if (shop_init){
                    Log.d("MapFragment","Actualizando el mapa")
                    shop_title?.let{
                        mMap.addMarker(MarkerOptions().position(LatLng(shop_lat!!,shop_lon!!)).title(shop_title))
                        mMap.addMarker(MarkerOptions().position(LatLng(user_lat!!,user_lon!!)).title("Tu posición"))
                        list.forEach {
                            Log.d("MapFragment","Se agrega delivery marker en:${it.latitude},${it.longitude}")
                            val marker = mMap.addMarker(MarkerOptions()
                                .position(LatLng(it.latitude,it.longitude))
                                .title("Delivery")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                            marker.tag = it.user_id
                        }
                    }

                }
            }

        })

        viewModel.repository.getOrder(order_id).observe(this, Observer {
            Log.d("Map fragment","Observing order in map")

            it?.let {

                Log.d("Map fragment","Order not null ")
                val coordinates = LatLng(user_lat!!,user_lon!!)
                order = it
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15.0.toFloat()))
                mMap.addMarker(MarkerOptions().position(coordinates).title("Tu posicion"))
            }
        })
        useHandler()

        viewModel.repository.currentOfferId.observe(this, Observer {
            it?.let {
                if (it > 0){
                    progressDialog = ProgressDialog.show(context
                        ,"Oferta realizada"
                        ,"Esperando respuesta (Max 2 min)"
                    )
                    val liveOffer = viewModel.repository.getOffer(it)
                    liveOffer.observe(this, Observer {
                        it?.let {
                            if (it.state == "offered"){
                                viewModel.repository.updateObservedOffer(it.id)
                            } else if (it.state == "accepted"){
                                progressDialog!!.dismiss()
                                findNavController().navigate(R.id.action_deliveryMapFragment_to_nav_home,
                                    null,
                                    NavOptions.Builder().setPopUpTo(R.id.nav_home,true).build())
                                viewModel.repository.currentOfferId.postValue(-1)
                            } else {
                                progressDialog!!.dismiss()
                                Toast.makeText(activity,"Oferta rechazada",Toast.LENGTH_LONG).show()
                                viewModel.repository.currentOfferId.postValue(-1)
                            }
                        }
                    })
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        mHandler = null
    }

    override fun onDetach() {
        super.onDetach()
    }

    var mHandler: Handler? = null

    fun useHandler() {
        mHandler = Handler()
        mHandler!!.postDelayed(mRunnable, 5000)
    }

    private val mRunnable = object : Runnable {

        override fun run() {

            viewModel.repository.refreshDeliveries(user_lat!!,user_lon!!)

            mHandler?.postDelayed(this, 5000)
        }
    }

}
