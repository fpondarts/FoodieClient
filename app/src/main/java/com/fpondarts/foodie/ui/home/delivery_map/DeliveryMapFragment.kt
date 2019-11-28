package com.fpondarts.foodie.ui.home.delivery_map

import android.app.ProgressDialog
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
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
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
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
    , CancelOrderDialog.CancelDialogListener
    , KodeinAware{


    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val options = NavOptions.Builder().setPopUpTo(R.id.deliveryMapFragment,true).build()
        findNavController().navigate(R.id.action_deliveryMapFragment_to_nav_home,null,options)
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        accumulatedEmpties = 0
    }

    fun showCancelDialog(msj:String = "Si decide salir, la orden se cancelará") {
        // Create an instance of the dialog fragment and show it
        val dialog = CancelOrderDialog.newInstance(msj)
        dialog.show(fragmentManager!!, "CancelOrderDialog")
    }


    fun onFavourMarkerClick(p0:Marker?){}

    override fun onMarkerClick(p0: Marker?): Boolean {
        if (p0!!.title == "Delivery"){

            val user_id = p0!!.tag as Long
            Log.d("MapFragment","Click on delivery: ${p0.tag}")
            val liveDel = viewModel.repository.getUser(user_id)
            liveDel.removeObservers(this)
            liveDel.observe(
                this, Observer {
                    it?.let{
                        val user = it
                        if (isFavour){


                        } else {
                            viewModel.repository.askDeliveryPrice(user_lat!!,user_lon!!,shop_id!!,it.user_id).observe(
                                this, Observer {
                                    it?.let {
                                        val dialog = DeliveryDialog.newInstance(
                                            order.payWithPoints
                                            ,user.user_id
                                            ,order_id
                                            ,it.price
                                            ,it.pay
                                            ,user.rating.toDouble()
                                            ,user.picture)
                                        dialog.show(childFragmentManager,"Ofrece tu pedido")
                                    }
                                }
                            )
                            liveDel.removeObservers(this)
                        }

                    }
                }
            )
        }

        return false
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

    var first_move = true
    private var user_lat:Double? = null
    private var user_lon:Double? = null
    private var shop_lat:Double? = null
    private var shop_lon:Double? = null
    private var shop_id:Long? = null
    private var isFavour: Boolean = false
    private lateinit var order: Order

    private var accumulatedEmpties = 0

    private val currentOfferId = MutableLiveData<Long>().apply {
        value = null
    }

    private lateinit var coordinates:Coordinates

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this,factory).get(DeliveryMapViewModel::class.java)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            showCancelDialog()
        }

        isFavour = arguments!!.getBoolean("isFavour",false)

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
            if (it.isEmpty()){
                accumulatedEmpties += 1
                if (accumulatedEmpties % 3 == 0){
                    Toast.makeText(context,"Buscando deliveries cercanos a la tienda",Toast.LENGTH_LONG).show()
                }
                if (accumulatedEmpties == 5){
                    showCancelDialog("No parece haber deliveries cercanos, desea salir y realizar el pedido más tarde?")
                }
            } else {
                accumulatedEmpties = 0
            }
            mMap?.apply {
                Log.d("MapFragment","Se observaron cambios en deliveries")
                val list = it
                this.clear()
                if (shop_init){
                    Log.d("MapFragment","Actualizando el mapa")
                    shop_title?.let{
                        mMap.addMarker(MarkerOptions().position(LatLng(shop_lat!!,shop_lon!!))
                            .title(shop_title)
                            .snippet(shop_title))
                        if (first_move){
                            val coordinates = LatLng(shop_lat!!,shop_lon!!)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15.0.toFloat()))
                            first_move = false
                        }
                        mMap.addMarker(MarkerOptions().position(LatLng(user_lat!!,user_lon!!))
                            .title("Tu posición")
                            .snippet(""))
                        list.forEach {
                            Log.d("MapFragment","Se agrega delivery marker en:${it.latitude},${it.longitude}")
                            val marker = mMap.addMarker(MarkerOptions()
                                .position(LatLng(it.latitude,it.longitude))
                                .title("Delivery")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                .snippet("Rating: ${it.rating.toString()}"))
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10.0.toFloat()))
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
                    val timeMilis = System.currentTimeMillis()
                    val liveOffer = viewModel.repository.getOffer(it)
                    liveOffer.observe(this, Observer {
                        it?.let {
                            if (it.state == "offered"){
                                if (System.currentTimeMillis() >= (timeMilis + (1000 * 65 * 2))){
                                    progressDialog!!.dismiss()
                                    Toast.makeText(activity,"Oferta rechazada",Toast.LENGTH_LONG).show()
                                    viewModel.repository.currentOfferId.postValue(-1)
                                } else
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            showCancelDialog()
            Log.d("Clicked","La puta madre")
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()

        mHandler = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as AppCompatActivity)
            .supportActionBar!!
            .setDisplayHomeAsUpEnabled(false)

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
