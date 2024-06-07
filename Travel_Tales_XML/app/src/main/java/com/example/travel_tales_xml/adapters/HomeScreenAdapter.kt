package com.example.travel_tales_xml.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travel_tales_xml.R
import com.example.travel_tales_xml.activities.ArticleScreen
import com.example.travel_tales_xml.models.Article
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.util.concurrent.Executors


class HomeScreenAdapter: RecyclerView.Adapter<HomeScreenAdapter.HomeScreenViewHolder> {

    var articles: List<Article> = emptyList()
    var context: Context? = null

    constructor(articles: List<Article>, context: Context) {
        this.articles = articles
        this.context = context
    }

    class HomeScreenViewHolder: RecyclerView.ViewHolder {
        var title: TextView? = null
        var image: ImageView? = null

        constructor(itemView: View): super(itemView) {
            title = itemView.findViewById(R.id.textViewTitle)
            image = itemView.findViewById(R.id.imageViewBackground)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeScreenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_card, parent, false)
        return HomeScreenViewHolder(view)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: HomeScreenViewHolder, position: Int) {
        holder.title?.text = articles[position].title
        val imageUrl = articles[position].imageUrl

        val imageView = holder.image
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
                    imageView!!.setImageBitmap(image)
                }
            }

            // If the URL doesnot point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        holder.itemView.setOnClickListener {
            Log.d("HomeScreenAdapter", "Clicked on article")
            val article = articles[position]
            val intent = Intent(context, ArticleScreen::class.java).apply {
                putExtra("articleid", article.id)
                putExtra("title", article.title)
                putExtra("description", article.description)
                putExtra("imageUrl", article.imageUrl)
                putExtra("author", article.authorId)
                putExtra("likesNo", article.likesNo)
            }
            context?.startActivity(intent)
        }
    }
}
