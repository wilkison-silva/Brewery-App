package com.ciandt.ciandtbrewery.extensions

import android.widget.ImageView
import coil.load
import com.ciandt.ciandtbrewery.R

fun ImageView.tryLoadImage(imageUrl: String? = null){
    load(imageUrl){
        fallback(R.drawable.ic_image_fallback)
        error(R.drawable.ic_image_error)
        placeholder(R.drawable.ic_placeholder)
    }
}
