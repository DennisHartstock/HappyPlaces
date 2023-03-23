package com.commcode.happyplaces.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.commcode.happyplaces.adapters.HappyPlacesAdapter
import com.commcode.happyplaces.database.DatabaseHandler
import com.commcode.happyplaces.databinding.ActivityMainBinding
import com.commcode.happyplaces.models.HappyPlaceModel

class MainActivity : AppCompatActivity(), HappyPlacesAdapter.OnItemClickListener {

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

    override fun onResume() {
        super.onResume()
        getHappyPlacesListFromLocalDb()
    }

    private fun setupHappyPlacesRecyclerView(list: ArrayList<HappyPlaceModel>) {
        binding.rvHappyPlacesList.setHasFixedSize(true)
        binding.rvHappyPlacesList.adapter = HappyPlacesAdapter(list, this)
    }

    private fun getHappyPlacesListFromLocalDb() {
        val dbHandler = DatabaseHandler(this)
        val happyPlacesList = dbHandler.getHappyPlacesList()

        if (happyPlacesList.isNotEmpty()) {
            for (happyPlace in happyPlacesList) {
                binding.tvEmptyList.visibility = View.GONE
                binding.rvHappyPlacesList.visibility = View.VISIBLE
                setupHappyPlacesRecyclerView(happyPlacesList)
            }
        } else {
            binding.tvEmptyList.visibility = View.VISIBLE
            binding.rvHappyPlacesList.visibility = View.GONE
        }
    }

    override fun onItemClick(position: Int, model: HappyPlaceModel) {
        val intent = Intent(this, HappyPlaceDetailActivity::class.java)
        intent.putExtra(EXTRA_HAPPY_PLACE_DETAILS, model)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_HAPPY_PLACE_DETAILS = "happy place details"
    }
}