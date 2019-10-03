package com.fpondarts.foodie.ui.home.ui.home.current_order

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.R
import com.fpondarts.foodie.model.OrderItem

class CurrentOrderViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_order_item,parent,false))
{
    var name:TextView? = null
    var units:TextView? = null
    var price:TextView? = null


    init {
        name = itemView.findViewById(R.id.order_item_name)
        price = itemView.findViewById(R.id.item_price)
        units = itemView.findViewById(R.id.order_item_units)
    }

    fun bind(item:OrderItem, listener: OnOrderItemClickListener){

        name?.text = item.name
        price?.text = "x $" + item.price.toString()
        itemView.findViewById<Button>(R.id.button_remove_item).setOnClickListener{ listener.onItemClick(item) }
    }
}