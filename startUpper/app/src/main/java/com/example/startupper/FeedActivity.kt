package com.example.startupper

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.adapter.feedAdapter
import com.example.startupper.adapter.feedUserAdapter
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


class FeedActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserId: String
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var storage: FirebaseStorage
    private lateinit var adapterFeed: feedAdapter
    private lateinit var adapterUser: feedUserAdapter
    private lateinit var recyclerview: RecyclerView

    private lateinit var feedList: ArrayList<NewBusinessClass>
    private var usertype: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        recyclerview = findViewById(R.id.feedRecylerView)
        val linearLayoutManager = LinearLayoutManager(
            this@FeedActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerview.layoutManager = linearLayoutManager
        adapterUser = feedUserAdapter(mutableListOf())

        recyclerview.adapter = adapterUser


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




        auth = Firebase.auth
        storage = Firebase.storage
        var currentUser = auth.currentUser
        if (currentUser != null) {
            currentUserId = currentUser.uid

        }
        //veri okuma
        var getdata = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {


                loop@ for (i in snapshot.children) {
                    Log.e("control", i.key.toString())
                    if (i.key.toString() == "Users") {
                        for (z in i.children) {

                            if (currentUserId == z.key.toString()) {
                                Log.e("deneme", z.child(currentUserId).getValue().toString())
                                usertype = z.child("userType")
                                    .getValue().toString()
                                Log.e("aaaaa", usertype)

                                if (usertype == "ideaSearcher") {
                                    recyclerview = findViewById(R.id.feedRecylerView)
                                    val linearLayoutManager = LinearLayoutManager(
                                        this@FeedActivity,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                    recyclerview.layoutManager = linearLayoutManager

                                    adapterFeed = feedAdapter(mutableListOf())
                                    recyclerview.adapter = adapterFeed
                                    Log.e("aaaaa", usertype)

                                    break
                                    break@loop


                                } else {
                                    Log.e("aaaaa", usertype)
                                    break@loop
                                }

                            }
                        }


                    }

                }

                if (usertype == "ideaSearcher") {
                    for (i in snapshot.children) {
                        Log.e("control", "xyxyxyxyx")
                        if (i.key.toString() == "feeds") {
                            Log.e("first children", i.toString())
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
                                    var image =
                                        z.child("imageUri").getValue().toString()
                                    Log.e("AAAAA3", description)
                                    adapterFeed.addBusiness(
                                        NewBusinessClass(
                                            businessname,
                                            location,
                                            description,
                                            image
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                if (usertype == "ideaOwner") {
                    for (i in snapshot.children) {
                        if (i.key.toString() == "Users") {
                            for (y in i.children) {
                                var name = y.child("name").getValue().toString()
                                var surname = y.child("surname").getValue().toString()
                                var location = y.child("location").getValue().toString()
                                var dob = y.child("date").getValue().toString()
                                var email = y.child("email").getValue().toString()
                                adapterUser.addUser(
                                    UserRegisterClass(
                                        name,
                                        surname,
                                        email,
                                        dob,
                                        location,
                                        "0000",
                                        "000",
                                        ""

                                    )
                                )
                            }
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