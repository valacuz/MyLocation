package com.example.valacuz.mylocations.ui

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.IntentMatchers.hasAction
import android.support.test.espresso.intent.matcher.IntentMatchers.toPackage
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.example.valacuz.mylocations.R
import com.example.valacuz.mylocations.list.PlaceListActivity
import com.example.valacuz.mylocations.ui.actions.ViewActionUtils.Companion.waitFor
import com.example.valacuz.mylocations.ui.matchers.MatcherUtils.Companion.withRecyclerItemText
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class PlaceFormActivityTest {

    @Rule
    @JvmField
    val mTestRule = IntentsTestRule(PlaceListActivity::class.java)

    // To grant permission for testing.
    @Rule
    @JvmField
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    /*
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(mTestRule.activity.getCountingIdlingResource())
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(mTestRule.activity.getCountingIdlingResource())
    }
    */

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
        val placeName = "ADD_PLACE"

        // Given success
        addSuccessPlace(placeName)

        // Then the new place must be display on the place list
        onView(withRecyclerItemText(placeName)).check(matches(isDisplayed()))
    }

    @Test
    fun editPlace_saved() {
        val oldPlaceName = "BEFORE_PLACE"
        val newPlaceName = "AFTER_PLACE"

        // Precondition: Given place name "BEFORE_PLACE"
        // If item with name "BEFORE_PLACE" is not displayed, add new place with that name
        if (!withRecyclerItemText(oldPlaceName).matches(isDisplayed())) {
            addSuccessPlace(oldPlaceName)
        }
        // and Click on item with name "BEFORE_PLACE"
        onView(withRecyclerItemText(oldPlaceName)).perform(click())

        // Given text "AFTER_PLACE" to replace on place name and close soft keyboard
        onView(withId(R.id.text_name)).perform(replaceText(newPlaceName), closeSoftKeyboard())

        // When save the place
        onView(withId(R.id.menu_action_save)).perform(click())

        // Then the new place name must be display on the place list
        onView(withRecyclerItemText(newPlaceName)).check(matches(isDisplayed()))

        // and old place name must does not exists
        onView(withRecyclerItemText(oldPlaceName)).check(doesNotExist())
    }

    // Due to this case must be tested on emulator which support google play service.
    // I have to temporary ignore this case until I can setup travis ci for testing it.
    @Ignore
    @Test
    fun showMap_displayDialog() {
        val placeName = "SHOW_PLACE"

        // Given place name "SHOW_PLACE" on the place list
        if (!withRecyclerItemText(placeName).matches(isDisplayed())) {
            addSuccessPlace(placeName)
        }
        // When long click on item name "SHOW_PLACE"
        onView(withRecyclerItemText(placeName)).perform(longClick())

        // and click on menu "Show on map"
        val menuText = InstrumentationRegistry.getTargetContext()
                .resources.getStringArray(R.array.item_choices)
        onView(withText(menuText[0])).inRoot(isDialog()).perform(click())

        // Then an intent resolving to the "google map" activity has been sent.
        intended(toPackage("com.google.android.apps.maps"))
    }

    // Due to this case must be tested on emulator which support google play service.
    // I have to temporary ignore this case until I can setup travis ci for testing it.
    @Ignore
    @Test
    fun sharePlace_displayDialog() {
        val placeName = "SHARE_PLACE"

        // Given place name "SHARE_PLACE" on the place list
        if (!withRecyclerItemText(placeName).matches(isDisplayed())) {
            addSuccessPlace(placeName)
        }
        // When long click at that item
        onView(withRecyclerItemText(placeName)).perform(longClick())

        // and click on menu "Share"
        val menuText = InstrumentationRegistry.getTargetContext()
                .resources.getStringArray(R.array.item_choices)
        onView(withText(menuText[1])).inRoot(isDialog()).perform(click())

        // Then an intent chooser for sharing must be display.
        intending(hasAction(Intent.ACTION_CHOOSER))
    }

    @Test
    fun deletePlace_doesNotExist() {
        val placeName = "DELETE_PLACE"

        // Given place name "DELETE_PLACE" on the place list
        if (!withRecyclerItemText(placeName).matches(isDisplayed())) {
            addSuccessPlace(placeName)
        }
        // When long click at that item
        onView(withRecyclerItemText(placeName)).perform(longClick())

        // and click on menu "Delete"
        val menuText = InstrumentationRegistry.getTargetContext()
                .resources.getStringArray(R.array.item_choices)
        onView(withText(menuText[2])).inRoot(isDialog()).perform(click())

        // Then the place name must disappear from the place list
        onView(withRecyclerItemText(placeName)).check(doesNotExist())
    }

    // Helper method that add place by specific name.
    private fun addSuccessPlace(placeName: String, needLocation: Boolean = true) {
        // Precondition: Click on add button
        onView(withId(R.id.add_button)).perform(click())

        // Given text "HOME" on place name and close soft keyboard
        onView(withId(R.id.text_name)).perform(typeText(placeName), closeSoftKeyboard())

        // If needLocation is set to 'true' perform pick location
        if (needLocation) {
            randomPickLocation()
        }

        // When save the place
        onView(withId(R.id.menu_action_save)).perform(click())
    }

    // Helper function which swipe randomly on google map view to pick location
    private fun randomPickLocation() {
        onView(withId(R.id.text_coordinate)).perform(click())
        onView(isRoot()).perform(waitFor(2000L))    // Delay for map view initialization.

        // Start with current location
        onView(withId(R.id.current_button)).perform(click())
        onView(isRoot()).perform(waitFor(3000L))    // Delay for zoom & pan animation.

        val random = Random()
        val numberOfSwipe = random.nextInt(4)

        // Swipe in random position 3 times
        for (i in 0..numberOfSwipe) {
            onView(isRoot()).perform(waitFor())

            val randomNumber = random.nextInt(4)   // Random number for position
            val mapViewInteraction = onView(withId(R.id.map_view))
            when (randomNumber % 4) {
                0 -> mapViewInteraction.perform(swipeUp())
                1 -> mapViewInteraction.perform(swipeRight())
                2 -> mapViewInteraction.perform(swipeDown())
                else -> mapViewInteraction.perform(swipeLeft())
            }
        }
        // Click to save position
        onView(withId(R.id.pick_button)).perform(click())
    }
}