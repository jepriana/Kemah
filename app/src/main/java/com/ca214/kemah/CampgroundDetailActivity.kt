package com.ca214.kemah

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ca214.kemah.data.repositories.CampgroundRepository
import java.util.UUID

class CampgroundDetailActivity : AppCompatActivity() {

    lateinit var textName: TextView
    lateinit var textLocation: TextView
    lateinit var textPrice: TextView
    lateinit var textAddress: TextView
    lateinit var textDescription: TextView
    lateinit var imgCampground: ImageView
    lateinit var btnReview: Button

    lateinit var campgroundRepository: CampgroundRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campground_detail)

        campgroundRepository = CampgroundRepository()

        textName = findViewById(R.id.text_name)
        textLocation = findViewById(R.id.text_location)
        textAddress = findViewById(R.id.text_address)
        textPrice = findViewById(R.id.text_price)
        textDescription = findViewById(R.id.text_description)
        imgCampground = findViewById(R.id.image_campground)
        btnReview = findViewById(R.id.button_review)

        // Mengambil data dari intent
        val id = intent.getStringExtra(MainActivity.EXTRA_CAMPGROUND_ID)
        if (id != null) {
            val progressDialog = ProgressDialog.show(this, "Loading Data", "Loading data campground...")
            // Mengakses data campground berdasarkan id
            campgroundRepository.getCampgroundById(UUID.fromString(id)).observe(this, Observer {
                    campground -> if (campground != null) {
                    // Menggunakan data dalam text view
                    textName.text = campground.name
                    textLocation.text = campground.location
                    textAddress.text = campground.address
                    textPrice.text = "Rp ${campground.price}"
                    textDescription.text = campground.description
                    btnReview.setText("${campground.comments.size} Comments")
                    if (!campground.imageUrl.isNullOrEmpty()) {
                        Glide.with(this).load(campground.imageUrl).into(imgCampground)
                    }
                    progressDialog.dismiss()
                }
            })

        }

        val actionBar = supportActionBar
        actionBar?.title = "Campground Detail"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
}