package com.fpondarts.foodie.network.response

data class AvailabilityResponse(
    val email: String?,
    val isAvailable : Boolean?,
    val message: String?
)