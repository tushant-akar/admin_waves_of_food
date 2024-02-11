package com.example.adminwaveoffoods

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.adminwaveoffoods.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        //Intialize auth and database
        auth = Firebase.auth
        database = Firebase.database.reference
        //Intialize Google Sign In
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        binding.login.setOnClickListener {
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                signIn(email, password)
            }
        }
        binding.googleBtn.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }
        binding.donthaveaccountBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                    updateUI(user)
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Create User: ${task.exception}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Authentication failed: ${task.exception}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d("Account", "Authentication failed", task.exception)
                            }
                        }
                }
            }
    }

    //Check if user is already logged in
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    //launcher for Google Sign In
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount = task.result
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Successfully sign in with Google",
                                    Toast.LENGTH_SHORT
                                ).show()
                                updateUI(authTask.result?.user)
                                finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Google: Sign In failed: ${authTask.exception}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d("Account", "Authentication failed", task.exception)
                            }
                        }
                } else {
                    Toast.makeText(
                        this,
                        "Google: Sign In failed: ${task.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("Account", "Authentication failed", task.exception)
                }
            }
        }
}