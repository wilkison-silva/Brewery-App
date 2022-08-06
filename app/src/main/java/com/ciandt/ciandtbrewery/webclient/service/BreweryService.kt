package com.ciandt.ciandtbrewery.webclient.service

import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.model.Photo
import com.ciandt.ciandtbrewery.webclient.model.EvaluationRequest
import com.ciandt.ciandtbrewery.webclient.model.EvaluationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface BreweryService {

    @GET("/breweries")
    suspend fun getBreweriesByCity(
        @Query("by_city") byCity: String
    ): Response<List<Brewery>>

    @GET("/breweries/{breweryId}")
    suspend fun getBreweryById(
        @Path("breweryId") breweryId: String
    ): Response<Brewery>

    @POST("/breweries")
    suspend fun postEvaluateBrewery(
        @Body evaluationRequest: EvaluationRequest
    ): Response<EvaluationResponse>

    @GET("/breweries/myEvaluations/{email}")
    suspend fun getMyEvaluations(
        @Path("email") email: String
    ): Response<List<Brewery>>

    @GET("/breweries/topTen")
    suspend fun getBreweriesTopTen(): Response<List<Brewery>>

    @GET("/breweries/photos/{breweryId}")
    suspend fun getBreweryPhotos(
        @Path("breweryId") breweryId: String): Response<List<Photo>>

    @Multipart
    @POST("/breweries/photos/upload")
    suspend fun uploadPicture(
        @Part part: MultipartBody.Part,
        @Part ("brewery_id") breweryId: RequestBody
    ): Response<Photo>
}