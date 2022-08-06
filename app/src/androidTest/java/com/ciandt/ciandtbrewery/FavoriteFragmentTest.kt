package com.ciandt.ciandtbrewery

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ciandt.ciandtbrewery.database.AppDatabase
import com.ciandt.ciandtbrewery.database.model.BreweryData
import com.ciandt.ciandtbrewery.ui.activity.MainActivity
import com.ciandt.ciandtbrewery.ui.recyclerview.adapter.SearchRecyclerViewAdapter
import com.ciandt.ciandtbrewery.utils.childOfViewAtPositionWithMatcher
import com.ciandt.ciandtbrewery.utils.clickOnViewChild
import com.ciandt.ciandtbrewery.utils.withDrawable
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
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
class FavoriteFragmentTest: AutoCloseKoinTest() {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private val appDatabase: AppDatabase by inject()
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)

        runBlocking {
            appDatabase.breweryDao().insert(
                BreweryData(
                    "automatic-brewing-co-blind-lady-alehouse-san-diego",
                    "Automatic Brewing Co. / Blind Lady Alehouse",
                    3.3,
                    "pub",
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
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for $delay milliseconds"
            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }

    @Test
    fun onLaunchClickToFavoriteFragment() {
        onView(isRoot()).perform(waitFor(6000))
        onView(withId(R.id.ivHeart)).perform(click())
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.clFavorite)).check(matches(isDisplayed()))
    }

    @Test
    fun onViewFavoriteFragmentRemoveFavoriteBrewery(){
        onLaunchClickToFavoriteFragment()
        onView(withId(R.id.favorite_recyclerview)).perform(
            actionOnItemAtPosition<SearchRecyclerViewAdapter.ViewHolder>(0,clickOnViewChild(R.id.ivLikeBrewery)))
        onView(withId(R.id.confirm_remove_btn)).perform(click())
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.cl_layout_no_favorite)).check(matches(isDisplayed()))
        onView(isRoot()).perform(waitFor(1000))
    }
}