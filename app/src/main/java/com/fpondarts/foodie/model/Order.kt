package com.fpondarts.foodie.model

class Order(val userId:Int,val shopId:Long) {
    val items = HashMap<Long,OrderItem>()
    var price = 0.0

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

}

