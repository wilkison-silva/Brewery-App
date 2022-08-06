package com.ciandt.ciandtbrewery.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ciandt.ciandtbrewery.repository.BreweryRepository
import com.ciandt.ciandtbrewery.repository.ErrorRepository
import com.ciandt.ciandtbrewery.repository.ResourceRepository
import com.ciandt.ciandtbrewery.webclient.model.EvaluationRequest
import com.ciandt.ciandtbrewery.webclient.model.EvaluationResponse
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

private const val BREWERY_ID = "1"
private const val EMAIL_EXAMPLE = "123@anything.com"
private const val RATING_BREWERY = 5.0

class EvaluationDialogViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockedBreweryRepository = mockk<BreweryRepository>()
    private val evaluationDialogViewModel = EvaluationDialogViewModel(mockedBreweryRepository)
    private val evaluationRequest = EvaluationRequest(EMAIL_EXAMPLE, BREWERY_ID, RATING_BREWERY)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When evaluationDialogViewModel calls postEvaluateBrewery() it Should change value of evaluationSuccess from evaluationDialogViewModel`() =
        runTest {

            evaluationDialogViewModel.evaluationSuccess.observeForever(evaluationSuccessObserver)
            evaluationDialogViewModel.evaluationError.observeForever(evaluationErrorObserver)
            coEvery {
                mockedBreweryRepository.postEvaluateBrewery(evaluationRequest)
            }.returns(mockedResourceRepositorySuccess)

            evaluationDialogViewModel.postEvaluateBrewery(EMAIL_EXAMPLE, BREWERY_ID, RATING_BREWERY)

            coVerify { mockedBreweryRepository.postEvaluateBrewery(evaluationRequest) }
            coVerify { evaluationSuccessObserver.onChanged(mockedResourceRepositorySuccess.data) }
            coVerify { evaluationErrorObserver wasNot called }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When evaluationDialogViewModel calls postEvaluateBrewery() it Should change value of evaluationError from evaluationDialogViewModel`() =
        runTest {
            evaluationDialogViewModel.evaluationSuccess.observeForever(evaluationSuccessObserver)
            evaluationDialogViewModel.evaluationError.observeForever(evaluationErrorObserver)
            coEvery {
                mockedBreweryRepository.postEvaluateBrewery(evaluationRequest)
            }.returns(mockedResourceRepositoryError)

            evaluationDialogViewModel.postEvaluateBrewery(EMAIL_EXAMPLE, BREWERY_ID, RATING_BREWERY)

            coVerify { mockedBreweryRepository.postEvaluateBrewery(evaluationRequest) }
            coVerify { evaluationSuccessObserver wasNot called }
            coVerify { evaluationErrorObserver.onChanged(true) }
        }

    private val evaluationSuccessObserver = mockk<Observer<EvaluationResponse>>(relaxed = true)
    private val mockedResourceRepositorySuccess = ResourceRepository<EvaluationResponse?>(
        data = EvaluationResponse(
        email = EMAIL_EXAMPLE,
            breweryId = BREWERY_ID,
            evaluationGrade = RATING_BREWERY
        )
    )
    private val mockedResourceRepositoryError = ResourceRepository<EvaluationResponse?>(
        data = null,
        error = ErrorRepository.ERROR
    )

    private val evaluationErrorObserver = mockk<Observer<Boolean>>(relaxed = true)
}