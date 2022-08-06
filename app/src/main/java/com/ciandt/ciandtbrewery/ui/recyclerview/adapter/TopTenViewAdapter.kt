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

class TopTenViewAdapter(
    private val topBreweryList: List<Brewery>
) : RecyclerView.Adapter<TopTenViewAdapter.ViewHolder>() {

    var onClickItem: (breweryId: String) -> Unit = {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopTenViewAdapter.ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.item_brewery, parent, false
        )
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: TopTenViewAdapter.ViewHolder, position: Int) {
        val brewery: Brewery = topBreweryList[position]
        holder.bind(brewery)
    }

    override fun getItemCount(): Int {
        return topBreweryList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(brewery: Brewery) {
            val nameBrewery = itemView.findViewById<TextView>(R.id.tvCardBreweryName)
            val averageBrewery = itemView.findViewById<TextView>(R.id.tvCardAverage)
            val breweryType = itemView.findViewById<TextView>(R.id.tvBreweryType)
            val breweryImage = itemView.findViewById<ImageView>(R.id.ivBrewery)

            brewery.name.let {
                nameBrewery.text = brewery.name
            }
            averageBrewery.text = brewery.average.toString()
            breweryType.text = brewery.breweryType
            breweryImage.tryLoadImage(brewery.photos?.get(0))

            itemView.setOnClickListener {
                onClickItem(brewery.id)
            }
        }
    }
}