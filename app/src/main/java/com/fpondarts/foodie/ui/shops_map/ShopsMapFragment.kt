package com.fpondarts.foodie.ui.shops_map


import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.Shop
import com.fpondarts.foodie.data.repository.UserRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class ShopsMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, ShopNavigator, KodeinAware {


    override val kodein by kodein()

    private val repository: UserRepository by instance()


    private lateinit var shops: LiveData<List<Shop>>
    private lateinit var  mMap: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        shops = repository.getAllShops()

        return inflater.inflate(R.layout.delivery_map_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val map = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)


    }

    private var progressDialog: ProgressDialog? = null

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0 as GoogleMap

        progressDialog = ProgressDialog.show(activity,"Cargando datos de tiendas", null)

        shops.observe(this, Observer {
            it?.let {
                mMap.clear()
                for (shop in it){
                    val options = MarkerOptions().position(LatLng(shop.latitude,shop.longitude))
                        .title(shop.name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    val marker = mMap.addMarker(options)
                    marker.tag = shop.id
                }
                val options = MarkerOptions()
                    .position(LatLng(repository.currentUser.value!!.latitude
                    ,repository.currentUser.value!!.longitude))
                    .title("Tu ubicación")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                val marker = mMap.addMarker(options)
                marker.tag = "User"
                progressDialog?.dismiss()
                val movement = CameraUpdateFactory.newLatLngZoom(marker.position,12.toFloat())
                mMap.animateCamera(movement)
            }
        })

        mMap.setOnMarkerClickListener(this)

    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        val marker = p0 as Marker
        if (marker.tag == "User")
            return false

        val shop_id = marker.tag as Long
        val dialog = ShopDialogFragment.createInstance(shop_id)
        dialog.setTargetFragment(this,0)
        dialog.show(fragmentManager!!,"Tienda")

        return false
    }

    override fun onNavigateToShop(id: Long){
        val bundle = bundleOf("shop_id" to id)
        findNavController().navigate(R.id.action_shopsMapFragment_to_shopFragment,bundle)
    }

}
