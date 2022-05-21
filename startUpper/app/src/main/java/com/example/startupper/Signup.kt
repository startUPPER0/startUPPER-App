package com.example.startupper

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
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
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.util.*


class Signup : AppCompatActivity() {
    private lateinit var dialog :DatePickerDialog
    private lateinit var dateButton :Button
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var userType: String = ""
    private lateinit var storage: FirebaseStorage
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var profilePicture: Uri? = null

    lateinit var list : MutableList<String>
    lateinit var  autoCompeteTxt : AutoCompleteTextView




    private fun getTodaysDate():String{
        val cal :Calendar = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        var month = cal.get(Calendar.MONTH)
        month+=1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        return makeDateString(day, month ,year)

    }


    private fun initDatePicker() {
        var  dateSetListener= DatePickerDialog.OnDateSetListener { datePicker, year:Int, month:Int, day:Int ->
            var monthh = month+1
            var date : String = makeDateString(day,monthh,year)
            dateButton.setText(date)
        }

        var year:Int = Calendar.YEAR
        var month:Int = Calendar.MONTH
        var day :Int = Calendar.DAY_OF_MONTH

        var style =AlertDialog.THEME_HOLO_LIGHT
        dialog = DatePickerDialog(this,style,dateSetListener,year,month,day)

    }

    private fun openDatePicker(){
        dialog.show()
    }


    private  fun makeDateString(day:Int,month:Int,year:Int): String {
        return getMonthFormat(month) + "/" + day + "/" + year
    }

    private fun getMonthFormat(month:Int):String{
        if(month==1)
            return "JAN"
        if(month==2)
            return "FEB"
        if(month==3)
            return "MAR"
        if(month==4)
            return "APR"
        if(month==5)
            return "MAY"
        if(month==6)
            return "JUN"
        if(month==7)
            return "JUL"
        if(month==8)
            return "AUG"
        if(month==9)
            return "SEP"
        if(month==10)
            return "OCT"
        if(month==11)
            return "NOV"
        if(month==12)
            return "DEC"
        return "JAN"

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDatePicker()
        dateButton = findViewById(R.id.dateButton)
        dateButton.setText(getTodaysDate())
        list = mutableListOf()
        autoCompeteTxt = binding.Interest
        database = FirebaseDatabase.getInstance().reference


        var getdata = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                loop@ for (i in snapshot.children) {
                    if (i.key.toString().equals("interests")) {
                        for(j in i.children){
                            list.add(j.key.toString())
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        database.addValueEventListener(getdata)

        /*var interest : AutoCompleteTextView = findViewById(R.id.Interest)

        interest.setAdapter(adapterItems)*/

        var adapterItems = ArrayAdapter<String>(this,R.layout.item_interest,list)
        binding.Interest.setAdapter(adapterItems)






        var database = FirebaseDatabase.getInstance().reference




        database = FirebaseDatabase.getInstance().reference

        auth = Firebase.auth
        registerLaunchers()
        storage = Firebase.storage
        val storageReference = storage.reference
        database = Firebase.database.reference


        binding.dateButton.setOnClickListener {
            openDatePicker()
            initDatePicker()
        }

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
            var date = binding.dateButton.text.toString().trim()
            var location = binding.locationText.text.toString().trim()
            var password = binding.passwordText.text.toString().trim()
            var interest = binding.Interest.text.toString().trim()
            var radiogroup = binding.radiogroup
            var image = binding.profileImage



            if(name.isEmpty()){
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
            if(interest.isEmpty()) {
                binding.Interest.setError("Choose an interest")
                binding.Interest.requestFocus()
                return@setOnClickListener
            }
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
            if(radiogroup.checkedRadioButtonId == null){
                binding.radiogroup.requestFocus()
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
                                    date,
                                    location,
                                    password,
                                    userType,
                                    interest,
                                    "",
                                    ""
                                )
                            )
                        }

                        auth.currentUser?.let { it1 -> database.child("Users").
                        child(it1.uid).child("disliked") }


                        auth.currentUser?.let { it1 -> database.child("Users").child(it1.uid).child("liked") }



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