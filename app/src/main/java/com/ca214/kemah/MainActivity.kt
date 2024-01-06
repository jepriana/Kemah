package com.ca214.kemah

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ca214.kemah.adapters.CampgroundGridAdapter
import com.ca214.kemah.adapters.CampgroundListAdapter
import com.ca214.kemah.data.database.DatabaseHelper
import com.ca214.kemah.data.models.Campground
import com.ca214.kemah.data.repositories.CampgroundRepository
import com.ca214.kemah.utils.TokenManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.UUID

class MainActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        var listCampgrounds = ArrayList<Campground>()
    }

    private lateinit var fabAddCampground: FloatingActionButton
    private lateinit var rvCampgroundList: RecyclerView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var campgroundRepository: CampgroundRepository
    private lateinit var tokenManager: TokenManager
    private lateinit var userId: UUID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        tokenManager = TokenManager(this)
        campgroundRepository = CampgroundRepository(tokenManager.getAccessToken().toString())
        userId = UUID.fromString(tokenManager.getUserId())

        fabAddCampground = findViewById(R.id.fab_add_campground)
        rvCampgroundList = findViewById(R.id.rv_campgrounds)

        fabAddCampground.setOnClickListener(this)

        val actionBar = supportActionBar
        actionBar?.title = "Kemah Data Campgrounds"
    }

    override fun onResume() {
        showCampgrounds()
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
        rvCampgroundList.layoutManager = LinearLayoutManager(this)
        val campgroundListAdapter = CampgroundListAdapter(listCampgrounds, userId)
        rvCampgroundList.adapter = campgroundListAdapter
    }

    private fun showCampgroundGrid() {
        rvCampgroundList.layoutManager = GridLayoutManager(this, 2)
        val campgroundGridAdapter = CampgroundGridAdapter(listCampgrounds, userId)
        rvCampgroundList.adapter = campgroundGridAdapter
    }

    private fun showCampgrounds()
    {
        try {
            // Membuat progres dialog
            val progressDialog = ProgressDialog.show(this, "Load Data", "Loading data campground...")
            val campgroundsLiveData = campgroundRepository.getAllCampgrounds()
            campgroundsLiveData.observe(this, Observer { campgrounds ->
                listCampgrounds.clear()
                listCampgrounds.addAll(campgrounds)
                progressDialog.dismiss()
                // Display as a list
                showCampgroundList()
            })
        } catch (er: Exception){
            Toast.makeText(this, er.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}