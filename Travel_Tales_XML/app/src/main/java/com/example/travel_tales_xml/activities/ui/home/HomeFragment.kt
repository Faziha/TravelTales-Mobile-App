package com.example.travel_tales_xml.activities.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travel_tales_xml.R
import com.example.travel_tales_xml.activities.SearchResult
import com.example.travel_tales_xml.adapters.HomeScreenAdapter
import com.example.travel_tales_xml.databinding.FragmentHomeBinding
import com.example.travel_tales_xml.models.Article
import com.example.travel_tales_xml.offline.MySqlHelper
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.searchIcon).setOnClickListener{
            val query = view.findViewById<EditText>(R.id.searchText)
            val searchQuery = query.text.toString()

            val intent = Intent(requireContext(), SearchResult::class.java)
            intent.putExtra("searchQuery", searchQuery)

            startActivity(intent)
        }

        val articles = ArrayList<Article>()

        val db = Firebase.firestore

        db.collection("Articles").get()
            .addOnSuccessListener { result ->
                var helper= context?.let { MySqlHelper(it) }
                for (document in result) {
                    val article = Article(document.id, document.data["title"].toString(), document.data["description"].toString(), document.data["imageUrl"].toString(), document.data["author"].toString(), document.data["likesNo"].toString().toInt())
                    articles.add(article)
                   // title: String, description: String, author: String, likesNo: Int, imageUrl: String, id: String
                    try {
                        helper!!.insert(article.title, article.description, article.authorId, article.likesNo, article.imageUrl, article.id)
                    } catch (e: Exception) {
                        Log.d("Firebase", "Error inserting documents: ", e)
                    }

                }
                val homeRecyclerView = view.findViewById<RecyclerView>(R.id.home_recycler_view)
                val adapter = HomeScreenAdapter(articles, requireContext())
                homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                homeRecyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.d("Firebase", "Error getting documents: ", exception)
                // get data from sqlite
                var helper= context?.let { MySqlHelper(it) }
                val articles = helper!!.readArticles()
                val homeRecyclerView = view.findViewById<RecyclerView>(R.id.home_recycler_view)
                val adapter = HomeScreenAdapter(articles, requireContext())
                homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                homeRecyclerView.adapter = adapter
            }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}