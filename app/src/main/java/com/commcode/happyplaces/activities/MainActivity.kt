package com.commcode.happyplaces.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.commcode.happyplaces.database.DatabaseHandler
import com.commcode.happyplaces.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.fabAddHappyPlace.setOnClickListener {
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivity(intent)
        }

        getHappyPlacesListFromLocalDb()
    }

    private fun getHappyPlacesListFromLocalDb() {
        val dbHandler = DatabaseHandler(this)
        val happyPlacesList = dbHandler.getHappyPlacesList()

        if (happyPlacesList.isNotEmpty()) {
            for (happyPlace in happyPlacesList) {
                Log.i("Title", happyPlace.title)
            }
        }
    }
}