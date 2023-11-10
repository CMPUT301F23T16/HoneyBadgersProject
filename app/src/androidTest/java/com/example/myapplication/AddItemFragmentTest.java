package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class AddItemFragmentTest {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference itemsRef = db.collection("Adi");

    @Rule
    public IntentsTestRule<ItemListActivity> activityRule = new
            IntentsTestRule<>(ItemListActivity.class);

    private IdlingResource idlingResource = new CountingIdlingResource("API Idling Resource");


    @Test
    public void test_1_check_add_fragment_fields() throws InterruptedException {
        //TODO will need to setup mocks for this but this is fine for now

        //The database's API takes some time to return the data.
        //this is to ensure that it gets enough time.
        //TODO ideally something which specifically waits for the response would be used
        Thread.sleep(1000);

        // Check if Add button is present on ItemListView
        onView(withId(R.id.add_item_button)).check(matches(isDisplayed()));

        // Click on Add Button
        onView(withId(R.id.add_item_button)).perform(click());

        // Check if all required fields present on fragment
        onView(withId(R.id.add_item_name)).check(matches(isDisplayed()));
        onView(withId(R.id.add_item_date)).check(matches(isDisplayed()));
        onView(withId(R.id.add_item_description)).check(matches(isDisplayed()));
        onView(withId(R.id.add_item_make)).check(matches(isDisplayed()));
        onView(withId(R.id.add_item_model)).check(matches(isDisplayed()));
        onView(withId(R.id.add_item_price)).check(matches(isDisplayed()));
        onView(withId(R.id.add_item_tag_spinner)).check(matches(isDisplayed()));
    }

    public void test_2_check_add_item() throws InterruptedException {
        //The database's API takes some time to return the data.
        //this is to ensure that it gets enough time.
        //TODO ideally something which specifically waits for the response would be used
        Thread.sleep(1000);

        // Check if Add button is present
        onView(withId(R.id.add_item_button)).check(matches(isDisplayed()));

        // Click on add button
        onView(withId(R.id.add_item_button)).perform(click());

//        onView(withText("Ok")).inRoot(isDialog()).check(matches(isDisplayed()))
//                .perform(click());

        // Fill in mandatory fields
        onView(withId(R.id.add_item_name)).perform(typeText("UI_Test_Add_Item"));
        onView(withId(R.id.add_item_date)).perform(typeText("2011-09-13"));
        onView(withId(R.id.add_item_description)).perform(typeText("UI TEST ADD ITEM"));
        onView(withId(R.id.add_item_price)).perform(typeText("20.20"));

        // Click on OK
        onView(withId(android.R.id.button1)).perform(click());

        // Check if item in ItemListView
        onView(withText("UI_Test_Add_Item")).check(matches(isDisplayed()));

        // Remove the test item from the document
        itemsRef.document("UI_Test_Add_Item").delete();
        Thread.sleep(1000);
    }
}