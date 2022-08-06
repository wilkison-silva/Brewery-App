package com.ciandt.ciandtbrewery.ui.dialog

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import com.ciandt.ciandtbrewery.R
import com.ciandt.ciandtbrewery.extensions.getBitmapFromUri
import com.ciandt.ciandtbrewery.ui.viewmodel.ChooseImageDialogViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

class ChooseImageDialogFragment(
    private val breweryId: String,
    private val onSuccess: () -> Unit = {},
    private val onError: () -> Unit = {}
) : BottomSheetDialogFragment() {

    private val chooseImageDialogViewModel: ChooseImageDialogViewModel by inject()

    private val requestPermissionLauncherCamera =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                getCamera.launch(intent)
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    private val getCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            var bitmap: Bitmap? = null
            if (it?.data?.extras?.get("data") != null) {
                bitmap = it?.data?.extras?.get("data") as Bitmap
                chooseImageDialogViewModel.uploadPicture(bitmap, breweryId)
            }
        }

    private val requestPermissionLauncherImage =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                val intent = Intent()
                intent.action = Intent.ACTION_PICK
                intent.type = "image/*"
                getImage.launch(intent)
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {activityResult ->
        val bitmap = activityResult?.data?.data?.getBitmapFromUri(requireContext())
        bitmap?.let {
            chooseImageDialogViewModel.uploadPicture(it, breweryId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.upload_photos_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponentsOfDialogChooseImage()
        observeUploadStatus()

    }

    private fun observeUploadStatus() {
        chooseImageDialogViewModel.uploadSuccess.observe(viewLifecycleOwner) {
            onSuccess()
            dismiss()
        }
        chooseImageDialogViewModel.uploadError.observe(viewLifecycleOwner) {
            onError()
            dismiss()
        }
    }

    private fun initComponentsOfDialogChooseImage() {
        view?.apply {
            val ibCamera = findViewById<ImageButton>(R.id.ib_image_camera)
            val ibGallery = findViewById<ImageButton>(R.id.ib_image_gallery)
            ibCamera.setOnClickListener {
                requestPermissionLauncherCamera.launch(
                    Manifest.permission.CAMERA
                )
            }
            ibGallery.setOnClickListener {
                requestPermissionLauncherImage.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }
}