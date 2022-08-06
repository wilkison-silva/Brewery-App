package com.ciandt.ciandtbrewery.domain

import com.ciandt.ciandtbrewery.R
import com.ciandt.ciandtbrewery.database.model.BreweryData
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.repository.BreweryRepository

class FavoriteUseCaseImpl(private val breweryRepository: BreweryRepository): FavoriteUseCase {

    override suspend fun insertOrDeleteFavorite(brewery: Brewery): FavoriteResult {
        val favorites = breweryRepository.getAllFavoriteBrewery()
        val hasFavorite = favorites.any { breweryData ->
            breweryData.id == brewery.id }
        if (hasFavorite){
            breweryRepository.deleteFavoriteBrewery(brewery.id)
            return FavoriteResult(
                false,
                R.drawable.ic_heart
            )
        }
        val breweryData = BreweryData(
            id = brewery.id,
            name = brewery.name,
            average = brewery.average,
            breweryType = brewery.breweryType
        )
        breweryRepository.insertFavoriteBrewery(breweryData)
        return FavoriteResult(
            true,
            R.drawable.ic_full_heart
        )
    }
}