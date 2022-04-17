package com.example.startupper

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(context: Context, val itemList: ArrayList<Int>) :
RecyclerView.Adapter<RecyclerAdapter.ItemListViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }



    class ItemListViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){

    }
}

