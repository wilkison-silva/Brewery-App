package com.ciandt.ciandtbrewery.repository

import com.ciandt.ciandtbrewery.database.dao.BreweryDao
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.model.Photo
import com.ciandt.ciandtbrewery.repository.ErrorRepository.*
import com.ciandt.ciandtbrewery.webclient.BreweryWebClient
import com.ciandt.ciandtbrewery.webclient.ErrorWebClient.*
import com.ciandt.ciandtbrewery.webclient.ResourceWebClient
import com.ciandt.ciandtbrewery.webclient.model.EvaluationRequest
import com.ciandt.ciandtbrewery.webclient.model.EvaluationResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.http.Multipart

private const val BREWERY_ID = "1"
private const val CITY_EXAMPLE = "anything"
private const val EMAIL_EXAMPLE = "123@anything.com"
private const val RATING_BREWERY = 5.0

class BreweryRepositoryTest {

    private val mockedBreweryWebClient = mockk<BreweryWebClient>()
    private val mockedBreweryDao = mockk<BreweryDao>()
    private val breweryRepository = BreweryRepository(mockedBreweryWebClient, mockedBreweryDao)
    private val evaluationRequest = EvaluationRequest(EMAIL_EXAMPLE, BREWERY_ID, RATING_BREWERY)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls getBreweriesByCity() it Should return mockedResourceRepositorySuccess`() =
        runTest {
            //Arrange
            coEvery {
                mockedBreweryWebClient.getBreweriesByCity(CITY_EXAMPLE)
            }.returns(mockedResourceWebClientSuccessList)
            //Act
            val resourceRepository = breweryRepository.getBreweriesByCity(CITY_EXAMPLE)
            //Assert
            coVerify { mockedBreweryWebClient.getBreweriesByCity(CITY_EXAMPLE) }
            assertEquals(mockedResourceRepositorySuccess.data, resourceRepository.data)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls getBreweriesByCity() it Should return mockedResourceRepositoryErrorNothingTyped`() =
        runTest {

            coEvery {
                mockedBreweryWebClient.getBreweriesByCity(CITY_EXAMPLE)
            }.returns(mockedResourceWebClientError400)

            val resourceRepository = breweryRepository.getBreweriesByCity(CITY_EXAMPLE)

            coVerify { mockedBreweryWebClient.getBreweriesByCity(CITY_EXAMPLE) }
            assertEquals(mockedResourceRepositoryErrorNothingTyped.error, resourceRepository.error)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls getBreweriesByCity() it Should return mockedResourceRepositoryErrorNoBreweriesFound`() =
        runTest {

            coEvery {
                mockedBreweryWebClient.getBreweriesByCity(CITY_EXAMPLE)
            }.returns(mockedResourceWebClientError404)

            val resourceRepository = breweryRepository.getBreweriesByCity(CITY_EXAMPLE)

            coVerify { mockedBreweryWebClient.getBreweriesByCity(CITY_EXAMPLE) }
            assertEquals(
                mockedResourceRepositoryErrorNoBreweriesFound.error,
                resourceRepository.error
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls getBreweriesByCity() it Should return mockedResourceRepositoryError`() =
        runTest {

            coEvery {
                mockedBreweryWebClient.getBreweriesByCity(CITY_EXAMPLE)
            }.returns(mockedResourceWebClientErrorUnknownList)

            val resourceRepository = breweryRepository.getBreweriesByCity(CITY_EXAMPLE)

            coVerify { mockedBreweryWebClient.getBreweriesByCity(CITY_EXAMPLE) }
            assertEquals(mockedResourceRepositoryError.error, resourceRepository.error)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls getBreweryById() it Should return mockedResourceRepositorySuccess`() =
        runTest {

            coEvery {
                mockedBreweryWebClient.getBreweryById(BREWERY_ID)
            }.returns(mockedResourceWebClientSuccessBrewery)

            val resourceRepository = breweryRepository.getBreweryById(BREWERY_ID)

            coVerify { mockedBreweryWebClient.getBreweryById(BREWERY_ID) }
            assertEquals(mockedResourceWebClientSuccessBrewery.data, resourceRepository.data)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls getBreweryById() it Should return mockedResourceRepositoryError`() =
        runTest {

            coEvery {
                mockedBreweryWebClient.getBreweryById(BREWERY_ID)
            }.returns(mockedResourceWebClientErrorUnknownBrewery)

            val resourceRepository = breweryRepository.getBreweryById(BREWERY_ID)

            coVerify { mockedBreweryWebClient.getBreweryById(BREWERY_ID) }
            assertEquals(mockedResourceRepositoryErrorBrewery.error, resourceRepository.error)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls getMyEvaluations() it Should return mockedResourceRepositorySuccess`() =
        runTest {

            coEvery {
                mockedBreweryWebClient.getMyEvaluations(EMAIL_EXAMPLE)
            }.returns(mockedResourceWebClientSuccessList)

            val resourceRepository = breweryRepository.getMyEvaluations(EMAIL_EXAMPLE)

            coVerify { mockedBreweryWebClient.getMyEvaluations(EMAIL_EXAMPLE) }
            assertEquals(mockedResourceRepositorySuccess.data, resourceRepository.data)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls getMyEvaluations() it Should return mockedResourceRepositoryError`() =
        runTest {

            coEvery {
                mockedBreweryWebClient.getMyEvaluations(EMAIL_EXAMPLE)
            }.returns(mockedResourceWebClientError404)

            val resourceRepository = breweryRepository.getMyEvaluations(EMAIL_EXAMPLE)

            coVerify { mockedBreweryWebClient.getMyEvaluations(EMAIL_EXAMPLE) }
            assertEquals(mockedResourceRepositoryError.error, resourceRepository.error)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls getBreweriesTopTen() it Should return mockedResourceRepositorySuccess`() =
        runTest {

            coEvery {
                mockedBreweryWebClient.getBreweriesTopTen()
            }.returns(mockedResourceWebClientSuccessList)

            val resourceRepository = breweryRepository.getBreweriesTopTen()

            coVerify { mockedBreweryWebClient.getBreweriesTopTen() }
            assertEquals(mockedResourceRepositorySuccess.data, resourceRepository.data)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls getBreweriesTopTen() it Should return mockedResourceRepositoryError`() =
        runTest {

            coEvery {
                mockedBreweryWebClient.getBreweriesTopTen()
            }.returns(mockedResourceWebClientError404)

            val resourceRepository = breweryRepository.getBreweriesTopTen()

            coVerify { mockedBreweryWebClient.getBreweriesTopTen() }
            assertEquals(mockedResourceRepositoryError.error, resourceRepository.error)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls postEvaluateBrewery() it Should return mockedResourceRepositorySuccessEvaluation`() =
        runTest {

            coEvery {
                mockedBreweryWebClient.postEvaluateBrewery(evaluationRequest)
            }.returns(mockedResourceWebClientSuccessEvaluation)

            val resourceRepository = breweryRepository.postEvaluateBrewery(evaluationRequest)

            coVerify { mockedBreweryWebClient.postEvaluateBrewery(evaluationRequest) }
            assertEquals(mockedResourceRepositorySuccessEvaluation.data, resourceRepository.data)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls postEvaluateBrewery() it Should return mockedResourceRepositoryErrorEvaluation`() =
        runTest {

            coEvery {
                mockedBreweryWebClient.postEvaluateBrewery(evaluationRequest)
            }.returns(mockedResourceWebClientErrorEvaluation)

            val resourceRepository = breweryRepository.postEvaluateBrewery(evaluationRequest)

            coVerify { mockedBreweryWebClient.postEvaluateBrewery(evaluationRequest) }
            assertEquals(mockedResourceRepositoryErrorEvaluation.error, resourceRepository.error)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls getBreweryPhotos() it Should return mockedResourceRepositorySuccessPhotoList`() =
        runTest {

            coEvery {
                mockedBreweryWebClient.getBreweryPhotos(BREWERY_ID)
            }.returns(mockedResourceWebClientSuccessPhotoList)

            val resourceRepository = breweryRepository.getBreweryPhotos(BREWERY_ID)

            coVerify { mockedBreweryWebClient.getBreweryPhotos(BREWERY_ID) }
            assertEquals(mockedResourceRepositorySuccessPhotoList.data, resourceRepository.data)

        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls getBreweryPhotos() it Should return mockedResourceRepositoryErrorPhotoList`() =
        runTest {

            coEvery {
                mockedBreweryWebClient.getBreweryPhotos(BREWERY_ID)
            }.returns(mockedResourceWebClientErrorPhotoList)

            val resourceRepository = breweryRepository.getBreweryPhotos(BREWERY_ID)

            coVerify { mockedBreweryWebClient.getBreweryPhotos(BREWERY_ID) }
            assertEquals(mockedResourceRepositoryErrorPhotoList.error, resourceRepository.error)

        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls uploadPicture() it Should return mockedResourceRepositorySuccessPhoto`() =
        runTest {
            coEvery {
                mockedBreweryWebClient.uploadPicture(mockedMultipart, mockedBreweryIdPart)
            }.returns(mockedResourceWebClientSuccessPhoto)

            val resourceResponse =
                breweryRepository.uploadPicture(mockedMultipart, mockedBreweryIdPart)

            coVerify { mockedBreweryWebClient.uploadPicture(mockedMultipart, mockedBreweryIdPart) }
            assertEquals(mockedResourceRepositorySuccessPhoto.data, resourceResponse.data)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When breweryRepository calls uploadPicture() it Should return mockedResourceRepositoryErrorPhoto`() =
        runTest {

            coEvery {
                mockedBreweryWebClient.uploadPicture(mockedMultipart, mockedBreweryIdPart)
            }.returns(mockedResourceWebClientErrorPhoto)

            val resourceResponse =
                breweryRepository.uploadPicture(mockedMultipart, mockedBreweryIdPart)

            coVerify { mockedBreweryWebClient.uploadPicture(mockedMultipart, mockedBreweryIdPart) }
            assertEquals(mockedResourceRepositoryErrorPhoto.error, resourceResponse.error)

        }

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
        error = ERROR_NOTHING_TYPED
    )

    private val mockedResourceRepositoryErrorNoBreweriesFound = ResourceRepository<List<Brewery>?>(
        data = null,
        error = ERROR_NO_BREWERIES_FOUND
    )

    private val mockedResourceRepositoryError = ResourceRepository<List<Brewery>?>(
        data = null,
        error = ERROR
    )

    private val mockedResourceWebClientSuccessList = ResourceWebClient<List<Brewery>?>(
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

    private val mockedResourceWebClientError400 = ResourceWebClient<List<Brewery>?>(
        data = null,
        error = ERROR_400
    )

    private val mockedResourceWebClientError404 = ResourceWebClient<List<Brewery>?>(
        data = null,
        error = ERROR_404
    )

    private val mockedResourceWebClientErrorUnknownList = ResourceWebClient<List<Brewery>?>(
        data = null,
        error = ERROR_UNKNOWN
    )

    private val mockedResourceWebClientSuccessBrewery = ResourceWebClient<Brewery?>(
        data = Brewery(
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

    private val mockedResourceWebClientErrorUnknownBrewery = ResourceWebClient<Brewery?>(
        data = null,
        error = ERROR_UNKNOWN
    )

    private val mockedResourceRepositoryErrorBrewery = ResourceRepository<Brewery?>(
        data = null,
        error = ERROR
    )

    private val mockedResourceWebClientSuccessEvaluation = ResourceWebClient<EvaluationResponse?>(
        data = EvaluationResponse(
            email = EMAIL_EXAMPLE,
            breweryId = BREWERY_ID,
            evaluationGrade = RATING_BREWERY
        )
    )

    private val mockedResourceRepositorySuccessEvaluation = ResourceRepository<EvaluationResponse?>(
        data = EvaluationResponse(
            email = EMAIL_EXAMPLE,
            breweryId = BREWERY_ID,
            evaluationGrade = RATING_BREWERY
        )
    )

    private val mockedResourceWebClientErrorEvaluation = ResourceWebClient<EvaluationResponse?>(
        data = null,
        error = ERROR_404
    )

    private val mockedResourceRepositoryErrorEvaluation = ResourceRepository<EvaluationResponse?>(
        data = null,
        error = ERROR
    )

    private val mockedResourceWebClientSuccessPhotoList = ResourceWebClient<List<Photo>?>(
        data = listOf(
            Photo(
                id = "foto1",
                BREWERY_ID,
                url = "www.yarin.com.br"
            )
        )
    )

    private val mockedResourceRepositorySuccessPhotoList = ResourceRepository<List<Photo>?>(
        data = listOf(
            Photo(
                id = "foto1",
                BREWERY_ID,
                url = "www.yarin.com.br"
            )
        )
    )

    private val mockedResourceRepositoryErrorPhotoList = ResourceRepository<List<Photo>?>(
        data = null,
        error = ERROR
    )

    private val mockedResourceWebClientErrorPhotoList = ResourceWebClient<List<Photo>?>(
        data = null,
        error = ERROR_400
    )

    private val mockedResourceWebClientSuccessPhoto = ResourceWebClient<Photo>(
        data = Photo(
            id = "foto1",
            BREWERY_ID,
            url = "www.yarin.com.br"
        )
    )

    private val mockedResourceRepositorySuccessPhoto = ResourceRepository<Photo>(
        data = Photo(
            id = "foto1",
            BREWERY_ID,
            url = "www.yarin.com.br"
        )
    )

    private val mockedResourceWebClientErrorPhoto = ResourceWebClient<Photo>(
        data = null,
        error = ERROR_400
    )

    private val mockedResourceRepositoryErrorPhoto = ResourceRepository<Photo>(
        data = null,
        error = ERROR
    )

    private val mockedMultipart = mockk<MultipartBody.Part>()

    private val mockedBreweryIdPart = mockk<RequestBody>()
}