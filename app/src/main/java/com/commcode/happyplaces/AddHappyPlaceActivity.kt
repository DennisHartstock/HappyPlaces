package com.commcode.happyplaces

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.commcode.happyplaces.databinding.ActivityAddHappyPlaceBinding

class AddHappyPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddHappyPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}