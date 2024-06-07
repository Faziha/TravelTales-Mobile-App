package com.example.travel_tales_xml.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travel_tales_xml.R
import com.example.travel_tales_xml.adapters.SearchResultArticleAdapter
import com.example.travel_tales_xml.models.Article
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class SearchResult : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        val searchQuery = intent.getStringExtra("searchQuery")

        val db = Firebase.firestore

        val articles = ArrayList<Article>()

        val searchList: RecyclerView = findViewById(R.id.search_result_rv)
        searchList.layoutManager = LinearLayoutManager(this)


        db.collection("Articles").whereEqualTo("title",searchQuery).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val article = Article(document.id, document.data["title"].toString(), document.data["description"].toString(), document.data["imageUrl"].toString(), document.data["authorId"].toString(), document.data["likesNo"].toString().toInt())
                    articles.add(article)
                }

                val adapter = SearchResultArticleAdapter(articles,this)
                searchList.adapter = adapter

            }
            .addOnFailureListener { exception ->
                Log.d("Firebase", "Error getting documents: ", exception)
            }
    }
}