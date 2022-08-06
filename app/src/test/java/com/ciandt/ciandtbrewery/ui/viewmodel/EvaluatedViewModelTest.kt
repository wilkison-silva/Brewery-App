package com.ciandt.ciandtbrewery.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.repository.BreweryRepository
import com.ciandt.ciandtbrewery.repository.ErrorRepository
import com.ciandt.ciandtbrewery.repository.ResourceRepository
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private const val EMAIL = "email@email.com"
class EvaluatedViewModelTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockedBreweryRepository = mockk<BreweryRepository>()
    private val evaluatedViewModel = EvaluatedViewModel(mockedBreweryRepository)

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `When EvaluatedViewModel calls updateBreweries() it Should change value of breweriesSuccess() from evaluatedViewModel with list of size bigger than 0` () {

        evaluatedViewModel.noBreweriesFound.observeForever(noBreweriesFoundObserver)
        evaluatedViewModel.breweriesSuccess.observeForever(breweriesSuccessObserver)
        coEvery {
            mockedBreweryRepository.getMyEvaluations(EMAIL)
        }.returns(mockedResourceRepositorySuccess)

        evaluatedViewModel.updateBreweries(EMAIL)
        coVerify { mockedBreweryRepository.getMyEvaluations(EMAIL) }
        coVerify { breweriesSuccessObserver.onChanged(mockedResourceRepositorySuccess.data) }
        coVerify { noBreweriesFoundObserver wasNot called }
    }

    @Test
    fun `When EvaluatedViewModel calls updateBreweries() it Should change value of noBreweriesFound() from evaluatedViewModel with list of size equals 0` () {

        evaluatedViewModel.breweriesSuccess.observeForever(breweriesSuccessObserver)
        evaluatedViewModel.noBreweriesFound.observeForever(noBreweriesFoundObserver)
        coEvery {
            mockedBreweryRepository.getMyEvaluations(EMAIL)
        }.returns(mockedEmptyList)

        evaluatedViewModel.updateBreweries(EMAIL)
        coVerify { mockedBreweryRepository.getMyEvaluations(EMAIL) }
        coVerify { noBreweriesFoundObserver.onChanged(true) }
        coVerify { breweriesSuccessObserver wasNot called }
    }

    @Test
    fun `When EvaluatedViewModel calls updateBreweries() it Should change value of noBreweriesFound()` () {

        evaluatedViewModel.breweriesSuccess.observeForever(breweriesSuccessObserver)
        evaluatedViewModel.noBreweriesFound.observeForever(noBreweriesFoundObserver)
        coEvery {
            mockedBreweryRepository.getMyEvaluations(EMAIL)
        }.returns(mockedResourceErrorNonNull)

        evaluatedViewModel.updateBreweries(EMAIL)
        coVerify { mockedBreweryRepository.getMyEvaluations(EMAIL) }
        coVerify { noBreweriesFoundObserver.onChanged(true) }
        coVerify { breweriesSuccessObserver wasNot called }
    }

    private val breweriesSuccessObserver = mockk<Observer<List<Brewery>>>(relaxed = true)
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

    private val noBreweriesFoundObserver = mockk<Observer<Boolean>>(relaxed = true)
    private val mockedEmptyList = ResourceRepository<List<Brewery>?>(
        data = listOf()
    )
    private val mockedResourceErrorNonNull = ResourceRepository<List<Brewery>?>(
        data = null,
        error = ErrorRepository.ERROR
    )
}