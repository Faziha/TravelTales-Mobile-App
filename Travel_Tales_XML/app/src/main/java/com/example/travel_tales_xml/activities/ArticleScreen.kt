package com.example.travel_tales_xml.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.travel_tales_xml.R
import com.example.travel_tales_xml.adapters.CommentsAdapter
import com.example.travel_tales_xml.models.Comment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.concurrent.Executors

class ArticleScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_screen)

        // Get all the data from the intent
        val articleId = intent.getStringExtra("articleid")
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val imageUrl = intent.getStringExtra("imageUrl")
        val author = intent.getStringExtra("author")
        val likesNo = intent.getIntExtra("likesNo", 0)

        // set the data to the views
        findViewById<TextView>(R.id.title).text = title
        findViewById<TextView>(R.id.description).text = description

        val storage = Firebase.storage
        val storageReference = storage.reference

        // ImageView to display the image
        val imageView = findViewById<ImageView>(R.id.imageHolder)
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var image: Bitmap? = null

        executor.execute {

            // Image URL
            val imageURL = imageUrl

            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {
                    imageView.setImageBitmap(image)
                }
            }

            // If the URL doesnot point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }


        findViewById<Button>(R.id.postCommentButton).setOnClickListener {
            // Post the comment to firestore
            val auth = Firebase.auth
            val email = auth.currentUser!!.email

            val db = Firebase.firestore
            val comment = hashMapOf(
                "articleId" to articleId,
                "authorId" to email,
                "comment" to findViewById<EditText>(R.id.commentEditText).text.toString(),
                "TimeStamp" to System.currentTimeMillis().toString()
            )

            db.collection("Comments")
                .add(comment)
                .addOnSuccessListener { documentReference ->
                    Log.d("Firebase", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("Firebase", "Error adding document", e)
                }
        }

        // Get the comments from firestore
        val db = Firebase.firestore

        // Get the comments from firestore
        val comments = ArrayList<Comment>()

        db.collection("Comments").whereEqualTo("articleId", articleId).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val comment = Comment(document.id, document.data["articleId"].toString(), document.data["authorId"].toString(), document.data["comment"].toString(), document.data["TimeStamp"].toString())
                    comments.add(comment)
                }

                // Set the comments to the recycler view
                val adapter = CommentsAdapter(comments,this)
                val commentsRV = findViewById<RecyclerView>(R.id.commentsRecyclerView)
                commentsRV.layoutManager = LinearLayoutManager(this)
                commentsRV.adapter = adapter

            }
            .addOnFailureListener { exception ->
                Log.d("Firebase", "Error getting documents: ", exception)
            }

    }
}