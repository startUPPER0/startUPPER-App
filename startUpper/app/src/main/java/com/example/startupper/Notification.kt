package com.example.startupper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.adapter.notificationRecylerAdapter
import com.example.startupper.data.dataSource
import com.google.android.material.bottomnavigation.BottomNavigationView

class Notification : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setSelectedItemId(R.id.incomingLikesBottomMenu)

        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.feedBottomMenu -> {
                    startActivity(Intent(this@Notification, FeedActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profileBottomMenu -> {
                    startActivity(Intent(this@Notification, Profile::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.outgoingLikesBottomMenu -> {
                    startActivity(Intent(this@Notification, peopleLiked::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })


    }
}