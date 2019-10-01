package com.fpondarts.foodie.ui.home.ui.home.shop_menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.data.db.entity.MenuItem
import com.fpondarts.foodie.ui.home.ui.home.OnMenuItemClickListener

class ShopMenuAdapter(private val menu: ArrayList<MenuItem>, val listener: OnMenuItemClickListener)
    : RecyclerView.Adapter<MenuItemViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return MenuItemViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        val item: MenuItem = menu[position]
        holder.bind(item,listener)
    }


    override fun getItemCount(): Int = menu.size

}