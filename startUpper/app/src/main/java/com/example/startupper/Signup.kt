package com.example.startupper

import android.Manifest
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.startupper.databinding.ActivitySignupBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*


class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var userType: String = ""
    private lateinit var storage: FirebaseStorage
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var profilePicture: Uri? = null
    private lateinit var currentUser: FirebaseUser
    private lateinit var currentUserId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var database = FirebaseDatabase.getInstance().reference

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        auth = Firebase.auth
        registerLaunchers()
        storage = Firebase.storage
        val reference = storage.reference
        database = Firebase.database.reference
        binding.link.setOnClickListener {
            startActivity(Intent(this@Signup, Login::class.java))
        }
        binding.profileImage.setOnClickListener {
            chooseImage()
        }
        binding.signUpButton.setOnClickListener {

            var name = binding.nameText.text.toString().trim()
            var surname = binding.surnameText.text.toString().trim()
            var email = binding.emailText.text.toString().trim()
            var date = binding.dateText
            val datee = DatePickerDialog(this,DatePickerDialog.OnDateSetListener { picker, mYear:Int, mMonth:Int, mDay:Int ->
                date.setText(""+mDay+"/"+mMonth+"/"+mYear)
            },year,month,day)

            var location = binding.locationText.text.toString().trim()
            var password = binding.passwordText.text.toString().trim()

            var radiogroup = binding.radiogroup
            var image = binding.profileImage



            if(name==""){
                binding.nameText.setError("Name is required")
                binding.nameText.requestFocus()
                return@setOnClickListener
            }
            if (surname.isEmpty()) {
                binding.surnameText.setError("Surname is required")
                binding.surnameText.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.emailText.setError("Email is required")
                binding.emailText.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailText.setError("Enter a valid email")
                binding.emailText.requestFocus()
                return@setOnClickListener
            }

            /*if(date.isEmpty()){
                binding.dateText.setError("Date of birth is required")
                binding.dateText.requestFocus()
                return@setOnClickListener
            }*/
            if(location.isEmpty()){
                binding.locationText.setError("Location is required")
                binding.locationText.requestFocus()
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.passwordText.setError("Enter at least 6 length password")
                binding.passwordText.requestFocus()
                return@setOnClickListener
            }
            if (radiogroup.checkedRadioButtonId == R.id.ideaOwner) {
                userType = "ideaOwner"
                Log.e("userType", userType)

            }
            if (radiogroup.checkedRadioButtonId == R.id.ideaSearcher) {
                userType = "ideaSearcher"
                Log.e("userType", userType)


            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        Toast.makeText(baseContext, "Welcome!", Toast.LENGTH_SHORT).show()

                        auth.currentUser?.let { it1 ->
                            database.child("Users").child(it1.uid).setValue(
                                UserRegisterClass(
                                    name,
                                    surname,
                                    email,
                                    datee.toString().trim(),
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
                                reference.child("userPicture").child(it1).putFile(profilePicture!!)
                                    .addOnSuccessListener {
                                        val imagereference =
                                            reference.child("userPicture").child(auth.currentUser?.uid!!)
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
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Email already used!", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

        }


    }

    private fun registerLaunchers() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        profilePicture = intentFromResult.data
                        profilePicture?.let { uri ->
                            binding.profileImage.setImageURI(uri)
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
                        this@Signup,
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
                    binding.profileImage,
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