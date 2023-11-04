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
import com.ca214.kemah.MainActivity
import com.ca214.kemah.R
import com.ca214.kemah.models.Campground

class CampgroundGridAdapter(private val listCampgrounds: ArrayList<Campground>) : Adapter<CampgroundGridAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCampgroundIllustration: ImageView = itemView.findViewById(R.id.img_campground_grid)
        val tvCampgroundName: TextView = itemView.findViewById(R.id.text_campground_grid_name)
        val tvCampgroundLocationAndPrice: TextView = itemView.findViewById(R.id.text_campground_grid_location_price)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CampgroundGridAdapter.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_grid_campground, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CampgroundGridAdapter.ListViewHolder, position: Int) {
        val campground = listCampgrounds.get(position)
        holder.tvCampgroundName.text = campground.name
        holder.tvCampgroundLocationAndPrice.text = "${campground.location} - Rp ${campground.price}"

        if (!campground.imageUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(campground.imageUrl)
                .into(holder.imgCampgroundIllustration)
        }

        holder.itemView.setOnClickListener {
            var openCampgroundDetail = Intent(holder.itemView.context, CampgroundDetailActivity::class.java)
            openCampgroundDetail.putExtra(MainActivity.EXTRA_NAME, campground.name)
            openCampgroundDetail.putExtra(MainActivity.EXTRA_PRICE, campground.price)
            openCampgroundDetail.putExtra(MainActivity.EXTRA_LOCATION, campground.location)
            openCampgroundDetail.putExtra(MainActivity.EXTRA_ADDRESS, campground.address)
            openCampgroundDetail.putExtra(MainActivity.EXTRA_IMAGE_URL, campground.imageUrl)
            openCampgroundDetail.putExtra(MainActivity.EXTRA_DESCRIPTION, campground.description)
            openCampgroundDetail.putExtra(MainActivity.SELECTED_CAMPGROUND_INDEX, position)
            holder.itemView.context.startActivity(openCampgroundDetail)
        }

        holder.itemView.setOnLongClickListener {
            var builder = AlertDialog.Builder(holder.itemView.context)
            builder.setMessage("Are you sure want to update campground data?")
                .setCancelable(false)
                .setPositiveButton("Sure") { dialog, id ->
                    var openCampgroundEntry = Intent(holder.itemView.context, CampgroundEntryActivity::class.java)
                    openCampgroundEntry.putExtra(MainActivity.SELECTED_CAMPGROUND_INDEX, position)
                    holder.itemView.context.startActivity(openCampgroundEntry)
                }
                .setNegativeButton("Cancel") { dialog, id -> }
            var alertDilog = builder.create()
            alertDilog.show()

            true
        }
    }

    override fun getItemCount(): Int {
        return listCampgrounds.size
    }
}