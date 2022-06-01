package com.example.startupper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.R
import com.example.startupper.model.notificationModel
import com.squareup.picasso.Picasso

class notificationRecylerAdapter(private  val context : Context,
                                 private val dataset : List<notificationModel>):
    RecyclerView.Adapter<notificationRecylerAdapter.ItemViewHolder>(){

    class  ItemViewHolder(private  val view : View): RecyclerView.ViewHolder(view){
        var imageUri: ImageView = view.findViewById(R.id.notificationImage)
        val name : TextView = view.findViewById(R.id.item_name)
        val email : TextView = view.findViewById(R.id.item_mail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.name.text =  item.name
        holder.email.text = item.email
        Picasso.get().load(item.imageuri).into(holder.imageUri)

    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}