package com.ca214.kemah.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ca214.kemah.CampgroundDetailActivity
import com.ca214.kemah.R
import com.ca214.kemah.models.Campground

class ListCampgroundAdapter(private val listCampground: ArrayList<Campground>) : RecyclerView.Adapter<ListCampgroundAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_campground, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCampground.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val (name, location, address, price, description, imageUrl) = listCampground[position]
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .into(holder.imgIllustration)
        }
        holder.tvName.text = name
        holder.tvPrice.text = "Rp $price"
        holder.itemView.setOnClickListener {
            var openCampgroundDetail = Intent(holder.itemView.context, CampgroundDetailActivity::class.java)
            openCampgroundDetail.putExtra(CampgroundDetailActivity.EXTRA_NAME, name)
            openCampgroundDetail.putExtra(CampgroundDetailActivity.EXTRA_LOCATION, location)
            openCampgroundDetail.putExtra(CampgroundDetailActivity.EXTRA_ADDRESS, address)
            openCampgroundDetail.putExtra(CampgroundDetailActivity.EXTRA_PRICE, price)
            openCampgroundDetail.putExtra(CampgroundDetailActivity.EXTRA_IMAGE_URL, imageUrl)
            openCampgroundDetail.putExtra(CampgroundDetailActivity.EXTRA_DESCRIPTION, description)
            holder.itemView.context.startActivity(openCampgroundDetail)
        }
    }
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIllustration: ImageView = itemView.findViewById(R.id.img_campground_ilustration)
        val tvName: TextView = itemView.findViewById(R.id.tv_campground_name)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_campground_price)
    }
}