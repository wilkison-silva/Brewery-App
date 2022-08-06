package com.ciandt.ciandtbrewery.ui.viewmodel

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.model.Photo
import com.ciandt.ciandtbrewery.repository.BreweryRepository
import com.ciandt.ciandtbrewery.repository.ErrorRepository
import com.ciandt.ciandtbrewery.repository.ResourceRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val BREWERY_ID = "1"

class ChooseImageDialogViewModelTest{

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockedBreweryRepository = mockk<BreweryRepository>()
    private val chooseImageDialogViewModel = ChooseImageDialogViewModel(mockedBreweryRepository)
    private val mockedBitmap = mockk<Bitmap>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `When chooseImageViewModel calls uploadPicture() it Should change value of chooseImageSuccessObserver from chooseImageViewModel` (){
        chooseImageDialogViewModel.uploadSuccess.observeForever(chooseImageSuccessObserver)

        every {
            mockedBitmap.compress(Bitmap.CompressFormat.PNG, 100, any())
        }.returns(true)

        coEvery {
            mockedBreweryRepository.uploadPicture(any(), any())
        }.returns(mockedResourceRepositorySuccess)

        chooseImageDialogViewModel.uploadPicture(mockedBitmap, BREWERY_ID)

        coVerify { mockedBreweryRepository.uploadPicture(any(),any())}
        coVerify { chooseImageSuccessObserver.onChanged(mockedResourceRepositorySuccess.data)}

    }

    @Test
    fun `When chooseImageViewModel calls uploadPicture() it Should not change value of chooseImageSuccessObserver from chooseImageViewModel` (){
        chooseImageDialogViewModel.uploadSuccess.observeForever(chooseImageSuccessObserver)

        every {
            mockedBitmap.compress(Bitmap.CompressFormat.PNG, 100, any())
        }.returns(true)

        coEvery {
            mockedBreweryRepository.uploadPicture(any(), any())
        }.returns(mockedResourceRepositoryError)

        chooseImageDialogViewModel.uploadPicture(mockedBitmap, BREWERY_ID)

        coVerify { mockedBreweryRepository.uploadPicture(any(),any())}
        coVerify { chooseImageSuccessObserver wasNot called }

    }

    private val chooseImageSuccessObserver = mockk<Observer<Photo>>(relaxed = true)

    private val mockedResourceRepositorySuccess = ResourceRepository<Photo?>(
        data = Photo(
            id = "foto1",
            BREWERY_ID,
            url = "www.yarin.com.br"
        )
    )

    private val mockedResourceRepositoryError = ResourceRepository<Photo?>(
        data = null,
        error = ErrorRepository.ERROR
    )

}