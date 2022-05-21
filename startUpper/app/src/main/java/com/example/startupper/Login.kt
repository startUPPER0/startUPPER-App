package com.example.startupper
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.startupper.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        isSomeoneLogIn()
        binding.loginButton.setOnClickListener {
            var email: String = binding.email.text.toString()
            var password: String = binding.password.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty())
                signInForListener(email, password)

        }
        binding.signUpText.setOnClickListener {
            startActivity(Intent(this@Login, Signup::class.java))

        }
    }

    private fun isSomeoneLogIn() {
        var currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this@Login, FeedActivity::class.java))
            finish()
        }
    }

    private fun signInForListener(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    startActivity(Intent(this@Login, FeedActivity::class.java))
                    finish()
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    // updateUI(null)
                }
            }
    }


}


