package com.fpondarts.foodie.ui.my_orders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.Order

class OrderViewHolder(inflater:LayoutInflater, parent:ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_order,parent,false)) {

    private var mShopName: TextView? = null;
    private var mDate: TextView? = null;
    private var mPrice: TextView? = null;

    init{
        mShopName = itemView.findViewById(R.id.order_shop_name)
        mDate = itemView.findViewById(R.id.order_date)
        mPrice = itemView.findViewById(R.id.order_card_price)
    }

    fun bind(order: Order, listener: OnMyOrderClickListener, active:Boolean){
        mDate!!.text = order.created_at
        mPrice!!.text = order.created_at
        listener.onActiveOrderClick(active,order.order_id,order.shop_id,order.delivery_id)
    }
}