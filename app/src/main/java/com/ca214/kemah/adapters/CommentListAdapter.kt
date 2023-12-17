package com.ca214.kemah.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.ca214.kemah.R
import com.ca214.kemah.data.models.Comment
import java.util.UUID

class CommentListAdapter(private val listComments: ArrayList<Comment>, private val campgroundId: UUID, private val userId: UUID) : Adapter<CommentListAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAuthor: TextView = itemView.findViewById(R.id.text_comment_author)
        val tvContent: TextView = itemView.findViewById(R.id.text_comment_content)
        val iconDelete: ImageView = itemView.findViewById(R.id.icon_delete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_comment, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val comment = listComments.get(position)
        holder.tvAuthor.text = comment.creatorUsername
        holder.tvContent.text = comment.content
        holder.iconDelete.visibility = if (comment.creatorId == userId) View.VISIBLE else View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return listComments.size
    }
}