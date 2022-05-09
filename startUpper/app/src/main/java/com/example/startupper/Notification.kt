package com.example.startupper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.adapter.notificationRecylerAdapter
import com.example.startupper.data.dataSource

class Notification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val myDataset = dataSource().loadNotificationModel()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = notificationRecylerAdapter(this, myDataset)
        recyclerView.setHasFixedSize(false)


    }
}