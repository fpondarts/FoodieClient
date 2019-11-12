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
import com.fpondarts.foodie.model.OrderItem
import com.fpondarts.foodie.model.OrderState
import com.fpondarts.foodie.network.request.OrderRequest
import com.google.firebase.database.DatabaseReference

class Repository(
    private val api: FoodieApi,
    private val db : FoodieDatabase
):SafeApiRequest() {

    lateinit var fbDb : DatabaseReference

    val topRanked = db.getShopDao().getAllOrdered()

    val currentUser = MutableLiveData<User>().apply {
        value = null
    }

    var token:String? = null
    var userId:Long? = null

    var currentOrder: OrderModel? = null
    val availableDeliveries = db.getDeliveryDao().getByAvailability(true)

    val AVAILABLE = "Available"
    val UNAVAILABLE = "Unavailable"
    val ERROR = "Error"

    val apiError = MutableLiveData<FoodieApiException>().apply {
        value = null
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

    suspend fun getShops():LiveData<List<Shop>>{
        return db.getShopDao().loadShops()
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
                val nextPage = db.getShopDao().getCount().value!! / SHOP_PAGE_SIZE
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
        Log.d("TAG shopId query",shopId.toString())
        if (liveMenu.value.isNullOrEmpty()){
            Coroutines.io {
                val menu: Menu? = api.getMenu(token!!,shopId).body()
                if (menu?.items.isNullOrEmpty()){
                    throw (FoodieApiException("La puta madre"))
                }
                menu!!.items.forEach {
                    Log.d("TAG: shopId",it.shopId.toString())
                    Log.d("TAG: id", it.id.toString())
                    Log.d("TAG name",it.name)
                }
                db.getMenuItemDao().upsert(menu!!.items)
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


    fun addItemToOrder(item: OrderItem) {
        currentOrder!!.addItem(item)
    }

    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    suspend fun askDeliveryPrice(lat:Double,long:Double): Float{
        val priceResponse = api.getDeliveryPrice(token!!,currentOrder!!.shopId,lat,long)
        priceResponse.isSuccessful?.let {
            currentOrder!!.setDeliveryPrice(priceResponse.body()!!.price)
            currentOrder!!.latitude = lat
            currentOrder!!.longitude = long
            return priceResponse.body()!!.price
        }
    }


    suspend fun confirmOrder():Boolean {
        val orderId = apiRequest{ api.confirmOrder(token!!,
            OrderRequest(2
            ,currentOrder!!.items.values
            ,currentOrder!!.latitude!!
            ,currentOrder!!.longitude!!
            ,currentOrder!!.payWitPoints
            ,currentOrder!!.favourPoints)) }.orderId
        currentOrder!!.id = orderId
        return true
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
               db.getDeliveryDao().upsert(apiRequest{ api.getDeliveries(token!!,lat,long) })
           } catch (e: FoodieApiException){

           }

        }
    }

    fun postOffer(deliveryId:Long){
        
    }

    fun getOrder(id:Long): LiveData<com.fpondarts.foodie.data.db.entity.Order>{
        val order = db.getOrderDao().getOrder(id)
        if (order.value == null) {
            Coroutines.io {
                try {
                    val order = apiRequest { api.getOrder(token!!, id) }
                    db.getOrderDao().upsert(order)
                } catch (e: FoodieApiException) {

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

                }
            }
        }
        return delivery
    }


    fun getOrdersByState(state: OrderState):LiveData<List<Order>>{
        val res = db.getOrderDao().getOrdersByState(state)
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

}