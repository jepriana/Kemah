package com.ca214.kemah

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.ca214.kemah.data.database.DatabaseHelper
import com.ca214.kemah.data.models.requests.CampgroundRequest
import com.ca214.kemah.data.repositories.CampgroundRepository
import com.ca214.kemah.utils.Constants.EXTRA_CAMPGROUND_ID
import com.ca214.kemah.utils.TokenManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.UUID

class CampgroundEntryActivity : AppCompatActivity(), View.OnClickListener {
    final lateinit var editName: EditText
    final lateinit var editLocation: EditText
    final lateinit var editAddress: EditText
    final lateinit var editPrice: EditText
    final lateinit var editImageUrl: EditText
    final lateinit var editDescription: EditText
    final lateinit var editLatitude: EditText
    final lateinit var editLongitude: EditText
    final lateinit var btnGetCurrentLocation: Button
    final lateinit var btnSave: Button
    private var selectedId: UUID? = null
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var campgroundRepository: CampgroundRepository
    private var progressDialog: ProgressDialog? = null
    private lateinit var tokenManager: TokenManager
    val PERMISSION_ID = 42
    final lateinit var  mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campground_entry)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        dbHelper = DatabaseHelper(this)
        tokenManager = TokenManager(this)
        campgroundRepository = CampgroundRepository(tokenManager.getAccessToken().toString())

        editName = findViewById(R.id.edit_name)
        editLocation = findViewById(R.id.edit_location)
        editAddress = findViewById(R.id.edit_address)
        editPrice = findViewById(R.id.edit_price)
        editImageUrl = findViewById(R.id.edit_image_url)
        editDescription = findViewById(R.id.edit_description)
        editLatitude = findViewById(R.id.edit_latitude)
        editLongitude = findViewById(R.id.edit_longitude)
        btnGetCurrentLocation = findViewById(R.id.button_get_current_location)
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
                    editLatitude.setText(campground.latitude?.toString().orEmpty())
                    editLongitude.setText(campground.longitude?.toString().orEmpty())
                    btnSave.setText(R.string.update)

                    actionBar?.title = "Campground Update"
                }
            })
        }

        btnSave.setOnClickListener(this)
        btnGetCurrentLocation.setOnClickListener(this)

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

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    private fun checkPermissions(): Boolean {
        return (
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission (
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        editLatitude.setText(location.latitude.toString())
                        editLongitude.setText(location.longitude.toString())
                        displayLocation(location.latitude, location.longitude)
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location? = locationResult.lastLocation
            if (mLastLocation != null) {
                editLatitude.setText(mLastLocation.latitude.toString())
                editLongitude.setText(mLastLocation.longitude.toString())
                displayLocation(mLastLocation.latitude, mLastLocation.longitude)
            }
        }
    }

    private fun displayLocation(latitude: Double, longitude: Double) {
        // Acquire a reference to the system Location Manager
        val mapView = findViewById<MapView>(R.id.map_view)
        mapView.setTileSource(TileSourceFactory.CLOUDMADESTANDARDTILES)
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)
        val geoPoint = GeoPoint(latitude, longitude)
        val mapController = mapView.controller
        mapController.setZoom(15.0)
        mapController.setCenter(geoPoint)

        // Add a marker to the map
        val marker = Marker(mapView)
        marker.position = geoPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapView.overlays.add(marker)
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

            R.id.button_get_current_location -> {
                getLastLocation()
            }
            R.id.button_save -> {
                // Get data from edit text
                val inputName = editName.text.toString().trim()
                val inputLocation = editLocation.text.toString().trim()
                val inputAddress = editAddress.text.toString().trim()
                val inputPrice = editPrice.text.toString().trim()
                val inputImageUrl = editImageUrl.text.toString().trim()
                val inputDescription = editDescription.text.toString().trim()
                val inputLatitude = editLatitude.text.toString().trim().toDoubleOrNull()
                val inputLongitude = editLongitude.text.toString().trim().toDoubleOrNull()
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
                        latitude = inputLatitude,
                        longitude = inputLongitude,
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