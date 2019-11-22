package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity

@Entity
data class ChatMessage(
    val orderId: Long,
    val from: String,
    val to: String,
    val body: String,
    val timestamp: Long
) {
}