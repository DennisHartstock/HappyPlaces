package com.commcode.happyplaces.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.commcode.happyplaces.models.HappyPlaceModel
import java.sql.SQLException

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "HappyPlacesDatabase"
        private const val TABLE_HAPPY_PLACES = "HappyPlacesTable"

        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_HAPPY_PLACES_TABLE =
            ("CREATE " + TABLE_HAPPY_PLACES + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TITLE + " TEXT,"
                    + KEY_IMAGE + " TEXT,"
                    + KEY_DESCRIPTION + " TEXT,"
                    + KEY_DATE + " TEXT,"
                    + KEY_LOCATION + " TEXT,"
                    + KEY_LATITUDE + " TEXT,"
                    + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(CREATE_HAPPY_PLACES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_HAPPY_PLACES")
        onCreate(db)
    }

    fun deleteHappyPlace(happyPlace: HappyPlaceModel): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_HAPPY_PLACES, KEY_ID + "=" + happyPlace.id, null)
        db.close()
        return result
    }

    fun addHappyPlace(happyPlace: HappyPlaceModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, happyPlace.title)
        contentValues.put(KEY_IMAGE, happyPlace.image)
        contentValues.put(KEY_DESCRIPTION, happyPlace.description)
        contentValues.put(KEY_DATE, happyPlace.date)
        contentValues.put(KEY_LOCATION, happyPlace.location)
        contentValues.put(KEY_LATITUDE, happyPlace.latitude)
        contentValues.put(KEY_LONGITUDE, happyPlace.longitude)

        val result = db.insert(TABLE_HAPPY_PLACES, null, contentValues)
        db.close()
        return result
    }

    fun updateHappyPlace(happyPlace: HappyPlaceModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, happyPlace.title)
        contentValues.put(KEY_IMAGE, happyPlace.image)
        contentValues.put(KEY_DESCRIPTION, happyPlace.description)
        contentValues.put(KEY_DATE, happyPlace.date)
        contentValues.put(KEY_LOCATION, happyPlace.location)
        contentValues.put(KEY_LATITUDE, happyPlace.latitude)
        contentValues.put(KEY_LONGITUDE, happyPlace.longitude)

        val result =
            db.update(TABLE_HAPPY_PLACES, contentValues, KEY_ID + "=" + happyPlace.id, null)
        db.close()
        return result
    }


    @SuppressLint("Range")
    fun getHappyPlacesList(): ArrayList<HappyPlaceModel> {
        val happyPlacesList = ArrayList<HappyPlaceModel>()
        val selectQuery = "SELECT * FROM $TABLE_HAPPY_PLACES"
        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val happyPlace = HappyPlaceModel(
                        id = cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        title = cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                        image = cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                        description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                        date = cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                        location = cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                        latitude = cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        longitude = cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))
                    )
                    happyPlacesList.add(happyPlace)
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return happyPlacesList
    }
}