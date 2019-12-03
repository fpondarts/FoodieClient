package com.fpondarts.foodie.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.ChatMessage

class SentMessageHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.item_my_message,parent,false)) {

    private lateinit var mBody: TextView

    init{
        mBody = itemView.findViewById<TextView>(R.id.message_body)
    }

    fun bind(message: ChatMessage){
        mBody.text = message.body
    }

}