package com.ciandt.ciandtbrewery.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ciandt.ciandtbrewery.R
import com.ciandt.ciandtbrewery.model.Brewery

class SearchRecyclerViewAdapter(
    private val breweriesList: List<Brewery>,
    private val isAllFavorite: Boolean
) : RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>() {

    var onClickItem: (breweryId: String) -> Unit = {}
    var onClickFavorite: (brewery: Brewery) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.search_recycler_view_item,
            parent,
            false
        )
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val brewery: Brewery = breweriesList[position]
        holder.bind(brewery)
    }

    override fun getItemCount(): Int {
        return breweriesList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(brewery: Brewery) {

            val initialLetterBreweryTextView =
                itemView.findViewById<TextView>(R.id.initial_letter_brewery_search_recycler_view_item)
            val nameBreweryTextView =
                itemView.findViewById<TextView>(R.id.name_brewery_search_recycler_view_item)
            val rateBreweryTextView =
                itemView.findViewById<TextView>(R.id.rate_brewery_search_recycler_view_item)
            val typeBreweryTextView =
                itemView.findViewById<TextView>(R.id.type_brewery_recycler_view_item)
            val ivLikeBrewery =
                itemView.findViewById<ImageView>(R.id.ivLikeBrewery)
            brewery.name?.let {
                initialLetterBreweryTextView.text = brewery.name[0].toString()
                nameBreweryTextView.text = brewery.name
            }
            rateBreweryTextView.text = brewery.average?.toString()
            typeBreweryTextView.text = brewery.breweryType.toString()
            if(isAllFavorite || brewery.isFavorite){
                ivLikeBrewery.setImageResource(R.drawable.ic_full_heart)
            }
            ivLikeBrewery.setOnClickListener {
                onClickFavorite(brewery)
            }
            itemView.setOnClickListener {
                onClickItem(brewery.id)
            }
        }
    }
}