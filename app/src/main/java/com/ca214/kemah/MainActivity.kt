package com.ca214.kemah

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ca214.kemah.adapters.CampgroundGridAdapter
import com.ca214.kemah.adapters.CampgroundListAdapter
import com.ca214.kemah.data.api.ApiConfig
import com.ca214.kemah.data.database.DatabaseHelper
import com.ca214.kemah.data.models.Campground
import com.ca214.kemah.data.models.responses.CampgroundResponse
import com.ca214.kemah.data.repositories.CampgroundRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        var EXTRA_CAMPGROUND_ID = "extra_campground_id"
        var SHARED_PREFERENCES_NAME = "KemahSP"
        val ACCESS_TOKEN = "accessToken"
        val REFRESH_TOKEN = "refreshToken"
        var listCampgrounds = ArrayList<Campground>()
    }

    private var progressDialog: ProgressDialog? = null
    private lateinit var campgroundRepository: CampgroundRepository
    private lateinit var fabAddCampground: FloatingActionButton
    private lateinit var rvCampgroundList: RecyclerView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        campgroundRepository = CampgroundRepository()

        fabAddCampground = findViewById(R.id.fab_add_campground)
        rvCampgroundList = findViewById(R.id.rv_campgrounds)

        fabAddCampground.setOnClickListener(this)

//        listCampgrounds.addAll(getListCampgrounds())
//        showCampgroundList()

        val actionBar = supportActionBar
        actionBar?.title = "Kemah Data Campgrounds"
    }

    override fun onResume() {
        //listCampgrounds = getListCampgrounds()
        showCampgroundList()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_display_list -> {
                showCampgroundList()
            }
            R.id.action_display_grid -> {
                showCampgroundGrid()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.fab_add_campground -> {
                val openCampgroundEntry = Intent(this@MainActivity, CampgroundEntryActivity::class.java)
                startActivity(openCampgroundEntry)
            }
        }
    }

    private fun showCampgroundList() {
        val campgroundsLiveData = campgroundRepository.getCampgrounds()
        showLoading("Loading data campground")
        campgroundsLiveData.observe(this, Observer { campgrounds ->
                listCampgrounds.clear()
                listCampgrounds.addAll(
                    campgrounds
                )
                progressDialog?.dismiss()

                rvCampgroundList.layoutManager = LinearLayoutManager(this)
                val campgroundListAdapter = CampgroundListAdapter(listCampgrounds)
                rvCampgroundList.adapter = campgroundListAdapter
             })
    }

    private fun showCampgroundGrid() {
        val campgroundsLiveData = campgroundRepository.getCampgrounds()
        showLoading("Loading data campground")
        campgroundsLiveData.observe(this, Observer { campgrounds ->
            listCampgrounds.clear()
            listCampgrounds.addAll(
                campgrounds
            )
            progressDialog?.dismiss()
            rvCampgroundList.layoutManager = GridLayoutManager(this, 2)
            val campgroundGridAdapter = CampgroundGridAdapter(listCampgrounds)
            rvCampgroundList.adapter = campgroundGridAdapter
        })
    }

    private fun showLoading(msg: String) {
        progressDialog = ProgressDialog.show(this, "Loading Data", msg)
    }
}