package com.example.valacuz.mylocations.ui

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.toPackage
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.example.valacuz.mylocations.R
import com.example.valacuz.mylocations.list.PlaceListActivity
import com.example.valacuz.mylocations.ui.matchers.MatcherUtils.Companion.withRecyclerItemText
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class PlaceFormActivityTest {

    @Rule
    @JvmField
    val mTestRule = IntentsTestRule(PlaceListActivity::class.java)

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(mTestRule.activity.getCountingIdlingResource())
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(mTestRule.activity.getCountingIdlingResource())
    }

    @Test
    fun emptyForm_notSaved() {
        // Precondition: Click on add button
        onView(withId(R.id.add_button)).perform(click())

        // Given empty text for place name and coordinate
        onView(withId(R.id.text_name)).perform(clearText())
        onView(withId(R.id.text_coordinate)).perform(clearText())

        // When save the place
        onView(withId(R.id.menu_action_save)).perform(click())

        // Then the activity still remain.
        // (a correct place would close this)
        onView(withId(R.id.text_name)).check(matches(isDisplayed()))
    }

    @Test
    fun addPlace_saved() {
        val placeName = "HOME"

        // Precondition: Click on add button
        onView(withId(R.id.add_button)).perform(click())

        // Given text "HOME" on place name and close soft keyboard
        onView(withId(R.id.text_name)).perform(typeText(placeName), closeSoftKeyboard())

        // When save the place
        onView(withId(R.id.menu_action_save)).perform(click())

        // Then the new place must be display on the place list
        onView(withRecyclerItemText(placeName)).check(matches(isDisplayed()))
    }

    @Test
    fun editPlace_saved() {
        val oldPlaceName = "HOME"
        val newPlaceName = "SCHOOL"

        // Precondition: Click on item with text "HOME"
        onView(withRecyclerItemText(oldPlaceName)).perform(click())

        // Given text "SCHOOL" to replace on place name and close soft keyboard
        onView(withId(R.id.text_name)).perform(replaceText(newPlaceName), closeSoftKeyboard())

        // When save the place
        onView(withId(R.id.menu_action_save)).perform(click())

        // Then the new place name must be display on the place list
        onView(withRecyclerItemText(newPlaceName)).check(matches(isDisplayed()))

        // and old place name must does not exists
        onView(withRecyclerItemText(oldPlaceName)).check(doesNotExist())
    }

    /*
    @Test
    fun showMap_displayDialog() {
        val placeName = "SCHOOL"

        // Given item name "SCHOOL" on the place list
        onView(withRecyclerItemText(placeName)).perform(longClick())

        // When "Show on map" menu clicked
        val menuText = InstrumentationRegistry.getTargetContext()
                .resources.getStringArray(R.array.item_choices)
        onView(withText(menuText[0])).inRoot(isDialog()).perform(click())

        // Then an intent resolving to the "phone" activity has been sent.
        intended(toPackage("com.google.android.apps.maps"))

        /*
        val activityResult = Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        intending(toPackage("com.google.android.apps.maps")).respondWith(activityResult)
        */
    }

    @Test
    fun sharePlace_displayDialog() {
        val placeName = "SCHOOL"

        // Given item name "SCHOOL" on the place list
        onView(withRecyclerItemText(placeName)).perform(longClick())

        // When "Share" menu clicked
        val menuText = InstrumentationRegistry.getTargetContext()
                .resources.getStringArray(R.array.item_choices)
        onView(withText(menuText[1])).inRoot(isDialog()).perform(click())

        // Then an intent resolving to the "phone" activity has been sent.
        intended(toPackage("com.google.android.apps.maps"))

        /*
        intended(allOf(
                hasAction(Intent.ACTION_SEND),
                hasExtra(Intent.EXTRA_TEXT, "")))
        */
    }
    */

    @Test
    fun deletePlace_doesNotExist() {
        val placeName = "HOME"

        // Given item name "SCHOOL" on the place list
        onView(withRecyclerItemText(placeName)).perform(longClick())

        // When "Delete this place" menu clicked
        val menuText = InstrumentationRegistry.getTargetContext()
                .resources.getStringArray(R.array.item_choices)
        onView(withText(menuText[2])).inRoot(isDialog()).perform(click())

        // Then the place name must disappear from the place list
        onView(withRecyclerItemText(placeName)).check(doesNotExist())
    }
}