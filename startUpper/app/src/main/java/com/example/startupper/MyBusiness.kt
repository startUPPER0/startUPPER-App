package com.example.startupper

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupper.adapter.myBusinessAdaptor
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

class MyBusiness : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserId: String
    private lateinit var storage: FirebaseStorage
    private lateinit var adapter: myBusinessAdaptor
    private lateinit var recyclerview: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mybusiness)
        recyclerview = findViewById(R.id.mybusinessRV)
        database = Firebase.database.reference
        auth = Firebase.auth
        storage = Firebase.storage

        var currentUser = auth.currentUser
        if (currentUser != null) {
            currentUserId = currentUser.uid

        }
        val linearLayoutManager = LinearLayoutManager(
            this@MyBusiness,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerview.layoutManager = linearLayoutManager


        var getdata = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                adapter = myBusinessAdaptor(mutableListOf())
                recyclerview.adapter = adapter
                for (i in snapshot.children) {
                    Log.e("dsdf", "İ")
                    if (i.key.toString() == "feeds") {
                        for (y in i.children) {
                            if (y.key.toString() == currentUserId) {
                                for (z in y.children) {
                                    var businessname =
                                        z.child("businessName").value.toString()
                                    var location =
                                        z.child("location").value.toString()
                                    var description =
                                        z.child("description").value.toString()
                                    var image =
                                        z.child("imageUri").value.toString()
                                    adapter.addBusiness(
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

            override fun onCancelled(error: DatabaseError) {
            }
        }
        database.addValueEventListener(getdata)

    }


}