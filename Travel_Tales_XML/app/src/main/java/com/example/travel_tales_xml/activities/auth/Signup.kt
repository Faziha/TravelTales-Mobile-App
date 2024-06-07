package com.example.travel_tales_xml.activities.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travel_tales_xml.R
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore


class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        var name: EditText
        var email: EditText
        var password: EditText
        var phone: EditText

        name=findViewById(R.id.etName)
        email=findViewById(R.id.etEmail)
        password=findViewById(R.id.etPassword)
        phone=findViewById(R.id.etPhone)

        var signupButton: Button
        signupButton=findViewById(R.id.btnSignup)
        signupButton.setOnClickListener{
            var auth = Firebase.auth
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Firebase", "createUserWithEmail:success")
                        val user = auth.currentUser
                        Toast.makeText(
                            baseContext,
                            "Sign Up successful.",
                            Toast.LENGTH_SHORT,
                        ).show()

                        val db = Firebase.firestore
                        val new_user = hashMapOf(
                            "name" to name.text.toString(),
                            "email" to email.text.toString(),
                            "phone" to phone.text.toString(),

                        )

                        db.collection("Users").add(new_user)

                        startActivity(Intent(this,Login::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Firebase", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }
        }
    }
}