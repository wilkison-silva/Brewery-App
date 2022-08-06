package com.ciandt.ciandtbrewery.model

import com.squareup.moshi.Json

data class Brewery(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "brewery_type") val breweryType: String?,
    @field:Json(name = "street") val street: String?,
    @field:Json(name = "address2") val address2: String?,
    @field:Json(name = "address3") val address3: String?,
    @field:Json(name = "city") val city: String?,
    @field:Json(name = "state") val state: String?,
    @field:Json(name = "postal_code") val postalCode: String?,
    @field:Json(name = "country") val country: String?,
    @field:Json(name = "longitude") val longitude: Double?,
    @field:Json(name = "latitude") val latitude: Double?,
    @field:Json(name = "website_url") val websiteUrl: String?,
    @field:Json(name = "phone") val phone: String?,
    @field:Json(name = "average") val average: Double,
    @field:Json(name = "size_evaluations") val sizeEvaluations: Int?,
    @field:Json(name = "photos") val photos: List<String>?,
    var isFavorite: Boolean = false
)