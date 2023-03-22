package com.commcode.happyplaces.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.commcode.happyplaces.databinding.ActivityHappyPlaceDetailBinding

class HappyPlaceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHappyPlaceDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHappyPlaceDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivDetailHappyPlace.clipToOutline = true
    }
}