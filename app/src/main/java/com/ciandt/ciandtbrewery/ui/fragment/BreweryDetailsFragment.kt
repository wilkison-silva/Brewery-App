package com.ciandt.ciandtbrewery.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.ciandt.ciandtbrewery.R
import com.ciandt.ciandtbrewery.extensions.showMessage
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.model.Photo
import com.ciandt.ciandtbrewery.ui.dialog.ChooseImageDialogFragment
import com.ciandt.ciandtbrewery.ui.dialog.EvaluationDialogFragment
import com.ciandt.ciandtbrewery.ui.recyclerview.adapter.BreweryPhotosAdapter
import com.ciandt.ciandtbrewery.ui.viewmodel.BreweryDetailsViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.divider.MaterialDivider
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

private const val MAX_EVALUATIONS = 500

class BreweryDetailsFragment : Fragment() {

    private val navController by lazy {
        findNavController()
    }

    private val breweryDetailsViewModel: BreweryDetailsViewModel by inject()
    private val arguments by navArgs<BreweryDetailsFragmentArgs>()
    private lateinit var uploadImageBtn: MaterialButton
    private lateinit var favoriteImageBtn: ImageButton
    private val breweryId by lazy {
        arguments.breweryId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.brewery_details_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
        getBreweryById()
        observeBrewery()
        showMap()
        getMyEvaluations(getString(R.string.email_example))
        observeMyEvaluations()
        showMap()
        observeBreweryPhotos()
        updateBreweryPhotos()
    }

    private fun updateBreweryPhotos() {
        breweryDetailsViewModel.updateBreweryPhotos(breweryId)
    }

    private fun observeBreweryPhotos() {
        breweryDetailsViewModel.breweryPhotosSuccess.observe(viewLifecycleOwner) { photosList ->
            showReceivedPhotos(photosList)
        }
    }

    private fun getMyEvaluations(email: String) {
        lifecycleScope.launch {
            breweryDetailsViewModel.getMyEvaluationsByEmail(email)
        }
    }

    private fun observeMyEvaluations() {
        breweryDetailsViewModel.myEvaluationsByEmailSuccess.observe(viewLifecycleOwner) {
            breweryDetailsViewModel.checkEvaluatedBrewery(breweryId)
            breweryDetailsViewModel.isEvaluatedBrewery.observe(viewLifecycleOwner) {
                showEvaluateButton(it)
            }
        }
    }

    private fun showEvaluateButton(resultEvaluation: Boolean) {
        view?.apply {
            val evaluateMaterialButton: MaterialButton = findViewById(R.id.mbAvaliation)
            val alreadyEvaluateImageView: ImageView = findViewById(R.id.iv_refresh_screen)
            val alreadyEvaluateTextView: TextView = findViewById(R.id.tv_success_msg)
            if (resultEvaluation) {
                evaluateMaterialButton.visibility = GONE
                alreadyEvaluateImageView.visibility = VISIBLE
                alreadyEvaluateTextView.visibility = VISIBLE
            } else {
                evaluateMaterialButton.visibility = VISIBLE
                alreadyEvaluateImageView.visibility = GONE
                alreadyEvaluateTextView.visibility = GONE
            }
            evaluateMaterialButton.setOnClickListener {
                showEvaluationDialog()
            }
        }
    }

    private fun observeBrewery() {
        breweryDetailsViewModel.breweryByIdSuccess.observe(viewLifecycleOwner) { brewery ->
            bindComponents(brewery)
        }
        breweryDetailsViewModel.favoriteNotify.observe(viewLifecycleOwner) {
            if (it) {
                favoriteImageBtn.setImageResource(R.drawable.ic_full_heart)
            } else {
                favoriteImageBtn.setImageResource(R.drawable.ic_heart)
            }
        }
    }

