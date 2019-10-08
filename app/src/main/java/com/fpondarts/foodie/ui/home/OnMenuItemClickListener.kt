package com.fpondarts.foodie.ui.home

import com.fpondarts.foodie.data.db.entity.MenuItem

interface OnMenuItemClickListener {

    fun onItemClick(item: MenuItem)
}