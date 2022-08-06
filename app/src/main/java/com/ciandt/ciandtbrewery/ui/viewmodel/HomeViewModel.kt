package com.ciandt.ciandtbrewery.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ciandt.ciandtbrewery.domain.FavoriteUseCase
import com.ciandt.ciandtbrewery.extensions.emitError
import com.ciandt.ciandtbrewery.extensions.emitSuccess
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.repository.BreweryRepository
import com.ciandt.ciandtbrewery.repository.ErrorRepository.ERROR_NOTHING_TYPED
import com.ciandt.ciandtbrewery.repository.ErrorRepository.ERROR_NO_BREWERIES_FOUND
import com.ciandt.ciandtbrewery.repository.ResourceRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val breweryRepository: BreweryRepository,
    private val favoriteUseCase: FavoriteUseCase)
    : ViewModel() {

    private val _breweriesByCitySuccess = MutableLiveData<List<Brewery>>()
    val breweriesByCitySuccess: LiveData<List<Brewery>>
        get() = _breweriesByCitySuccess

    private val _breweriesByCityErrorNothingTyped = MutableLiveData<Boolean>()
    val breweriesByCityErrorNothingTyped: LiveData<Boolean>
        get() = _breweriesByCityErrorNothingTyped

    private val _breweriesByCityNoBreweriesFound = MutableLiveData<Boolean>()
    val breweriesByCityNoBreweriesFound: LiveData<Boolean>
        get() = _breweriesByCityNoBreweriesFound

    suspend fun updateBreweriesByCity(city: String) {

        val resourceRepository = breweryRepository.getBreweriesByCity(city)
        when (resourceRepository.error) {
            null -> {
                val favoriteList = getAllFavorites()
                val orderedByAverage = resourceRepository.data?.sortedByDescending { brewery ->
                    brewery.average
                }
                orderedByAverage?.forEach{ brewery ->
                    if (favoriteList.contains(brewery.id)){
                        brewery.isFavorite = true
                    }
                }
                val resourceRepositoryOrderedByAverage = ResourceRepository<List<Brewery>?>(data = orderedByAverage)
                emitSuccess(resourceRepositoryOrderedByAverage, _breweriesByCitySuccess)
            }
            ERROR_NOTHING_TYPED -> emitError(_breweriesByCityErrorNothingTyped)
            ERROR_NO_BREWERIES_FOUND -> emitError(_breweriesByCityNoBreweriesFound)
        }
    }

    private val _breweriesTopTenSuccess = MutableLiveData<List<Brewery>>()
    val breweriesTopTenSuccess: LiveData<List<Brewery>>
        get() = _breweriesTopTenSuccess

    suspend fun updateBreweriesTopTen() {
        val resourceRepository = breweryRepository.getBreweriesTopTen()
        if(resourceRepository.error == null)
            emitSuccess(resourceRepository, _breweriesTopTenSuccess)
    }

    fun insertOrDeleteFavorite(brewery: Brewery){
        viewModelScope.launch {
            favoriteUseCase.insertOrDeleteFavorite(brewery)
            cityTyped.value?.let {
                updateBreweriesByCity(it)
            }
        }
    }

    private suspend fun getAllFavorites(): List<String>{
        val favoriteList: List<String> = breweryRepository.getAllFavoriteBrewery().map {
                it.id
            }
        return favoriteList
    }

    private val _cityTyped = MutableLiveData<String>()
    val cityTyped: LiveData<String>
        get() = _cityTyped

    fun updateCityTyped(city: String){
        viewModelScope.launch {
            _cityTyped.postValue(city)
        }
    }
}