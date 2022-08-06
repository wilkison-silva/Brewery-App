package com.ciandt.ciandtbrewery.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ciandt.ciandtbrewery.database.model.BreweryData
import com.ciandt.ciandtbrewery.domain.FavoriteResult
import com.ciandt.ciandtbrewery.domain.FavoriteUseCase
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.repository.BreweryRepository
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteBreweryViewModelTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockedBreweryRepository = mockk<BreweryRepository>()
    private val mockedFavoriteUseCase = mockk<FavoriteUseCase>()
    private val favoriteBreweryViewModel =
        FavoriteBreweryViewModel(mockedBreweryRepository, mockedFavoriteUseCase)

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When favoriteBreweryViewModel calls getAllFavoriteBrewery() it Should change value of favoriteBrewerySuccess from favoriteBreweryViewModel`() =
        runTest {
            favoriteBreweryViewModel.favoriteBreweriesSuccess.observeForever(
                favoriteBrewerySuccessObserver
            )
            favoriteBreweryViewModel.favoriteBreweriesEmpty.observeForever(
                favoriteBreweriesEmptyObserver
            )
            coEvery {
                mockedBreweryRepository.getAllFavoriteBrewery()
            }.returns(mockedBreweryData)

            val breweryList = mockedBreweryList
            favoriteBreweryViewModel.updateAllFavorite()

            coVerify { mockedBreweryRepository.getAllFavoriteBrewery() }
            coVerify {
                favoriteBrewerySuccessObserver.onChanged(breweryList)
            }
            coVerify { favoriteBreweriesEmptyObserver wasNot called }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When favoriteBreweryViewModel calls getAllFavoriteBrewery() it Should not change value of favoriteBrewerySuccess from favoriteBreweryViewModel`() =
        runTest {
            favoriteBreweryViewModel.favoriteBreweriesSuccess.observeForever(
                favoriteBrewerySuccessObserver
            )
            favoriteBreweryViewModel.favoriteBreweriesEmpty.observeForever(
                favoriteBreweriesEmptyObserver
            )
            coEvery {
                mockedBreweryRepository.getAllFavoriteBrewery()
            }.returns(mockedBreweryDataEmpty)

            favoriteBreweryViewModel.updateAllFavorite()

            coVerify { mockedBreweryRepository.getAllFavoriteBrewery() }
            coVerify {
                favoriteBrewerySuccessObserver wasNot called
            }
            coVerify { favoriteBreweriesEmptyObserver.onChanged(true) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `insertOrDeleteFavorite()`() {
        coEvery {
            mockedFavoriteUseCase.insertOrDeleteFavorite(mockedBrewery)
        }.returns(mockedFavoriteResult)

        favoriteBreweryViewModel.insertOrDeleteFavorite(mockedBrewery)

        coVerify { mockedFavoriteUseCase.insertOrDeleteFavorite(mockedBrewery) }
    }

    private val mockedBreweryData: List<BreweryData> = listOf(
        BreweryData(
            id = "1",
            name = "Brewery Test",
            average = 5.0,
            breweryType = null
        )
    )

    private val mockedBreweryList: List<Brewery> = listOf(
        Brewery(
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
    )

    private val mockedBrewery = Brewery(
        "1",
        "Brewery Test",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        5.0,
        5,
        null
    )

    private val mockedFavoriteResult = FavoriteResult(
        isFavorite = true,
        icon = 0
    )

    private val mockedBreweryDataEmpty: List<BreweryData> = listOf()

    private val favoriteBrewerySuccessObserver = mockk<Observer<List<Brewery>>>(relaxed = true)
    private val favoriteBreweriesEmptyObserver = mockk<Observer<Boolean>>(relaxed = true)
}