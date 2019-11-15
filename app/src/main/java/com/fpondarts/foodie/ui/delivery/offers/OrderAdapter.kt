package com.fpondarts.foodie.ui.delivery.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.model.OrderPricedItem

class OrderAdapter(private val list: Collection<OrderPricedItem>):
    RecyclerView.Adapter<OrderItemViewHolder>(){

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val item = list.elementAt(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return OrderItemViewHolder(inflater,parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}