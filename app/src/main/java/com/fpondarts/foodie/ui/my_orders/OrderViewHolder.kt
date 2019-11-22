package com.fpondarts.foodie.ui.my_orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.Order

class OrderViewHolder(inflater:LayoutInflater, parent:ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_order,parent,false)) {

    private var mShopName: TextView? = null;
    private var mDate: TextView? = null;
    private var mPrice: TextView? = null;
    private lateinit var button : Button
    init{
        mShopName = itemView.findViewById(R.id.order_shop_name)
        mDate = itemView.findViewById(R.id.order_date)
        mPrice = itemView.findViewById(R.id.order_card_price)
        button = itemView.findViewById(R.id.view_order_button)
    }

    fun bind(order: Order, listener: OnMyOrderClickListener, active:Boolean){
        mShopName!!.text = "Orden # ${order.order_id}"
        mDate!!.text = order.created_at.substring(0,17)
        val number2digits :Double = Math.round(order.price * 100.0) / 100.0
        mPrice!!.text = "$${number2digits.toString()}"
        button.setOnClickListener(View.OnClickListener {
            listener.onActiveOrderClick(active,order.order_id,order.shop_id,order.delivery_id)
        })
    }
}