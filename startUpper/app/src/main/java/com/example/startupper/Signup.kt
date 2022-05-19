package com.example.startupper

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.startupper.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
        var database  = FirebaseDatabase.getInstance().reference
        auth = Firebase.auth

        database = Firebase.database.reference
        binding.link.setOnClickListener{
            startActivity(Intent(this@Signup, Login::class.java))
        }

        binding.signUpButton.setOnClickListener {

            var name = binding.nameText.text.toString().trim()
            var surname = binding.surnameText.text.toString().trim()
            var email = binding.emailText.text.toString().trim()
            var date = binding.dateText.text.toString().trim()
            var location = binding.locationText.text.toString().trim()
            var password = binding.passwordText.text.toString().trim()


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



            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        Toast.makeText(baseContext, "Welcome!", Toast.LENGTH_SHORT).show()
                        auth.currentUser?.let { it1 -> database.child("Users").child(it1.uid).
                            setValue(UserRegisterClass(name,surname,email,date,location,password)) }
                        startActivity(Intent(this@Signup, Login::class.java))
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Email already used!", Toast.LENGTH_SHORT).show()

                    }
                }

        }


    }
}