    private fun bindComponents(data: Brewery) {
        view?.apply {
            data.name?.let {
                val nameBreweryTextView: TextView = findViewById(R.id.tvNameBreweryDetails)
                val letterBreweryTextView: TextView = findViewById(R.id.tvLetterBreweryDetails)
                nameBreweryTextView.text = data.name
                letterBreweryTextView.text = data.name[0].toString()
            }
            uploadImageBtn = findViewById(R.id.mbAddPhoto)
            val breweryTypeDetailsTextView: TextView = findViewById(R.id.tvBreweryTypeDetails)
            val addressDetailsTextView: TextView = findViewById(R.id.tvAddressDetails)
            val webSiteTextView: TextView = findViewById(R.id.tvWebSite)
            val averageDetailsTextView: TextView = findViewById(R.id.tvAverageDetails)
            val averageRatingBar: RatingBar = findViewById(R.id.ratingBarDetails)
            breweryTypeDetailsTextView.text = data.breweryType
            addressDetailsTextView.text = breweryDetailsViewModel.getBreweryAddress(data)
            webSiteTextView.text = data.websiteUrl
            findViewById<TextView>(R.id.tvSizeEvalutionsDetails).apply {
                data.sizeEvaluations?.let {
                    text = if (it > MAX_EVALUATIONS) {
                        getString(R.string.plus_500)
                    } else {
                        it.toString()
                    }
                }

            }

            findViewById<TextView>(R.id.tvAvaliationDetails).apply {
                data.sizeEvaluations?.let {
                    text = if (it > 1) {
                        getString(R.string.plural_evaluation)
                    } else {
                        getString(R.string.singular_evaluation)
                    }
                }
            }

            averageDetailsTextView.text = data.average.toString()
            averageRatingBar.rating = data.average.toFloat()
            favoriteImageBtn = findViewById(R.id.ibLikeDetails)
            favoriteImageBtn.setOnClickListener {
                setFavorite()
            }
            uploadImageBtn.setOnClickListener {
                showChooseImageDialog()
            }
        }
    }

    private fun setFavorite() {
        breweryDetailsViewModel.insertOrDeleteFavorite()
    }

    private fun getBreweryById() {
        breweryDetailsViewModel.getBreweryById(breweryId)
    }

    private fun setupButtons() {
        view?.apply {
            val returnImageButton: ImageButton = findViewById(R.id.ibReturn)
            returnImageButton.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    private fun showEvaluationDialog() {
        EvaluationDialogFragment(breweryId) {
            getMyEvaluations(getString(R.string.email_example))
            observeMyEvaluations()
        }
            .show(childFragmentManager, "Dialog")
    }

    private fun showMap() {
        breweryDetailsViewModel.breweryByIdSuccess.observe(viewLifecycleOwner) { brewery ->
            addActionToTextViewShowMap(brewery)
        }
    }

    private fun addActionToTextViewShowMap(data: Brewery) {
        view?.apply {
            val showMapTextView: TextView = findViewById(R.id.tvMapDetails)
            val showMapIcon: ImageView = findViewById(R.id.ivMapDetails)
            val showMapDivider: MaterialDivider = findViewById(R.id.materialDividerMap)
            if (breweryDetailsViewModel.getUriString(data) == null) {
                showMapTextView.visibility = GONE
                showMapIcon.visibility = GONE
                showMapDivider.visibility = GONE
            } else {
                Uri.parse(breweryDetailsViewModel.getUriString(data)).let {
                    val mapIntent = Intent(Intent.ACTION_VIEW, it)
                    mapIntent.setPackage(getString(R.string.googleMapsPackage))
                    showMapTextView.setOnClickListener { startActivity(mapIntent) }
                }
            }
            if (breweryDetailsViewModel.hasAddressLink(data)) {
                findViewById<ImageView>(R.id.ivIconWorld).visibility = VISIBLE
                findViewById<TextView>(R.id.tvWebSite).visibility = VISIBLE
                findViewById<MaterialDivider>(R.id.mdWorld).visibility = VISIBLE
            } else {
                findViewById<ImageView>(R.id.ivIconWorld).visibility = GONE
                findViewById<TextView>(R.id.tvWebSite).visibility = GONE
                findViewById<MaterialDivider>(R.id.mdWorld).visibility = GONE
            }
        }
    }

    private fun showReceivedPhotos(photosList: List<Photo>) {
        val photoRecyclerView = requireView().findViewById<RecyclerView>(R.id.image_recycleview)
        val photoRecyclerViewAdapter = BreweryPhotosAdapter(photosList)

        photoRecyclerView.adapter = photoRecyclerViewAdapter

    }

    private fun showChooseImageDialog() {
        ChooseImageDialogFragment(
            breweryId = breweryId,
            onSuccess = {
                view?.let {
                    showMessage(
                        it,
                        getString(R.string.dialog_camera_success),
                        getString(R.string.close)
                    )
                    updateBreweryPhotos()
                }
            },
            onError = {
                view?.let {
                    showMessage(
                        it,
                        getString(R.string.dialog_camera_error),
                        getString(R.string.close)
                    )
                }
            }
        ).show(childFragmentManager, "Dialog")
    }
}