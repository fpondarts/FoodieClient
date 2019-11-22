package com.fpondarts.foodie.ui.delivery.offers

interface OnOfferItemClickListener {

    fun onAcceptClick(offer_id:Long,order_id:Long)

    fun onRejectClick(offer_id:Long,order_id:Long)

    fun onViewClick(offer_id:Long,order_id:Long)
}