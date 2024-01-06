package com.ca214.kemah.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.ca214.kemah.CampgroundDetailActivity
import com.ca214.kemah.CampgroundEntryActivity
import com.ca214.kemah.R
import com.ca214.kemah.data.models.Campground
import com.ca214.kemah.utils.Constants.EXTRA_CAMPGROUND_ID
import java.util.UUID

class CampgroundListAdapter(private val listCampgrounds: ArrayList<Campground>, private val userId: UUID) : Adapter<CampgroundListAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCampgroundIllustration: ImageView = itemView.findViewById(R.id.author_image)
        val tvCampgroundName: TextView = itemView.findViewById(R.id.text_comment_author)
        val tvCampgroundLocation: TextView = itemView.findViewById(R.id.text_comment_content)
        val tvCampgroundPrice: TextView = itemView.findViewById(R.id.text_campground_row_price)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CampgroundListAdapter.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_campground, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CampgroundListAdapter.ListViewHolder, position: Int) {
        val campground = listCampgrounds.get(position)
        holder.tvCampgroundName.text = campground.name
        holder.tvCampgroundLocation.text = campground.location
        holder.tvCampgroundPrice.text = "Rp ${campground.price} by ${campground.creatorUsername}"

        if (!campground.imageUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(campground.imageUrl)
                .into(holder.imgCampgroundIllustration)
        }

        holder.itemView.setOnClickListener {
            var openCampgroundDetail = Intent(holder.itemView.context, CampgroundDetailActivity::class.java)
            openCampgroundDetail.putExtra(EXTRA_CAMPGROUND_ID, campground.id.toString())
            holder.itemView.context.startActivity(openCampgroundDetail)
        }

        if (campground.creatorId == userId) {
            holder.itemView.setOnLongClickListener {
                val builder = AlertDialog.Builder(holder.itemView.context)
                builder.setMessage("Are you sure want to update campground data?")
                    .setCancelable(false)
                    .setPositiveButton("Sure") { dialog, id ->
                        val openCampgroundEntry =
                            Intent(holder.itemView.context, CampgroundEntryActivity::class.java)
                        openCampgroundEntry.putExtra(EXTRA_CAMPGROUND_ID, campground.id.toString())
                        holder.itemView.context.startActivity(openCampgroundEntry)
                    }
                    .setNegativeButton("Cancel") { dialog, id -> }
                val alertDialog = builder.create()
                alertDialog.show()

                true
            }
        }
    }

    override fun getItemCount(): Int {
        return listCampgrounds.size
    }
}