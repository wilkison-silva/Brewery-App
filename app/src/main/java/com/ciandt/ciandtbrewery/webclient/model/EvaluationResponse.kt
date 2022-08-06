package com.ciandt.ciandtbrewery.webclient.model

import com.squareup.moshi.Json

data class EvaluationResponse(
    @field:Json(name = "email")
    private val email: String,
    @field:Json(name = "breweryId")
    private val breweryId: String,
    @field:Json(name = "evaluationGrade")
    private val evaluationGrade: Double
) {
}