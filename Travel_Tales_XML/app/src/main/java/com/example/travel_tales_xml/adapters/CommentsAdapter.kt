package com.example.travel_tales_xml.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travel_tales_xml.R
import com.example.travel_tales_xml.models.Comment

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    var comments: List<Comment> = emptyList()
    var context: Context? = null

    constructor(comments: List<Comment>, context: Context) {
        this.comments = comments
        this.context = context
    }

    class CommentsViewHolder: RecyclerView.ViewHolder {
        var title: TextView? = null
        var comment: TextView? = null
        var timeStamp: TextView? = null

        constructor(itemView: View): super(itemView) {
            title = itemView.findViewById(R.id.textViewCommenterName)
            timeStamp = itemView.findViewById(R.id.textViewCommentInfo)
            comment = itemView.findViewById(R.id.textViewCommentContent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_row, parent, false)
        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.comment?.text = comments[position].comment
        holder.title?.text = comments[position].authorId
        holder.timeStamp?.text = comments[position].timeStamp
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}
