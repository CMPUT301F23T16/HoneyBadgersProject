package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ItemListActivityTest {
    @Rule
    public IntentsTestRule<ItemListActivity> activityRule = new
            IntentsTestRule<>(ItemListActivity.class);

    private IdlingResource idlingResource = new CountingIdlingResource("API Idling Resource");

    @Test
    public void testItemListActivity() throws InterruptedException {
        //TODO will need to setup mocks for this but this is fine for now

        //The database's API takes some time to return the data.
        //this is to ensure that it gets enough time.
        //TODO ideally something which specifically waits for the response would be used
        Thread.sleep(1000);

        // Check if Chair item is present
        onView(withText("Chair")).check(matches(isDisplayed()));
        onView(withText("2023-11-01")).check(matches(isDisplayed()));
        onView(withText("$12342.00")).check(matches(isDisplayed()));

        // Check if Bed item is present
        onView(withText("Bed")).check(matches(isDisplayed()));
        onView(withText("2023-11-03")).check(matches(isDisplayed()));
        onView(withText("$60.00")).check(matches(isDisplayed()));

        // Check that the total is correct
        onView(withId(R.id.total_amount)).check(matches(withText("$12402.00")));

        // Click the checkbox next to "Bed"
        onView(allOf(withId(R.id.check_box), hasSibling(withText("Bed")))).perform(click());

        // There is a delete button, click it to delete the selected item
        onView(withId(R.id.delete_item_button)).perform(click());

        // 'DELETE' is the text on the button
        onView(withText("DELETE")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());

        // Check that "Bed" is no longer in the list
        onView(withId(R.id.item_list)).check(matches(not(hasDescendant(withText("Bed")))));
    }

}
