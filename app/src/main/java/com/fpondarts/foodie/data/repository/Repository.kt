package com.fpondarts.foodie.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fpondarts.foodie.data.db.FoodieDatabase
import com.fpondarts.foodie.model.OrderModel
import com.fpondarts.foodie.network.FoodieApi
import com.fpondarts.foodie.network.SafeApiRequest
import com.fpondarts.foodie.network.response.SignInResponse
import com.fpondarts.foodie.util.Coroutines
import com.fpondarts.foodie.util.exception.FoodieApiException
import com.google.android.gms.common.api.ApiException
import android.util.Log
import com.fpondarts.foodie.data.db.entity.*
import com.fpondarts.foodie.model.Coordinates
import com.fpondarts.foodie.model.OrderItem
import com.fpondarts.foodie.model.OrderState
import com.fpondarts.foodie.network.DirectionsApi
import com.fpondarts.foodie.network.request.OrderRequest
import com.fpondarts.foodie.network.request.PostOfferRequest
import com.fpondarts.foodie.network.response.PricingResponse
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.delay
import java.lang.Exception

class Repository(
    private val api: FoodieApi,
    private val directionsApi: DirectionsApi,
    private val db : FoodieDatabase
):SafeApiRequest(), PositionUpdater{




    lateinit var fbDb : DatabaseReference

    val currentUser = MutableLiveData<User>().apply {
        value = null
    }

    var token:String? = null
    var userId:Long? = null

    var currentOrder: OrderModel? = null

    var currentOfferId = MutableLiveData<Long>().apply {
        value = -1
    }

    var observedOffer : MutableLiveData<Offer>? = null

    val apiError = MutableLiveData<FoodieApiException>().apply {
        value = null
    }


    val availableDeliveries = MutableLiveData<List<Delivery>>().apply {
        value = ArrayList<Delivery>()
    }


    val SHOP_PAGE_SIZE = 3;

    suspend fun foodieSignInLive(email:String, password: String?, fbToken: String):LiveData<User>{
        Coroutines.main{
            try {
                val response = foodieSignIn(email, password, fbToken)
            } catch (e: ApiException){

            }

        }
        return currentUser
    }

    suspend fun foodieSignIn(email: String, password:String?, fbToken:String):SignInResponse{
        return apiRequest { api.signIn(email,password, fbToken) }
    }

    fun initUser(token: String, id:Long){
        this.token = token
        this.userId = id
        Coroutines.io{
            try{
                currentUser.postValue(apiRequest{ api.getUserById(token,id) })
            } catch (e:FoodieApiException){
                apiError.postValue(e)
                initUser(token,id)
            }
        }
    }

    fun getAllShops():LiveData<List<Shop>>{
        val ans = db.getShopDao().getAllOrdered()
        if (ans.value == null || ans.value!!.isEmpty()){
            Coroutines.io{
                try {
                    val top = api.getShopsPage(token!!,0,SHOP_PAGE_SIZE)
                    if (top.isSuccessful){
                        db.getShopDao().upsertBatch((top.body()!!))
                    }
                } catch (e:FoodieApiException) {
                    throw e
                }
            }
        }

        return ans
    }

    fun getMoreShops(){
        Coroutines.io{
            try{
                val nextPage = db.getShopDao().getCount()  / SHOP_PAGE_SIZE
                val moreShops = apiRequest{api.getShopsPage(token!!,nextPage,SHOP_PAGE_SIZE)}
                db.getShopDao().upsertBatch(moreShops)
            } catch (e:FoodieApiException){
                apiError.postValue(e)
            }

        }
    }

    fun newOrder(shopId:Long){
        currentOrder = OrderModel(currentUser.value!!.user_id,shopId)
    }

    fun getShopMenu(shopId:Long): LiveData<List<MenuItem>>{
        val liveMenu = db.getMenuItemDao().loadMenu(shopId)
        if (liveMenu.value.isNullOrEmpty()){
            Coroutines.io {
                try {
                    val products = apiRequest{ api.getMenu(token!!,shopId) }
                    db.getMenuItemDao().upsert(products)
                } catch(e:FoodieApiException){
                    apiError.postValue(e)
                }
            }
        }
        return liveMenu
    }

    fun getItemName(itemId:Long):String?{
        return db.getMenuItemDao().loadItem(itemId).value?.let {
            return it.name
        }
        return null
    }


    fun addItemToOrder(item: OrderItem,name:String,itemPrice:Float) {
        currentOrder!!.addItem(item,name,itemPrice)
    }

    fun askDeliveryPrice(lat:Double,long:Double,shop_id:Long,delivery_id:Long): LiveData<PricingResponse>{

        val liveData = MutableLiveData<PricingResponse>().apply {
            value = null
        }

        Coroutines.io {
            try {
                val priceResponse = apiRequest { api.getDeliveryPrice(token!!,shop_id,lat,long,userId!!,delivery_id) }
                liveData.postValue(priceResponse)
            } catch (e:FoodieApiException){
                apiError.postValue(e)
                liveData.postValue(PricingResponse(-1.0,-1.0))
            }
        }
        return liveData
    }

    fun setOrderCoordinates(lat:Double,lon:Double){
        currentOrder!!.latitude = lat
        currentOrder!!.longitude = lon
    }


    fun confirmOrder():LiveData<Boolean> {

        val liveData = MutableLiveData<Boolean>().apply {
            value = null
        }
        Coroutines.io {
            try {
                val orderId = apiRequest{ api.confirmOrder(token!!,
                    OrderRequest(currentOrder!!.shopId
                        ,currentOrder!!.items.values
                        ,Coordinates(currentOrder!!.latitude!!,currentOrder!!.longitude!!)
                        ,currentOrder!!.payWitPoints
                        ,currentOrder!!.favourPoints
                        ,currentOrder!!.price.toFloat()
                        ,userId!!)) }.order_id
                currentOrder!!.id = orderId
                liveData.postValue(true)
            } catch (e:FoodieApiException){
                apiError.postValue(e)
                liveData.postValue(false)
            }

        }

        return liveData
    }

    fun getShop(id:Long):LiveData<Shop>{
        val shop = db.getShopDao().loadShop(id)
        shop.value?: Coroutines.io {
            val fetched = apiRequest { api.getShop(token!!,id) }
            db.getShopDao().upsert(fetched)
        }
        return shop
    }

    fun getCurrentShop():LiveData<Shop>{
        return getShop(currentOrder!!.shopId!!)
    }

    fun refreshDeliveries(lat:Double,long:Double){
        Coroutines.io{
           try {
               val lat_rounded = Math.round(lat* 1000.0) / 1000.0
               val lon_roundded = Math.round(long * 1000.0) / 1000.0
               val response = apiRequest{ api.getDeliveries(token!!,lat_rounded,lon_roundded) }
               availableDeliveries.postValue(response)
           } catch (e: FoodieApiException){
               apiError.postValue(e)
               availableDeliveries.postValue(ArrayList<Delivery>())
           }
        }
    }

    fun postOffer(deliveryId:Long,order_id:Long,price:Double,pay:Double):LiveData<Long>{
        val liveData = MutableLiveData<Long>().apply {
            value = null
        }
        Coroutines.io {
            try {
                val apiResponse = apiRequest { api.postOffer(token!!,deliveryId,PostOfferRequest(price,pay,order_id,deliveryId)) }
                liveData.postValue(apiResponse.id)
            } catch (e:FoodieApiException){
                apiError.postValue(e)
                liveData.postValue(-1)
            }
        }
        return liveData
    }

    fun getOrder(id:Long): LiveData<Order>{
        val order = db.getOrderDao().getOrder(id)
        if (order.value == null || order.value?.state == "created" || order.value?.state=="onWay")  {
            Coroutines.io {
                try {
                    val order = apiRequest { api.getOrder(token!!, id) }
                    db.getOrderDao().upsert(order)
                } catch (e: FoodieApiException) {
                    apiError.postValue(e)
                } catch (e:Exception){
                    Log.d("Db error",e.message)
                }
            }
        }
        return order
    }

    fun getDelivery(id:Long): LiveData<Delivery> {
        val delivery = db.getDeliveryDao().getDelivery(id)
        if (delivery.value == null){
            Coroutines.io {
                try {
                    val fetched = apiRequest{ api.getDelivery(token!!,id) }
                    db.getDeliveryDao().upsert(fetched)
                } catch (e:FoodieApiException){
                    apiError.postValue(e)
                }
            }
        }
        return delivery
    }


    fun getOrdersByState(state: OrderState):LiveData<List<Order>>{
        val str = state.stringVal
        val res = db.getOrderDao().getOrdersByState(str)
        Coroutines.io{
            try {
                val fetched = apiRequest{ api.getOrdersByState(token!!
                    ,currentUser.value!!.user_id!!
                    ,state.stringVal,db.getOrderDao().getCount().value!!,10)}
                db.getOrderDao().upsert(fetched)
            } catch (e: FoodieApiException){

            }
        }
        return res
    }

    fun sendMessage(orderId:Long, message:String, to: String){
        val newMessage = ChatMessage(orderId,currentUser.value!!.firebase_uid!!, to,message,System.currentTimeMillis()/1000)
        val key = fbDb.child("chats").child(orderId.toString()).child("messages").push().key
        if (key == null){

        }
        fbDb.child("chats").child(orderId.toString()).child("messages/$key").setValue(newMessage)
    }

    fun getUserPoints():Int{
        //TODO
        return -1
    }

    override fun updatePosition(latitude:Double,longitude:Double):LiveData<Boolean>{
        val liveData = MutableLiveData<Boolean>().apply {
            value = null
        }
        Coroutines.io {
            try {

                val apiResponse = apiRequest {
                    api.updateCoordinates(token!!,userId!!,
                        Coordinates(latitude,longitude))
                }

                liveData.postValue(true)
            } catch (e:FoodieApiException){
                apiError.postValue(e)
                liveData.postValue(false)
            }
        }
        return liveData
    }


    fun updateDeliveries(latitude:Double, longitude: Double){
        Coroutines.io {
            try {
                val deliveries = apiRequest { api.getDeliveries(token!!,latitude,longitude) }
                availableDeliveries.postValue(deliveries)
            } catch (e:FoodieApiException){
                apiError.postValue(e)
                availableDeliveries.postValue(ArrayList<Delivery>())
            }
        }
    }
     fun getAvailableDeliveries():LiveData<List<Delivery>>{
         return availableDeliveries
    }

    fun getOffer(offer_id:Long):LiveData<Offer>{

        observedOffer = MutableLiveData<Offer>().apply {
            value = null
        }

        Coroutines.io {
            try {
                val apiResponse = apiRequest { api.getOffer(token!!,offer_id) }
                observedOffer!!.postValue(apiResponse)
            } catch (e:FoodieApiException){
                apiError.postValue(e)
            }
        }
        return observedOffer!!
    }


    fun updateObservedOffer(id:Long){
        Coroutines.io {
            try{
                val apiResponse = apiRequest{ api.getOffer(token!!,id) }
                observedOffer?.postValue(apiResponse)
            } catch ( e: FoodieApiException){
                apiError.postValue(e)
                delay(500)
                updateObservedOffer(id)
            }
        }
    }

}