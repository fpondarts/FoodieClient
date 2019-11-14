package com.fpondarts.foodie.ui.delivery.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.R

class OfferItemViewHolder(inflater: LayoutInflater, parent:ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_offer,parent,false)) {

    var time: TextView? = null
    var earnings: TextView? = null

    init {
        time = itemView.findViewById(R.id.tiempo_restante)
        earnings = itemView.findViewById(R.id.tv_earnings)
    }


}