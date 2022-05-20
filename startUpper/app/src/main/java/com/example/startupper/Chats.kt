package com.example.startupper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.adapter.chatsRecyclerAdapter
import com.example.startupper.data.dataSource

class Chats : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        val dataList = dataSource().loadChatsModel()
        val recyclerView : RecyclerView = findViewById(R.id.chatRecyclerView)
        recyclerView.adapter = chatsRecyclerAdapter(this, dataList)



    }
}