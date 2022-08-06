package com.ciandt.ciandtbrewery.ui.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ciandt.ciandtbrewery.R
import com.ciandt.ciandtbrewery.extensions.checkInternetConnection
import com.ciandt.ciandtbrewery.extensions.showMessage
import com.ciandt.ciandtbrewery.model.Brewery
import com.ciandt.ciandtbrewery.ui.recyclerview.adapter.EvaluatedAdapter
import com.ciandt.ciandtbrewery.ui.recyclerview.adapter.SearchRecyclerViewAdapter
import com.ciandt.ciandtbrewery.ui.viewmodel.EvaluatedViewModel
import com.ciandt.ciandtbrewery.ui.viewmodel.EvaluationDialogViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class EvaluationFragment : Fragment() {

    private val evaluationDialogViewModel: EvaluationDialogViewModel by inject()
    private val evaluatedViewModel: EvaluatedViewModel by inject()

    private lateinit var layoutEmail: ConstraintLayout
    private lateinit var layoutBlankResult: ConstraintLayout
    private lateinit var layoutEvaluatedList: ConstraintLayout
    private lateinit var emailTextInputEditText: TextInputEditText
    private lateinit var emailTextInputLayout: TextInputLayout
    private lateinit var evaluateMaterialButton: MaterialButton

    private val navController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.personal_evaluation_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponentsOfDialogEvaluation()
        setupButtons()
        observeBreweriesStatus()
        updateEnteredEmail()
        updateEmailFieldColor()
    }

    private fun observeBreweriesStatus() {
        evaluatedViewModel.breweriesSuccess.observe(viewLifecycleOwner) { breweriesList ->
            showEvaluatedBreweries(breweriesList)
            layoutEmail.visibility = View.GONE
            layoutEvaluatedList.visibility = View.VISIBLE
            layoutBlankResult.visibility = View.GONE
        }
        evaluatedViewModel.noBreweriesFound.observe(viewLifecycleOwner) {
            layoutEmail.visibility = View.GONE
            layoutEvaluatedList.visibility = View.GONE
            layoutBlankResult.visibility = View.VISIBLE
        }
    }

    private fun showEvaluatedBreweries(breweriesList: List<Brewery>) {
        val evaluatedRecycler =
            requireView().findViewById<RecyclerView>(R.id.evaluated_recyclerview)
        val evaluatedAdapter = EvaluatedAdapter(breweriesList)

        evaluatedRecycler.adapter = evaluatedAdapter

        val totalResultsTextView = requireView()
            .findViewById<TextView>(R.id.tvResultsEvaluated)
        totalResultsTextView.text = if (breweriesList.size > 1) {
            getString(R.string.title_total_result_plural, breweriesList.size)
        } else {
            getString(R.string.title_total_result_singular, breweriesList.size)
        }
    }

    private fun setupButtons() {
        view?.apply {
            val returnImageButton: ImageButton = findViewById(R.id.ib_evaluation_back)
            returnImageButton.setOnClickListener {
                navController.popBackStack()
            }
        }
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

    private fun initComponentsOfDialogEvaluation() {
        view?.apply {
            layoutEmail = findViewById(R.id.container1)
            layoutBlankResult = findViewById(R.id.container2)
            layoutEvaluatedList = findViewById(R.id.container3)
            emailTextInputEditText = findViewById(R.id.tiet_email_evaluation)
            emailTextInputLayout = findViewById(R.id.til_breweryEmail_evaluation)
            evaluateMaterialButton = findViewById(R.id.confirm_btn_evaluation)
            evaluateMaterialButton.setOnClickListener {
                if (evaluationDialogViewModel.isValidEmail(emailTextInputEditText.text.toString()))
                    evaluatedViewModel.updateBreweries(emailTextInputEditText.text.toString())
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

    private fun updateEnteredEmail() {
        emailTextInputEditText.addTextChangedListener {
            evaluationDialogViewModel.updateEmail(it.toString())
        }
    }
}