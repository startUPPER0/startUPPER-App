package com.example.startupper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.startupper.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Profile : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var appbar: Toolbar
    private lateinit var binding: ActivityProfileBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appbar = findViewById(R.id.profiletoolbar)
        setSupportActionBar(appbar)

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

    private fun newBusiness() {

    }

    private fun manageInterests() {
        TODO("Not yet implemented")
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

