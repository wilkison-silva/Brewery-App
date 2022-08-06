package com.ciandt.ciandtbrewery.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ciandt.ciandtbrewery.R
import com.ciandt.ciandtbrewery.database.model.BreweryData
import com.ciandt.ciandtbrewery.domain.FavoriteUseCase
import com.ciandt.ciandtbrewery.extensions.emitSuccess
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.model.Photo
import com.ciandt.ciandtbrewery.repository.BreweryRepository
import kotlinx.coroutines.launch

class BreweryDetailsViewModel(
    private val breweryRepository: BreweryRepository,
    private val favoriteUseCase: FavoriteUseCase
) : ViewModel() {

    private val geoStreet = "geo: 0, 0?q="
    private val geoCoordinates = "geo:"

    private val _favoriteNotify = MutableLiveData<Boolean>()
    val favoriteNotify: LiveData<Boolean>
        get() = _favoriteNotify

    private val _breweryByIdSuccess = MutableLiveData<Brewery>()
    val breweryByIdSuccess: LiveData<Brewery>
        get() = _breweryByIdSuccess

    fun getBreweryById(breweryId: String) {
        viewModelScope.launch {
            val resourceRepository = breweryRepository.getBreweryById(breweryId)
            if (resourceRepository.error == null)
                emitSuccess(resourceRepository, _breweryByIdSuccess)
            if (favoriteStatusCheck(breweryId)) {
                _favoriteNotify.postValue(true)
            } else {
                _favoriteNotify.postValue(false)
            }
        }
    }

    private val _myEvaluationsByEmailSuccess = MutableLiveData<List<Brewery>>()
    val myEvaluationsByEmailSuccess: LiveData<List<Brewery>>
        get() = _myEvaluationsByEmailSuccess

    suspend fun getMyEvaluationsByEmail(email: String) {
        val resourceRepository = breweryRepository.getMyEvaluations(email)
        if (resourceRepository.error == null)
            emitSuccess(resourceRepository, _myEvaluationsByEmailSuccess)
    }

    private val _isEvaluatedBrewery = MutableLiveData<Boolean>()
    val isEvaluatedBrewery: LiveData<Boolean>
        get() = _isEvaluatedBrewery

    fun checkEvaluatedBrewery(breweryId: String) {
        var isEvaluated = false
        _myEvaluationsByEmailSuccess.value?.forEach {
            if (it.id == breweryId) {
                isEvaluated = true
            }
        }
        _isEvaluatedBrewery.postValue(isEvaluated)
    }

    fun getUriString(brewery: Brewery): String? {
        return if (brewery.street != null) {

            "$geoStreet ${brewery.street}, ${brewery.city}, ${brewery.state}"

        } else if (brewery.latitude != null && brewery.longitude != null) {

            "$geoCoordinates ${brewery.latitude}, ${brewery.longitude}"

        } else {
            null
        }
    }

    fun getBreweryAddress(brewery: Brewery): String? {
        if (brewery.street == null) {
            return "${brewery.city}, ${brewery.state}"
        }
        return "${brewery.street}, ${brewery.city}, ${brewery.state}"
    }

    private suspend fun favoriteStatusCheck(id: String): Boolean {
        val idList = breweryRepository.getAllFavoriteBrewery().map {
            it.id
        }
        return idList.contains(id)
    }

    fun insertOrDeleteFavorite() {
        viewModelScope.launch {
            val brewery = breweryByIdSuccess.value
            brewery?.let {
                val result = favoriteUseCase.insertOrDeleteFavorite(brewery)
                _favoriteNotify.postValue(result.isFavorite)
            }
        }
    }

    private val _breweryPhotosSuccess = MutableLiveData<List<Photo>>()
    val breweryPhotosSuccess: LiveData<List<Photo>>
        get() = _breweryPhotosSuccess

    fun updateBreweryPhotos(breweryId: String) {
        viewModelScope.launch {
            val resourceRepository = breweryRepository.getBreweryPhotos(breweryId)
            if (resourceRepository.error == null)
                emitSuccess(resourceRepository, _breweryPhotosSuccess)
        }
    }

    fun hasAddressLink(brewery: Brewery): Boolean{
        if (brewery.websiteUrl == null) {
            return false
        }
        if (brewery.websiteUrl.isBlank()) {
            return false
        }
        return true
    }
}
