package com.fpondarts.foodie.data.db.entity

import androidx.room.Entity
import com.google.firebase.database.Exclude

@Entity
data class ChatMessage(
   // val orderId: Long,
    var from: String="",
    var to: String="",
    var body: String="",
    var timestamp: Long=0
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