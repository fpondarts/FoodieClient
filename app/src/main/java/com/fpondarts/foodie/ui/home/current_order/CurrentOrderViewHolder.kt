package com.fpondarts.foodie.ui.home.current_order

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
    var units:TextView? = null
    var price:TextView? = null


    init {
        price = itemView.findViewById(R.id.order_item_unit_price)
        units = itemView.findViewById(R.id.order_item_units)
    }

    fun bind(item:OrderItem, listener: OnOrderItemClickListener){

        units?.text = item.units.toString()
        itemView.findViewById<Button>(R.id.button_remove_item).setOnClickListener{
            listener.onItemClick(item)
        }
    }
}