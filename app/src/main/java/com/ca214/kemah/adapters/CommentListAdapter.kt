package com.ca214.kemah.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ca214.kemah.R
import com.ca214.kemah.data.models.Comment
import java.util.UUID

class CommentListAdapter (private val listComments: ArrayList<Comment>, private val campgroundId: UUID, private val userId: UUID)
    : RecyclerView.Adapter<CommentListAdapter.ListViewHolder>() {
    class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAuthor: TextView = itemView.findViewById(R.id.text_comment_author)
        val tvContent: TextView = itemView.findViewById(R.id.text_comment_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_comment, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listComments.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val comment = listComments[position]
        holder.tvAuthor.text = comment.creatorUsername
        holder.tvContent.text = comment.content
    }
}