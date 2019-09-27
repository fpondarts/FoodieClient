package com.fpondarts.foodie.ui.home.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.data.db.entity.Shop

class ShopAdapter(private val list: MutableList<Shop>)
    : RecyclerView.Adapter<ShopViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return ShopViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val movie: Shop = list[position]
        holder.bind(movie)
    }

    fun updateShops(newShops:List<Shop>){
        list.addAll(newShops);
    }

    override fun getItemCount(): Int = list.size

}