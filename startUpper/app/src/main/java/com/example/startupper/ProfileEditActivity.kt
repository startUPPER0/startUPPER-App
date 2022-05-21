package com.example.startupper
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.startupper.databinding.ActivityProfileeditBinding
import com.example.startupper.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileeditBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        binding = ActivityProfileeditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference

        auth = Firebase.auth
        database = Firebase.database.reference

        auth.currentUser?.let {
            database.child("Users").child(it.uid).
            get().addOnSuccessListener {
                binding.nameText.setText(it.child("name").value.toString())
                binding.surnameText.setText(it.child("surname").value.toString())
                binding.emailText.setText(it.child("email").value.toString())
                binding.Interest.setText(it.child("interest").value.toString())
                if(it.child("userType").value=="ideaSearcher")
                    binding.radiogroup.check(binding.ideaSearcher.id)
                if(it.child("userType").value=="ideaOwner")
                    binding.radiogroup.check(binding.ideaOwner.id)

            }
        }





        binding.saveButton.setOnClickListener {

            var name = binding.nameText.text.toString().trim()
            var surname = binding.surnameText.text.toString().trim()
            var email = binding.emailText.text.toString().trim()
            var interest = binding.Interest.text.toString()
            var bio = binding.bioText.text.toString()

        }

    }
}