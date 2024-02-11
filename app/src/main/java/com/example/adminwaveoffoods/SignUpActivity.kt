package com.example.adminwaveoffoods

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adminwaveoffoods.databinding.ActivitySignUpBinding
import com.example.adminwaveoffoods.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var nameOfRestaurent: String
    private lateinit var database: DatabaseReference

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // initialize Firebase Auth
        auth = Firebase.auth
        // Initialize Database
        database = Firebase.database.reference

        binding.createBtn.setOnClickListener {
            //get text from edit text
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()
            userName = binding.name.text.toString().trim()
            nameOfRestaurent = binding.restaurentName.text.toString().trim()

            if (userName.isBlank() || nameOfRestaurent.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }
        }
        binding.alreadyAccountBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        val locationList =
            arrayOf("Jaipur", "Odisha", "Bundi", "Sikar", "Bihar", "Jodhpur", "Delhi", "New Delhi")
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    saveUserData()
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error: ${task.exception}", Toast.LENGTH_SHORT).show()
                    Log.d("Account", "createAccount: ${task.exception}")
                }
            }
    }

    //save data in to database
    private fun saveUserData() {
        val user = UserModel(userName, nameOfRestaurent, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        // save user data to Realtime Database
        database.child("user").child(userId).setValue(user)
    }
}