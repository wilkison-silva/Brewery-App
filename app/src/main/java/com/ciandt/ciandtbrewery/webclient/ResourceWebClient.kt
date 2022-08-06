package com.ciandt.ciandtbrewery.webclient

data class ResourceWebClient<T> (
    val data: T?,
    val error: ErrorWebClient? = null
)


