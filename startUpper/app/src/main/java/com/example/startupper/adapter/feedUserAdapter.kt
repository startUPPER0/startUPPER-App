package com.example.startupper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.NewBusinessClass
import com.example.startupper.R
import com.example.startupper.UserRegisterClass

class feedUserAdapter(var feedUserlist: MutableList<UserRegisterClass>) :
    RecyclerView.Adapter<feedUserAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.feedUserLayoutName)
        val surname: TextView = view.findViewById(R.id.feedUserLayoutSurname)
        val location: TextView = view.findViewById(R.id.feedUserLayoutLocation)
        val dob: TextView = view.findViewById(R.id.feedUserLayoutDOB)
        //  val contactDeleteIV: ImageView = view.findViewById(R.id.layoutContactDeleteIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.feeduserlayout, parent, false)
        return ViewHolder(v)
    }

    fun addUser(feedUser: UserRegisterClass) {
        feedUserlist.add(feedUser)
        notifyItemInserted(feedUserlist.size - 1)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user = feedUserlist[position]
        holder.name.text = user.name
        holder.surname.text = user.surname
        holder.location.text = user.location
        holder.dob.text = user.date
    }

    override fun getItemCount(): Int {
        return feedUserlist.size
    }
}