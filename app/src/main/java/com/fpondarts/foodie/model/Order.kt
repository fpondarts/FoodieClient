package com.fpondarts.foodie.model

class Order(val userId:Int,val shopId:Int) {
    val items = HashMap<Int,OrderItem>()
    var price = 0.0
    fun addItems(id:Int,number:Int,price:Float){
        if (items.containsKey(id)){
            items[id]!!.addUnits(number)
        } else {
            items[id] = OrderItem(id.toLong(),number,price)
        }
        updatePrice()
    }

    fun updatePrice() {
        price = 0.0
        for ((id,item) in items){
            price += item.itemPrice()
        }
    }

}

