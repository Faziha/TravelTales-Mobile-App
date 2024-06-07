package com.example.travel_tales_xml.activities.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.travel_tales_xml.R
import com.example.travel_tales_xml.activities.auth.Login
import com.example.travel_tales_xml.databinding.FragmentDashboardBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var imageView: ImageView
    private var selectedImageUri: Uri? = null
    private val binding get() = _binding!!
    var SELECT_PICTURE = 200

    private lateinit var errorLayout: ConstraintLayout
    private lateinit var linearLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        imageView = root.findViewById(R.id.imageHolder)
        errorLayout = root.findViewById(R.id.error_layout)
        linearLayout = root.findViewById(R.id.authenticated)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = Firebase.auth

        Log.d("DashboardFragment", "User: ${auth.currentUser}")

        if (auth.currentUser == null) {
            Log.d("DashboardFragment", "User not logged in")

            errorLayout.visibility = View.VISIBLE
            linearLayout.visibility = View.GONE
            view.findViewById<Button>(R.id.loginbtn).setOnClickListener {
                val intent = Intent(activity, Login::class.java)
                startActivity(intent)
            }
        } else {
            Log.d("DashboardFragment", "User logged in")

            errorLayout.visibility = View.GONE
            linearLayout.visibility = View.VISIBLE

            view.findViewById<Button>(R.id.uploadImageButton).setOnClickListener {
                imageChooser()
            }

            view.findViewById<Button>(R.id.postButton).setOnClickListener {
                uploadData()
            }
        }
    }

    private fun imageChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE)
    }

    private fun uploadData() {
        val title = binding.title.text.toString()
        val description = binding.description.text.toString()

        if (selectedImageUri != null) {
            val storage = Firebase.storage
            val storageRef = storage.reference
            val imageRef = storageRef.child("images/${selectedImageUri?.lastPathSegment}")
            val uploadTask = imageRef.putFile(selectedImageUri!!)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    val auth = Firebase.auth
                    val email = auth.currentUser!!.email

                    val data = hashMapOf(
                        "title" to title,
                        "description" to description,
                        "imageUrl" to downloadUri.toString(),
                        "author" to email,
                        "likesNo" to 0
                    )

                    val db = Firebase.firestore
                    db.collection("Articles").add(data)
                        .addOnSuccessListener {
                            // Data uploaded successfully
                            // You can add any further actions here
                        }.addOnFailureListener {
                            // Handle failure
                        }
                } else {
                    // Handle unsuccessful upload
                }
            }
        } else {
            // Handle case where no image is selected
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                imageView.setImageURI(uri)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}