package com.fpondarts.foodie.ui.delivery.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.R
import com.fpondarts.foodie.model.OrderPricedItem

class OrderItemViewHolder(inflater: LayoutInflater, parent: ViewGroup)
    : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_delivery_order_item,parent,false)){

    val tv_units = itemView.findViewById<TextView>(R.id.order_item_units)
    val tv_unit_price:TextView = itemView.findViewById<TextView>(R.id.order_item_unit_price)
    val tv_name= itemView.findViewById<TextView>(R.id.order_item_name)


    fun bind(item:OrderPricedItem) {
        tv_name.text = item.name
        tv_units.text = item.units.toString() + " unidades"
        tv_unit_price.text = "$"+item.unitPrice.toString()+" / unidad"
    }

}