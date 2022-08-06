package com.ciandt.ciandtbrewery.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ciandt.ciandtbrewery.R
import com.ciandt.ciandtbrewery.extensions.tryLoadImage
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.model.Photo

class BreweryPhotosAdapter(
    private val photosList: List<Photo>
) : RecyclerView.Adapter<BreweryPhotosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BreweryPhotosAdapter.ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.item_image_brewery, parent, false
        )
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: BreweryPhotosAdapter.ViewHolder, position: Int) {
        val photo: Photo = photosList[position]
        holder.bind(photo)
    }

    override fun getItemCount(): Int {
        return photosList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(photo: Photo) {
            val breweryImage = itemView.findViewById<ImageView>(R.id.ivBrewery)
            breweryImage.tryLoadImage(photo.url)
        }
    }
}