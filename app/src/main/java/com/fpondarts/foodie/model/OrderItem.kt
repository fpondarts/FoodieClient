package com.fpondarts.foodie.model

data class OrderItem(val id:Long, var units:Int, val price:Float) {

    fun addUnits(moreUnits:Int){
        units+=moreUnits
    }

    fun itemPrice():Float{
        return units * price
    }
}