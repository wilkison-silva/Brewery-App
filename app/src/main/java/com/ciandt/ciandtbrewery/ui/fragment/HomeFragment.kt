package com.ciandt.ciandtbrewery.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ciandt.ciandtbrewery.R
import com.ciandt.ciandtbrewery.extensions.checkInternetConnection
import com.ciandt.ciandtbrewery.extensions.showMessage
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.ui.recyclerview.adapter.SearchRecyclerViewAdapter
import com.ciandt.ciandtbrewery.ui.recyclerview.adapter.TopTenViewAdapter
import com.ciandt.ciandtbrewery.ui.viewmodel.HomeViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class HomeFragment : Fragment() {
    private val navController by lazy {
        findNavController()
    }

    private val homeViewModel: HomeViewModel by inject()
    private lateinit var searchImageButton: ImageButton
    private lateinit var cityNameTextInputEditText: TextInputEditText
    private lateinit var layoutSearchRecyclerView: ConstraintLayout
    private lateinit var layoutNothingTyped: ConstraintLayout
    private lateinit var layoutBlankResult: ConstraintLayout
    private lateinit var layoutTopTenView: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.home_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents(view)
        setupSearchImageButton()
        updateBreweriesByCity()
        updateBreweryTopTen()
        observeBreweriesByCityStatus()
        observeBreweryTopTenStatus()
        observeCityTyped()
    }

    private fun updateBreweryTopTen() {
        lifecycleScope.launch {
            homeViewModel.updateBreweriesTopTen()
        }
    }

    private fun observeBreweryTopTenStatus() {
        homeViewModel.breweriesTopTenSuccess.observe(viewLifecycleOwner) { breweriesTopTenList ->
            showReceivedItemsBreweryTopTen(breweriesTopTenList)
        }
    }

    private fun setupSearchImageButton() {
        searchImageButton.setOnClickListener {
            performSearch()
        }
    }

    private fun performSearch() {
        if (checkInternetConnection(requireContext())) {
            val city = cityNameTextInputEditText.text.toString()
            homeViewModel.updateCityTyped(city)
        } else {
            createSnackBarNoInternetConnection()
        }
    }

    private fun updateBreweriesByCity() {
        homeViewModel.cityTyped.value?.let { cityTyped ->
            lifecycleScope.launch {
                homeViewModel.updateBreweriesByCity(cityTyped)
            }
        }
    }

    private fun observeCityTyped() {
        homeViewModel.cityTyped.observe(viewLifecycleOwner) { cityTyped ->
            lifecycleScope.launch {
                homeViewModel.updateBreweriesByCity(cityTyped)
            }
        }
    }

    private fun observeBreweriesByCityStatus() {
        homeViewModel.breweriesByCitySuccess.observe(viewLifecycleOwner) { breweriesByCityList ->
            showReceivedItemsBreweryByCity(breweriesByCityList)
            layoutTopTenView.visibility = GONE
            layoutSearchRecyclerView.visibility = VISIBLE
            layoutNothingTyped.visibility = GONE
            layoutBlankResult.visibility = GONE
        }
        homeViewModel.breweriesByCityErrorNothingTyped.observe(viewLifecycleOwner) {
            layoutTopTenView.visibility = GONE
            layoutSearchRecyclerView.visibility = GONE
            layoutNothingTyped.visibility = VISIBLE
            layoutBlankResult.visibility = GONE
        }
        homeViewModel.breweriesByCityNoBreweriesFound.observe(viewLifecycleOwner) {
            layoutTopTenView.visibility = GONE
            layoutSearchRecyclerView.visibility = GONE
            layoutNothingTyped.visibility = GONE
            layoutBlankResult.visibility = VISIBLE
        }
    }

    private fun initComponents(view: View) {
        searchImageButton = view.findViewById(R.id.ivButtonSearch)
        cityNameTextInputEditText = view.findViewById(R.id.tietSearchLocal)
        layoutSearchRecyclerView = view.findViewById(R.id.layout_search_recyclerview)
        layoutNothingTyped = view.findViewById(R.id.cl_layout_nothing_typed)
        layoutBlankResult = view.findViewById(R.id.cl_layout_blank_result)
        layoutTopTenView = view.findViewById(R.id.clTopTen)
        setupFavoriteButton()
        setupEvaluatedButton()
        setupCityNameTextInputEditText()
    }

    private fun setupCityNameTextInputEditText() {
        cityNameTextInputEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
            }
            true
        }
    }

    private fun setupFavoriteButton() {
        val favoriteButton = requireView().findViewById<ImageView>(R.id.ivHeart)
        favoriteButton.setOnClickListener {
            val directions = HomeFragmentDirections.actionHomeFragmentToFavoriteBreweriesFragment()
            navController.navigate(directions)
        }
    }

    private fun setupEvaluatedButton() {
        val evaluatedButton = requireView().findViewById<ImageView>(R.id.ivStar)
        evaluatedButton.setOnClickListener {
            val directions = HomeFragmentDirections.actionHomeFragmentToEvaluationFragment()
            navController.navigate(directions)
        }
    }

    private fun showReceivedItemsBreweryByCity(breweriesList: List<Brewery>) {
        val searchRecyclerView =
            requireView().findViewById<RecyclerView>(R.id.evaluated_recyclerview)
        val searchRecyclerViewAdapter = SearchRecyclerViewAdapter(breweriesList, false)
        searchRecyclerViewAdapter.onClickItem = { breweryId: String ->
            tryGoToBreweryDetailsFragment(breweryId)
        }
        searchRecyclerViewAdapter.onClickFavorite = { brewery ->
            homeViewModel.insertOrDeleteFavorite(brewery)
        }

        searchRecyclerView.adapter = searchRecyclerViewAdapter

        val totalResultsTextView = requireView()
            .findViewById<TextView>(R.id.total_results_search_recycler_view_item)
        totalResultsTextView.text = breweriesList.size.toString()

        view?.apply {
            findViewById<TextView>(R.id.total_results_search_recycler_view_item_results).apply {
                text = if (breweriesList.size > 1) {
                    getString(R.string.plural_results)
                } else {
                    getString(R.string.singular_result)
                }
            }
        }

    }

    private fun showReceivedItemsBreweryTopTen(breweriesList: List<Brewery>) {
        val searchRecyclerView = requireView().findViewById<RecyclerView>(R.id.rvTopTen)
        val topTenViewAdapter = TopTenViewAdapter(breweriesList)
        topTenViewAdapter.onClickItem = { breweryId: String ->
            tryGoToBreweryDetailsFragment(breweryId)
        }
        searchRecyclerView.adapter = topTenViewAdapter
    }

    private fun tryGoToBreweryDetailsFragment(breweryId: String) {
        if (checkInternetConnection(requireContext())) {
            goToBreweryDetailsFragment(breweryId)
        } else {
            createSnackBarNoInternetConnection()
        }
    }

    private fun createSnackBarNoInternetConnection() {
        view?.let {
            showMessage(it, getString(R.string.no_internet_connection), getString(R.string.close))
        }
    }

    private fun goToBreweryDetailsFragment(breweryId: String) {
        val directions = HomeFragmentDirections
            .actionHomeFragmentToBreweryDetailsFragment(breweryId)
        navController.navigate(directions)
    }
}
