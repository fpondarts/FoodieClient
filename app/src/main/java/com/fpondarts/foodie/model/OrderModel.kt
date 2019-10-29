package com.fpondarts.foodie.model

class OrderModel(val userId:Long, val shopId:Long) {
    val items = HashMap<Long,OrderItem>()
    var price = 0.0
    var id:Long?=null
    var latitude: Double? = null
    var longitude: Double? = null
    var payWitPoints:Boolean = false
    var favourPoints:Int = 0
    private var deliveryPrice:Float?=null


    fun addItem(item:OrderItem){
        if (items.containsKey(item.id)){
            items[item.id]!!.units+=item.units
        } else {
            items[item.id] = item
        }
        updatePrice()
    }

    fun updatePrice() {
        price = 0.0
        for ((id,item) in items){
            price += item.price * item.units
        }
    }

    fun removeItem(itemId:Long){
        items.remove(itemId)
        updatePrice()
    }

    fun setDeliveryPrice(price:Float){
        if (price > 0){
            deliveryPrice = price
        }
    }

    fun getDeliveryPrice():Float?{
        return deliveryPrice
    }


}

