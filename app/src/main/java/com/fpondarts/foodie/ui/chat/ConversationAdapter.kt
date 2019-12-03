package com.fpondarts.foodie.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.data.db.entity.ChatMessage
import com.fpondarts.foodie.ui.my_orders.OrderViewHolder

class ConversationAdapter(val list:List<ChatMessage>, val myFbId: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val RECEIVED_TYPE = 1
    val SENT_TYPE = 2

    override fun getItemViewType(position: Int): Int {
        val message = list[position]
        var type: Int = 0
        if (message.from == myFbId) {
            type = SENT_TYPE
        } else {
            type = RECEIVED_TYPE
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == SENT_TYPE){
            return SentMessageHolder(inflater,parent)
        }
        return ReceivedMessageHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = list[position]
        if (holder.itemViewType == RECEIVED_TYPE) {
            (holder as ReceivedMessageHolder).bind(message)
        }
        else if (holder.itemViewType == SENT_TYPE){
            (holder as SentMessageHolder).bind(message)
        }
    }
}
