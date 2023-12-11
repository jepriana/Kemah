package com.ca214.kemah.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ca214.kemah.data.models.Campground
import java.util.ArrayList
import java.util.UUID

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "mykemah.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_FAVORITE_CAMP = "favorite_camp"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_LOCATION = "location"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_IMAGE_URL = "image_url"
        private const val COLUMN_LONGITUDE = "longitude"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_CREATOR_ID = "creator_id"
        private const val COLUMN_CREATOR_USERNAME = "creator_username"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var query = "CREATE TABLE $TABLE_FAVORITE_CAMP (" +
                "$COLUMN_ID TEXT PRIMARY KEY," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_LOCATION TEXT," +
                "$COLUMN_ADDRESS TEXT," +
                "$COLUMN_PRICE INT," +
                "$COLUMN_DESCRIPTION TEXT," +
                "$COLUMN_LONGITUDE REAL," +
                "$COLUMN_LATITUDE REAL," +
                "$COLUMN_CREATOR_ID TEXT," +
                "$COLUMN_CREATOR_USERNAME TEXT," +
                "$COLUMN_IMAGE_URL TEXT)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, v1: Int, v2: Int) {
        val query = "DROP TABLE IF EXISTS $TABLE_FAVORITE_CAMP"
        db?.execSQL(query)
        onCreate(db)
    }

    // METHOD INSERT
    fun insertCampground(campground: Campground) {
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
        db.insert(TABLE_FAVORITE_CAMP, null, values)
        db.close()
    }

    // METHOD UPDATE
    fun updateCampground(campground: Campground) {
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
        db.update(TABLE_FAVORITE_CAMP, values, whereClause, whereArgs)
        db.close()
    }

    // METHOD DELETE
    fun deleteCampground(id: UUID) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID =?"
        val whereArgs = arrayOf(id.toString())
        db.delete(TABLE_FAVORITE_CAMP, whereClause, whereArgs)
        db.close()
    }

    // METHOD GET ALL
    fun getAllCampgrounds() : List<Campground> {
        val campgrounds = ArrayList<Campground>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_FAVORITE_CAMP"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val campground = Campground(
                    id = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                    address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                    price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE)),
                    latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                    creatorId = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATOR_ID))),
                    creatorUsername = cursor.getString(cursor.getColumnIndexOrThrow(
                        COLUMN_CREATOR_USERNAME)),
                )
                campgrounds.add(campground)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return  campgrounds
    }

    // METHOD GET BY ID
    fun findCampgroundById(id: UUID) : Campground? {
        var campground: Campground? = null
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_FAVORITE_CAMP WHERE $COLUMN_ID = '${id.toString()}'"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            campground = Campground(
                id = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE)),
                latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                creatorId = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATOR_ID))),
                creatorUsername = cursor.getString(cursor.getColumnIndexOrThrow(
                    COLUMN_CREATOR_USERNAME)),
            )
        }
        cursor.close()
        db.close()
        return campground
    }
}