package com.commcode.happyplaces.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.commcode.happyplaces.activities.AddHappyPlaceActivity
import com.commcode.happyplaces.activities.MainActivity
import com.commcode.happyplaces.databinding.ItemHappyPlaceBinding
import com.commcode.happyplaces.models.HappyPlaceModel

class HappyPlacesAdapter(
    private val context: Context,
    private val list: ArrayList<HappyPlaceModel>,
    private val listener: OnItemClickListener,
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

        holder.itemView.setOnClickListener {
            listener.onItemClick(position, model)
        }
    }

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) {
        val intent = Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_HAPPY_PLACE_DETAILS, list[position])
        activity.startActivityForResult(
            intent,
            requestCode
        )
        notifyItemChanged(position)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, model: HappyPlaceModel)
    }
}