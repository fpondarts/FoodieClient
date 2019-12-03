package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import com.google.firebase.database.Exclude

@Entity
data class ChatMessage(
   // val orderId: Long,
    val from: String,
    val to: String,
    val body: String,
    val timestamp: Long
) {

    @Exclude
    fun toMap():Map<String,Any?>{
        return mapOf(
            "from" to from,
            "to" to to,
            "body" to body,
            "timestamp" to timestamp
        )
    }
}