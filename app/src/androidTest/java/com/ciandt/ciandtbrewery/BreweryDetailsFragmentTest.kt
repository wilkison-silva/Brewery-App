package com.ciandt.ciandtbrewery

import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ciandt.ciandtbrewery.database.AppDatabase
import com.ciandt.ciandtbrewery.database.model.BreweryData
import com.ciandt.ciandtbrewery.ui.activity.MainActivity
import com.ciandt.ciandtbrewery.ui.recyclerview.adapter.SearchRecyclerViewAdapter
import com.ciandt.ciandtbrewery.utils.withDrawable
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class BreweryDetailsFragmentTest: AutoCloseKoinTest() {

    @get: Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)
    @get:Rule
    val intentsTestRule = IntentsTestRule(MainActivity::class.java)

    private val appDatabase: AppDatabase by inject()
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)

        runBlocking {
            appDatabase.breweryDao().insert(
                BreweryData(
                    "angry-scotsman-brewing-oklahoma-city",
                    "Angry Scotsman Brewing",
                    3.3,
                    "contract",
                )
            )
        }
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    private fun waitFor(delay: Long): ViewAction?{
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()
            override fun getDescription(): String = "wait for $delay milliseconds"
            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }

    private fun withIndex(matcher: Matcher<View>, index: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>(){
            private var currentIndex = 0

            override fun describeTo(description: Description?) {
                description?.appendText("with index: ")
                description?.appendValue(index)
                matcher.describeTo(description)

            }

            override fun matchesSafely(view: View?): Boolean {
                return matcher.matches(view) && currentIndex++ == index
            }
        }
    }

    @Test
    fun onLaunchClickToDetail(){
        onView(isRoot()).perform(waitFor(6000))
        onView(withId(R.id.tietSearchLocal)).perform(click()).perform(typeText("Oklahoma"))
        onView(withId(R.id.ivButtonSearch)).perform(click())
        onView(isRoot()).perform(waitFor(5000))
        onView(withId(R.id.evaluated_recyclerview)).perform(
            actionOnItemAtPosition<SearchRecyclerViewAdapter.ViewHolder>(0, click()))
        onView(withIndex(withId(R.id.clBreweryDetails),0)).check(matches(isDisplayed()))
        onView(isRoot()).perform(waitFor(3000))
    }

    @Test
    fun onViewDetailsClickToFavorite(){
        onLaunchClickToDetail()
        onView(withId(R.id.ibLikeDetails)).perform(click())
        onView(withId(R.id.ibLikeDetails)).check(matches(withDrawable(R.drawable.ic_heart)))
        onView(isRoot()).perform(waitFor(3000))
    }

    @Test
    fun onViewDetailsClickMap(){
        onLaunchClickToDetail()
        onView(withId(R.id.tvMapDetails)).perform(click())
        intending(hasComponent(Intent.ACTION_VIEW))
    }

    @Test
    fun onViewDetailsEvaluateBrewery(){
        onLaunchClickToDetail()
        onView(withId(R.id.mbAvaliation)).perform(click())
        onView(isRoot()).perform(waitFor(3000))
        onView(withId(R.id.ratingBarDetails)).perform(click())
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.tiet_email))
            .perform(click())
            .perform(typeText("Francisn@ciandt.com"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.save_btn)).perform(click())
    }
}