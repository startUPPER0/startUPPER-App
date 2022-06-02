package com.example.startupper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.R
import com.example.startupper.UserRegisterClass
import com.squareup.picasso.Picasso

class feedUserAdapter(var feedUserlist: MutableList<UserRegisterClass>) :
    RecyclerView.Adapter<feedUserAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.userName)
        val surname: TextView = view.findViewById(R.id.userSurname)
        val interest: TextView = view.findViewById(R.id.userInterest)
        val bio: TextView = view.findViewById(R.id.userBio)
        val imageView : ImageView = view.findViewById(R.id.imageViewUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_feeduser, parent, false)
        return ViewHolder(v)
    }

    fun addUser(feedUser: UserRegisterClass) {
        if(!feedUserlist.contains(feedUser))
            feedUserlist.add(feedUser)
        //notifyItemInserted(feedUserlist.size)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user = feedUserlist[position]
        holder.name.text = user.name
        holder.surname.text = user.surname
        holder.interest.text = user.interest
        holder.bio.text = user.bio
        Picasso.get().load(user.profileImage).into(holder.imageView)
    }


    override fun getItemCount(): Int {
        return feedUserlist.size
    }
    fun getCurrentUser() : UserRegisterClass{
        return feedUserlist[0]
    }

}