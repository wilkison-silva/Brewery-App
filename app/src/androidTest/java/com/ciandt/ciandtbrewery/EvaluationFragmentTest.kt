package com.ciandt.ciandtbrewery

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ciandt.ciandtbrewery.ui.activity.MainActivity
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest

@RunWith(AndroidJUnit4::class)
class EvaluationFragmentTest: AutoCloseKoinTest() {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private fun waitFor(delay: Long): ViewAction?{
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for $delay milliseconds"
            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }

    @Test
    fun onLaunchClickToEvaluationFragment() {
        onView(isRoot()).perform(waitFor(6000))
        onView(withId(R.id.ivStar)).perform(click())
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.clEvaluatedFragment)).check(matches(isDisplayed()))
    }

    @Test
    fun onViewEvaluationFragmentTypeEmailToShowEvaluatedBreweryList(){
        onLaunchClickToEvaluationFragment()
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.tiet_email_evaluation))
            .perform(click())
            .perform(typeText("Francisn@ciandt.com"))
            .perform(closeSoftKeyboard())
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.confirm_btn_evaluation)).perform(click())
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.container3)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(isRoot()).perform(waitFor(3000))
    }

    @Test
    fun onViewEvaluationFragmentTypeEmailToShowBlankEvaluatedBreweryList(){
        onLaunchClickToEvaluationFragment()
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.tiet_email_evaluation))
            .perform(click())
            .perform(typeText("Gabrielrm@ciandt.com"))
            .perform(closeSoftKeyboard())
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.confirm_btn_evaluation)).perform(click())
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.container2)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(isRoot()).perform(waitFor(3000))
    }
}