package com.example.startupper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class Login : AppCompatActivity() {
    final lateinit var email: String
    final lateinit var passw: String
    lateinit var edittextMail: EditText
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var edittextPassw: EditText
    lateinit var loginButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email = "startupper@gmail.com"
        passw = "erdinc0808"
        edittextMail = findViewById(R.id.email)
        edittextPassw = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            logInMethod()
        }


    }

    fun logInMethod() {

        var emailEntered = edittextMail.text.toString()
        var passwEntered = edittextPassw.text.toString()
        if (emailEntered.equals(email)) {
            if (passwEntered.equals(passw)) {
                Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()

                val intentFeed = Intent(this, FeedActivity::class.java)

                startActivity(intentFeed)
            } else if (passwEntered.equals("")) {
                Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show()
            }
        } else if (emailEntered.equals("")) {
            Toast.makeText(this, "Please Enter Your e-mail", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "User Not Found", Toast.LENGTH_SHORT).show()

        }


    }
}