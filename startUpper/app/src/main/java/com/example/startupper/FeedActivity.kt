package com.example.startupper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.adapter.feedAdaptor
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class FeedActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserId: String
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var storage: FirebaseStorage
    private lateinit var adapter: feedAdaptor
    private lateinit var recyclerview: RecyclerView
    private lateinit var feedList: ArrayList<NewBusinessClass>
    private lateinit var snap: DataSnapshot
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setSelectedItemId(R.id.feedBottomMenu)

        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.notificationBottomMenu -> {
                    startActivity(Intent(this@FeedActivity, Notification::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profileBottomMenu -> {
                    startActivity(Intent(this@FeedActivity, Profile::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.inboxBottomMenu -> {
                    startActivity(Intent(this@FeedActivity, Inbox::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })

        database = Firebase.database.reference
        feedList = ArrayList()

        recyclerview = findViewById(R.id.feedRecylerView)
        adapter = feedAdaptor(mutableListOf())
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this@FeedActivity)


        auth = Firebase.auth
        storage = Firebase.storage
        var currentUser = auth.currentUser
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        //veri okuma
        var getdata = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    for (y in i.children) {
                        for (z in y.children) {
                            var businessname =
                                z.child("businessName").getValue().toString()
                            Log.e("AAAAA1", businessname)
                            var location =
                                z.child("location").getValue().toString()
                            Log.e("AAAAA2", location)
                            var description =
                                z.child("description").getValue().toString()
                            Log.e("AAAAA3", description)
                            adapter.addBusiness(
                                NewBusinessClass(
                                    businessname,
                                    location,
                                    description
                                )
                            )
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        database.addValueEventListener(getdata)
    }

    override fun onStart() {
        super.onStart()
        bottomNavigationView.setSelectedItemId(R.id.feedBottomMenu)


    }
}