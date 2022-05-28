package com.example.firebasechatappwithkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasechatappwithkotlin.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    lateinit var messageAdapter: MessageAdapter
    lateinit var messageList: ArrayList<Message>
    lateinit var mDbRef:DatabaseReference

    var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mDbRef = FirebaseDatabase.getInstance().getReference()

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        //current logged in user uid's id

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        //unique room for sender and receiver
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = "chatting with: $name"

        messageList = arrayListOf()
        messageAdapter = MessageAdapter(this, messageList)
        binding.chatRecyclerView.adapter = messageAdapter

        //add data to recyclerview

        mDbRef.child("chats").child(senderRoom!!).child("messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                messageList.clear()
                for (ps in snapshot.children){
                    val messages = ps.getValue(Message::class.java)
                    messageList.add(messages!!)
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        //adding the message to database
        mDbRef = FirebaseDatabase.getInstance().getReference()
        binding.sendBtn.setOnClickListener {

            val message = binding.messageBox.text.toString()

            val messageObject = Message(message,senderUid)

            //make new node of chats
            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            binding.messageBox.setText("")
        }
    }
}