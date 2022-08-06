package com.ciandt.ciandtbrewery.ui.viewmodel

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ciandt.ciandtbrewery.extensions.emitError
import com.ciandt.ciandtbrewery.extensions.emitSuccess
import com.ciandt.ciandtbrewery.repository.BreweryRepository
import com.ciandt.ciandtbrewery.webclient.model.EvaluationRequest
import com.ciandt.ciandtbrewery.webclient.model.EvaluationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EvaluationDialogViewModel(
    private val breweryRepository: BreweryRepository
) : ViewModel() {

    fun isValidEmail(target: CharSequence): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    private val _evaluationSuccess = MutableLiveData<EvaluationResponse>()
    val evaluationSuccess: LiveData<EvaluationResponse>
        get() = _evaluationSuccess

    private val _evaluationError = MutableLiveData<Boolean>()
    val evaluationError: LiveData<Boolean>
        get() = _evaluationError

    fun postEvaluateBrewery(email: String, breweryId: String, rating: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val evaluationRequest = EvaluationRequest(
                email = email,
                breweryId = breweryId,
                evaluationGrade = rating
            )
            val resourceRepository = breweryRepository.postEvaluateBrewery(evaluationRequest)
            if (resourceRepository.error == null)
                emitSuccess(resourceRepository, _evaluationSuccess)
            else
                emitError(_evaluationError)
        }
    }

    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    fun updateEmail(email: String) {
        _email.postValue(email)
    }
}