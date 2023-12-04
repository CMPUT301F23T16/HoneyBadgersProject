package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.AllOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ItemListActivityTest {
    public int rand = 2000 + (new Random().nextInt(1000));

    @Rule
    public IntentsTestRule<LoginSignupActivity> activityRule = new
            IntentsTestRule<>(LoginSignupActivity.class);

    @Before
    public void login_user() throws InterruptedException {

        // Enter correct credentials
        onView(withId(R.id.loginEmail)).perform(typeText("bushra1@ualberta.ca"));
        onView(withId(R.id.loginPassword)).perform(typeText("Kikos100$"), closeSoftKeyboard());

        // Perform a click on the login button
        onView(withId(R.id.buttonLoginSubmit)).perform(click());


        // ENSURE LIST ITEMS ARE THERE IN CASE THEY ARE DELETED DUE TO PREVIOUS FAILED TESTS
        try {
            Thread.sleep(rand);
            onView(withText("Chair")).check(matches(isDisplayed()));
            onView(withText("milk")).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            // create milk item
            ViewInteraction appCompatButton = onView(Matchers.allOf(withText("+"),isDisplayed()));
            appCompatButton.perform(click());

            ViewInteraction appCompatEditText3 = onView(
                    Matchers.allOf(withId(R.id.add_item_name), isDisplayed()));
            appCompatEditText3.perform(replaceText("milk"), closeSoftKeyboard());

            ViewInteraction appCompatEditText4 = onView(
                    Matchers.allOf(withId(R.id.add_item_date), isDisplayed()));
            appCompatEditText4.perform(replaceText("2023-11-30"), closeSoftKeyboard());

            ViewInteraction appCompatEditText5 = onView(
                    Matchers.allOf(withId(R.id.add_item_description), isDisplayed()));
            appCompatEditText5.perform(replaceText("test_add"), closeSoftKeyboard());

            ViewInteraction appCompatEditText9 = onView(Matchers.allOf(withId(R.id.add_item_price),isDisplayed()));
            appCompatEditText9.perform(replaceText("6576.00"), closeSoftKeyboard());

            ViewInteraction materialButton2 = onView(
                    Matchers.allOf(withId(android.R.id.button1), withText("OK"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.ScrollView")),
                                            0),
                                    3)));
            materialButton2.perform(scrollTo(), click());
            Thread.sleep(rand);

            // create Chair item
            ViewInteraction appCompatButton33 = onView(Matchers.allOf(withText("+"),isDisplayed()));
            appCompatButton33.perform(click());

            ViewInteraction appCompatEditText34 = onView(
                    Matchers.allOf(withId(R.id.add_item_name), isDisplayed()));
            appCompatEditText34.perform(replaceText("Chair"), closeSoftKeyboard());

            ViewInteraction appCompatEditText35 = onView(
                    Matchers.allOf(withId(R.id.add_item_date), isDisplayed()));
            appCompatEditText35.perform(replaceText("2023-12-01"), closeSoftKeyboard());

            ViewInteraction appCompatEditText36 = onView(
                    Matchers.allOf(withId(R.id.add_item_description), isDisplayed()));
            appCompatEditText36.perform(replaceText("test_add"), closeSoftKeyboard());

            ViewInteraction appCompatEditText37 = onView(Matchers.allOf(withId(R.id.add_item_price),isDisplayed()));
            appCompatEditText37.perform(replaceText("1234.00"), closeSoftKeyboard());


            ViewInteraction materialButton3 = onView(
                    Matchers.allOf(withId(android.R.id.button1), withText("OK"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.ScrollView")),
                                            0),
                                    3)));
            materialButton3.perform(scrollTo(), click());

            Thread.sleep(rand);

            onView(withText("Chair")).check(matches(isDisplayed()));
            onView(withText("milk")).check(matches(isDisplayed()));
        }

    }
    @Test
    public void test_1_check_list_view_item() throws InterruptedException {
        Thread.sleep(rand);

        // Check if Milk item is present
        onView(withText("milk")).check(matches(isDisplayed()));
        onView(withText("2023-11-30")).check(matches(isDisplayed()));
        onView(withText("$6576.00")).check(matches(isDisplayed()));
        Thread.sleep(rand);

        // Check if Chair item is present
        onView(withText("Chair")).check(matches(isDisplayed()));
        onView(withText("2023-12-01")).check(matches(isDisplayed()));
        onView(withText("$1234.00")).check(matches(isDisplayed()));
    }

    @Test
    public void test_2_check_total_amount() throws InterruptedException {
        Thread.sleep(rand);
        // Check that the total is correct
        onView(withId(R.id.total_amount)).check(matches(withText("$7810.00")));

    }


    @Test
    public void test_3_check_delete_items() throws InterruptedException {
        Thread.sleep(rand);


        // Click the checkbox next to "milk"
        onView(allOf(withId(R.id.check_box), hasSibling(withText("milk")))).perform(click());

        // Click the checkbox next to "Chair"
        onView(allOf(withId(R.id.check_box), hasSibling(withText("Chair")))).perform(click());

        // There is a delete button, click it to batch delete selected items
        onView(withId(R.id.delete_item_button)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        // Check that items are deleted
        onView(withId(R.id.item_list)).check(matches(not(hasDescendant(withText("Chair")))));
        onView(withId(R.id.item_list)).check(matches(not(hasDescendant(withText("milk")))));
    }

    @After
    public void cleanup() throws InterruptedException {
        // Create bed item if it was deleted so these tests run again properly
        try {
            Thread.sleep(rand);
            onView(withText("Chair")).check(matches(isDisplayed()));
            onView(withText("milk")).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            // create milk item
            ViewInteraction appCompatButton = onView(Matchers.allOf(withText("+"),isDisplayed()));
            appCompatButton.perform(click());

            ViewInteraction appCompatEditText3 = onView(
                    Matchers.allOf(withId(R.id.add_item_name), isDisplayed()));
            appCompatEditText3.perform(replaceText("milk"), closeSoftKeyboard());

            ViewInteraction appCompatEditText4 = onView(
                    Matchers.allOf(withId(R.id.add_item_date), isDisplayed()));
            appCompatEditText4.perform(replaceText("2023-11-30"), closeSoftKeyboard());

            ViewInteraction appCompatEditText5 = onView(
                    Matchers.allOf(withId(R.id.add_item_description), isDisplayed()));
            appCompatEditText5.perform(replaceText("test_add"), closeSoftKeyboard());

            ViewInteraction appCompatEditText9 = onView(Matchers.allOf(withId(R.id.add_item_price),isDisplayed()));
            appCompatEditText9.perform(replaceText("6576.00"), closeSoftKeyboard());

            ViewInteraction materialButton2 = onView(
                    Matchers.allOf(withId(android.R.id.button1), withText("OK"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.ScrollView")),
                                            0),
                                    3)));
            materialButton2.perform(scrollTo(), click());

            // create Chair item
            ViewInteraction appCompatButton33 = onView(Matchers.allOf(withText("+"),isDisplayed()));
            appCompatButton33.perform(click());

            ViewInteraction appCompatEditText34 = onView(
                    Matchers.allOf(withId(R.id.add_item_name), isDisplayed()));
            appCompatEditText34.perform(replaceText("Chair"), closeSoftKeyboard());

            ViewInteraction appCompatEditText35 = onView(
                    Matchers.allOf(withId(R.id.add_item_date), isDisplayed()));
            appCompatEditText35.perform(replaceText("2023-12-01"), closeSoftKeyboard());

            ViewInteraction appCompatEditText36 = onView(
                    Matchers.allOf(withId(R.id.add_item_description), isDisplayed()));
            appCompatEditText36.perform(replaceText("test_add"), closeSoftKeyboard());

            ViewInteraction appCompatEditText37 = onView(Matchers.allOf(withId(R.id.add_item_price),isDisplayed()));
            appCompatEditText37.perform(replaceText("1234.00"), closeSoftKeyboard());


            ViewInteraction materialButton3 = onView(
                    Matchers.allOf(withId(android.R.id.button1), withText("OK"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.ScrollView")),
                                            0),
                                    3)));
            materialButton3.perform(scrollTo(), click());

            Thread.sleep(rand);

            onView(withText("Chair")).check(matches(isDisplayed()));
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