package com.example.startupper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.R
import com.example.startupper.model.chatsModel
import de.hdodenhof.circleimageview.CircleImageView

class chatsRecyclerAdapter(val context : Context,
                           val dataList : List<chatsModel>) : RecyclerView.Adapter<chatsRecyclerAdapter.chatsViewHolder>()
{

    class chatsViewHolder(val itemView :View) : RecyclerView.ViewHolder(itemView){
        val contactName : TextView = itemView.findViewById(R.id.contactName)
        val lastMessage : TextView = itemView.findViewById(R.id.lastMessage)
        val pp : CircleImageView = itemView.findViewById(R.id.contactProfilePicture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chatsViewHolder {
        val chatCard = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
        return chatsViewHolder(chatCard)
    }

    override fun onBindViewHolder(holder: chatsViewHolder, position: Int) {
        val item = dataList[position]

        holder.contactName.text = context.resources.getString(item.contactNameResourceId)
        holder.lastMessage.text = context.resources.getString(item.lastMessageResourceId)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}







