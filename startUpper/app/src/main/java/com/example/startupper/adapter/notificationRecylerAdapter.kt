package com.example.startupper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.R
import com.example.startupper.model.notificationModel

class notificationRecylerAdapter(private  val context : Context,
                                 private val dataset : List<notificationModel>):
                                RecyclerView.Adapter<notificationRecylerAdapter.ItemViewHolder>(){

    class  ItemViewHolder(private  val view : View): RecyclerView.ViewHolder(view){
        val textView : TextView = view.findViewById(R.id.item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): notificationRecylerAdapter.ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: notificationRecylerAdapter.ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text =  context.resources.getString(item.stringResourceId)

    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}