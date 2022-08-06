package com.ciandt.ciandtbrewery.ui.dialog

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.ciandt.ciandtbrewery.R
import com.ciandt.ciandtbrewery.ui.viewmodel.EvaluationDialogViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject


class EvaluationDialogFragment(
    private val breweryId: String,
    var onEvaluationFinished: () -> Unit = {}
) : BottomSheetDialogFragment() {

    private lateinit var emailTextInputEditText: TextInputEditText
    private lateinit var emailTextInputLayout: TextInputLayout

    private val evaluationDialogViewModel: EvaluationDialogViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.evaluation_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponentsOfDialogEvaluation()
        setupSaveButtonDialogEvaluation()
        updateEnteredEmail()
        updateEmailFieldColor()
        setupButtonClose()
    }

    private fun updateEmailFieldColor() {
        evaluationDialogViewModel.email.observe(viewLifecycleOwner) { email ->
            if (evaluationDialogViewModel.isValidEmail(email)) {
                configColorSuccessEmailTextInputLayout()
            } else {
                configColorErrorEmailTextInputLayout()
            }
        }
    }

    private fun updateEnteredEmail() {
        emailTextInputEditText.addTextChangedListener {
            evaluationDialogViewModel.updateEmail(it.toString())
        }
    }

    private fun setupButtonClose() {
        view?.apply {
            val evaluationCloseImageView: ImageView = findViewById(R.id.iv_close)
            evaluationCloseImageView.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun initComponentsOfDialogEvaluation() {
        view?.apply {
            emailTextInputEditText = findViewById(R.id.tiet_email)
            emailTextInputLayout = findViewById(R.id.til_breweryEmail)
        }
    }

    private fun setupSaveButtonDialogEvaluation() {
        view?.apply {
            val saveButton: Button = findViewById(R.id.save_btn)
            saveButton.setOnClickListener {
                evaluationDialogViewModel.email.observe(viewLifecycleOwner) { email ->
                    if (evaluationDialogViewModel.isValidEmail(email)) {
                        createEvaluate(email)
                    }
                }
            }
        }
    }

    private fun configColorErrorEmailTextInputLayout() {
        emailTextInputLayout.error = context?.getString(R.string.put_valid_email_message)
    }

    private fun configColorSuccessEmailTextInputLayout() {
        emailTextInputLayout.error = null
        emailTextInputLayout.boxStrokeColor = ContextCompat
            .getColor(
                requireActivity(),
                R.color.success_green
            )
        val colorInt = ContextCompat.getColor(requireActivity(), R.color.success_green)
        val csl = ColorStateList.valueOf(colorInt)
        emailTextInputLayout.hintTextColor = csl
    }

    private fun createEvaluate(
        email: String,
    ) {
        view?.apply {
            val evaluationRatingBar: RatingBar = findViewById(R.id.ratingBarDetails)
            evaluateBrewery(email, breweryId, evaluationRatingBar.rating.toDouble())
            observeEvaluation()
        }
    }

    private fun evaluateBrewery(
        email: String,
        breweryId: String,
        rating: Double
    ) {
        evaluationDialogViewModel
            .postEvaluateBrewery(
                email,
                breweryId,
                rating
            )
    }

    private fun observeEvaluation() {
        evaluationDialogViewModel.evaluationSuccess.observe(viewLifecycleOwner) {
            showDialogSucessEvaluation()
        }
        evaluationDialogViewModel.evaluationError.observe(viewLifecycleOwner) {
            showDialogErrorEvaluation()
        }
    }

    private fun showDialogErrorEvaluation() {
        ErrorEvaluationDialog(requireActivity()).show()
        dismiss()
    }

    private fun showDialogSucessEvaluation() {
        SuccessEvaluationDialog(requireActivity()).show()
        onEvaluationFinished()
        dismiss()
    }
}