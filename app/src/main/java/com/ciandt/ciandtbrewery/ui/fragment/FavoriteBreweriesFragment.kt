package com.ciandt.ciandtbrewery.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ciandt.ciandtbrewery.R
import com.ciandt.ciandtbrewery.extensions.createConfirmationDialog
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.ui.recyclerview.adapter.SearchRecyclerViewAdapter
import com.ciandt.ciandtbrewery.ui.viewmodel.FavoriteBreweryViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FavoriteBreweriesFragment : Fragment() {

    private val navController by lazy {
        findNavController()
    }
    private val favoriteBreweryViewModel: FavoriteBreweryViewModel by inject()
    private lateinit var layoutNoFavorite: ConstraintLayout
    private lateinit var layoutFavoriteRecyclerView: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.favorite_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents(view)
        updateFavoriteBreweries()
        observeFavoriteBreweryListStatus()
    }

    private fun initComponents(view: View) {
        setupButtons()
        layoutNoFavorite = view.findViewById(R.id.cl_layout_no_favorite)
        layoutFavoriteRecyclerView = view.findViewById(R.id.clFavoriteBrewery)
    }

    private fun setupButtons() {
        view?.apply {
            val returnImageButton: ImageButton = findViewById(R.id.ibReturnFavorite)
            returnImageButton.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    private fun showFavoriteBreweries(breweriesList: List<Brewery>) {
        val searchRecyclerView =
            requireView().findViewById<RecyclerView>(R.id.favorite_recyclerview)
        val searchRecyclerViewAdapter = SearchRecyclerViewAdapter(breweriesList, true)
        searchRecyclerView.adapter = searchRecyclerViewAdapter
        val totalResultsTextView = requireView()
            .findViewById<TextView>(R.id.tvNumberResultsFavorite)

        totalResultsTextView.text = if (breweriesList.size > 1) {
            getString(R.string.title_total_result_plural, breweriesList.size)
        } else {
            getString(R.string.title_total_result_singular, breweriesList.size)
        }

        searchRecyclerViewAdapter.onClickFavorite = { brewery ->
            createConfirmationDialog(
                context = requireContext(),
                onConfirmation = {
                    favoriteBreweryViewModel.insertOrDeleteFavorite(brewery)
                }
            )
        }
    }

    private fun updateFavoriteBreweries() {
        lifecycleScope.launch {
            favoriteBreweryViewModel.updateAllFavorite()
        }
    }

    private fun observeFavoriteBreweryListStatus() {
        favoriteBreweryViewModel.favoriteBreweriesSuccess.observe(viewLifecycleOwner) {
            showFavoriteBreweries(it)
            layoutFavoriteRecyclerView.visibility = View.VISIBLE
            layoutNoFavorite.visibility = View.GONE
        }
        favoriteBreweryViewModel.favoriteBreweriesEmpty.observe(viewLifecycleOwner) {
            layoutFavoriteRecyclerView.visibility = View.GONE
            layoutNoFavorite.visibility = View.VISIBLE
        }
    }
}