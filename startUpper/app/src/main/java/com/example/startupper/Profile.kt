package com.example.startupper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar

class Profile : AppCompatActivity() {
    private lateinit var appbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        appbar = findViewById(R.id.profiletoolbar)
        setSupportActionBar(appbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
        /*  when (item.itemId) {
              R.id.editProfile ->
                  R.id.logoutProfile->
          }*/
    }
}

