package com.ciandt.ciandtbrewery.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ciandt.ciandtbrewery.database.model.BreweryData
import com.ciandt.ciandtbrewery.domain.FavoriteUseCase
import com.ciandt.ciandtbrewery.extensions.emitSuccess
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.repository.BreweryRepository
import kotlinx.coroutines.launch

class FavoriteBreweryViewModel(
    private val breweryRepository: BreweryRepository,
    private val favoriteUseCase: FavoriteUseCase
) : ViewModel() {

    private val _favoriteBreweriesSuccess = MutableLiveData<List<Brewery>>()
    val favoriteBreweriesSuccess: LiveData<List<Brewery>>
        get() = _favoriteBreweriesSuccess

    private val _favoriteBreweriesEmpty = MutableLiveData<Boolean>()
    val favoriteBreweriesEmpty: LiveData<Boolean>
        get() = _favoriteBreweriesEmpty

    suspend fun updateAllFavorite() {
        val favoriteBreweryDataList = breweryRepository.getAllFavoriteBrewery()
        val breweryList = createBreweryList(favoriteBreweryDataList)
        val orderedByAverage = breweryList.sortedByDescending { brewery ->
            brewery.average
        }
        if (breweryList.isNotEmpty())
            _favoriteBreweriesSuccess.postValue(orderedByAverage)
        else
            _favoriteBreweriesEmpty.postValue(true)
    }

    private fun createBreweryList(breweryDataList: List<BreweryData>): List<Brewery> {
        var breweryList: MutableList<Brewery> = mutableListOf()
        breweryDataList.forEach { breweryDataList ->
            breweryList.add(
                Brewery(
                    id = breweryDataList.id,
                    name = breweryDataList.name,
                    average = breweryDataList.average,
                    breweryType = breweryDataList.breweryType,
                    street = null,
                    address2 = null,
                    address3 = null,
                    city = null,
                    state = null,
                    postalCode = null,
                    country = null,
                    latitude = null,
                    longitude = null,
                    websiteUrl = null,
                    phone = null,
                    sizeEvaluations = null,
                    photos = null

                )
            )
        }
        return breweryList
    }

    fun insertOrDeleteFavorite(brewery: Brewery) {
        viewModelScope.launch {
            favoriteUseCase.insertOrDeleteFavorite(brewery)
            updateAllFavorite()
        }
    }
}