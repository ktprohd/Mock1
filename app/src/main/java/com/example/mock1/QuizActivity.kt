package com.example.mock1

import com.example.mock1.Model.User
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.mock1.databinding.ActivityQuizBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.mock1.Model.Score
import com.google.firebase.auth.ktx.auth

class QuizActivity : AppCompatActivity() {


     private lateinit var binding: ActivityQuizBinding
     var countDownTimer: CountDownTimer? = null
     private val handler = Handler(Looper.getMainLooper())
     private lateinit var child1:String
     var i:Int = 1
     var correct:Int=0
     var wrong:Int=0
     var result:String? =""
     val email1 = Firebase.auth.currentUser
     val database = Firebase.database
     val myRef = database.getReference("question")
     val myRefscore = database.getReference("Score")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progessbar.visibility = View.VISIBLE
        binding.layoutquestion.visibility = View.GONE
        readData("$i")
        i++
        timer()
        binding.btnnext.setOnClickListener {
            handler.post {
                resetButton()
                setButtonEnable()
                timer()
                binding.txvcorrect.text= correct.toString()
                binding.txvwrong.text= wrong.toString()

                if (i < 4) {
                    readData("$i")
                    i++
                } else{
                    i=1
                    alertDialog()
                }
            }
        }

        binding.btna.setOnClickListener {
            if (result == "A") {
                binding.btna.setBackgroundResource(R.drawable.btn_style2)
                correct++
            } else {
                binding.btna.setBackgroundResource(R.drawable.btn_style3)
                wrong++
                showTrueAnswer()
            }
            setButtonDisable()
        }

        binding.btnb.setOnClickListener {
            if (result == "B") {
                binding.btnb.setBackgroundResource(R.drawable.btn_style2)
                correct++
            } else {
                binding.btnb.setBackgroundResource(R.drawable.btn_style3)
                wrong++
                showTrueAnswer()
            }
            setButtonDisable()
        }

        binding.btnc.setOnClickListener {
            if (result == "C") {
                binding.btnc.setBackgroundResource(R.drawable.btn_style2)
                correct++
            } else {
                binding.btnc.setBackgroundResource(R.drawable.btn_style3)
                wrong++
                showTrueAnswer()
            }
            setButtonDisable()
        }

        binding.btnd.setOnClickListener {
            if (result == "D") {
                binding.btnd.setBackgroundResource(R.drawable.btn_style2)
                correct++
            } else {
                binding.btnd.setBackgroundResource(R.drawable.btn_style3)
                wrong++
                showTrueAnswer()
            }
            setButtonDisable()
        }

        binding.btnfinish.setOnClickListener {
          i=4
          binding.btnnext.performClick()
        }
    }

    fun addUser(user: User){
        myRef.child("1").setValue(user)
    }

    fun addScore(score: Score){
        myRefscore.child("1").setValue(score)
        // gọi ở AlertDialog để sau gửi lên mỗi lần hoàn thành Quiz
    }

    fun readData(child1:String) {
        val questionRef = myRef.child(child1)
        questionRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user2 = dataSnapshot.getValue(User::class.java)
                binding.edtquestion.text = user2?.cauhoi
                binding.btna.text = user2?.A
                binding.btnb.text = user2?.B
                binding.btnc.text = user2?.C
                binding.btnd.text = user2?.D
                result = user2?.Dapan
                setProgessBar()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun alertDialog(){
        val dialog= AlertDialog.Builder(this@QuizActivity)
        dialog.apply {
            setTitle("QuizGame")
            setMessage(getText(R.string.alertmessage))
            setNegativeButton("Play Again"){ dialogInterface: DialogInterface, a:Int->
                readData("1")
            }
            setPositiveButton("SeeResult"){dialogInterface: DialogInterface, a:Int->
                val email = email1?.email
                val score = Score(email,correct)
                addScore(score)
                correct = 0
                wrong = 0
                val intent=Intent(this@QuizActivity,ResultActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        dialog.show()
    }

    fun resetButton(){
        binding.btna.setBackgroundResource(R.drawable.btn_style)
        binding.btnb.setBackgroundResource(R.drawable.btn_style)
        binding.btnc.setBackgroundResource(R.drawable.btn_style)
        binding.btnd.setBackgroundResource(R.drawable.btn_style)
    }

    fun setProgessBar(){
        if (result != "") {
            binding.progessbar.visibility = View.GONE
            binding.layoutquestion.visibility = View.VISIBLE
        }
    }

    fun showTrueAnswer(){
        when(result){
            "A" -> binding.btna.setBackgroundResource(R.drawable.btn_style2)
            "B" -> binding.btnb.setBackgroundResource(R.drawable.btn_style2)
            "C" -> binding.btnc.setBackgroundResource(R.drawable.btn_style2)
            "D" -> binding.btnd.setBackgroundResource(R.drawable.btn_style2)
        }
    }

    fun setButtonEnable(){
        binding.btna.isEnabled=true
        binding.btnd.isEnabled=true
        binding.btnb.isEnabled=true
        binding.btnc.isEnabled=true
    }

    fun setButtonDisable(){
        binding.btna.isEnabled=false
        binding.btnd.isEnabled=false
        binding.btnb.isEnabled=false
        binding.btnc.isEnabled=false
    }

    fun checkAnswered(){
        if (wrong+correct<i) { wrong++}
    }

    fun timer() {
        countDownTimer?.cancel()
        countDownTimer = object: CountDownTimer(7000,1000) {
            override fun onTick(p0: Long) {
                binding.timer.text = "${p0/1000}"
            }
            override fun onFinish() {
                binding.timer.text = "0"
                binding.edtquestion.text = resources.getString(R.string.timeup)
                checkAnswered()
                handler.postDelayed({
                    // Thực hiện tác vụ bạn muốn sau khi đã trôi qua khoảng thời gian delayMillis
                    binding.btnnext.performClick()
                }, 4000)
            }
        }
        countDownTimer?.start()
    }



}