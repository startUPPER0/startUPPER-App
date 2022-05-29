package com.example.startupper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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

    private var usertype: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        database = Firebase.database.reference
        auth = Firebase.auth
        storage = Firebase.storage

        auth.currentUser?.let {
            database.child("Users").child(it.uid).child("userType").get()
                .addOnSuccessListener {
                    usertype = it.value.toString()
                }
        }

        var cardStackView = findViewById<CardStackView>(R.id.feedStackView)
        val manager = CardStackLayoutManager(this, object : CardStackListener {

            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onCardSwiped(direction: Direction?) {
                if (usertype == "ideaSearcher") {
                    var likedID = ""
                    var likedBusiness = adapterFeed.getCurrentIdea().businessName

                    database.child("feeds").get().addOnSuccessListener {
                        //users
                        loop@ for (i in it.children) {
                            //each user's ideas'
                            for (k in i.children) {
                                if (k.child("businessName").value?.equals(likedBusiness) == true) {
                                    likedID = i.key.toString()
                                    break@loop
                                }
                            }
                        }
                        if (direction == Direction.Top) {
                            database.child("Users").child(currentUserId).child("liked")
                                .child(likedID).child(likedBusiness).setValue("")
                            cardStackView.adapter?.notifyDataSetChanged()

                        }
                        if (direction == Direction.Bottom) {
                            database.child("Users").child(currentUserId).child("disliked")
                                .child(likedID).child(likedBusiness).setValue("")
                            cardStackView.adapter?.notifyDataSetChanged()
                        }
                    }
                } else if (usertype == "ideaOwner") {
                    var likedID = ""
                    var likedName = adapterUser.getCurrentUser().name
                    database.child("Users").get().addOnSuccessListener {
                        //users
                        loop@ for (i in it.children) {
                            if (i.child("name").value?.toString().equals(likedName)) {
                                likedID = i.key.toString()
                                break@loop
                            }
                        }
                        if (direction == Direction.Top) {
                            database.child("Users").child(currentUserId).child("liked")
                                .child(likedID).setValue(likedName)
                            cardStackView.adapter?.notifyDataSetChanged()
                        }
                        if (direction == Direction.Bottom) {
                            database.child("Users").child(currentUserId).child("disliked")
                                .child(likedID).setValue(likedName)
                            cardStackView.adapter?.notifyDataSetChanged()

                        }
                    }
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
        manager.setVisibleCount(3)
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
                /* R.id.notificationBottomMenu -> {
                     startActivity(Intent(this@FeedActivity, Notification::class.java))
                     overridePendingTransition(0, 0)
                     return@OnNavigationItemSelectedListener true
                 }*/
                R.id.profileBottomMenu -> {
                    startActivity(Intent(this@FeedActivity, Profile::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                /* R.id.inboxBottomMenu -> {
                     startActivity(Intent(this@FeedActivity, Inbox::class.java))
                     overridePendingTransition(0, 0)
                     return@OnNavigationItemSelectedListener true
                 } */
            }
            false
        })


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
                                usertype = z.child("userType").value.toString()
                                if (usertype == "ideaSearcher") {
                                    break
                                } else
                                    break@loop
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
                                    if (currentUserId != y.key.toString()) {
                                        var hasSeen = false

                                        for(k in snapshot.child("Users").child(currentUserId).child("liked").children) {

                                            if(k.key.toString() == y.key.toString()) {
                                                hasSeen = true
                                            }
                                        }
                                        for(k in snapshot.child("Users").child(currentUserId).child("disliked").children) {
                                            if(k.key.toString() == y.key.toString())
                                                hasSeen = true
                                        }

                                        if(!hasSeen){
                                            var businessname =
                                                z.child("businessName").value.toString()
                                            var location =
                                                z.child("location").value.toString()
                                            var description =
                                                z.child("description").value.toString()
                                            var image =
                                                z.child("imageUri").value.toString()

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
                    }

                }
                if (usertype == "ideaOwner") {
                    cardStackView.adapter = feedUserAdapter(mutableListOf())
                    adapterUser = cardStackView.adapter as feedUserAdapter
                    for (y in snapshot.child("Users").children) {
                        if (currentUserId != y.key.toString()) {
                            var hasSeen = false

                            for(k in snapshot.child("Users").child(currentUserId).child("liked").children) {

                                if(k.key.toString() == y.key.toString()) {
                                    hasSeen = true
                                }
                            }
                            for(k in snapshot.child("Users").child(currentUserId).child("disliked").children) {
                                if(k.key.toString() == y.key.toString())
                                    hasSeen = true
                            }
                            if(!hasSeen) {
                                var name = y.child("name").value.toString()
                                var surname = y.child("surname").value.toString()
                                var location = y.child("location").value.toString()
                                var dob = y.child("date").value.toString()
                                var email = y.child("email").value.toString()
                                var image = y.child("imageUri").value.toString()
                                var bio = y.child("bio").value.toString()
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
                                        bio
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

