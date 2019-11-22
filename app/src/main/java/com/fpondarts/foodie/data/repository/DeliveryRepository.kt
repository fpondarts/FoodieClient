package com.fpondarts.foodie.data.repository

import android.util.JsonReader
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fpondarts.foodie.data.db.FoodieDatabase
import com.fpondarts.foodie.data.db.entity.*
import com.fpondarts.foodie.data.parser.RoutesParser
import com.fpondarts.foodie.model.Coordinates
import com.fpondarts.foodie.network.DirectionsApi
import com.fpondarts.foodie.network.FoodieApi
import com.fpondarts.foodie.network.SafeApiRequest
import com.fpondarts.foodie.network.request.ChangePasswordRequest
import com.fpondarts.foodie.network.request.StateChangeRequest
import com.fpondarts.foodie.network.request.UpdatePictureRequest
import com.fpondarts.foodie.network.response.SuccessResponse
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.JsonObject
import java.lang.Exception

class DeliveryRepository(
    private val api:FoodieApi,
    private val directionsApi: DirectionsApi,
    private val db:FoodieDatabase
): SafeApiRequest() , PositionUpdater{

    val apiError = MutableLiveData<FoodieApiException>().apply {
        value = null
    }

    var token:String? = null
    var userId:Long? = null

    var currentUser = MutableLiveData<Delivery>().apply {
        value = null
    }

    val isWorking = MutableLiveData<Boolean>().apply {
        value = false
    }

    var current_order:Long = -1

    fun refreshUser(){
        initUser(this.token!!,this.userId!!)
    }

    fun initUser(token:String, id:Long){
        this.token = token
        userId = id
        Coroutines.io{
            try {
                val user = apiRequest{ api.getDelivery(token,id) }
                currentUser.postValue(user)
                if (user.state == "working"){
                    current_order = user.current_order!!
                    isWorking.postValue(true)
                }

            } catch (e:FoodieApiException) {
                apiError.postValue(e)
                if (e.code > 500){
                    initUser(token,id)
                }
            }
        }

    }

    fun getRoute(origin: LatLng, destination:LatLng, waypoint:LatLng ): LiveData<List<PolylineOptions>>{
        val response = MutableLiveData<List<PolylineOptions>>().apply {
            value = null
        }
        Coroutines.io{
            try{
                var string = "?origin="
                string+=origin.latitude.toString()+","+origin.longitude.toString()
                string+="&destination="+destination.latitude.toString()+","+destination.longitude.toString()
                string+="&waypoints="+waypoint.latitude.toString()+","+waypoint.longitude.toString()

                val json = apiRequest{ directionsApi.getRoute(string) }

                val parser = RoutesParser()

                response.postValue(parser.parse(json))


            } catch (e:FoodieApiException){
                apiError.postValue(e)
            }


        }

        return response
    }

    fun getCurrentOffers():LiveData<List<Offer>>{
        val offers = MutableLiveData<List<Offer>>().apply {
            value = null
        }
        Coroutines.io {
            try{
                val apiResponse = apiRequest { api.getCurrentOffers(token!!,userId!!) }
                offers.postValue(apiResponse)
            } catch (e:FoodieApiException){
                apiError.postValue(e)
                offers.postValue(ArrayList<Offer>())
            }
        }
        return offers
    }

    fun acceptOffer(offer_id:Long):LiveData<Boolean>{

        val successResponse = MutableLiveData<Boolean>().apply{
            value = null
        }

        Coroutines.io {
            try{
                val apiResponse = apiRequest {api.changeOfferState(token!!,userId!!,offer_id, StateChangeRequest("accepted")) }
                successResponse.postValue(true)
            } catch (e:FoodieApiException){
                apiError.postValue(e)
                successResponse.postValue(false)
            }
        }

        return successResponse
    }

    fun rejectOffer(offer_id:Long):LiveData<Boolean>{
        val successResponse = MutableLiveData<Boolean>().apply{
            value = null
        }
        Coroutines.io{
            try{
                val apiResponse = apiRequest{api.changeOfferState(token!!,userId!!,offer_id,
                    StateChangeRequest("rejected")
                )}
                successResponse.postValue(true)
            } catch (e:FoodieApiException){
                apiError.postValue(e)
                successResponse.postValue(false)
            }
        }
        return successResponse
    }

    fun getOrder(order_id:Long):LiveData<Order>{
        val liveData = MutableLiveData<Order>().apply {
            value = null
        }
        Coroutines.io{
            try{
                val response = apiRequest { api.getOrder(token!!,order_id) }
                liveData.postValue(response)

            } catch (e:FoodieApiException){
                apiError.postValue(e)
            }
        }
        return liveData
    }

    fun getOrderItems(order_id:Long):LiveData<List<OrderItem>>{
        var liveData = db.getOrderItemDao().getOrderItems(order_id)
        if (liveData.value.isNullOrEmpty()){
            liveData = MutableLiveData<List<OrderItem>>().apply {
                value = null
            }
            Coroutines.io{
                try {
                    val apiResponse = apiRequest { api.getOrderItems(token!!,order_id) }
                    liveData.postValue(apiResponse)
                    db.getOrderItemDao().upsert(apiResponse)
                } catch (e:FoodieApiException){
                    apiError.postValue(e)
                }
            }
        }
        return liveData
    }

    fun getMenu(shop_id:Long):LiveData<List<MenuItem>>{
        val menu = db.getMenuItemDao().loadMenu(shop_id)
        if (menu.value.isNullOrEmpty()){
            Coroutines.io {
                try {
                    val apiResponse = apiRequest { api.getMenu(token!!, shop_id) }
                    db.getMenuItemDao().upsert(apiResponse)
                } catch (e: FoodieApiException) {
                    apiError.postValue(e)
                }
            }
        }
        return menu
    }

    fun getMenuItem(product_id:Long):LiveData<MenuItem>{
        val item = db.getMenuItemDao().loadItem(product_id)
        if (item.value == null){
            Coroutines.io{
                try{
                    val apiResponse = apiRequest { api.getProduct(token!!,product_id) }
                    db.getMenuItemDao().upsert(apiResponse)
                } catch (e :FoodieApiException){
                    apiError.postValue(e)
                }
            }
        }
        return item
    }

    fun finishDelivery(order_id:Long):LiveData<Boolean>{
        val liveData = MutableLiveData<Boolean>().apply {
            value = null
        }
        Coroutines.io{
            try {
                val apiResponse = apiRequest { api.finishOrder(token!!,order_id,StateChangeRequest("delivered")) }
                liveData.postValue(true)
            } catch (e:FoodieApiException){
                apiError.postValue(e)
                liveData.postValue(false)
            }
        }
        return liveData
    }

    override fun updatePosition (latitude:Double,longitude:Double):LiveData<Boolean>{
        val liveData = MutableLiveData<Boolean>().apply {
            value = null
        }
        Coroutines.io {
            try {

                val apiResponse = apiRequest {
                    api.updateCoordinates(token!!,userId!!,
                        Coordinates(latitude ,longitude)
                    )
                }
                liveData.postValue(true)
            } catch (e:FoodieApiException){
                apiError.postValue(e)
                liveData.postValue(false)
            }
        }
        return liveData
    }


    fun changePassword(new_pass:String):LiveData<SuccessResponse>{
        val live = MutableLiveData<SuccessResponse>().apply {
            value = null
        }
        Coroutines.io{
            try {
                val apiResponse = apiRequest { api.changePassword(token!!,userId!!,
                    ChangePasswordRequest(new_pass)
                ) }
                live.postValue(apiResponse)
            } catch (e:FoodieApiException){
                apiError.postValue(e)
                live.postValue(SuccessResponse(e.message,400))
            }
        }
        return live
    }


    fun updatePic(url:String):LiveData<SuccessResponse>{
        val live = MutableLiveData<SuccessResponse>().apply {
            value = null
         }
        Coroutines.io{
            try{
                val apiResp = apiRequest { api.updateUserPicture(token!!,userId!!,
                    UpdatePictureRequest(url)
                ) }
                live.postValue(apiResp)
            } catch (e: FoodieApiException){
                apiError.postValue(e)
            }
        }
        return live
    }
}