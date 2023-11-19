package com.ca214.kemah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ca214.kemah.data.DatabaseHelper
import java.util.UUID

class CampgroundDetailActivity : AppCompatActivity() {

    lateinit var textName: TextView
    lateinit var textLocation: TextView
    lateinit var textPrice: TextView
    lateinit var textAddress: TextView
    lateinit var textDescription: TextView
    lateinit var imgCampground: ImageView
    lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campground_detail)

        dbHelper = DatabaseHelper(this)

        textName = findViewById(R.id.text_name)
        textLocation = findViewById(R.id.text_location)
        textAddress = findViewById(R.id.text_address)
        textPrice = findViewById(R.id.text_price)
        textDescription = findViewById(R.id.text_description)
        imgCampground = findViewById(R.id.image_campground)

        // Mengambil data dari intent
        val id = intent.getStringExtra(MainActivity.EXTRA_ID)
        if (id != null) {
            val campground = dbHelper.findCampgroundById(id)
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
        actionBar?.setTitle("Campground Detail")

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