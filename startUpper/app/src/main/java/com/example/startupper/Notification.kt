package com.example.startupper

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.adapter.notificationRecylerAdapter
import com.example.startupper.adapter.peopleLikedAdapter
import com.example.startupper.data.dataSource
import com.example.startupper.model.notificationModel
import com.example.startupper.model.peopleLikedModel
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

class Notification : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserId: String
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var storage: FirebaseStorage
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: notificationRecylerAdapter

    private var usertype: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)


        recyclerview = findViewById(R.id.recycler_view)
        val linearLayoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL,
            false
        )
        recyclerview.layoutManager = linearLayoutManager
        database = Firebase.database.reference
        auth = Firebase.auth
        storage = Firebase.storage


        var currentUser = auth.currentUser
        if (currentUser != null) {
            currentUserId = currentUser.uid

        }


        var getData = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                adapter = notificationRecylerAdapter(mutableListOf())
                recyclerview.adapter = adapter
                usertype =
                    snapshot.child("Users").child(currentUserId).child("userType").value.toString()

                if (usertype == "ideaOwner") {
                    //my liked businesses
                    for (i in snapshot.child("Users").child(currentUserId).child("peopleWhoLikedMe").children) {
                        //each person who liked it
                        for(k in i.children) {
                            var image = snapshot.child("Users").child(k.key.toString())
                                .child("imageUri").value.toString()
                            var name = "Name : " + snapshot.child("Users").child(k.key.toString())
                                .child("name").value.toString() + " liked " + i.key.toString()
                            var email = "Email : " + snapshot.child("Users").child(k.key.toString())
                                .child("email").value.toString()
                            adapter.addPerson(
                                notificationModel(name, email, image)
                            )
                        }


                    }

                }


                if (usertype == "ideaSearcher") {
                    for (i in snapshot.child("Users").child(currentUserId)
                        .child("peopleWhoLikedMe").children) {

                        var image = snapshot.child("Users").child(i.key.toString())
                            .child("imageUri").value.toString()
                        var name = "Name : " + snapshot.child("Users").child(i.key.toString())
                            .child("name").value.toString()//
                        var email = "Email : " + snapshot.child("Users").child(i.key.toString())
                            .child("email").value.toString()
                        adapter.addPerson(
                            notificationModel(name, email, image)
                        )


                    }

                }


            }

            override fun onCancelled(error: DatabaseError) {
            }

        }

        database.addValueEventListener(getData)


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