package com.fpondarts.foodie.ui.chat


import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.db.entity.ChatMessage
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_conversation.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class ConversationFragment : Fragment(), KodeinAware, ChildEventListener {


    override fun onCancelled(p0: DatabaseError) {

    }

    override fun onChildMoved(p0: DataSnapshot, p1: String?) {

    }

    override fun onChildChanged(p0: DataSnapshot, p1: String?) {

    }

    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        val message = p0.getValue(ChatMessage::class.java)
        message?.let{
            messageList.add(message)
        }
    }

    override fun onChildRemoved(p0: DataSnapshot) {

    }

    override val kodein by kodein()

    private lateinit var database: DatabaseReference

    val messageList = ArrayList<ChatMessage>()

    var order_id: Long = -1
    private lateinit var my_id: String
    private lateinit var their_id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        database = FirebaseDatabase.getInstance().reference

        order_id = arguments!!.getLong("order_id")
        my_id = arguments!!.getString("my_id", "")
        their_id = arguments!!.getString("their_id","")

        return inflater.inflate(R.layout.fragment_conversation,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerview_message_list.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        recyclerview_message_list.apply {
            adapter = ConversationAdapter(messageList,my_id)
        }

        val conversation = database.child("chats").child(order_id.toString()).orderByChild("timestamp")
        conversation.addChildEventListener(this)


        button_chatbox_send.setOnClickListener {
            val body = edittext_chatbox.text.toString()
            if (body.isNullOrBlank()){

            } else {
                sendMessage(body)
            }
        }
    }

    fun sendMessage(body:String){
        val key =  database.child("chats").child("$order_id").push().key
        sending_message.visibility = View.VISIBLE

        if (key == null){
            Log.w("ChatMessage", "Couldn't get push key for message")
            sending_message.visibility = View.GONE
            return
        }
        val chatMessage = ChatMessage(my_id,their_id,body,System.currentTimeMillis())
        val messageValues = chatMessage.toMap()

        val childUpdates = HashMap<String,Any>()
        childUpdates["chats/$order_id/$key"] = messageValues

        database.updateChildren(childUpdates).addOnCompleteListener {
            if (it.isSuccessful){
                edittext_chatbox.text.clear()
                sending_message.visibility = View.GONE
            } else {
                Toast.makeText(activity,"No pudo enviarse el mensaje, intente nuevamente",Toast.LENGTH_SHORT).show()
                sending_message.visibility = View.GONE
            }
        }
        sending_message.visibility = View.GONE

    }

}
