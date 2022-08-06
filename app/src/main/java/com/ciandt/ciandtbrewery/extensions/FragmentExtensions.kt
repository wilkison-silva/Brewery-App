package com.ciandt.ciandtbrewery.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.ciandt.ciandtbrewery.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import java.io.File

fun Fragment.createConfirmationDialog(
    context: Context,
    onConfirmation: () -> Unit,
    onCancel: () -> Unit = {},
) {
    AlertDialog.Builder(context)
        .setView(R.layout.confirmation_fragment)
        .show().apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val confirmButton = findViewById<MaterialButton>(R.id.confirm_remove_btn)
            confirmButton?.setOnClickListener {
                onConfirmation()
                dismiss()
            }
            val dismissButton = findViewById<MaterialButton>(R.id.cancel_btn)
            dismissButton?.setOnClickListener {
                onCancel()
                dismiss()
            }
        }
}

fun Fragment.showMessage(view: View, message: String, textButtonClose: String) {
    Snackbar
        .make(view, message, Snackbar.LENGTH_LONG)
        .setAction(textButtonClose) {}
        .show()
}

fun Fragment.checkInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}