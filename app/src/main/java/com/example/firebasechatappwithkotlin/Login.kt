package com.example.firebasechatappwithkotlin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasechatappwithkotlin.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser


class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    lateinit var authStateListener: AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


//        authStateListener = AuthStateListener { firebaseAuth ->
//            val firebaseUser = firebaseAuth.currentUser
//            if (firebaseUser != null) {
//                val intent = Intent(this@Login, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
        binding.tvRegisterHere.setOnClickListener {
            startActivity(Intent(this@Login, Register::class.java))
            finish()
//            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    private fun loginUser() {
        val email: String = binding.etLoginEmail.text.toString()
        val password: String = binding.etLoginPassword.text.toString()

        when {
            TextUtils.isEmpty(email) -> {
                binding.etLoginEmail.error = "Email cannot be empty"
                binding.etLoginEmail.requestFocus()
            }
            TextUtils.isEmpty(password) -> {
                binding.etLoginPassword.error = "Password cannot be empty"
                binding.etLoginPassword.requestFocus()
            }
            else -> {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@Login, "User logged in successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@Login, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this@Login, "Log in Error: " + task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

    }
//
    // override fun onStart() {
//        super.onStart()
//        auth.addAuthStateListener(authStateListener)
//    }
}