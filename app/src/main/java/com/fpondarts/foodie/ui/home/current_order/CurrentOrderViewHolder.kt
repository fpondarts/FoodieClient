package com.fpondarts.foodie.ui.home.current_order

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.R
import com.fpondarts.foodie.model.NamedOrderItem
import com.fpondarts.foodie.model.OrderItem

class CurrentOrderViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_order_item,parent,false))
{
    var units:TextView = itemView.findViewById(R.id.order_item_units)
    var price:TextView = itemView.findViewById(R.id.order_item_unit_price)
    var name:TextView = itemView.findViewById(R.id.order_item_name)
    var id:Long = 0


    fun bind(item: NamedOrderItem, listener: OnOrderItemClickListener){
        id = item.id
        units.text = item.units.toString() + " unidades"
        price.text = "$"+item.price.toString().substring(0,4)
        name.text = item.name
        itemView.findViewById<Button>(R.id.button_remove_item).setOnClickListener{
            listener.onItemClick(id)
        }
    }
}