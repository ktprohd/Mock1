package com.example.mock1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mock1.databinding.ActivityForgotPassBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resetpassbtn.setOnClickListener {
            forgotpassword()
        }

        binding.icback.setOnClickListener { onBackPressed() }
    }

    fun forgotpassword(){
        val emailAddress = binding.edtfotgotpass.text.toString()
        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@ForgotPassActivity,"Sent", Toast.LENGTH_SHORT).show()
                }
            }
    }
}