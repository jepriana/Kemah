package com.ca214.kemah.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ca214.kemah.models.Campground
import java.util.ArrayList
import java.util.UUID

class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "kemah.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "favorite_camp"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_LOCATION = "location"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_IMAGE_URL = "image_url"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID TEXT PRIMARY KEY, " +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_LOCATION TEXT," +
                "$COLUMN_ADDRESS TEXT," +
                "$COLUMN_PRICE INT," +
                "$COLUMN_DESCRIPTION TEXT," +
                "$COLUMN_IMAGE_URL TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertCampground(campground: Campground){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, campground.id.toString())
            put(COLUMN_NAME, campground.name)
            put(COLUMN_LOCATION, campground.location)
            put(COLUMN_ADDRESS, campground.address)
            put(COLUMN_PRICE, campground.price)
            put(COLUMN_DESCRIPTION, campground.description)
            put(COLUMN_IMAGE_URL, campground.imageUrl)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun gelAllCampgrounds() : List<Campground> {
        val campgrounds = ArrayList<Campground>()
        val db = readableDatabase
        val readCampgroundQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(readCampgroundQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()){
                do {
                    val campground = Campground(
                        id = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                        price = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE))),
                        description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
                    )
                    campgrounds.add(campground)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return campgrounds
    }

    fun updateCampground(campground: Campground){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, campground.name)
            put(COLUMN_LOCATION, campground.location)
            put(COLUMN_ADDRESS, campground.address)
            put(COLUMN_PRICE, campground.price)
            put(COLUMN_DESCRIPTION, campground.description)
            put(COLUMN_IMAGE_URL, campground.imageUrl)
        }
        val whereClause = "$COLUMN_ID =?"
        val whereArgs = arrayOf(campground.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun deleteCampground(id: UUID) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID =?"
        val whereArgs = arrayOf(id.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
    fun findCampgroundById(id: String) : Campground? {
        val db = readableDatabase
        var campground: Campground? = null
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = '$id'"
        var cursor = db.rawQuery(query, null)
        if (cursor != null && cursor.moveToFirst()) {
            campground = Campground(
                id = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                price = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE))),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
            )
        }
        cursor.close()
        db.close()
        return campground
    }
}