package com.fpondarts.foodie.ui.home.ui.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.Shop

class ShopViewHolder(inflater: LayoutInflater, parent:ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_shop, parent, false)){

    private var mShopIm : ImageView? = null
    private var mShopName: TextView? = null
    private var mShopRating: RatingBar? = null

    init {

        mShopIm = itemView.findViewById(R.id.shop_im)
        mShopName = itemView.findViewById(R.id.shop_name)
        mShopRating = itemView.findViewById(R.id.shop_rating)

    }

    fun bind(shop : Shop){

        mShopIm?.setImageURI(Uri.parse(shop.photoUrl))
        mShopName?.text = shop.name
        mShopRating?.rating = shop.rating

    }
}