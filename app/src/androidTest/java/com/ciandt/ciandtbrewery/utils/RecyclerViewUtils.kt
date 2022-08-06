package com.ciandt.ciandtbrewery.utils

import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher


fun clickOnViewChild(viewId: Int) = object: ViewAction {
    override fun getConstraints() = null

    override fun getDescription() = "Click on a child view with specified id."

    override fun perform(uiController: UiController?, view: View?) = click().perform(uiController, view?.findViewById(viewId))

}

fun childOfViewAtPositionWithMatcher(position: Int, childId: Int, @NonNull itemMatcher: Matcher<View?>): Matcher<View?> {
    return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has item at position $position: ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            val viewHolder = view.findViewHolderForAdapterPosition(position)
                ?: // has no item on such position
                return false
            val child = viewHolder.itemView.findViewById<View>(childId)
            return itemMatcher.matches(child)
        }
    }
}