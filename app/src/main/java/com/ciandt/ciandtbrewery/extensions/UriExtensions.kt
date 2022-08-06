package com.ciandt.ciandtbrewery.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.ContentProviderCompat.requireContext
import java.io.FileDescriptor
import java.io.IOException

@Throws(IOException::class)
fun Uri.getBitmapFromUri(context: Context): Bitmap? {
    val parcelFileDescriptor = context.contentResolver.openFileDescriptor(this, "r")
    val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
    val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor.close()
    return image
}