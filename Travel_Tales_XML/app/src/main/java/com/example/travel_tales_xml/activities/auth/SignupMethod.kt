package com.example.travel_tales_xml.activities.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.travel_tales_xml.R
import com.example.travel_tales_xml.activities.HomeNav

class SignupMethod : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_method)

        findViewById<LinearLayout>(R.id.llGoogleSignup).setOnClickListener{
            // Implement Google Signup
        }

        findViewById<LinearLayout>(R.id.llEmailSignup).setOnClickListener{
            // Implement Email Signup

            startActivity(Intent(this, Signup::class.java))
        }

        findViewById<LinearLayout>(R.id.llGuestSignup).setOnClickListener{
            // Continue as Guest

            // Go to home screen
            startActivity(Intent(this, HomeNav::class.java))
        }
    }
}