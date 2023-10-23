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
        var EXTRA_DESCRIPTION = "extra_image_url"
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
            val retreivedDescription = data.getStringExtra(EXTRA_DESCRIPTION)
            listCampgrounds.add(
                Campground(
                name = retreivedName,
                location = retreivedLocation,
                address = retreivedAddress,
                price = retreivedPrice,
                imageUrl = retreivedImageUrl,
                    description = retreivedDescription,
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
        val list = listOf(
            Campground(
                name = "Jungle Camping Kintamani",
                location = "Kintamani",
                address = "Pinggan, Kintamani, Bangli Regency, Bali 80652",
                price = 250000,
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. At risus viverra adipiscing at in tellus integer feugiat scelerisque. Pellentesque diam volutpat commodo sed. Blandit volutpat maecenas volutpat blandit aliquam etiam. Ac tincidunt vitae semper quis lectus nulla at volutpat diam. Sed tempus urna et pharetra pharetra massa massa ultricies mi. In nisl nisi scelerisque eu ultrices vitae auctor eu augue. Nunc scelerisque viverra mauris in aliquam sem fringilla ut morbi. Et magnis dis parturient montes nascetur ridiculus mus. Urna id volutpat lacus laoreet non curabitur.",
                imageUrl = "https://unsplash.com/photos/BPCsppbNRMI/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8Mnx8Y2FtcGdyb3VuZHxlbnwwfHx8fDE2OTc5ODE4ODd8MA&force=true&w=640"
            ),
            Campground(
                name = "Kintamani Camping & Staycation",
                location = "Kintamani",
                address = "Belong, Jl. Banjar Dalem, Songan B, Kec. Kintamani, Kabupaten Bangli, Bali 80652",
                price = 350000,
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Turpis nunc eget lorem dolor sed viverra ipsum nunc. Tempus imperdiet nulla malesuada pellentesque elit eget gravida. Cursus turpis massa tincidunt dui ut ornare lectus sit. Lacus suspendisse faucibus interdum posuere lorem. Pellentesque elit eget gravida cum sociis natoque penatibus. Urna id volutpat lacus laoreet non. Amet justo donec enim diam vulputate ut. Pellentesque habitant morbi tristique senectus et netus et malesuada fames. Diam maecenas ultricies mi eget mauris pharetra et ultrices neque. Euismod in pellentesque massa placerat duis. Quis enim lobortis scelerisque fermentum dui. In pellentesque massa placerat duis ultricies lacus. Quis auctor elit sed vulputate mi sit amet mauris.",
                imageUrl = "https://unsplash.com/photos/2DH-qMX6M4E/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8M3x8Y2FtcGdyb3VuZHxlbnwwfHx8fDE2OTc5ODE4ODd8MA&force=true&w=640"
            ),
            Campground(
                name = "Catur Camp Camping Beratan",
                location = "Bedugul",
                address = "Candikuning, Baturiti, Tabanan Regency, Bali",
                price = 250000,
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Vel facilisis volutpat est velit. Odio tempor orci dapibus ultrices in iaculis nunc sed augue. Sed turpis tincidunt id aliquet risus. Quam viverra orci sagittis eu volutpat odio facilisis. Vitae semper quis lectus nulla at volutpat. Tempor orci dapibus ultrices in iaculis nunc sed augue. Diam quis enim lobortis scelerisque fermentum dui faucibus in. Sed felis eget velit aliquet sagittis. Amet aliquam id diam maecenas ultricies mi eget. Ut porttitor leo a diam sollicitudin tempor. Amet dictum sit amet justo donec. Et malesuada fames ac turpis egestas sed tempus. Sed risus ultricies tristique nulla. Semper feugiat nibh sed pulvinar proin. Tristique senectus et netus et malesuada fames. Amet nisl purus in mollis nunc sed id.",
                imageUrl = "https://unsplash.com/photos/m1PFxGQ-5x0/download?ixid=M3wxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjk3OTgzNTUxfA&force=true&w=640"
            ),
            Campground(
                name = "Buyan Camping Camp",
                location = "Bedugul",
                address = "Jl. Sugriwa Dasong Pancasari No.4, Pancasari, Kec. Sukasada, Kabupaten Buleleng, Bali 81161",
                price = 250000,
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Porttitor rhoncus dolor purus non enim praesent elementum facilisis leo. Felis imperdiet proin fermentum leo vel. Vel quam elementum pulvinar etiam non quam. Purus viverra accumsan in nisl nisi scelerisque eu ultrices. A diam maecenas sed enim ut sem viverra aliquet eget. Lacus viverra vitae congue eu. In egestas erat imperdiet sed euismod. Id neque aliquam vestibulum morbi blandit. At lectus urna duis convallis convallis tellus id interdum. Natoque penatibus et magnis dis parturient montes nascetur ridiculus. Sit amet justo donec enim diam vulputate. Dignissim enim sit amet venenatis urna. Ornare quam viverra orci sagittis eu volutpat odio facilisis mauris. Ullamcorper sit amet risus nullam eget felis. Euismod elementum nisi quis eleifend quam adipiscing vitae proin sagittis. Mauris nunc congue nisi vitae suscipit tellus. Pretium nibh ipsum consequat nisl. Arcu cursus euismod quis viverra nibh cras pulvinar mattis. Aliquam ultrices sagittis orci a scelerisque purus semper eget.",
                imageUrl = "https://unsplash.com/photos/F4GGnyJ8aiI/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8NHx8Y2FtcGdyb3VuZHxlbnwwfHx8fDE2OTc5ODE4ODd8MA&force=true&w=640"
            )
        )
        listCampgrounds.addAll(list)
        return listCampgrounds
    }

    private fun showCampgroundList() {
        rvCampgrounds.layoutManager = LinearLayoutManager(this)
        val listCampgroundAdapter = ListCampgroundAdapter(listCampgrounds)
        rvCampgrounds.adapter = listCampgroundAdapter
    }
}