package com.ciandt.ciandtbrewery.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ciandt.ciandtbrewery.extensions.emitError
import com.ciandt.ciandtbrewery.extensions.emitSuccess
import com.ciandt.ciandtbrewery.model.Photo
import com.ciandt.ciandtbrewery.repository.BreweryRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class ChooseImageDialogViewModel(
    private val breweryRepository: BreweryRepository
) : ViewModel() {

    private val _uploadSuccess = MutableLiveData<Photo>()
    val uploadSuccess: LiveData<Photo>
        get() = _uploadSuccess

    private val _uploadError = MutableLiveData<Boolean>()
    val uploadError: LiveData<Boolean>
        get() = _uploadError

    fun uploadPicture(bitmap: Bitmap, breweryId: String) {
        viewModelScope.launch {

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()

            val breweryIdPart = breweryId.toRequestBody("text/plain".toMediaTypeOrNull())
            val requestFile: RequestBody =
                byteArray.toRequestBody(
                    "multipart/form-data".toMediaTypeOrNull(),
                    0,
                    byteArray.size
                )
            val part: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", "", requestFile)
            val resourceRepository = breweryRepository.uploadPicture(part, breweryIdPart)
            if (resourceRepository.error == null)
                emitSuccess(resourceRepository, _uploadSuccess)
            else
                emitError(_uploadError)
        }
    }
}