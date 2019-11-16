package com.fpondarts.foodie.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.fpondarts.foodie.FoodieApp
import com.fpondarts.foodie.data.repository.DeliveryRepository
import com.fpondarts.foodie.data.repository.PositionUpdater
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.ui.delivery.DeliveryHomeActivity
import com.google.android.gms.location.LocationResult
import org.kodein.di.Kodein
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.KodeinAware
import org.kodein.di.LazyKodein
import org.kodein.di.android.closestKodein
import org.kodein.di.android.kodein

import java.lang.Exception
import java.lang.StringBuilder

class MyLocationService() : BroadcastReceiver(),KodeinAware{

    override lateinit var  kodein : Kodein

    var updater: PositionUpdater? = null

    companion object {
        val ACTION_PROCESS_UPDATE_USER="fpondarts.foodie.UPDATE_LOCATION_USER"
        val ACTION_PROCESS_UPDATE_DELIVERY="fpondarts.foodie.UPDATE_LOCATION_DELIVERY"
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("LocationService","update")


        if (intent!=null){

            kodein = (context!!.applicationContext as FoodieApp).kodein

            val action = intent!!.action
            Log.d("LocationService","Action es "+action)
            if (action.equals(ACTION_PROCESS_UPDATE_USER)){
                val repository: Repository by instance()
                updater = repository
            }
            else if (action.equals(ACTION_PROCESS_UPDATE_DELIVERY)) {
                val repository: DeliveryRepository by instance()
                updater = repository
            }
            else {
                return
            }

            val result = LocationResult.extractResult(intent)
            if (result!=null){
                val location = result.lastLocation
                Log.d("LocationService","Location es "+location.latitude.toString()+","+location.longitude.toString())
                try {
                    updater
                    Log.d("LocationService","before updating")
                    updater?.updatePosition(location.latitude , location.longitude)
                } catch (e:Exception){
                    Log.d("LocationService","Exception: "+e.message)
                }
            } else {
                Log.d("LocationService","Result is null")
            }
        }

    }


}
