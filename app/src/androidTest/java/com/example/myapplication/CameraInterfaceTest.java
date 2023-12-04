package com.example.myapplication;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CameraInterfaceTest {

    public int rand = 2000 + (new Random().nextInt(1000));
    @Rule
    public ActivityScenarioRule<LoginSignupActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginSignupActivity.class);

    public void login_user() throws InterruptedException {

        // Enter correct credentials
        onView(withId(R.id.loginEmail)).perform(typeText("bushra1@ualberta.ca"));
        onView(withId(R.id.loginPassword)).perform(typeText("Kikos100$"), closeSoftKeyboard());

        // Perform a click on the login button
        onView(withId(R.id.buttonLoginSubmit)).perform(click());
        Thread.sleep(rand);
    }
    @Test
    public void cameraInterfaceTest() throws InterruptedException {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.loginEmail),isDisplayed()));
        appCompatEditText.perform(replaceText("bushra1@ualberta.ca"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.loginPassword), isDisplayed()));
        appCompatEditText2.perform(replaceText("Kikos100$"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.buttonLoginSubmit), withText("SIGN IN"), isDisplayed()));
        materialButton.perform(click());

        Thread.sleep(rand + 1000);

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.add_item_button), withText("+"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(android.R.id.button3), withText("Photos"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        materialButton2.perform(scrollTo(), click());


        onView(withId(R.id.photoGalleryButton)).check(matches(isDisplayed()));
        onView(withId(R.id.photoCameraButton)).check(matches(isDisplayed()));


        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton4.perform(scrollTo(), click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(android.R.id.button2), withText("Cancel"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        materialButton5.perform(scrollTo(), click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.logout_button), withText("Logout"),
                        childAtPosition(
                                allOf(withId(R.id.items_nav_bar),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        materialButton6.perform(click());
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
