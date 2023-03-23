package com.commcode.happyplaces.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.commcode.happyplaces.databinding.ActivityHappyPlaceDetailBinding
import com.commcode.happyplaces.models.HappyPlaceModel

class HappyPlaceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHappyPlaceDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHappyPlaceDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivDetailHappyPlace.clipToOutline = true

        val happyPlaceModel =
            intent.getParcelableExtra(MainActivity.EXTRA_HAPPY_PLACE_DETAILS) as HappyPlaceModel?

        setupDetails(happyPlaceModel)
    }

    private fun setupDetails(model: HappyPlaceModel?) {
        binding.tvHappyPlaceTitle.text = model?.title
        binding.ivDetailHappyPlace.setImageURI(model?.image?.toUri())
        binding.tvDetailDescription.text = model?.description
        binding.tvDetailLocation.text = model?.location
    }
}