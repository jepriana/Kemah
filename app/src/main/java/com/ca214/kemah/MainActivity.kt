package com.ca214.kemah

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ca214.kemah.adapters.ListCampgroundAdapter
import com.ca214.kemah.models.Campground
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var fabAddCampground: FloatingActionButton
    private lateinit var rvCampgrounds: RecyclerView
    private val listCampgrounds = ArrayList<Campground>()
    companion object {
        var EXTRA_NAME = "extra_name"
        var EXTRA_LOCATION = "extra_location"
        var EXTRA_ADDRESS = "extra_address"
        var EXTRA_PRICE = "extra_price"
        var EXTRA_IMAGE_URL = "extra_image_url"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAddCampground = findViewById(R.id.fab_add_campground)

        fabAddCampground.setOnClickListener(this)

        rvCampgrounds = findViewById(R.id.rv_campgrounds)
        rvCampgrounds.setHasFixedSize(true)

        listCampgrounds.addAll(getListCampgrounds())
        showCampgroundList()
    }

    private val openEntryRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result -> if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
        if (data != null) {
            val retreivedName = data.getStringExtra(EXTRA_NAME)
            val retreivedLocation = data.getStringExtra(EXTRA_LOCATION)
            val retreivedAddress = data.getStringExtra(EXTRA_ADDRESS)
            val retreivedPrice = data.getIntExtra(EXTRA_PRICE, 0)
            val retreivedImageUrl = data.getStringExtra(EXTRA_IMAGE_URL)
            listCampgrounds.add(
                Campground(
                name = retreivedName,
                location = retreivedLocation,
                address = retreivedAddress,
                price = retreivedPrice,
                imageUrl = retreivedImageUrl,
                ))
            showCampgroundList()
        }
        }
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.fab_add_campground -> {
                val openCampgroundEntry = Intent(this@MainActivity, CampgroundEntryActivity::class.java)
                openEntryRequest.launch(openCampgroundEntry)
            }
        }
    }

    private fun getListCampgrounds(): ArrayList<Campground> {
        val listCampgrounds = ArrayList<Campground>()
        listCampgrounds.add(
            Campground(
                name = "Jungle Camping Kintamani",
                location = "Kintamani",
                address = "Pinggan, Kintamani, Bangli Regency, Bali 80652",
                price = 250000,
                imageUrl = "https://unsplash.com/photos/BPCsppbNRMI/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8Mnx8Y2FtcGdyb3VuZHxlbnwwfHx8fDE2OTc5ODE4ODd8MA&force=true&w=640"
            )
        )
        listCampgrounds.add(
            Campground(
                name = "Kintamani Camping & Staycation",
                location = "Kintamani",
                address = "Belong, Jl. Banjar Dalem, Songan B, Kec. Kintamani, Kabupaten Bangli, Bali 80652",
                price = 350000,
                imageUrl = "https://unsplash.com/photos/2DH-qMX6M4E/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8M3x8Y2FtcGdyb3VuZHxlbnwwfHx8fDE2OTc5ODE4ODd8MA&force=true&w=640"
            )
        )
        listCampgrounds.add(
            Campground(
                name = "Catur Camp Camping Beratan",
                location = "Bedugul",
                address = "Candikuning, Baturiti, Tabanan Regency, Bali",
                price = 250000,
                imageUrl = "https://unsplash.com/photos/m1PFxGQ-5x0/download?ixid=M3wxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjk3OTgzNTUxfA&force=true&w=640"
            )
        )
        listCampgrounds.add(
            Campground(
                name = "Buyan Camping Camp",
                location = "Bedugul",
                address = "Jl. Sugriwa Dasong Pancasari No.4, Pancasari, Kec. Sukasada, Kabupaten Buleleng, Bali 81161",
                price = 250000,
                imageUrl = "https://unsplash.com/photos/F4GGnyJ8aiI/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8NHx8Y2FtcGdyb3VuZHxlbnwwfHx8fDE2OTc5ODE4ODd8MA&force=true&w=640"
            )
        )
        return listCampgrounds
    }

    private fun showCampgroundList() {
        rvCampgrounds.layoutManager = LinearLayoutManager(this)
        val listCampgroundAdapter = ListCampgroundAdapter(listCampgrounds)
        rvCampgrounds.adapter = listCampgroundAdapter
    }
}