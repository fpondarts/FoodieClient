package com.fpondarts.foodie.ui.home.ui.home

import com.fpondarts.foodie.data.db.entity.Shop

interface OnShopClickListener {

    fun onItemClick(shop: Shop);
}