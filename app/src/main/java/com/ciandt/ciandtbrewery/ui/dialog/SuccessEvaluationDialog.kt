package com.ciandt.ciandtbrewery.ui.dialog

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import com.ciandt.ciandtbrewery.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class SuccessEvaluationDialog(
    context: Context
    ) : BottomSheetDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rating_success_fragment)
        findViewById<ImageView>(R.id.iv_close)?.let {
            val successCloseImageView = it
            successCloseImageView.setOnClickListener {
                dismiss()
            }
        }
    }
}