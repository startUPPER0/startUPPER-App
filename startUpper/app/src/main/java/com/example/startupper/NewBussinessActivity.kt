package com.example.startupper

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.startupper.databinding.ActivityNewbusinessBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class NewBussinessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewbusinessBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserId: String
    private lateinit var storage: FirebaseStorage
    var businessPicture: Uri? = null
    private lateinit var downloadUri:String
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewbusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        downloadUri=""
        registerLaunchers()
        auth = Firebase.auth
        storage = Firebase.storage

        currentUser = auth.currentUser!!

        if (currentUser != null) {
            currentUserId = currentUser.uid
        }

        binding.createBusinessImg.setOnClickListener {
            chooseImage()
        }

        binding.saveNewBusiness.setOnClickListener {
            createBusiness()
        }

    }

    private fun createBusiness() {
        if(businessPicture == null) {
            binding.createBusinessImg.requestFocus()
            Toast .makeText(this,"Choose a picture",Toast.LENGTH_SHORT).show()
            return
        }
        var businessName = binding.businessName.text.toString()
        var location = binding.location.text.toString()
        var desc = binding.description.text.toString()
        var nb = NewBusinessClass(businessName, location, desc, "")

        val reference = storage.reference
        database = Firebase.database.reference

        val imageReference =
            reference.child("feedPicture").child(currentUserId).child(nb.businessName)
                .putFile(businessPicture!!).addOnSuccessListener {
                    val imagereference =
                        reference.child("feedPicture").child(currentUserId).child(nb.businessName)
                    imagereference.downloadUrl.addOnSuccessListener {
                         downloadUri = it.toString()
                         nb.imageuri = downloadUri as String
                        Log.e("URİ", downloadUri)
                        nb.imageuri=downloadUri
                        database.child("feeds").child(currentUserId).child(nb.businessName).child("imageUri").setValue(downloadUri)


                    }
                }

        database.child("feeds").child(currentUserId).child(nb.businessName).child("businessName").setValue(nb.businessName)
        database.child("feeds").child(currentUserId).child(nb.businessName).child("description").setValue(nb.description)
        database.child("feeds").child(currentUserId).child(nb.businessName).child("location").setValue(nb.location)
        //database.child("feeds").child(currentUserId).child(nb.businessName).child("imageUri").setValue(downloadUri)
            .addOnCompleteListener(this@NewBussinessActivity) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext, "New Business Idea is created",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@NewBussinessActivity, FeedActivity::class.java))
                } else {

                    Toast.makeText(
                        baseContext, "Can not created new business idea",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    private fun registerLaunchers() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        businessPicture = intentFromResult.data
                        businessPicture?.let { uri ->
                            binding.createBusinessImg.setImageURI(uri)
                        }
                    }
                }

            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    val intentGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentGallery)
                } else {
                    Toast.makeText(
                        this@NewBussinessActivity,
                        "Permission Needed!",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }
    }

    private fun chooseImage() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Snackbar.make(
                    binding.createBusinessImg,
                    "Permission needed for gallery!",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Give Permission") {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                    }.show()
            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

            }
        } else {
            val intentGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentGallery)


        }
    }
}