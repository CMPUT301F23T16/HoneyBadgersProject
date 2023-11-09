package com.example.myapplication;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class LoginSignupActivityTest {

    @Rule
    public ActivityScenarioRule<LoginSignupActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginSignupActivity.class);

    @Test
    public void testLoginFieldsVisible() {
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.loginUsername)).check(matches(isDisplayed()));
        onView(withId(R.id.loginPassword)).check(matches(isDisplayed()));
    }

    @Test
    public void testSignupFieldsVisible() {
        onView(withId(R.id.buttonSignup)).perform(click());
        onView(withId(R.id.signupFirstName)).check(matches(isDisplayed()));
        onView(withId(R.id.signupLastName)).check(matches(isDisplayed()));
        onView(withId(R.id.signupDateOfBirth)).check(matches(isDisplayed()));
        onView(withId(R.id.signupUsername)).check(matches(isDisplayed()));
        onView(withId(R.id.signupEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.signupCreatePassword)).check(matches(isDisplayed()));
        onView(withId(R.id.signupRepeatPassword)).check(matches(isDisplayed()));
    }

    @Test
    public void testCreateAccountNavigatesToItemListActivity() {
        onView(withId(R.id.buttonSignup)).perform(click());
        onView(withId(R.id.buttonSignupSubmit)).perform(click());
        intended(hasComponent(ItemListActivity.class.getName()));
    }

    @Test
    public void testSignInNavigatesToItemListActivity() {
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.buttonLoginSubmit)).perform(click());
        intended(hasComponent(ItemListActivity.class.getName()));
    }

    @Test
    public void testCancelLoginQuitsActivity() {
        onView(withId(R.id.buttonLoginCancel)).perform(click());
    }

    @Test
    public void testCancelSignupQuitsActivity() {
        onView(withId(R.id.buttonSignup)).perform(click());
        onView(withId(R.id.buttonSignupCancel)).perform(click());
    }
}
