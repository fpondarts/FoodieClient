package com.fpondarts.foodie.ui.delivery.delivered

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.data.db.entity.Order
import com.fpondarts.foodie.ui.my_orders.OnMyOrderClickListener
import com.fpondarts.foodie.ui.my_orders.OrderViewHolder

class DeliveredOrdersAdapter(private val list: List<Order>, val listener: OnDeliveredOrderClickListener)
    : RecyclerView.Adapter<DeliveredOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveredOrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DeliveredOrderViewHolder(inflater,parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DeliveredOrderViewHolder, position: Int) {
        val order = list[position]
        holder.bind(order,listener)
    }


}