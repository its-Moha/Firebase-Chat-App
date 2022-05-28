package com.example.firebasechatappwithkotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatappwithkotlin.databinding.ListItemBinding
import com.example.firebasechatappwithkotlin.databinding.ReceiveBinding
import com.example.firebasechatappwithkotlin.databinding.SentBinding
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, private val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SEND = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1){
            // inflate receive
            return ReceiveViewHolder(ReceiveBinding.inflate(LayoutInflater.from(parent.context),parent, false))

        }else{
            return SentViewHolder(SentBinding.inflate(LayoutInflater.from(parent.context),parent, false))

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
        } else {
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        //if the UID of the current User matches the sender id of this message we have to in inflate
        // send view holder

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderUid)){
            return ITEM_SEND
        }else{
            // you are not logged in and other user is sending message
            return ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {

        return messageList.size
    }

    class SentViewHolder(private var sentBinding: SentBinding) :
        RecyclerView.ViewHolder(sentBinding.root) {

        val sentMessage = itemView.findViewById<TextView>(R.id.sentMessage)
    }

    class ReceiveViewHolder(private var receiveBinding: ReceiveBinding) :
        RecyclerView.ViewHolder(receiveBinding.root) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.receiveMessage)
    }

}