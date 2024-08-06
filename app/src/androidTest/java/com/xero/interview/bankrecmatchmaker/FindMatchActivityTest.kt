package com.xero.interview.bankrecmatchmaker;

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertNotEquals
import org.junit.Rule

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4::class)
class FindMatchActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(FindMatchActivity::class.java)

    @Test
    fun testRecyclerViewPopulated() {
        onView(withId(R.id.recycler_view))
            .check(matches(hasMinimumChildCount(1)))
    }

    @Test
    fun testItemClickUpdatesTotal() {
        val initialTotal = getDisplayedTotal()
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
        val updatedTotal = getDisplayedTotal()
        assertNotEquals(initialTotal, updatedTotal)
    }

    @Test
    fun testToolbarTitle() {
        onView(withId(R.id.toolbar))
            .check(matches(hasDescendant(withText(R.string.title_find_match))))
    }

    private fun getDisplayedTotal(): String {
        var total = ""
        onView(withId(R.id.match_text)).check { view, _ ->
            total = (view as TextView).text.toString()
        }
        return total
    }

}
