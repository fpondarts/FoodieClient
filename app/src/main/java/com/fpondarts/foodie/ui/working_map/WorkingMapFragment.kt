package com.fpondarts.foodie.ui.working_map


import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.DeliveryRepository
import com.fpondarts.foodie.data.repository.UserRepository
import com.fpondarts.foodie.model.Directions
import com.fpondarts.foodie.model.Route
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class WorkingMapFragment : Fragment(), OnMapReadyCallback, KodeinAware {


    lateinit var mMap: GoogleMap

    override val kodein by kodein()

    val repository: UserRepository by instance()
    val deliveryRepository: DeliveryRepository by instance()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var destLatLng: LatLng
    private var shopLatLng: LatLng? = null
    private lateinit var myLatLng: LatLng
    private var pickedUp = false
    private var isFavour = false

    private var mHandler :Handler? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val destination_lat = arguments!!.getFloat("dest_lat")
        val destination_lon = arguments!!.getFloat("dest_lon")
        destLatLng = LatLng(destination_lat.toDouble(),destination_lon.toDouble())

        val shop_lat = arguments!!.getFloat("shop_lat")
        val shop_lon = arguments!!.getFloat("shop_lon")
        shopLatLng = LatLng(shop_lat.toDouble(),shop_lon.toDouble())

        pickedUp = arguments!!.getBoolean("pickedUp")

        isFavour = arguments!!.getBoolean("isFavour",false)

        return inflater.inflate(R.layout.delivery_map_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val map = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)

        fusedLocationProviderClient = FusedLocationProviderClient(context!!)

    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0 as GoogleMap

        mMap.setMinZoomPreference(12.0.toFloat())
        mMap.setMaxZoomPreference(20.0.toFloat())

        useHandler()

    }

    fun useHandler() {
        mHandler = Handler()
        mHandler!!.postDelayed(mRunnable, 5000)
    }

    override fun onDestroy() {
        super.onDestroy()

        mHandler = null
    }




    fun drawRoutes(directions: Directions){
        if (directions.status.equals("OK")) {
            val legs = directions.routes[0].legs[0]
            var total_distance = 0
            var total_time = 0
            for (leg in directions.routes[0].legs){
                total_distance+=leg.distance.value
                total_time += leg.duration.value
            }
            val route = Route("Tu ubicacion", "Punto de entrega", legs.startLocation.lat, legs.startLocation.lng, legs.endLocation.lat, legs.endLocation.lng, directions.routes[0].overviewPolyline.points)
            setMarkersAndRoute(route,total_distance,total_time)

        } else {
            Toast.makeText(activity,directions.status, Toast.LENGTH_LONG).show()
        }
    }

    fun setMarkersAndRoute(route: Route, totalDistance:Int, totalTime:Int) {
        mMap.clear()
        val startMarkerOptions: MarkerOptions = MarkerOptions().position(myLatLng).title("Tu posición").icon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

        if (!pickedUp) {
            val shopMarkerOptions: MarkerOptions =
                MarkerOptions().position(shopLatLng!!).title("Tienda").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                )
            val shopMarker = mMap.addMarker(shopMarkerOptions)
        }
        val endMarkerOptions: MarkerOptions = MarkerOptions().position(destLatLng).title("Punto de entrega").icon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
        )
        val startMarker = mMap.addMarker(startMarkerOptions)
        val endMarker = mMap.addMarker(endMarkerOptions)

        val polylineOptions = PolylineOptions().color(0xff0088ff.toInt()).clickable(true)
        val pointsList = PolyUtil.decode(route.overviewPolyline)
        for (point in pointsList) {
            polylineOptions.add(point)
        }

        val transparent = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
        val options = MarkerOptions()
            .position(myLatLng)
            .alpha(0.toFloat())
            .title("Distancia: ${totalDistance.toString()} metros")
            .snippet("Tiempo de viaje: ${(totalTime / 60).toString()} minutos")
            .icon(transparent)
            .anchor(0.5.toFloat(), 0.5.toFloat()) //puts the info window on the polyline

        val transparentMarker = mMap.addMarker(options)

        mMap.addPolyline(polylineOptions)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(myLatLng))
        mMap.setOnPolylineClickListener {
            transparentMarker.showInfoWindow()
        }


    }

    private val mRunnable = object : Runnable {

        override fun run() {

            if (pickedUp)
                shopLatLng = null

            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                it?.let{
                    myLatLng = LatLng(it.latitude,it.longitude)
                    if (isFavour){
                        repository.updatePosition(it.latitude,it.longitude)
                        repository.getRoute(myLatLng,destLatLng,shopLatLng).observe(this@WorkingMapFragment, Observer {
                            it?.let{
                                drawRoutes(it)
                            }
                        })
                    } else {
                        deliveryRepository.updatePosition(it.latitude,it.longitude)
                        deliveryRepository.getRoute(myLatLng,destLatLng,shopLatLng).observe(this@WorkingMapFragment, Observer {
                            it?.let{
                                drawRoutes(it)
                            }
                        })
                    }

                } ?.run {

                }
            }.addOnFailureListener{
                Toast.makeText(activity,it.message, Toast.LENGTH_LONG).show()
            }
            mHandler?.postDelayed(this, 5000)
        }
    }


}

