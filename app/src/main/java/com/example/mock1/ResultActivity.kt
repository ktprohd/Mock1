package com.example.mock1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mock1.Model.Score
import com.example.mock1.databinding.ActivityResultBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        readData()
        binding.btnexit.setOnClickListener { finish() }
        binding.btnplayagain.setOnClickListener {
            val intent = Intent(this@ResultActivity,QuizActivity::class.java)
            startActivity(intent)
            finish()
        }

}
    fun readData() {
        val database = Firebase.database
        val myRefscore = database.getReference("Score")
        val questionRef = myRefscore.child("1")
        questionRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(Score::class.java)
                binding.txvcorrect1.text = user?.correct1.toString()
                binding.txvwrong1.text = (3- user?.correct1!!).toString()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}