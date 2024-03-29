package com.example.startupper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.NewBusinessClass
import com.example.startupper.R

import com.squareup.picasso.Picasso

class myBusinessAdaptor(var feedlist: MutableList<NewBusinessClass>) :
    RecyclerView.Adapter<myBusinessAdaptor.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val businessname: TextView = view.findViewById(R.id.projectNameTextview)
        val location: TextView = view.findViewById(R.id.businessLocationTextview)
        val desc: TextView = view.findViewById(R.id.businessdescTextview)
        var imageBusiness: ImageView = view.findViewById(R.id.businessFeedImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.mybusinesslayout, parent, false)
        return ViewHolder(v)
    }
    fun addBusiness(business: NewBusinessClass) {
        feedlist.add(business)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var feed = feedlist[position]
        holder.businessname.text = feed.businessName
        holder.location.text = feed.location
        holder.desc.text = feed.description
        Picasso.get().load(feed.imageuri).into(holder.imageBusiness)

    }


    override fun getItemCount(): Int {
        return feedlist.size
    }
}