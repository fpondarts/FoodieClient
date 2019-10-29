package com.fpondarts.foodie.model

enum class OrderState(val stringVal: String) {
    DELIVERED("delivered"),
    ON_WAY("onWay"),
    CREATED("created"),
    CANCELLED("cancelled")
}