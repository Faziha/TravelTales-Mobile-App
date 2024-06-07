package com.example.travel_tales_xml.activities.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.travel_tales_xml.R
import com.example.travel_tales_xml.activities.HomeNav
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btn= findViewById<Button>(R.id.btnLogin)

        btn.setOnClickListener{
            val email = findViewById<EditText>(R.id.etEmail)
            val password = findViewById<EditText>(R.id.etPassword)

            val auth = Firebase.auth
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Firebase", "signInWithEmail:success")
                        val user = auth.currentUser
                        Toast.makeText(
                            baseContext,
                            "Authentication successful.",
                            Toast.LENGTH_SHORT,
                        ).show()

                        // Go to home screen
                        startActivity(Intent(this, HomeNav::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Firebase", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        findViewById<TextView>(R.id.tvSignup).setOnClickListener {
            startActivity(Intent(this, SignupMethod::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val auth = Firebase.auth
        if (auth.currentUser != null) {
            startActivity(Intent(this, HomeNav::class.java))
        }
    }
}