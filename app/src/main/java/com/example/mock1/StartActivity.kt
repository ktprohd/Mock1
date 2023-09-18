package com.example.mock1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.mock1.databinding.SignupLayoutBinding
import com.example.mock1.databinding.StartLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class StartActivity : AppCompatActivity() {
    private lateinit var binding: StartLayoutBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= StartLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val email = it.email
            // Check if user's email is verified
            val emailVerified = it.isEmailVerified
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = it.uid

            binding.txtwelcom.text= "Welcome $email"

            binding.startquiz.setOnClickListener {
                val intent=Intent(this,QuizActivity::class.java)
                startActivity(intent)
                    finishAffinity()
            }

            binding.btnsignout.setOnClickListener {
                Firebase.auth.signOut()
                val intent = Intent(this@StartActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

}