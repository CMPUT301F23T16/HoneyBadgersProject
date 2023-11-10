package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.IdlingResource;
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
public class EditItemFragmentTest {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference itemsRef = db.collection("Adi");

    @Rule
    public IntentsTestRule<ItemListActivity> activityRule = new
            IntentsTestRule<>(ItemListActivity.class);

    private IdlingResource idlingResource = new CountingIdlingResource("API Idling Resource");


    @Test
    public void test_1_check_edit_fragment_fields() throws InterruptedException {
        //TODO will need to setup mocks for this but this is fine for now

        //The database's API takes some time to return the data.
        //this is to ensure that it gets enough time.
        //TODO ideally something which specifically waits for the response would be used
        Thread.sleep(1000);

        // Check if Add button is present on ItemListView
        onView(withId(R.id.add_item_button)).check(matches(isDisplayed()));
//
//        // Click on Add Button
//        onView(withId(R.id.add_item_button)).perform(click());

//        // Check if all required fields present on fragment
//        onView(withId(R.id.add_item_name)).check(matches(isDisplayed()));
//        onView(withId(R.id.add_item_date)).check(matches(isDisplayed()));
//        onView(withId(R.id.add_item_description)).check(matches(isDisplayed()));
//        onView(withId(R.id.add_item_make)).check(matches(isDisplayed()));
//        onView(withId(R.id.add_item_model)).check(matches(isDisplayed()));
//        onView(withId(R.id.add_item_price)).check(matches(isDisplayed()));
//        onView(withId(R.id.add_item_tag_spinner)).check(matches(isDisplayed()));

        //The database's API takes some time to return the data.
        //this is to ensure that it gets enough time.
        //TODO ideally something which specifically waits for the response would be used
        Thread.sleep(1000);


        // Edit the item
        onView(withText("Knife")).perform(click());

        // Check if edited item fragment pops up
        onView(withId(R.id.add_item_name)).check(matches(withText("Knife")));
        onView(withId(R.id.add_item_date)).check(matches(withText("2023-11-05")));
        onView(withId(R.id.add_item_description)).check(matches(withText("new knife")));


//        // Remove the test item from the document
//        itemsRef.document("UI_Test_Edit_Item").delete();
        Thread.sleep(1000);
    }
}
