package com.example.startupper

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.startupper.adapter.feedIdeaAdapter
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
import com.yuyakaido.android.cardstackview.*


class FeedActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserId: String
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var storage: FirebaseStorage
    private lateinit var adapterFeed: feedIdeaAdapter
    private lateinit var adapterUser: feedUserAdapter

    private lateinit var feedList: ArrayList<NewBusinessClass>
    private var usertype: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)



        val cardStackView = findViewById<CardStackView>(R.id.feedStackView)
        val manager = CardStackLayoutManager(this, object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction?) {
            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {
            }

            override fun onCardAppeared(view: View?, position: Int) {
            }

            override fun onCardDisappeared(view: View?, position: Int) {
            }

        })
        manager.setStackFrom(StackFrom.Right)
        manager.setDirections(Direction.VERTICAL)
        manager.setVisibleCount(2)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setCanScrollHorizontal(false)
        cardStackView.layoutManager = manager



            val linearLayoutManager = LinearLayoutManager(
                this@FeedActivity,
                LinearLayoutManager.VERTICAL,
                false
            )



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
                                    usertype = z.child("userType").getValue().toString()
                                    Log.e("aaaaa", usertype)

                                    if (usertype == "ideaSearcher") {
                                        Log.e("aaaaa", usertype)
                                        break

                                    } else {
                                        Log.e("aaaaa", usertype)
                                        break@loop
                                    }
                                }
                            }
                        }
                    }
                    if (usertype == "ideaSearcher") {
                        cardStackView.adapter = feedIdeaAdapter(mutableListOf())
                        adapterFeed = cardStackView.adapter as feedIdeaAdapter
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
                        cardStackView.adapter = feedUserAdapter(mutableListOf())
                        adapterUser = cardStackView.adapter as feedUserAdapter
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

