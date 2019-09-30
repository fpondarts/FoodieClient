package com.fpondarts.foodie.data.db.entity

class Menu (
    val shopId:Int,
    val items:ArrayList<MenuItem>
){
    fun addItem(menuItem: MenuItem){
        items.add(menuItem)
    }

}