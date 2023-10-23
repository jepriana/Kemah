package com.ca214.kemah.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.ca214.kemah.R
import com.ca214.kemah.models.Campground

class CampgroundListAdapter(private val listCampgrounds: ArrayList<Campground>) : Adapter<CampgroundListAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCampgroundIllustration: ImageView = itemView.findViewById(R.id.img_campground_row)
        val tvCampgroundName: TextView = itemView.findViewById(R.id.text_campground_row_name)
        val tvCampgroundLocation: TextView = itemView.findViewById(R.id.text_campground_row_location)
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
        holder.tvCampgroundPrice.text = "Rp ${campground.price}"
    }

    override fun getItemCount(): Int {
        return listCampgrounds.size
    }
}