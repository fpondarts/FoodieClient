package com.fpondarts.foodie.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.fpondarts.foodie.ui.delivery.DeliveryHomeActivity
import com.google.android.gms.location.LocationResult
import java.lang.Exception
import java.lang.StringBuilder

class MyLocationService : BroadcastReceiver() {


    companion object {
        val ACTION_PROCESS_UPDATE="fpondarts.foodie.UPDATE_LOCATION"
    }


    override fun onReceive(p0: Context?, intent: Intent?) {
        if (intent!=null){
            val action = intent!!.action
            if (action.equals(ACTION_PROCESS_UPDATE)){
                val result = LocationResult.extractResult(intent)
                if (result!=null){
                    val location = result.lastLocation
                    val locationString = StringBuilder(location.latitude.toString())
                        .append("/").append(location.longitude).toString()
                    try {

                    } catch (e:Exception){

                    }
                }
            }
        }
    }


}
