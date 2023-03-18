package com.commcode.happyplaces.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.commcode.happyplaces.databinding.ItemHappyPlaceBinding
import com.commcode.happyplaces.models.HappyPlaceModel

class HappyPlacesAdapter(
    private val list: ArrayList<HappyPlaceModel>,
) : RecyclerView.Adapter<HappyPlacesAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemHappyPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivHappyPlace
        val title = binding.tvTitle
        val description = binding.tvDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHappyPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        holder.image.setImageURI(Uri.parse(model.image))
        holder.title.text = model.title
        holder.description.text = model.description
    }

}