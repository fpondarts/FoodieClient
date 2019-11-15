package com.fpondarts.foodie.ui.delivery.offers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.R
import com.fpondarts.foodie.model.OfferItem

class OfferItemViewHolder(inflater: LayoutInflater, parent:ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_offer,parent,false)) {

    val time = itemView.findViewById<TextView>(R.id.tiempo_restante)

    val earnings: TextView = itemView.findViewById(R.id.tv_earnings)


    fun bind(item:OfferItem,listener: OnOfferItemClickListener){
        time.text = item.remainingSeconds.toString()
        earnings.text = item.earnings.toString()
        itemView.findViewById<Button>(R.id.button_accept).setOnClickListener(View.OnClickListener {
            listener.onAcceptClick()
        })

        itemView.findViewById<Button>(R.id.button_reject).setOnClickListener(View.OnClickListener {
            listener.onRejectClick()
        })

        itemView.findViewById<Button>(R.id.button_detail).setOnClickListener(View.OnClickListener {
            listener.onViewClick()
        })
    }



}