package com.example.mock1

import android.app.DownloadManager.Query
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mock1.databinding.ActivityMainBinding
import com.example.mock1.databinding.LoginLayoutBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: LoginLayoutBinding
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val RC_SIGN_IN = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick1()
        auth = Firebase.auth

        binding.forgotpass.setOnClickListener {
          val intent = Intent(this@MainActivity,ForgotPassActivity::class.java)
            startActivity(intent)
        }
    }

    fun onClick1() {
        binding.forgotpass.setOnClickListener {
            Toast.makeText(this@MainActivity, "ga vl", Toast.LENGTH_SHORT).show()
        }
        //------------------- clickable string --------------------//
        clickableText()
        binding.txtSignup.movementMethod = LinkMovementMethod.getInstance()
        //---------------------login------------------------------//
        binding.btnLogin1.setOnClickListener { checkUser() }
        binding.btnLogin2.setOnClickListener { signIn() }
    }

    fun checkUser() {
        val username = binding.edtusername.text.toString().trim()
        val password = binding.edtpassword.text.toString().trim()
        if (username.isEmpty()) {
            binding.edtusername.setError("Can't be null")
        }
        if (password.isEmpty()) {
            binding.edtpassword.setError("Can't be null")
        } else {
            val auth = FirebaseAuth.getInstance()
            // Đăng nhập người dùng bằng email và mật khẩu
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Đăng nhập thành công
                        val user: FirebaseUser? = auth.currentUser
                        if (user != null) {
                            // Người dùng đã đăng nhập
                            // Thực hiện các hành động cần thiết sau khi đăng nhập thành công
                            val intent = Intent(this@MainActivity, StartActivity::class.java)
                            startActivity(intent)
                                finishAffinity()
                        }
                    } else {
                        // Đăng nhập thất bại
                        // Xử lý lỗi hoặc hiển thị thông báo lỗi cho người dùng
                        Toast.makeText(this@MainActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    fun clickableText() {
        val spannable: CharSequence = getText(R.string.sign_up)
        val spannableString = SpannableString(spannable)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val intent = Intent(this@MainActivity, SignupActivity::class.java)
                startActivity(intent)
                Toast.makeText(this@MainActivity, "SignUp", Toast.LENGTH_SHORT).show()
            }
        }
        spannableString.setSpan(clickableSpan, (spannable.length - 7), spannable.length, 0)
        binding.txtSignup.text = spannableString
    }


    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Signed in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, StartActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}


