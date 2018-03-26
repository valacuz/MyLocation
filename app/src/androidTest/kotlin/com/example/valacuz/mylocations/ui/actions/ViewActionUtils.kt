package com.example.valacuz.mylocations.ui.actions

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import org.hamcrest.Matcher

class ViewActionUtils {

    companion object {

        fun waitFor(timeout: Long = 1200L) =
                object : ViewAction {
                    override fun getDescription(): String = "Wait for $timeout"

                    override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()

                    override fun perform(uiController: UiController?, view: View?) {
                        uiController?.loopMainThreadForAtLeast(timeout)
                    }
                }
    }
}