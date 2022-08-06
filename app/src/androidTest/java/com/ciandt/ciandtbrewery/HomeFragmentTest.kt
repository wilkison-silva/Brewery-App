package com.ciandt.ciandtbrewery

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
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
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest: AutoCloseKoinTest() {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private val appDatabase: AppDatabase by inject()
    private val client: OkHttpClient by inject()

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

    private fun withIndex(matcher: Matcher<View>, index: Int): Matcher<View>{
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
    fun onLaunchCheckHomeFragmentDisplayed(){
        onView(isRoot()).perform(waitFor(3000))
        onView(withIndex(withId(R.id.tbHome), 0)).check(matches(isDisplayed()))
    }

    @Test
    fun onLaunchCheckTop10ListDisplayed(){
        onView(isRoot()).perform(waitFor(6000))
        onView(withIndex(withId(R.id.rvTopTen), 0)).check(matches(isDisplayed()))
    }

    @Test
    fun onTop10DisplayedClickForDetail(){
        onView(isRoot()).perform(waitFor(6000))
        onView(withIndex(withId(R.id.tvCardBreweryName), 0)).perform(click())
        onView(isRoot()).perform(waitFor(3000))
        onView(withId(R.id.clBreweryDetails)).check(matches(isDisplayed()))
    }

    @Test
    fun onSearchByCityListDisplayedClickForDetail(){
        onView(isRoot()).perform(waitFor(6000))
        onView(withId(R.id.tietSearchLocal)).perform(click()).perform(typeText("Boston"))
        onView(withId(R.id.ivButtonSearch)).perform(click())
        onView(isRoot()).perform(waitFor(5000))
        onView(withIndex(withId(R.id.name_brewery_search_recycler_view_item),0)).check(matches(isDisplayed()))
        onView(withIndex(withId(R.id.name_brewery_search_recycler_view_item), 0)).perform(click())
        onView(isRoot()).perform(waitFor(3000))
        onView(withId(R.id.clBreweryDetails)).check(matches(isDisplayed()))
    }

    @Test
    fun onLaunchCheckHomeFragmentDisplayedClickForFavorite(){
        onView(isRoot()).perform(waitFor(6000))
        onView(withId(R.id.ivHeart)).perform(click())
        onView(isRoot()).perform(waitFor(3000))
        onView(withId(R.id.clFavorite)).check(matches(isDisplayed()))
    }

    @Test
    fun onSearchByCityListDisplayedClickToFavorite(){
        onView(isRoot()).perform(waitFor(6000))
        onView(withId(R.id.tietSearchLocal)).perform(click()).perform(typeText("San Diego"))
        onView(withId(R.id.ivButtonSearch)).perform(click())
        onView(isRoot()).perform(waitFor(5000))
        onView(withId(R.id.evaluated_recyclerview)).perform(
            actionOnItemAtPosition<SearchRecyclerViewAdapter.ViewHolder>(0, clickOnViewChild(R.id.ivLikeBrewery))
        )
        onView(withId(R.id.evaluated_recyclerview)).check(matches(
            childOfViewAtPositionWithMatcher(0, R.id.ivLikeBrewery, withDrawable(R.drawable.ic_full_heart))
        ))
        onView(isRoot()).perform(waitFor(3000))
    }
}