package com.ca214.kemah

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class CampgroundEntryActivity : AppCompatActivity(), View.OnClickListener {
    final lateinit var editName: EditText
    final lateinit var editLocation: EditText
    final lateinit var editAddress: EditText
    final lateinit var editPrice: EditText
    final lateinit var editImageUrl: EditText
    final lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campground_entry)

        editName = findViewById(R.id.edit_name)
        editLocation = findViewById(R.id.edit_location)
        editAddress = findViewById(R.id.edit_address)
        editPrice = findViewById(R.id.edit_price)
        editImageUrl = findViewById(R.id.edit_image_url)
        btnSave = findViewById(R.id.button_save)

        btnSave.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.button_save -> {
                // Get data from edit text
                val inputName = editName.text.toString().trim()
                val inputLocation = editLocation.text.toString().trim()
                val inputAddress = editAddress.text.toString().trim()
                val inputPrice = editPrice.text.toString().trim()
                val inputImageUrl = editImageUrl.text.toString().trim()
                var invalidEntries = false

                // Validate data
                if (inputName.isEmpty()) {
                    invalidEntries = true
                    editName.setError("Campground name is required")
                }
                if (inputLocation.isEmpty()) {
                    invalidEntries = true
                    editLocation.setError("Campground location is required")
                }
                if (inputAddress.isEmpty()) {
                    invalidEntries = true
                    editAddress.setError("Campground address is required")
                }
                if (inputPrice.isEmpty()) {
                    invalidEntries = true
                    editPrice.setError("Campground price is required")
                }

                // Send data to detail activity
                if (!invalidEntries) {
                    var openCampgroundDetail = Intent(this@CampgroundEntryActivity, CampgroundDetailActivity::class.java)
                    openCampgroundDetail.putExtra(CampgroundDetailActivity.EXTRA_NAME, inputName)
                    openCampgroundDetail.putExtra(CampgroundDetailActivity.EXTRA_LOCATION, inputLocation)
                    openCampgroundDetail.putExtra(CampgroundDetailActivity.EXTRA_ADDRESS, inputAddress)
                    openCampgroundDetail.putExtra(CampgroundDetailActivity.EXTRA_PRICE, inputPrice)
                    openCampgroundDetail.putExtra(CampgroundDetailActivity.EXTRA_IMAGE_URL, inputImageUrl)
                    startActivity(openCampgroundDetail)
                }
            }
        }
    }
}