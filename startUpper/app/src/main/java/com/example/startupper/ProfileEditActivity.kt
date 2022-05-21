package com.example.startupper
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.startupper.databinding.ActivityProfileeditBinding
import com.example.startupper.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileeditBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    lateinit var list : MutableList<String>
    lateinit var  autoCompeteTxt : AutoCompleteTextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileeditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = mutableListOf()
        autoCompeteTxt = binding.Interest
        database = FirebaseDatabase.getInstance().reference


        auth = Firebase.auth
        database = Firebase.database.reference

        auth.currentUser?.let {
            database.child("Users").child(it.uid).
            get().addOnSuccessListener {
                binding.nameText.setText(it.child("name").value.toString())
                binding.surnameText.setText(it.child("surname").value.toString())
                binding.locationText.setText(it.child("location").value.toString())
                binding.Interest.setText(it.child("interest").value.toString())
                binding.bioText.setText(it.child("bio").value.toString())
                if(it.child("userType").value=="ideaSearcher")
                    binding.radiogroup.check(binding.ideaSearcher.id)
                if(it.child("userType").value=="ideaOwner")
                    binding.radiogroup.check(binding.ideaOwner.id)

            }
        }


        binding.Interest.setOnClickListener{
            var getdata = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    loop@ for (i in snapshot.children) {
                        if (i.key.toString() == "interests") {
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
            var adapterItems = ArrayAdapter<String>(this,R.layout.item_interest,list)
            binding.Interest.setAdapter(adapterItems)
        }

        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@ProfileEditActivity, Profile::class.java))
        }


        binding.saveButton.setOnClickListener {

            var name = binding.nameText.text.toString().trim()
            var surname = binding.surnameText.text.toString().trim()
            var location = binding.locationText.text.toString().trim()
            var interest = binding.Interest.text.toString()
            var bio = binding.bioText.text.toString()
            var radiogroup = binding.radiogroup
            var userType = ""
            if (radiogroup.checkedRadioButtonId == R.id.ideaOwner) {
                userType = "ideaOwner"
                Log.e("userType", userType)

            }
            if (radiogroup.checkedRadioButtonId == R.id.ideaSearcher) {
                userType = "ideaSearcher"
                Log.e("userType", userType)
            }

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
            if(!(radiogroup.checkedRadioButtonId == R.id.ideaOwner || radiogroup.checkedRadioButtonId == R.id.ideaSearcher)){
                binding.radiogroup.requestFocus()
                Toast .makeText(this,"Choose user type",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.currentUser?.let { it1 ->
                database.child("Users").child(it1.uid).child("name").setValue(name)
            }
            auth.currentUser?.let { it1 ->
                database.child("Users").child(it1.uid).child("surname").setValue(surname)
            }
            auth.currentUser?.let { it1 ->
                database.child("Users").child(it1.uid).child("location").setValue(location)
            }
            auth.currentUser?.let { it1 ->
                database.child("Users").child(it1.uid).child("interest").setValue(interest)
            }
            auth.currentUser?.let { it1 ->
                database.child("Users").child(it1.uid).child("bio").setValue(bio)
            }
            auth.currentUser?.let { it1 ->
                database.child("Users").child(it1.uid).child("userType").setValue(userType)
            }
            startActivity(Intent(this@ProfileEditActivity, Profile::class.java))



        }

    }
}