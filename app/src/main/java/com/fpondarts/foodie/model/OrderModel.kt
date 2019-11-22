package com.fpondarts.foodie.model

class OrderModel(val userId:Long, val shopId:Long) {
    val items = HashMap<Long,OrderItem>()
    val prices = HashMap<Long,Float>()
    val names = HashMap<Long,String>()
    var price = 0.0
    var id:Long?=null
    var latitude: Double? = null
    var longitude: Double? = null
    var payWitPoints:Boolean = false
    var favourPoints:Int = 0
    private var deliveryPrice:Float?=null


    fun addItem(item:OrderItem,name:String,itemPrice:Float){
        if (items.containsKey(item.product_id)){
            items[item.product_id]!!.units+=item.units
            prices.replace(item.product_id,itemPrice)
        } else {
            items[item.product_id] = item
            prices.put(item.product_id,itemPrice)
            names.put(item.product_id,name)
        }

        updatePrice()
    }

    fun updatePrice() {
        price = 0.0
        for ((id,item) in items){
            price += prices.get(id)!! * item.units
        }
    }

    fun removeItem(itemId:Long){
        items.remove(itemId)
        names.remove(itemId)
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

    fun isEmpty():Boolean{
        return (items.isEmpty() || price == 0.0)
    }


}

