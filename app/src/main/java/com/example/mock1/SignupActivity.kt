package com.example.mock1

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mock1.databinding.SignupLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: SignupLayoutBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SignupLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.icback.setOnClickListener {
            onBackPressed()
        }

        binding.signup.setOnClickListener {
            val user = binding.signupname.text.toString().trim()
            val pass = binding.signuppass.text.toString().trim()
            if (user.isEmpty()) {
                binding.signupname.setError("Can't be null")
            }
            if (pass.isEmpty()) {
                binding.signuppass.setError("Can't be null")
            } else {
                auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Đăng ký thành công
                        val user = auth.currentUser
                        // Điều hướng đến màn hình sau khi đăng ký thành công
                    } else {
                        // Đăng ký thất bại, xử lý lỗi nếu cần
                        val exception = task.exception
                        // Hiển thị lỗi cho người dùng hoặc ghi log
                        Toast.makeText(this@SignupActivity, "Sign Up Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }}