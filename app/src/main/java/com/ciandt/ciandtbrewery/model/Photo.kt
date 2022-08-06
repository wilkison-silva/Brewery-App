package com.ciandt.ciandtbrewery.model

import com.squareup.moshi.Json

data class Photo(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "brewery_id") val breweryId: String,
    @field:Json(name = "url") val url: String
)