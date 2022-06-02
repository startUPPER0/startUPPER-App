package com.example.startupper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.adapter.peopleLikedAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
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

class peopleLiked : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserId: String
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var storage: FirebaseStorage
    private lateinit var recyclerview: RecyclerView
    private var usertype: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people_liked)

        recyclerview = findViewById(R.id.peopleLikedRW)
        val linearLayoutManager = LinearLayoutManager(
            this,LinearLayoutManager.VERTICAL,
            false
        )
        recyclerview.layoutManager = linearLayoutManager
        recyclerview.adapter = peopleLikedAdapter(mutableListOf())
        database = Firebase.database.reference
        auth = Firebase.auth
        storage = Firebase.storage



        var currentUser = auth.currentUser
        if (currentUser != null) {
            currentUserId = currentUser.uid

        }


        var getData = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                usertype = snapshot.child("Users").child(currentUserId).child("userType").value.toString()

                if (usertype =="ideaOwner") {
                    for(i in snapshot.child("Users").child(currentUserId).)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }








        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setSelectedItemId(R.id.outgoingLikesBottomMenu)

        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.feedBottomMenu -> {
                    startActivity(Intent(this, FeedActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profileBottomMenu -> {
                    startActivity(Intent(this,Profile::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.incomingLikesBottomMenu -> {
                    startActivity(Intent(this, Notification::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }
}