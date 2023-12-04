package com.example.myapplication;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.AllOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddItemFragmentTest {
    public int rand = 2000 + (new Random().nextInt(1000));

    @Rule
    public ActivityScenarioRule<LoginSignupActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginSignupActivity.class);

    @Before
    public void login_user() throws InterruptedException {

        // Enter correct credentials
        onView(withId(R.id.loginEmail)).perform(typeText("bushra1@ualberta.ca"));
        onView(withId(R.id.loginPassword)).perform(typeText("Kikos100$"), closeSoftKeyboard());

        // Perform a click on the login button
        onView(withId(R.id.buttonLoginSubmit)).perform(click());
        Thread.sleep(rand);
    }

    @Test
    public void addItemTest() throws InterruptedException {

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.add_item_button), withText("+"),isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.add_item_name), isDisplayed()));
        appCompatEditText3.perform(replaceText("test_add"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.add_item_date),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("2023-04-22"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.add_item_description), isDisplayed()));
        appCompatEditText5.perform(replaceText("test_add"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.add_item_make), isDisplayed()));
        appCompatEditText6.perform(replaceText("1234"), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.add_item_model), isDisplayed()));
        appCompatEditText7.perform(replaceText("1234"), closeSoftKeyboard());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.add_item_serial_number), isDisplayed()));
        appCompatEditText8.perform(replaceText("1234"), closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(allOf(withId(R.id.add_item_price),isDisplayed()));
        appCompatEditText9.perform(replaceText("23"), closeSoftKeyboard());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.add_item_comment),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                7),
                        isDisplayed()));
        appCompatEditText10.perform(replaceText("test"), closeSoftKeyboard());


        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.add_item_tag_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                8),
                        isDisplayed()));
        appCompatEditText14.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton2.perform(scrollTo(), click());

        Thread.sleep(rand);

        onView(withText("test_add")).check(matches(isDisplayed()));
        onView(withText("2023-04-22")).check(matches(isDisplayed()));
        onView(withText("$23.00")).check(matches(isDisplayed()));

        Thread.sleep(rand);
    }

    @After
    public void cleanup() throws InterruptedException {
        // Create bed item if it was deleted so these tests run again properly
        try {
            onView(withText("test_add")).check(matches(isDisplayed()));
            onView(AllOf.allOf(withId(R.id.check_box), hasSibling(withText("test_add")))).perform(click());
            onView(withId(R.id.delete_item_button)).perform(click());
            onView(withId(android.R.id.button1)).perform(click());

        } catch (NoMatchingViewException e) {
            onView(withText("milk")).check(matches(isDisplayed()));
        }

        // Logout user
        onView(withId(R.id.logout_button)).perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }


}
