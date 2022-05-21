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
                binding.radiogroup.setText(it.child("userType").value.toString())
            }
        }





        binding.saveButton.setOnClickListener {

            var name = binding.nameText.text.toString().trim()
            var surname = binding.surnameText.text.toString().trim()
            var email = binding.emailText.text.toString().trim()
            var interest = binding.Interest.text.toString()
            var bio = binding.bioText.text.toString()




            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        Toast.makeText(baseContext, "Welcome!", Toast.LENGTH_SHORT).show()

                        auth.currentUser?.let { it1 ->
                            database.child("Users").child(it1.uid).setValue(
                                UserRegisterClass(
                                    name,
                                    surname,
                                    email,
                                    date,
                                    location,
                                    password,
                                    userType,
                                    ""
                                )
                            )
                        }
                        auth.currentUser?.uid

                        val imageReference =
                            auth.currentUser?.uid?.let { it1 ->
                                storageReference.child("userPicture").child(it1).putFile(profilePicture!!)
                                    .addOnSuccessListener {
                                        val imagereference =
                                            storageReference.child("userPicture").child(auth.currentUser?.uid!!)
                                        imagereference.downloadUrl.addOnSuccessListener {
                                            val downloadUri = it.toString()
                                            Log.e("URİ", downloadUri)
                                            database.child("Users").child(auth.currentUser?.uid!!)
                                                .child("imageUri")
                                                .setValue(downloadUri)


                                        }
                                    }
                            }


                        startActivity(Intent(this@Signup, Login::class.java))
                    } else {
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Email already used!", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

        }

    }
}