package com.example.startupper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.NewBusinessClass
import com.example.startupper.R
import com.example.startupper.model.peopleLikedModel
import com.squareup.picasso.Picasso

class peopleLikedAdapter(private val dataset : MutableList<peopleLikedModel>):
    RecyclerView.Adapter<peopleLikedAdapter.ItemViewHolder>(){

    class  ItemViewHolder(private  val view : View): RecyclerView.ViewHolder(view){
        var imageUri: ImageView = view.findViewById(R.id.imageIliked)
        val name : TextView = view.findViewById(R.id.primaryText)
        var secondary : TextView = view.findViewById(R.id.secondaryText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_people_liked, parent, false)

        return ItemViewHolder(adapterLayout)
    }
    fun addPerson(person: peopleLikedModel) {
        dataset.add(person)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.name.text =  item.name
        holder.secondary.text = item.secondary
        Picasso.get().load(item.imageuri).into(holder.imageUri)

    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}