package com.ciandt.ciandtbrewery.webclient.model

import com.squareup.moshi.Json

data class EvaluationRequest(
    @field:Json(name = "email")
    private val email: String,
    @field:Json(name = "brewery_id")
    private val breweryId: String,
    @field:Json(name = "evaluation_grade")
    private val evaluationGrade: Double
) {
}