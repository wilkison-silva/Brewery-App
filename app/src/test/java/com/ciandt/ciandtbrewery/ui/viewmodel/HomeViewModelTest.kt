package com.ciandt.ciandtbrewery.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ciandt.ciandtbrewery.database.model.BreweryData
import com.ciandt.ciandtbrewery.domain.FavoriteUseCase
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.repository.BreweryRepository
import com.ciandt.ciandtbrewery.repository.ErrorRepository
import com.ciandt.ciandtbrewery.repository.ResourceRepository
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

private const val CITY_EXAMPLE = "San Diego"

class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockedBreweryRepository = mockk<BreweryRepository>()
    private val mockedFavoriteUseCase = mockk<FavoriteUseCase>()
    private val homeViewModel = HomeViewModel(mockedBreweryRepository, mockedFavoriteUseCase)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When homeViewModel calls updateBreweriesByCity() it Should change value of breweriesByCitySuccess from homeViewModel`() =
        runTest {

            homeViewModel.breweriesByCitySuccess.observeForever(breweriesByCitySuccessObserver)
            homeViewModel.breweriesByCityErrorNothingTyped
                .observeForever(breweriesByCityErrorNothingTypedObserver)
            homeViewModel.breweriesByCityNoBreweriesFound
                .observeForever(breweriesByCityErrorNoBreweriesFoundObserver)
            coEvery {
                mockedBreweryRepository.getBreweriesByCity(CITY_EXAMPLE)
            }.returns(mockedResourceRepositorySuccess)
            coEvery {
                mockedBreweryRepository.getAllFavoriteBrewery()
            }.returns(mockedBreweryData)

            homeViewModel.updateBreweriesByCity(CITY_EXAMPLE)

            coVerify { mockedBreweryRepository.getBreweriesByCity(CITY_EXAMPLE) }
            coVerify {
                breweriesByCitySuccessObserver.onChanged(mockedResourceRepositorySuccess.data)
            }
            coVerify { breweriesByCityErrorNothingTypedObserver wasNot called }
            coVerify { breweriesByCityErrorNoBreweriesFoundObserver wasNot called }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When homeViewModel calls updateBreweriesByCity() it Should change value of breweriesByCityErrorNothingTyped from homeViewModel`() =
        runTest {

            homeViewModel.breweriesByCitySuccess.observeForever(breweriesByCitySuccessObserver)
            homeViewModel.breweriesByCityErrorNothingTyped
                .observeForever(breweriesByCityErrorNothingTypedObserver)
            homeViewModel.breweriesByCityNoBreweriesFound
                .observeForever(breweriesByCityErrorNoBreweriesFoundObserver)
            coEvery {
                mockedBreweryRepository.getBreweriesByCity(CITY_EXAMPLE)
            }.returns(mockedResourceRepositoryErrorNothingTyped)

            homeViewModel.updateBreweriesByCity(CITY_EXAMPLE)

            coVerify { mockedBreweryRepository.getBreweriesByCity(CITY_EXAMPLE) }
            coVerify {
                breweriesByCitySuccessObserver wasNot called
            }
            coVerify { breweriesByCityErrorNothingTypedObserver.onChanged(true) }
            coVerify { breweriesByCityErrorNoBreweriesFoundObserver wasNot called }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When homeViewModel calls updateBreweriesByCity() it Should change value of breweriesByCityNoBreweriesFound from homeViewModel`() =
        runTest {

            homeViewModel.breweriesByCitySuccess.observeForever(breweriesByCitySuccessObserver)
            homeViewModel.breweriesByCityErrorNothingTyped
                .observeForever(breweriesByCityErrorNothingTypedObserver)
            homeViewModel.breweriesByCityNoBreweriesFound
                .observeForever(breweriesByCityErrorNoBreweriesFoundObserver)
            coEvery {
                mockedBreweryRepository.getBreweriesByCity(CITY_EXAMPLE)
            }.returns(mockedResourceRepositoryErrorNoBreweriesFound)

            homeViewModel.updateBreweriesByCity(CITY_EXAMPLE)

            coVerify { mockedBreweryRepository.getBreweriesByCity(CITY_EXAMPLE) }
            coVerify {
                breweriesByCitySuccessObserver wasNot called
            }
            coVerify { breweriesByCityErrorNothingTypedObserver wasNot called }
            coVerify { breweriesByCityErrorNoBreweriesFoundObserver.onChanged(true) }
        }

    private val breweriesByCitySuccessObserver = mockk<Observer<List<Brewery>>>(relaxed = true)
    private val mockedResourceRepositorySuccess = ResourceRepository<List<Brewery>?>(
        data = listOf(
            Brewery(
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
        )
    )
    private val mockedResourceRepositoryErrorNothingTyped = ResourceRepository<List<Brewery>?>(
        data = null,
        error = ErrorRepository.ERROR_NOTHING_TYPED
    )
    private val mockedResourceRepositoryErrorNoBreweriesFound = ResourceRepository<List<Brewery>?>(
        data = null,
        error = ErrorRepository.ERROR_NO_BREWERIES_FOUND
    )

    private val breweriesByCityErrorNothingTypedObserver = mockk<Observer<Boolean>>(relaxed = true)
    private val breweriesByCityErrorNoBreweriesFoundObserver =
        mockk<Observer<Boolean>>(relaxed = true)

    private val mockedBreweryData: List<BreweryData> = listOf(
        BreweryData(
            id = "1",
            name = "Brewery Test",
            average = 5.0,
            breweryType = null
        )
    )

}
