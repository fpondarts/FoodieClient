package com.fpondarts.foodie.model

class OfferItem(secondsTimestamp:Long,val earnings:Float,val offer_id:Long,val order_id:Long) {

    var remainingSeconds:Long = 0

    init {
        val unixTime = System.currentTimeMillis() / 1000L
        remainingSeconds = 120 - (unixTime - secondsTimestamp)
    }


}