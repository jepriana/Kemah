package com.ca214.kemah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class CampgroundDetailActivity : AppCompatActivity() {
    companion object {
        var EXTRA_NAME = "extra_name"
        var EXTRA_LOCATION = "extra_location"
        var EXTRA_ADDRESS = "extra_address"
        var EXTRA_PRICE = "extra_price"
        var EXTRA_IMAGE_URL = "extra_image_url"
        var EXTRA_DESCRIPTION = "extra_image_url"
    }

    lateinit var textName: TextView
    lateinit var textLocation: TextView
    lateinit var textPrice: TextView
    lateinit var textAddress: TextView
    lateinit var imageIllustration: ImageView
    lateinit var textDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campground_detail)

        textName = findViewById(R.id.text_name)
        textLocation = findViewById(R.id.text_location)
        textAddress = findViewById(R.id.text_address)
        textPrice = findViewById(R.id.text_price)
        imageIllustration = findViewById(R.id.image_campground)
        textDescription = findViewById(R.id.text_description)

        // Mengambil data dari intent
        val name = intent.getStringExtra(EXTRA_NAME)
        val location = intent.getStringExtra(EXTRA_LOCATION)
        val address = intent.getStringExtra(EXTRA_ADDRESS)
        val price = intent.getStringExtra(EXTRA_PRICE)
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)

        // Menggunakan data dalam text view
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .into(imageIllustration)
        }
        textName.text = name
        textLocation.text = location
        textAddress.text = address
        textPrice.text = "Rp $price"
        textDescription.text = description
    }
}