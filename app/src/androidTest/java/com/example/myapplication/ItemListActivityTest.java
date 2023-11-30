package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

import android.util.Log;
import android.widget.Toast;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ItemListActivityTest {

    @Rule
    public IntentsTestRule<LoginSignupActivity> activityRule = new
            IntentsTestRule<>(LoginSignupActivity.class);

    @Before
    public void login_user() throws InterruptedException {

        // Enter correct credentials
        onView(withId(R.id.loginEmail)).perform(typeText("tester@test.com"));
        onView(withId(R.id.loginPassword)).perform(typeText("12345678"), closeSoftKeyboard());

        // Perform a click on the login button
        onView(withId(R.id.buttonLoginSubmit)).perform(click());

    }
    @Test
    public void test_1_check_list_view_item() throws InterruptedException {

        // Check if Chair item is present
        onView(withText("Table")).check(matches(isDisplayed()));
        onView(withText("2020-11-11")).check(matches(isDisplayed()));
        onView(withText("150.00")).check(matches(isDisplayed()));

        // Check if Bed item is present
        onView(withText("Bed")).check(matches(isDisplayed()));
        onView(withText("2023-11-03")).check(matches(isDisplayed()));
        onView(withText("$60.00")).check(matches(isDisplayed()));

        // Check that the total is correct
        onView(withId(R.id.total_amount)).check(matches(withText("$210.00")));

        // Click the checkbox next to "Bed"
        onView(allOf(withId(R.id.check_box), hasSibling(withText("Bed")))).perform(click());

        // There is a delete button, click it to delete the selected item
        onView(withId(R.id.delete_item_button)).perform(click());

        // 'DELETE' is the text on the button
        onView(withText("DELETE")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());

        // Check that "Bed" is no longer in the list
        onView(withId(R.id.item_list)).check(matches(not(hasDescendant(withText("Bed")))));
    }

    @Test
    public void test_2_check_total_amount() throws InterruptedException {

        // Check that the total is correct
        onView(withId(R.id.total_amount)).check(matches(withText("$210.00")));

        // Click the checkbox next to "Bed"
        onView(allOf(withId(R.id.check_box), hasSibling(withText("Bed")))).perform(click());

        // There is a delete button, click it to delete the selected item
        onView(withId(R.id.delete_item_button)).perform(click());

        // 'DELETE' is the text on the button
        onView(withText("DELETE")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());

        // Check that "Bed" is no longer in the list
        onView(withId(R.id.item_list)).check(matches(not(hasDescendant(withText("Bed")))));
    }

    @Test
    public void test_3_check_delete_items() throws InterruptedException {

        // Click the checkbox next to "Bed"
        onView(allOf(withId(R.id.check_box), hasSibling(withText("Bed")))).perform(click());

        // There is a delete button, click it to delete the selected item
        onView(withId(R.id.delete_item_button)).perform(click());

        // 'DELETE' is the text on the button
        onView(withText("DELETE")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());

        // Check that "Bed" is no longer in the list
        onView(withId(R.id.item_list)).check(matches(not(hasDescendant(withText("Bed")))));

        // Check "table" is still there
        onView(withText("Table")).check(matches(isDisplayed()));
    }

//    @After
//    public void cleanup() throws InterruptedException {
//        // Create bed item if it was deleted so these tests run again properly
//        try {
//            onView(withText("Bed")).check(matches(isDisplayed()));
//        } catch (NoMatchingViewException e) {
//            // create Bed item
//            onView(withId(R.id.add_item_button)).perform(click());
//            onView(withId(R.id.add_item_name)).perform(typeText("Bed"));
//            onView(withId(R.id.add_item_date)).perform(typeText("2023-11-03"));;
//            onView(withId(R.id.add_item_description)).perform(typeText("Test Description"));
//            onView(withId(R.id.add_item_price)).perform(typeText("60"));
//            onView(withText("OK")).perform(click());
//            onView(withText("Bed")).check(matches(isDisplayed()));
//        }
//
//        // Logout user
//        onView(withId(R.id.logout_button)).perform(click());
//
//    }


}
