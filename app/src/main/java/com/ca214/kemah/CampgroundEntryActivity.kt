package com.ca214.kemah

import android.app.Activity
import android.app.ProgressDialog
import android.content.ClipDescription
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ca214.kemah.data.database.DatabaseHelper
import com.ca214.kemah.data.models.Campground
import com.ca214.kemah.data.models.requests.CampgroundRequest
import com.ca214.kemah.data.repositories.CampgroundRepository
import com.ca214.kemah.utils.Constants.EXTRA_CAMPGROUND_ID
import com.ca214.kemah.utils.TokenManager
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
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var campgroundRepository: CampgroundRepository
    private var progressDialog: ProgressDialog? = null
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campground_entry)

        dbHelper = DatabaseHelper(this)
        tokenManager = TokenManager(this)
        campgroundRepository = CampgroundRepository(tokenManager.getAccessToken().toString())

        editName = findViewById(R.id.edit_name)
        editLocation = findViewById(R.id.edit_location)
        editAddress = findViewById(R.id.edit_address)
        editPrice = findViewById(R.id.edit_price)
        editImageUrl = findViewById(R.id.edit_image_url)
        editDescription = findViewById(R.id.edit_description)
        btnSave = findViewById(R.id.button_save)

        val actionBar = supportActionBar
        actionBar?.title = "Campground Entry"

        // Mengambil id campground dari intent
        val id = intent.getStringExtra(EXTRA_CAMPGROUND_ID)
        if (id != null) {
            selectedId = UUID.fromString(id)
            showLoadingDialog("Get existing campground")
            // Mengakses data campground berdasarkan id
            campgroundRepository.getCampgroundById(UUID.fromString(id)).observe(this, Observer {
                campground -> if (campground != null) {
                    progressDialog?.dismiss()
                    editName.setText(campground.name)
                    editLocation.setText(campground.location)
                    editAddress.setText(campground.address)
                    editPrice.setText(campground.price.toString())
                    editDescription.setText(campground.description)
                    editImageUrl.setText(campground.imageUrl)
                    btnSave.setText(R.string.update)

                    actionBar?.title = "Campground Update"
                }
            })
        }

        btnSave.setOnClickListener(this)

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
                        showLoadingDialog("Deleting campground data...")
                        campgroundRepository.deleteCampground(UUID.fromString(selectedId.toString()))
                            .observe(this, Observer { isSuccess ->
                                if (isSuccess) {
                                    progressDialog?.dismiss()
                                    finish()
                                } else {
                                    progressDialog?.dismiss()
                                    Toast.makeText(this, "Failed to delete data campground", Toast.LENGTH_LONG).show()
                                }
                            })
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
                    // Membuat object campground request baru
                    var newCampground = CampgroundRequest(
                        name = inputName,
                        location = inputLocation,
                        address = inputAddress,
                        price = inputPrice.toInt(),
                        imageURL = null,
                        description = inputDescription,
                        latitude = null,
                        longitude = null,
                    )

                    if (inputImageUrl.isNotEmpty()) {
                        newCampground = newCampground.copy(imageURL = inputImageUrl)
                    }

                    if (selectedId != null) {
                        // Pembaharuan data campground
                        showLoadingDialog("Updating data campground...")
                        campgroundRepository.updateCampground(UUID.fromString(selectedId.toString()), newCampground)
                            .observe(this, Observer {
                                isSuccess -> if (isSuccess) {
                                    progressDialog?.dismiss()
                                    finish()
                                } else {
                                    progressDialog?.dismiss()
                                    Toast.makeText(this, "Failed to update data campground", Toast.LENGTH_LONG).show()
                                }
                            })
                    } else {
                        // Menyimpan campground baru
                        showLoadingDialog("Saving data campground...")
                        campgroundRepository.addCampground(newCampground).observe(this, Observer {
                            createdCampground -> if (createdCampground != null) {
                                progressDialog?.dismiss()
                                finish()
                            } else {
                                progressDialog?.dismiss()
                                Toast.makeText(this, "Failed to save data campground", Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                }
            }
        }
    }

    fun showLoadingDialog(message: String) {
        progressDialog = ProgressDialog.show(this, "Processing Data", message)
    }
}