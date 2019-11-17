package com.fpondarts.foodie.network

import com.fpondarts.foodie.data.db.entity.*
import com.fpondarts.foodie.model.Coordinates
import com.fpondarts.foodie.network.request.*
import com.fpondarts.foodie.network.response.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface FoodieApi {


    @GET(API_PREFIX+"user/availability/{email}")
    suspend fun checkEmailIsAvailable(@Path("email") email: String):Response<AvailabilityResponse>

    @POST
    suspend fun signIn(email: String, password: String?, fbToken: String):Response<SignInResponse>

    @GET(USERS_PREFIX+"/{order_id}")
    suspend fun getUserById(@Header(API_KEY_HEADER)token:String,@Path("order_id")id:Long):Response<User>

    @GET(API_PREFIX+"shops/{order_id}")
    suspend fun getShop(@Header(API_KEY_HEADER) token:String, @Path("order_id") id:Long):Response<Shop>

    @GET(API_PREFIX+"shops/{order_id}/menu")
    suspend fun getMenu(@Header(API_KEY_HEADER) token:String, @Path("order_id") id:Long):Response<List<MenuItem>>

    @GET(API_PREFIX+"shops/top")
    suspend fun getTopShops(@Header(API_KEY_HEADER) token: String):Response<List<Shop>>

    @GET(API_PREFIX+"shops/{shop_id}/deliveryPrice")
    suspend fun getDeliveryPrice(@Header(API_KEY_HEADER) token:String
                                 ,@Path("shop_id")id:Long
                                 ,@Query("latitude")lat:Double
                                 ,@Query("longitude")long:Double
                                 ,@Query("user_id")user_id:Long
                                 ,@Query("delivery_id")delivery_id:Long):Response<PricingResponse>



    @GET(API_PREFIX+"deliveries")
    suspend fun getDeliveries(@Header(API_KEY_HEADER)token: String, @Query("latitude") lat:Double, @Query("longitude") long:Double,@Query("cantidad")cantidad:Int=10):Response<List<Delivery>>


    @GET(API_PREFIX+"orders/{orderId}")
    suspend fun getOrder(@Header(API_KEY_HEADER)token:String, @Path("orderId") orderId:Long):Response<Order>

    @GET(API_PREFIX+"deliveries/{order_id}")
    suspend fun getDelivery(@Header(API_KEY_HEADER)token:String, @Body id:Long):Response<Delivery>

    @GET(API_PREFIX+"orders")
    suspend fun getOrdersByState(@Header(API_KEY_HEADER)token:String,
                                 @Query("user_id")userId:Long,
                                 @Query("state")state:String?,
                                 @Query("offset")offset:Int,
                                 @Query("limit")limit:Int):Response<List<Order>>

    @POST(USERS_PREFIX)
    suspend fun registerUser(@Body body: UserRegisterRequest):Response<SuccessResponse>

    @POST(USERS_PREFIX)
    suspend fun registerDelivery(@Body body:DeliveryRegisterRequest):Response<SuccessResponse>

    @POST(USERS_PREFIX+"/login")
    suspend fun tokenLogin(@Body body: TokenLoginRequest):Response<SignInResponse>

    @POST(USERS_PREFIX+"/login")
    suspend fun passwordLogin(@Body body: PasswordLoginRequest):Response<SignInResponse>

    @GET(SHOPS_PREFIX)
    suspend fun getShopsPage(@Header(API_KEY_HEADER)token:String,@Query("p")page:Int, @Query("pSize")pageSize:Int):Response<List<Shop>>

    //Users
    @PATCH(USERS_PREFIX+"/{order_id}/position")
    suspend fun updateCoordinates(@Header(API_KEY_HEADER)token: String, @Path("order_id") userId:Long, @Body coordinates:Coordinates):Response<SuccessResponse>


    // PRODUCTS
    @GET("products/{order_id}")
    suspend fun getProduct(@Header(API_KEY_HEADER)token: String,@Path("order_id")id:Long):Response<MenuItem>

    // Orders
    @GET("orders/{id}/items")
    suspend fun getOrderItems(@Header(API_KEY_HEADER) token: String,@Path("id") order_id:Long):Response<List<OrderItem>>

    @PATCH("orders/{id}")
    suspend fun finishOrder(@Header(API_KEY_HEADER) token: String,@Path("id")order_id:Long,@Body state: StateChangeRequest):Response<SuccessResponse>

    @POST(API_PREFIX+"orders")
    suspend fun confirmOrder(@Header(API_KEY_HEADER)token:String, @Body order:OrderRequest):Response<ConfirmOrderResponse>


    // Offers
    @GET("delivery/{id}/offers")
    suspend fun getCurrentOffers(@Header(API_KEY_HEADER)token: String,@Path("id")id:Long):Response<List<Offer>>

    @PUT("offers/{id}")
    suspend fun changeOfferState(@Header(API_KEY_HEADER)token: String,@Path("id")id:Long,@Body state:StateChangeRequest):Response<SuccessResponse>

    @POST("delivery/{id}/offers")
    suspend fun postOffer(@Header(API_KEY_HEADER)token: String, @Path("id")id:Long,@Body request: PostOfferRequest):Response<IdResponse>

    @GET("offers/{id}")
    suspend fun getOffer(@Header(API_KEY_HEADER)token:String,@Path("id")id:Long):Response<Offer>

    companion object {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY

        }

        val httpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(logging)
        }



        operator fun invoke() : FoodieApi {
            return Retrofit
                .Builder()
                .baseUrl("https://taller2-foodie.herokuapp.com/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FoodieApi::class.java)
        }

        const val USERS_PREFIX="users"
        const val SHOPS_PREFIX="shops"
        const val OFFERS_PREFIX="offers"
        const val API_PREFIX = ""
        const val API_KEY_HEADER = "FOODIE-API-KEY"
    }

}