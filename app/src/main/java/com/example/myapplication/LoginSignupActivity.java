package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Provides a user interface for both login and signup processes.
 * The user can toggle between the login and signup forms, submit their credentials to sign in,
 * or provide details to create a new account. There are also options to cancel either action.
 */
public class LoginSignupActivity extends AppCompatActivity {

    private LinearLayout layoutLogin, layoutSignup;
    private Button buttonLogin, buttonSignup;

    /**
     * Called when the activity is starting. This is where most initialization should go.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup); // make sure the name matches your XML file name

        layoutLogin = findViewById(R.id.layoutLogin);
        layoutSignup = findViewById(R.id.layoutSignup);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);

        // Initially show the login form
        showLoginForm();
    }

    /**
     * Shows the login form and hides the signup form.
     * @param view The view that was clicked.
     */
    public void onLoginClicked(View view) {
        showLoginForm();
    }

    /**
     * Shows the signup form and hides the login form.
     * @param view The view that was clicked.
     */
    public void onSignupClicked(View view) {
        showSignupForm();
    }

    /**
     * Handles the logic when the login form is submitted. Placeholder for actual implementation.
     * @param view The view that was clicked.
     */
    public void onLoginSubmitClicked(View view) {
        // will implement login logic here
        // For now, just a placeholder implementation
        navigateToItemListActivity();
    }

    /**
     * Handles the logic when the signup form is submitted. Placeholder for actual implementation.
     * @param view The view that was clicked.
     */
    public void onSignupSubmitClicked(View view) {
        // will implement signup logic here
        // For now, just a placeholder implementation
        navigateToItemListActivity();
    }

    /**
     * Closes the current activity when the cancel action is invoked.
     * @param view The view that was clicked.
     */
    public void onCancelClicked(View view) {
        // will implement cancel logic here
        // For now, just finish the activity
        finish();
    }

    /**
     * Private helper method to show the login form.
     */
    private void showLoginForm() {
        layoutLogin.setVisibility(View.VISIBLE);
        layoutSignup.setVisibility(View.GONE);
        buttonLogin.setEnabled(false);
        buttonSignup.setEnabled(true);
    }

    /**
     * Private helper method to show the signup form.
     */
    private void showSignupForm() {
        layoutSignup.setVisibility(View.VISIBLE);
        layoutLogin.setVisibility(View.GONE);
        buttonSignup.setEnabled(false);
        buttonLogin.setEnabled(true);
    }

    /**
     * Navigates to the ItemListActivity.
     */
    private void navigateToItemListActivity() {
        Intent intent = new Intent(LoginSignupActivity.this, ItemListActivity.class);
        startActivity(intent);
        // For now, we'll just show a log message
        System.out.println("Navigating to ItemListActivity...");
    }
}
