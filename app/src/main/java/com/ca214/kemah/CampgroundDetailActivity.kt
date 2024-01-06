package com.ca214.kemah

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ca214.kemah.data.models.CampgroundDetail
import com.ca214.kemah.data.repositories.CampgroundRepository
import com.ca214.kemah.utils.Constants.EXTRA_CAMPGROUND_ID
import com.ca214.kemah.utils.TokenManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
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

    lateinit var campgroundRepository: CampgroundRepository
    lateinit var tokenManager: TokenManager
    lateinit var selectedCampgroud: CampgroundDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campground_detail)
//        Configuration.getInstance().setCachePath(this.cacheDir.absolutePath)
        tokenManager = TokenManager(this)
        campgroundRepository = CampgroundRepository(tokenManager.getAccessToken().toString())

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
            val progressDialog = ProgressDialog.show(this, "Loading Data", "Loading data campground...")
            campgroundId = UUID.fromString(id)
            // Mengakses data campground berdasarkan id
            campgroundRepository.getCampgroundById(campgroundId).observe(this, Observer {
                    campground -> if (campground != null) {
                    selectedCampgroud = campground
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
                    if (campground.latitude != null && campground.longitude != null) {
                        displayLocation(campground.latitude, campground.longitude)
                    }
                    progressDialog.dismiss()
                }
            })

        }

        btnReview.setOnClickListener(this)

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

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.button_review -> {
                if (selectedCampgroud.comments.isEmpty()) {
                    val navigateToEmptyComment = Intent(this@CampgroundDetailActivity, EmptyCommentActivity::class.java)
                    startActivity(navigateToEmptyComment)
                } else {
                    val navigateToComment =
                        Intent(this@CampgroundDetailActivity, CommentActivity::class.java)
                    navigateToComment.putExtra(EXTRA_CAMPGROUND_ID, campgroundId.toString())
                    startActivity(navigateToComment)
                }
            }
        }
    }
    private fun displayLocation(latitude: Double, longitude: Double) {
        // Acquire a reference to the system Location Manager
        val mapView = findViewById<MapView>(R.id.map_view)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
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
}