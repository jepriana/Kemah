package com.ca214.kemah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class CampgroundDetailActivity : AppCompatActivity() {

    lateinit var textName: TextView
    lateinit var textLocation: TextView
    lateinit var textPrice: TextView
    lateinit var textAddress: TextView
    lateinit var textDescription: TextView
    lateinit var imgCampground: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campground_detail)

        textName = findViewById(R.id.text_name)
        textLocation = findViewById(R.id.text_location)
        textAddress = findViewById(R.id.text_address)
        textPrice = findViewById(R.id.text_price)
        textDescription = findViewById(R.id.text_description)
        imgCampground = findViewById(R.id.image_campground)

        // Mengambil data dari intent
        val name = intent.getStringExtra(MainActivity.EXTRA_NAME)
        val location = intent.getStringExtra(MainActivity.EXTRA_LOCATION)
        val address = intent.getStringExtra(MainActivity.EXTRA_ADDRESS)
        val price = intent.getIntExtra(MainActivity.EXTRA_PRICE, 0)
        val imageUrl = intent.getStringExtra(MainActivity.EXTRA_IMAGE_URL)
        val description = intent.getStringExtra(MainActivity.EXTRA_DESCRIPTION)

        // Menggunakan data dalam text view
        textName.text = name
        textLocation.text = location
        textAddress.text = address
        textPrice.text = "Rp $price"
        textDescription.text = description

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this).load(imageUrl).into(imgCampground)
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