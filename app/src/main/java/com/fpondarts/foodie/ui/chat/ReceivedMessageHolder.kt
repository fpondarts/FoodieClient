package com.fpondarts.foodie.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.ChatMessage

class ReceivedMessageHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_their_message,parent,false)){

    private lateinit var mBody: TextView
    private lateinit var mName: TextView

    init {
        mBody = itemView.findViewById(R.id.message_body)
        mName = itemView.findViewById(R.id.name)
    }

    fun bind(message: ChatMessage){
        mBody.text = message.body
        mName.text = ""
    }
}