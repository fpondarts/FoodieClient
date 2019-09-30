package com.fpondarts.foodie.ui.home.ui.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.MenuItem
import com.fpondarts.foodie.data.db.entity.Shop

class MenuItemViewHolder(inflater: LayoutInflater, parent:ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_menu_item, parent, false)){

    private var mName : TextView? = null
    private var mDescription: TextView? = null
    private var mPrice: TextView? = null

    init {
        mName = itemView.findViewById(R.id.item_name)
        mDescription = itemView.findViewById(R.id.item_description)
        mPrice = itemView.findViewById(R.id.item_price)
    }

    fun bind(item: MenuItem, listener: OnMenuItemClickListener){
        mName?.text = item.name
        mDescription?.text = item.description
        mPrice?.text = "$" + item.price.toString()
        itemView.setOnClickListener { listener.onItemClick(item) }
    }
}