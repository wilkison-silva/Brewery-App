package com.ciandt.ciandtbrewery.repository

import com.ciandt.ciandtbrewery.database.dao.BreweryDao
import com.ciandt.ciandtbrewery.database.model.BreweryData
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.model.Photo
import com.ciandt.ciandtbrewery.repository.ErrorRepository.*
import com.ciandt.ciandtbrewery.webclient.BreweryWebClient
import com.ciandt.ciandtbrewery.webclient.ErrorWebClient.ERROR_400
import com.ciandt.ciandtbrewery.webclient.ErrorWebClient.ERROR_404
import com.ciandt.ciandtbrewery.webclient.model.EvaluationRequest
import com.ciandt.ciandtbrewery.webclient.model.EvaluationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BreweryRepository(
    private val breweryWebClient: BreweryWebClient,
    private val breweryDao: BreweryDao
) {
    suspend fun getBreweriesByCity(city: String): ResourceRepository<List<Brewery>?> {
        val resourceWebClient = breweryWebClient.getBreweriesByCity(city)
        if (resourceWebClient.data != null) return ResourceRepository(data = resourceWebClient.data)
        return when (resourceWebClient.error) {
            ERROR_400 -> ResourceRepository(data = null, error = ERROR_NOTHING_TYPED)
            ERROR_404 -> ResourceRepository(data = null, error = ERROR_NO_BREWERIES_FOUND)
            else -> ResourceRepository(data = null, error = ERROR)
        }
    }

    suspend fun getBreweryById(breweryId: String): ResourceRepository<Brewery?> {
        val resourceWebClient = breweryWebClient.getBreweryById(breweryId)
        if (resourceWebClient.data != null) return ResourceRepository(data = resourceWebClient.data)
        return ResourceRepository(data = null, error = ERROR)
    }

    suspend fun postEvaluateBrewery(
        evaluationRequest: EvaluationRequest
    ): ResourceRepository<EvaluationResponse?> {
        val resourceWebClient = breweryWebClient.postEvaluateBrewery(evaluationRequest)
        if (resourceWebClient.data != null) return ResourceRepository(data = resourceWebClient.data)
        return ResourceRepository(data = null, error = ERROR)
    }

    suspend fun getMyEvaluations(email: String): ResourceRepository<List<Brewery>?> {
        val resourceWebClient = breweryWebClient.getMyEvaluations(email)
        if (resourceWebClient.data != null) return ResourceRepository(data = resourceWebClient.data)
        return ResourceRepository(data = null, error = ERROR)
    }

    suspend fun getBreweriesTopTen(): ResourceRepository<List<Brewery>?> {
        val resourceResponse = breweryWebClient.getBreweriesTopTen()
        if (resourceResponse.data != null) return ResourceRepository(data = resourceResponse.data)
        return ResourceRepository(data = null, error = ERROR)
    }

    suspend fun insertFavoriteBrewery(breweryData: BreweryData) {
        breweryDao.insert(breweryData)
    }

    suspend fun getAllFavoriteBrewery(): List<BreweryData> {
        return breweryDao.getAll()
    }

    suspend fun deleteFavoriteBrewery(id: String) {
        breweryDao.deleteById(id)
    }

    suspend fun getBreweryPhotos(breweryId: String): ResourceRepository<List<Photo>?> {
        val resourceResponse = breweryWebClient.getBreweryPhotos(breweryId)
        if (resourceResponse.data != null) return ResourceRepository(data = resourceResponse.data)
        return ResourceRepository(data = null, error = ERROR)
    }

    suspend fun uploadPicture(part: MultipartBody.Part,
                              breweryId: RequestBody): ResourceRepository<Photo?> {
        val resourceResponse = breweryWebClient.uploadPicture(part, breweryId)
        if (resourceResponse.data != null) return ResourceRepository(data = resourceResponse.data)
        return ResourceRepository(data = null, error = ERROR)
    }
}

