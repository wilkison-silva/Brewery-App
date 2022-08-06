package com.ciandt.ciandtbrewery.ui.viewmodel

import androidx.lifecycle.*
import com.ciandt.ciandtbrewery.extensions.emitError
import com.ciandt.ciandtbrewery.extensions.emitSuccess
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.repository.BreweryRepository
import com.ciandt.ciandtbrewery.repository.ErrorRepository
import com.ciandt.ciandtbrewery.repository.ResourceRepository
import kotlinx.coroutines.launch

class EvaluatedViewModel(
    private val breweryRepository: BreweryRepository
) : ViewModel() {

    private val _noBreweriesFound = MutableLiveData<Boolean>()
    val noBreweriesFound: LiveData<Boolean>
        get() = _noBreweriesFound
    private val _breweriesSuccess = MutableLiveData<List<Brewery>>()
    val breweriesSuccess: LiveData<List<Brewery>>
        get() = _breweriesSuccess

    fun updateBreweries(email: String) {
        viewModelScope.launch {
        val resourceRepository = breweryRepository.getMyEvaluations(email)
        when (resourceRepository.error) {
            null -> {
                resourceRepository.data?.size?.let {size ->
                    if (size > 0) {
                        val orderedByAverage = resourceRepository.data.sortedByDescending { brewery ->
                            brewery.average
                        }
                        val resourceRepositoryOrderedByAverage = ResourceRepository<List<Brewery>?>(data = orderedByAverage)
                        emitSuccess(resourceRepositoryOrderedByAverage, _breweriesSuccess)
                    }
                    else {
                        emitError(_noBreweriesFound)
                    }
                }
            }
            ErrorRepository.ERROR -> emitError(_noBreweriesFound)
        }}
    }
}