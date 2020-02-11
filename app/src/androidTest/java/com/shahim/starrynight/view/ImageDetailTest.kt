package com.shahim.starrynight.view


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.shahim.starrynight.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ImageDetailTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )

    @Test
    fun imageDetailTest() {
        val imageView = onView(
            allOf(
                withId(R.id.list_image),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val cardView = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.recyclerView),
                        childAtPosition(
                            withId(R.id.container),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        cardView.perform(click())

        val imageView2 = onView(
            allOf(
                withId(R.id.detail_image),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.widget.ScrollView::class.java),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        imageView2.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.detail_title), withText("M33: The Triangulum Galaxy"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout),
                        childAtPosition(
                            withId(R.id.container),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.detail_save),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            withClassName(`is`("androidx.core.widget.NestedScrollView")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val progressBar = onView(
            allOf(
                withId(R.id.progressBar),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.widget.ScrollView::class.java),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        progressBar.check(matches(isDisplayed()))

        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.detail_save),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            withClassName(`is`("androidx.core.widget.NestedScrollView")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        val appCompatImageButton3 = onView(
            allOf(
                withId(R.id.detail_save),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            withClassName(`is`("androidx.core.widget.NestedScrollView")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
