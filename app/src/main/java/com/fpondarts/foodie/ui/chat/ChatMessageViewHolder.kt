package com.fpondarts.foodie.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.ChatMessage
import java.time.format.DateTimeFormatter

class ChatMessageViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_sent_message,parent,false)) {


    private var mMessage: TextView? = null
    private var mDate: TextView? = null


    init{
        mMessage = itemView.findViewById(R.id.message_body)
        mDate = itemView.findViewById(R.id.order_date)
    }

    fun bind(message: ChatMessage){
        mMessage!!.text = message.body
        mDate!!.text = DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(message.timestamp))
    }

}