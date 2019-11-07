package com.fpondarts.foodie.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.data.db.entity.ChatMessage
import java.time.Instant
import java.time.format.DateTimeFormatter

class ChatMessageAdapter(val list: List<ChatMessage>) : RecyclerView.Adapter<ChatMessageViewHolder>() {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        val message = list[position]
        holder.bind(message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ChatMessageViewHolder(inflater,parent)
    }
}