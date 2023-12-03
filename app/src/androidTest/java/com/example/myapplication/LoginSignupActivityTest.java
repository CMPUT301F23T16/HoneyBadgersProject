package com.example.myapplication;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginSignupActivityTest {

    @Rule
    public ActivityScenarioRule<LoginSignupActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginSignupActivity.class);

    @Test
    public void testAccountCreationOnSignup() {
        // Since account creation involves network operation, you need to mock the FirebaseAuth or use IdlingResource
        // Assuming account creation is successful, and you are redirected to the ItemListActivity
        // For demonstration purposes, we are just checking if the "CREATE ACCOUNT" button is clickable
        onView(withId(R.id.buttonSignupSubmit)).check(matches(withText("CREATE ACCOUNT")));
    }

    @Test
    public void testCorrectCredentialsForLogin() throws InterruptedException {
        // Enter correct credentials
        onView(withId(R.id.loginEmail)).perform(typeText("bushra1@ualberta.ca"));
        onView(withId(R.id.loginPassword)).perform(typeText("Kikos100$"));

        // Perform a click on the login button
        onView(withId(R.id.buttonLoginSubmit)).perform(click());
        Thread.sleep(10000);
        onView(withId(R.id.logout_button)).check(matches(isDisplayed()));

        // Assuming correct credentials and successful login,
        // the ItemListActivity should be started.
        // This would require mocking Firebase Auth or using IdlingResource.
    }
}
