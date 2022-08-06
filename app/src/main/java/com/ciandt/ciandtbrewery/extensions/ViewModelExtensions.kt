package com.ciandt.ciandtbrewery.extensions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ciandt.ciandtbrewery.repository.ResourceRepository

fun <T> ViewModel.emitSuccess(
    resourceRepository: ResourceRepository<T?>,
    mutableLiveDataSuccess: MutableLiveData<T>
) {
    if (resourceRepository.data != null) {
        val data = resourceRepository.data as T
        mutableLiveDataSuccess.postValue(data)
    }
}

fun ViewModel.emitError(
    mutableLiveDataError: MutableLiveData<Boolean>
) {
    mutableLiveDataError.postValue(true)
}