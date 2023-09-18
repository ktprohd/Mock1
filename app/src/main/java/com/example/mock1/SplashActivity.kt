package com.example.mock1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mock1.databinding.LoginLayoutBinding
import com.example.mock1.databinding.SplashLayoutBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: SplashLayoutBinding
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            binding = SplashLayoutBinding.inflate(layoutInflater)
            setContentView(binding.root)
            binding.ivNote.alpha=0f
            binding.ivNote.animate().setDuration(5000).alpha(1f).withEndAction{
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    finishAffinity()

            }
    }
}