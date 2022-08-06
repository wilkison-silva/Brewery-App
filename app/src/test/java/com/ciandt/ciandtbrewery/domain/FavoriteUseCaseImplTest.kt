package com.ciandt.ciandtbrewery.domain

import com.ciandt.ciandtbrewery.database.model.BreweryData
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.repository.BreweryRepository
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteUseCaseImplTest() {

    private val mockedBreweryRepository = mockk<BreweryRepository>()
    private val favoriteUseCaseImpl = FavoriteUseCaseImpl(mockedBreweryRepository)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When favoriteUseCaseImpl calls deleteFavoriteBrewery() it should return a FavoriteResult with isFavorite=true`() =
        runTest {
            coEvery {
                mockedBreweryRepository.getAllFavoriteBrewery()
            }.returns(mockedListBreweryData)
            coEvery {
                mockedBreweryRepository.deleteFavoriteBrewery(mockedBrewery.id)
            }.returns(Unit)

            val favoriteResult = favoriteUseCaseImpl.insertOrDeleteFavorite(mockedBrewery)

            coVerify { mockedBreweryRepository.getAllFavoriteBrewery() }
            coVerify { mockedBreweryRepository.deleteFavoriteBrewery(mockedBrewery.id) }
            assertEquals(mockedFavoriteResultFalse.isFavorite, favoriteResult.isFavorite)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When favoriteUseCaseImpl calls deleteFavoriteBrewery() it should return a FavoriteResult with isFavorite=false`() =
        runTest {
            coEvery {
                mockedBreweryRepository.getAllFavoriteBrewery()
            }.returns(mockedListBreweryData)
            coEvery {
                mockedBreweryRepository.insertFavoriteBrewery(mockedBreweryData)
            }.returns(Unit)

            val favoriteResult = favoriteUseCaseImpl.insertOrDeleteFavorite(mockedBrewery2)

            coVerify { mockedBreweryRepository.getAllFavoriteBrewery() }
            coVerify { mockedBreweryRepository.insertFavoriteBrewery(mockedBreweryData) }
            assertEquals(mockedFavoriteResultTrue.isFavorite, favoriteResult.isFavorite)
        }

    private val mockedBrewery = Brewery(
        id = "1",
        name = "Brewery Test",
        average = 5.0,
        breweryType = null,
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

    private val mockedBrewery2 = Brewery(
        id = "2",
        name = "Brewery Test2",
        average = 5.0,
        breweryType = null,
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

    private val mockedBreweryData = BreweryData(
        id = "2",
        name = "Brewery Test2",
        average = 5.0,
        breweryType = null
    )

    private val mockedListBreweryData: List<BreweryData> = listOf(
        BreweryData(
            id = "1",
            name = "Brewery Test",
            average = 5.0,
            breweryType = null
        )
    )

    private val mockedFavoriteResultFalse = FavoriteResult(
        isFavorite = false,
        icon = 0
    )

    private val mockedFavoriteResultTrue = FavoriteResult(
        isFavorite = true,
        icon = 0
    )

}