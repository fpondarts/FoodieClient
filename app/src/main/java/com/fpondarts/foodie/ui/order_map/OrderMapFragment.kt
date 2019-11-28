package com.fpondarts.foodie.ui.order_map


import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.model.Directions
import com.fpondarts.foodie.model.Route
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import com.google.maps.android.PolyUtil


/**
 * A simple [Fragment] subclass.
 */
class OrderMapFragment : Fragment(), OnMapReadyCallback, KodeinAware {

    override val kodein by kodein()

    val repository: Repository by instance()

    private lateinit var mMap: GoogleMap

    private var mRouteMarkerList = ArrayList<Marker>()
    private lateinit var mRoutePolyline: Polyline

    var orderId:Long = -1
    var isFavour = false
    lateinit var shopLatLng: LatLng

    var mHandler: Handler? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        orderId = arguments!!.getLong("order_id")

        val shop_lat = arguments!!.getDouble("shop_lat")
        val shop_lon = arguments!!.getDouble("shop_long")

        shopLatLng = LatLng(shop_lat,shop_lon)

        isFavour = arguments!!.getBoolean("is_favour")

        return inflater.inflate(R.layout.delivery_map_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val map = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)

    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0 as GoogleMap

        mMap.setMinZoomPreference(12.0.toFloat())
        mMap.setMaxZoomPreference(20.0.toFloat())

        mMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(-34.652230,-58.631)))


        val BUENOS_AIRES = LatLngBounds(
            LatLng(-34.881, -58.779561), LatLng(-34.472, -58.309541)
        )
        mMap.setLatLngBoundsForCameraTarget(BUENOS_AIRES)


        repository.getRoute(LatLng(-34.652230,-58.631), LatLng(-34.651594, -58.624467),
            LatLng(-34.657156, -58.626635)).observe(this, Observer {
            it?.let{
                drawRoutes(it)
            }
        })

    }

    fun drawRoutes(directions:Directions){
        if (directions.status.equals("OK")) {
            val legs = directions.routes[0].legs[0]
            val route = Route("Tu ubicacion", "Punto de entrega", legs.startLocation.lat, legs.startLocation.lng, legs.endLocation.lat, legs.endLocation.lng, directions.routes[0].overviewPolyline.points)
            setMarkersAndRoute(route)
        } else {
            Toast.makeText(activity,directions.status,Toast.LENGTH_LONG).show()
        }
    }

    fun setMarkersAndRoute(route: Route) {
        val startLatLng = LatLng(route.startLat!!, route.startLng!!)
        val startMarkerOptions: MarkerOptions = MarkerOptions().position(startLatLng).title(route.startName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        val endLatLng = LatLng(route.endLat!!, route.endLng!!)
        val endMarkerOptions: MarkerOptions = MarkerOptions().position(endLatLng).title(route.endName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        val startMarker = mMap.addMarker(startMarkerOptions)
        val endMarker = mMap.addMarker(endMarkerOptions)
        mRouteMarkerList.add(startMarker)
        mRouteMarkerList.add(endMarker)

        val polylineOptions = PolylineOptions().color(0xff0088ff.toInt()).clickable(true)
        val pointsList = PolyUtil.decode(route.overviewPolyline)
        for (point in pointsList) {
            polylineOptions.add(point)
        }

        mRoutePolyline = mMap.addPolyline(polylineOptions)

        mMap.animateCamera(CameraUpdateFactory.newLatLng(startLatLng))
    }

}
