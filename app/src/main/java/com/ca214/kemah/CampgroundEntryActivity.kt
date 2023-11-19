package com.ca214.kemah

import android.app.Activity
import android.content.ClipDescription
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.ca214.kemah.data.DatabaseHelper
import com.ca214.kemah.models.Campground
import java.util.UUID

class CampgroundEntryActivity : AppCompatActivity(), View.OnClickListener {
    final lateinit var editName: EditText
    final lateinit var editLocation: EditText
    final lateinit var editAddress: EditText
    final lateinit var editPrice: EditText
    final lateinit var editImageUrl: EditText
    final lateinit var editDescription: EditText
    final lateinit var btnSave: Button
    private var selectedId: UUID? = null
    final lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campground_entry)

        dbHelper = DatabaseHelper(this)
        editName = findViewById(R.id.edit_name)
        editLocation = findViewById(R.id.edit_location)
        editAddress = findViewById(R.id.edit_address)
        editPrice = findViewById(R.id.edit_price)
        editImageUrl = findViewById(R.id.edit_image_url)
        editDescription = findViewById(R.id.edit_description)
        btnSave = findViewById(R.id.button_save)

        // Mengambil id campground dari intent
        val id = intent.getStringExtra(MainActivity.EXTRA_ID)
        if (id != null) {
            val campground = dbHelper.findCampgroundById(id)
            if (campground != null) {
                selectedId = campground.id
                editName.setText(campground.name)
                editLocation.setText(campground.location)
                editAddress.setText(campground.address)
                editPrice.setText(campground.price.toString())
                editDescription.setText(campground.description)
                editImageUrl.setText(campground.imageUrl)
                btnSave.setText(R.string.update)
            }
        }

        btnSave.setOnClickListener(this)

        val actionBar = supportActionBar
        actionBar?.title = "Campground Entry"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.campground_entry_menu, menu)
        val menuDelete = menu?.findItem(R.id.action_delete_campground)
        if (menuDelete != null) {
            menuDelete.isVisible = selectedId != null;
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete_campground -> {
                // Menampilkan dialog konfirmasi penghapusan data
                var builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure want to delete campground data?")
                    .setCancelable(false)
                    .setPositiveButton("Sure") { dialog, id ->
                        // Menghapus data campground
                        // MainActivity.listCampgrounds.removeAt(selectedIndex)
                        dbHelper.deleteCampground(UUID.fromString(selectedId.toString()))
                        finish()
                    }
                    .setNegativeButton("Cancel") { dialog, id -> }
                var alertDilog = builder.create()
                alertDilog.show()
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
                val inputDescription = editDescription.text.toString().trim()
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

                // Penyimpanan data campground
                if (!invalidEntries) {
                    // Membuat object campground baru
                    var newCampground = Campground(
                        id = UUID.randomUUID(),
                        name = inputName,
                        location = inputLocation,
                        address = inputAddress,
                        price = inputPrice.toInt(),
                        imageUrl = inputImageUrl,
                        description = inputDescription,
                    )

                    if (selectedId != null) {
                        // Pembaharuan data campground
                        // MainActivity.listCampgrounds[selectedIndex] = newCampground
                        val updatedCampground = newCampground.copy(id = selectedId)
                        dbHelper.updateCampground(updatedCampground)
                    } else {
                        // Menyimpan campground baru
//                        MainActivity.listCampgrounds.add(newCampground)
                        dbHelper.insertCampground(newCampground)
                    }

                    finish()
                }
            }
        }
    }
}