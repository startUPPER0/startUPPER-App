package com.example.startupper

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.startupper.adapter.feedAdapter
import com.example.startupper.databinding.ActivityProfileBinding
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
import com.squareup.picasso.Picasso


class Profile : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var appbar: Toolbar
    private lateinit var currentUserId: String
    private lateinit var binding: ActivityProfileBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appbar = findViewById(R.id.profiletoolbar)
        setSupportActionBar(appbar)
        database = Firebase.database.reference
        auth = Firebase.auth


        storage = Firebase.storage
        var currentUser = auth.currentUser
        if (currentUser != null) {
            currentUserId = currentUser.uid

        }
        var getdata = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (i in snapshot.children) {

                    if (i.key.toString() == "Users") {
                        Log.e("basari", "basari")
                        for (z in i.children) {
                            if (currentUserId == z.key.toString()) {
                                binding.nameProfile.text = z.child("name").getValue().toString()
                                binding.surnameProfile.text =
                                    z.child("surname").getValue().toString()
                                binding.email.text =
                                    z.child("email").getValue().toString()
                                var uri = z.child("imageUri").getValue()
                                Picasso.get().load(uri.toString()).into(binding.imageViewProfile)
                            }
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        database.addValueEventListener(getdata)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setSelectedItemId(R.id.profileBottomMenu)

        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.feedBottomMenu -> {
                    startActivity(Intent(this@Profile, FeedActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.inboxBottomMenu -> {
                    startActivity(Intent(this@Profile, Inbox::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.notificationBottomMenu -> {
                    startActivity(Intent(this@Profile, Notification::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })

        auth = Firebase.auth
        binding.startNewBussinessBT.setOnClickListener {
            startActivity(Intent(this@Profile, NewBussinessActivity::class.java))
            finish()
        }
        binding.manageInterestBT.setOnClickListener {
            manageInterests()
        }
    }


    private fun manageInterests() {

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.editProfile -> {
                startActivity(Intent(this@Profile, ProfileEditActivity::class.java))
            }
            R.id.logoutProfile -> {
                auth.signOut()
                startActivity(Intent(this@Profile, Login::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

