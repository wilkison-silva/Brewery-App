package com.ciandt.ciandtbrewery.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ciandt.ciandtbrewery.database.dao.BreweryDao
import com.ciandt.ciandtbrewery.domain.FavoriteUseCase
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.repository.BreweryRepository
import com.ciandt.ciandtbrewery.repository.ErrorRepository.ERROR
import com.ciandt.ciandtbrewery.repository.ResourceRepository
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private const val BREWERY_ID = "1"
private const val EMAIL_EXAMPLE = "123@anything.com"

class BreweryDetailsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockedBreweryRepository = mockk<BreweryRepository>()
    private val mockedFavoriteUseCase = mockk<FavoriteUseCase>()
    private val breweryDetailsViewModel = BreweryDetailsViewModel(mockedBreweryRepository, mockedFavoriteUseCase)

    @Before
    fun setUp(){
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryDetailsViewModel calls getBreweryById() it Should change value of breweryByIdSuccess from breweryDetailsViewModel`() {

        breweryDetailsViewModel.breweryByIdSuccess.observeForever(breweryByIdSuccessObserver)
        coEvery {
            mockedBreweryRepository.getBreweryById(BREWERY_ID)
        }.returns(mockedResourceRepositorySuccess)

        breweryDetailsViewModel.getBreweryById(BREWERY_ID)

        coVerify { mockedBreweryRepository.getBreweryById(BREWERY_ID) }
        coVerify { breweryByIdSuccessObserver.onChanged(mockedResourceRepositorySuccess.data) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryDetailsViewModel calls getBreweryById() it Should not change value of breweryByIdSuccess from breweryDetailsViewModel`() {

        breweryDetailsViewModel.breweryByIdSuccess.observeForever(breweryByIdSuccessObserver)
        coEvery {
            mockedBreweryRepository.getBreweryById(BREWERY_ID)
        }.returns(mockedResourceRepositoryError)

        breweryDetailsViewModel.getBreweryById(BREWERY_ID)

        coVerify { mockedBreweryRepository.getBreweryById(BREWERY_ID) }
        coVerify { breweryByIdSuccessObserver wasNot called }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryDetailsViewModel calls getMyEvaluationsByEmail() it Should change value of myEvaluationsByEmailSuccess from breweryDetailsViewModel`() =
        runTest {

            breweryDetailsViewModel.myEvaluationsByEmailSuccess.observeForever(
                myEvaluationsByEmailSuccessObserver
            )
            coEvery {
                mockedBreweryRepository.getMyEvaluations(EMAIL_EXAMPLE)
            }.returns(mockedResourceRepositoryListSuccess)

            breweryDetailsViewModel.getMyEvaluationsByEmail(EMAIL_EXAMPLE)

            coVerify { mockedBreweryRepository.getMyEvaluations(EMAIL_EXAMPLE) }
            coVerify {
                myEvaluationsByEmailSuccessObserver
                    .onChanged(mockedResourceRepositoryListSuccess.data)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryDetailsViewModel calls getMyEvaluationsByEmail() it Should not change value of myEvaluationsByEmailSuccess from breweryDetailsViewModel`() =
        runTest {

            breweryDetailsViewModel.myEvaluationsByEmailSuccess.observeForever(
                myEvaluationsByEmailSuccessObserver
            )
            coEvery {
                mockedBreweryRepository.getMyEvaluations(EMAIL_EXAMPLE)
            }.returns(mockedResourceRepositoryListError)

            breweryDetailsViewModel.getMyEvaluationsByEmail(EMAIL_EXAMPLE)

            coVerify { mockedBreweryRepository.getMyEvaluations(EMAIL_EXAMPLE) }
            coVerify {
                myEvaluationsByEmailSuccessObserver wasNot called
            }
        }


    private val breweryByIdSuccessObserver = mockk<Observer<Brewery>>(relaxed = true)
    private val mockedResourceRepositorySuccess = ResourceRepository<Brewery?>(
        data = Brewery(
            BREWERY_ID,
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
    private val mockedResourceRepositoryError = ResourceRepository<Brewery?>(
        data = null,
        error = ERROR
    )

    private val myEvaluationsByEmailSuccessObserver = mockk<Observer<List<Brewery>>>(relaxed = true)
    private val mockedResourceRepositoryListSuccess = ResourceRepository<List<Brewery>?>(
        data = listOf(
            Brewery(
                BREWERY_ID,
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
    private val mockedResourceRepositoryListError = ResourceRepository<List<Brewery>?>(
        data = null,
        error = ERROR
    )

}