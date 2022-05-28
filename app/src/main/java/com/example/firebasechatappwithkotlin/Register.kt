package com.example.firebasechatappwithkotlin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasechatappwithkotlin.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.*


class Register : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    lateinit var databaseReference: DatabaseReference
    lateinit var dataBase: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)



        auth = FirebaseAuth.getInstance()

        dataBase = FirebaseDatabase.getInstance()

        databaseReference = dataBase.reference.child("Users")

        binding.btnRegister.setOnClickListener {
            createUser()
        }
        binding.tvLoginHere.setOnClickListener {
            startActivity(Intent(this@Register, Login::class.java))
            finish()
        }

    }

    private fun createUser() {

        val userName: String = binding.etRegName.text.toString()
        val email: String = binding.etRegEmail.text.toString()
        val password: String = binding.etRegPassword.text.toString()

        when {
            TextUtils.isEmpty(userName) -> {
                binding.etRegName.error = "Name cannot be empty"
                binding.etRegName.requestFocus()
            }
            TextUtils.isEmpty(email) -> {
                binding.etRegEmail.error = "Email cannot be empty"
                binding.etRegEmail.requestFocus()
            }
            TextUtils.isEmpty(password) -> {
                binding.etRegPassword.error = "Password cannot be empty"
                binding.etRegPassword.requestFocus()
            }
            else -> {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val userId = auth.currentUser!!.uid
                            val currentUserDb = databaseReference.child(userId)
                            currentUserDb.child("UserName").setValue(userName)
                            currentUserDb.child("uid").setValue(userId)

//                            val userId = auth.currentUser!!.uid
//                            dataBase = FirebaseDatabase.getInstance()
//
//                            databaseReference = dataBase.reference.child("Users").child(userId)


                            Toast.makeText(
                                this@Register, "User registered successfully", Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@Register, Login::class.java))
                        } else {
                            Toast.makeText(
                                this@Register,
                                "Registration Error: " + task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}