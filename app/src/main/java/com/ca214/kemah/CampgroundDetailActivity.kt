package com.ca214.kemah

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ca214.kemah.utils.Constants.EXTRA_CAMPGROUND_ID
import java.util.UUID

class CampgroundDetailActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var textName: TextView
    lateinit var textLocation: TextView
    lateinit var textPrice: TextView
    lateinit var textAddress: TextView
    lateinit var textDescription: TextView
    lateinit var imgCampground: ImageView
    lateinit var btnReview: Button
    lateinit var campgroundId: UUID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campground_detail)

        textName = findViewById(R.id.text_name)
        textLocation = findViewById(R.id.text_location)
        textAddress = findViewById(R.id.text_address)
        textPrice = findViewById(R.id.text_price)
        textDescription = findViewById(R.id.text_description)
        imgCampground = findViewById(R.id.image_campground)
        btnReview = findViewById(R.id.button_review)


        // Mengambil data dari intent
        val id = intent.getStringExtra(EXTRA_CAMPGROUND_ID)
        if (id != null) {
            // Mengakses data campground berdasarkan id
            campgroundId = UUID.fromString(id)
            val campground = MainActivity.listCampgrounds.firstOrNull { cmp -> cmp.id == campgroundId }
            if (campground != null) {
                // Menggunakan data dalam text view
                textName.text = campground.name
                textLocation.text = campground.location
                textAddress.text = campground.address
                textPrice.text = "Rp ${campground.price}"
                textDescription.text = campground.description

                if (!campground.imageUrl.isNullOrEmpty()) {
                    Glide.with(this).load(campground.imageUrl).into(imgCampground)
                }
            }
        }

        val actionBar = supportActionBar
        actionBar?.title = "Campground Detail"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnReview.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.button_review -> {
                val navigateToReview = Intent(this@CampgroundDetailActivity, CommentActivity::class.java)
                navigateToReview.putExtra(EXTRA_CAMPGROUND_ID, campgroundId.toString())
                startActivity(navigateToReview)
            }
        }
    }
}