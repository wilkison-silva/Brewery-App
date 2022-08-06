package com.ciandt.ciandtbrewery.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ciandt.ciandtbrewery.R
import com.ciandt.ciandtbrewery.model.Brewery

class EvaluatedAdapter(private val breweriesList: List<Brewery>) : RecyclerView.Adapter<EvaluatedAdapter.ViewHolder>()  {

    var onClickItem: (breweryId: String) -> Unit = {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EvaluatedAdapter.ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.card_item_brewery_evaluate,
            parent,
            false
        )
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: EvaluatedAdapter.ViewHolder, position: Int) {
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
            brewery.name?.let {
                initialLetterBreweryTextView.text = brewery.name[0].toString()
                nameBreweryTextView.text = brewery.name
            }
            rateBreweryTextView.text = brewery.average?.toString()

            itemView.setOnClickListener {
                onClickItem(brewery.id)
            }
        }
    }
}