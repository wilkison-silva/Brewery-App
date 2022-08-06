package com.ciandt.ciandtbrewery.domain

import com.ciandt.ciandtbrewery.model.Brewery

interface FavoriteUseCase {

    suspend fun insertOrDeleteFavorite(brewery: Brewery): FavoriteResult
}