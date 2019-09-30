package com.fpondarts.foodie.model

class Order(userId:Int,shopId:Int) {
    val prices = HashMap<Int,Float>()
    val items = HashMap<Int,Int>()

    var price = 0.0

    fun addItems(id:Int,number:Int,price:Float){
        if (items.containsKey(id)){
            items.put(id,items.get(id)!! + number)
        } else {
            items.put(id,number)
        }
        prices.put(id,price)
        updatePrice()
    }

    fun updatePrice() {
        price = 0.0
        for ((id,number) in items){
            price += prices.get(id)!! * number
        }
    }

}

