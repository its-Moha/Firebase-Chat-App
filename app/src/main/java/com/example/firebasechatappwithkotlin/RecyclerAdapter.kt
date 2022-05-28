package com.example.firebasechatappwithkotlin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatappwithkotlin.databinding.ListItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener

class RecyclerAdapter(private val context: Context, private val userList: ArrayList<Data>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var name = userList[position]
        holder.bind(name)

        holder.itemView.setOnClickListener {
            val intent = Intent(context,ChatActivity::class.java)
            intent.putExtra("name",name.UserName)
            intent.putExtra("uid",name.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder(var listItemBinding: ListItemBinding): RecyclerView.ViewHolder(listItemBinding.root){

        fun bind(mydata: Data){
            listItemBinding.name.text = mydata.UserName

        }
    }

}
