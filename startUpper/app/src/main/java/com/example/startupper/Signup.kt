package com.example.startupper

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.startupper.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Signup : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        database = Firebase.database.reference

        binding.signUpButton.setOnClickListener {
            var name = binding.nameText.text.toString().trim()
            var surname = binding.surnameText.text.toString().trim()
            var email = binding.emailText.text.toString().trim()
            var date = binding.dateText.text.toString().trim()
            var location = binding.locationText.text.toString().trim()
            var password = binding.passwordText.text.toString().trim()
            Log.v("test", "$name $surname $email")

            if(name==""){
                binding.nameText.setError("Name is required")
                binding.nameText.requestFocus()
                return@setOnClickListener
            }
            if(surname.isEmpty()){
                binding.surnameText.setError("Surname is required")
                binding.surnameText.requestFocus()
                return@setOnClickListener
            }
            if(email.isEmpty()){
                binding.emailText.setError("Email is required")
                binding.emailText.requestFocus()
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.emailText.setError("Enter a valid email")
                binding.emailText.requestFocus()
                return@setOnClickListener
            }
            if(date.isEmpty()){
                binding.dateText.setError("Date of birth is required")
                binding.dateText.requestFocus()
                return@setOnClickListener
            }
            if(location.isEmpty()){
                binding.locationText.setError("Location is required")
                binding.locationText.requestFocus()
                return@setOnClickListener
            }
            if(password.length < 6){
                binding.passwordText.setError("Enter at least 6 length password")
                binding.passwordText.requestFocus()
                return@setOnClickListener
            }
        }


    }
}