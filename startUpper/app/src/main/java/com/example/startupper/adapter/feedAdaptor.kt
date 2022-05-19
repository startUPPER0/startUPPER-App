package com.example.startupper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.NewBusinessClass
import com.example.startupper.R

class feedAdaptor(var feedlist: MutableList<NewBusinessClass>) :
    RecyclerView.Adapter<feedAdaptor.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val businessname: TextView = view.findViewById(R.id.projectNameTextview)
        val location: TextView = view.findViewById(R.id.businessLocationTextview)
        val desc: TextView = view.findViewById(R.id.businessdescTextview)
        //  val contactDeleteIV: ImageView = view.findViewById(R.id.layoutContactDeleteIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.feedlayout, parent, false)
        return ViewHolder(v)
    }

    fun addBusiness(business: NewBusinessClass) {
        feedlist.add(business)
        notifyItemInserted(feedlist.size - 1)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var feed = feedlist[position]
        holder.businessname.text = feed.businessName
        holder.location.text = feed.location
        holder.desc.text = feed.description
    }

    override fun getItemCount(): Int {
        return feedlist.size
    }
}