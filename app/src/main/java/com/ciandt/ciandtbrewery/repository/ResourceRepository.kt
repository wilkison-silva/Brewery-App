package com.ciandt.ciandtbrewery.repository

data class ResourceRepository<T> (
    val data: T? ,
    val error: ErrorRepository? = null
)
