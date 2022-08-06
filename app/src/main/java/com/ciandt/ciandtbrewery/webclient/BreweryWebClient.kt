package com.ciandt.ciandtbrewery.webclient

import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.model.Photo
import com.ciandt.ciandtbrewery.webclient.ErrorWebClient.*
import com.ciandt.ciandtbrewery.webclient.model.EvaluationRequest
import com.ciandt.ciandtbrewery.webclient.model.EvaluationResponse
import com.ciandt.ciandtbrewery.webclient.service.BreweryService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.Query

class BreweryWebClient(private val breweryService: BreweryService) {

    suspend fun getBreweriesByCity(city: String): ResourceWebClient<List<Brewery>?> {
        try {
            val response = breweryService.getBreweriesByCity(city)
            if(response.isSuccessful) {
                return ResourceWebClient(data = response.body())
            }
            return when(response.code()) {
                400 -> ResourceWebClient(data = null, error = ERROR_400)
                404 -> ResourceWebClient(data = null, error = ERROR_404)
                else -> ResourceWebClient(data = null, error = ERROR_UNKNOWN)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResourceWebClient(data = null, error = ERROR_EXCEPTION)
        }
    }

    suspend fun getBreweryById(breweryId: String): ResourceWebClient<Brewery?> {
        try {
            val response = breweryService.getBreweryById(breweryId)
            if(response.isSuccessful) {
                return ResourceWebClient(data = response.body())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ResourceWebClient(data = null, error = ERROR_UNKNOWN)
    }

    suspend fun postEvaluateBrewery(evaluationRequest: EvaluationRequest): ResourceWebClient<EvaluationResponse?> {
        try {
            val response = breweryService.postEvaluateBrewery(evaluationRequest)
            if(response.isSuccessful) {
                return ResourceWebClient(data = response.body())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ResourceWebClient(data = null, error = ERROR_UNKNOWN)
    }

    suspend fun getMyEvaluations(email: String): ResourceWebClient<List<Brewery>?> {
        try {
            val response = breweryService.getMyEvaluations(email)
            if(response.isSuccessful) {
                return ResourceWebClient(data = response.body())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ResourceWebClient(data = null, error = ERROR_UNKNOWN)
    }

    suspend fun getBreweriesTopTen(): ResourceWebClient<List<Brewery>?> {
        try {
            val response = breweryService.getBreweriesTopTen()
            if(response.isSuccessful) {
                return ResourceWebClient(data = response.body())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ResourceWebClient(data = null, error = ERROR_UNKNOWN)
    }

    suspend fun getBreweryPhotos(breweryId: String): ResourceWebClient<List<Photo>?> {
        try {
            val response = breweryService.getBreweryPhotos(breweryId)
            if(response.isSuccessful) {
                return ResourceWebClient(data = response.body())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ResourceWebClient(data = null, error = ERROR_UNKNOWN)
    }

    suspend fun uploadPicture(part: MultipartBody.Part,
                            breweryId: RequestBody): ResourceWebClient<Photo> {
        try {
            val response = breweryService.uploadPicture(part, breweryId)
            if(response.isSuccessful) {
                return ResourceWebClient(data = response.body())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ResourceWebClient(data = null, error = ERROR_UNKNOWN)
    }
}