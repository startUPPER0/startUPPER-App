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
    private var manager: CardStackLayoutManager? = null


    private var usertype: String = ""

    companion object{
        var feedIdeaList: MutableList<NewBusinessClass> = mutableListOf()
        var feedUserList: MutableList<UserRegisterClass> = mutableListOf()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)



        val cardStackView = findViewById<CardStackView>(R.id.feedStackView)
        val manager = CardStackLayoutManager(this, object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction?) {
                if(direction == Direction.Top) {
                   // Log.v("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                    //    feedIdeaList.get(0).businessName)
                   // feedIdeaList.removeAt(0)
                }
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
                        if (i.key.toString() == "Users") {
                            for (z in i.children) {

                                if (currentUserId == z.key.toString()) {
                                    usertype = z.child("userType").getValue().toString()

                                    if (usertype == "ideaSearcher") {
                                        break

                                    } else {
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
                            if (i.key.toString() == "feeds") {
                                for (y in i.children) {
                                    for (z in y.children) {
                                        var businessname =
                                            z.child("businessName").getValue().toString()
                                        var location =
                                            z.child("location").getValue().toString()
                                        var description =
                                            z.child("description").getValue().toString()
                                        var image =
                                            z.child("imageUri").getValue().toString()
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
                                    if(currentUserId != y.key.toString()){
                                        var name = y.child("name").getValue().toString()
                                        var surname = y.child("surname").getValue().toString()
                                        var location = y.child("location").getValue().toString()
                                        var dob = y.child("date").getValue().toString()
                                        var email = y.child("email").getValue().toString()
                                        var image = y.child("imageUri").getValue().toString()
                                        adapterUser.addUser(
                                            UserRegisterClass(
                                                name,
                                                surname,
                                                email,
                                                dob,
                                                location,
                                                "0000",
                                                "000",
                                                "",
                                                image,
                                                ""
                                            )
                                        )
                                    }
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

