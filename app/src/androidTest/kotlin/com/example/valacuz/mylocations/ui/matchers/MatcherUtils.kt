package com.example.valacuz.mylocations.ui.matchers

import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

class MatcherUtils {

    companion object {
        fun withRecyclerItemText(itemText: String): Matcher<View> {
            return object : TypeSafeMatcher<View>() {
                override fun matchesSafely(item: View?): Boolean {
                    return allOf(
                            isDescendantOfA(isAssignableFrom(RecyclerView::class.java)),
                            withText(itemText))
                            .matches(item)
                }

                override fun describeTo(description: Description?) {
                    description?.appendText("is isDescendantOfA RecyclerView with text " + itemText)
                }
            }
        }
    }
}