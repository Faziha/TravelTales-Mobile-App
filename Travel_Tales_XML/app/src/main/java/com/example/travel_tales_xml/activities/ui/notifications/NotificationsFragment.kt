package com.example.travel_tales_xml.activities.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travel_tales_xml.R
import com.example.travel_tales_xml.activities.auth.Login
import com.example.travel_tales_xml.adapters.HomeScreenAdapter
import com.example.travel_tales_xml.databinding.FragmentNotificationsBinding
import com.example.travel_tales_xml.models.Article
import com.example.travel_tales_xml.offline.MySqlHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    private lateinit var errorLayout: ConstraintLayout
    private lateinit var linearLayout: LinearLayout

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        errorLayout = root.findViewById(R.id.error_layout)
        linearLayout = root.findViewById(R.id.authenticated)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val auth = Firebase.auth

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

            view.findViewById<Button>(R.id.Logout).setOnClickListener{
                auth.signOut()
                val intent = Intent(activity, Login::class.java)
                startActivity(intent)
            }

            // name of the user and email of user
            val name = view.findViewById<TextView>(R.id.title)
            val email = view.findViewById<TextView>(R.id.description)

            val db = Firebase.firestore
            db.collection("Users").whereEqualTo("email", auth.currentUser!!.email).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        name.text = document.data["name"].toString()
                        email.text = document.data["email"].toString()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("DashboardFragment", "Error getting documents: ", exception)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}