package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;


/**
 * Activity responsible for handling user login and sign-up operations.
 * Users can toggle between login and sign-up forms, authenticate to sign in,
 * or register a new account. Cancellation of actions is also supported.
 */
public class LoginSignupActivity extends AppCompatActivity {

    private LinearLayout layoutLogin, layoutSignup;
    private Button buttonLogin, buttonSignup;

    // Add EditText member variables for signup fields
    private EditText signupFirstName, signupLastName, signupDateOfBirth,
            signupUsername, signupEmail, signupCreatePassword, signupRepeatPassword;

    // Add Firebase Firestore instance
    private FirebaseFirestore firestoreDB;
    private FirebaseAuth firebaseAuth;

    /**
     * Initializes the activity. This method sets up the user interface
     * and initializes Firebase instances.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     *                           If the activity has never existed before, the value of the Bundle is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup); // make sure the name matches your XML file name

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();

        layoutLogin = findViewById(R.id.layoutLogin);
        layoutSignup = findViewById(R.id.layoutSignup);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);

        signupFirstName = findViewById(R.id.signupFirstName);
        signupLastName = findViewById(R.id.signupLastName);
        signupDateOfBirth = findViewById(R.id.signupDateOfBirth);
        signupUsername = findViewById(R.id.signupUsername);
        signupEmail = findViewById(R.id.signupEmail);
        signupCreatePassword = findViewById(R.id.signupCreatePassword);
        signupRepeatPassword = findViewById(R.id.signupRepeatPassword);
        // Initially show the login form
        showLoginForm();
    }

    /**
     * Called after onCreate(Bundle) — or after onRestart() when the activity had been stopped, but
     * is now again being displayed to the user. It will be followed by onResume().
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in, navigate to the ItemListActivity
            navigateToItemListActivity();
        }
    }

    /**
     * Shows the login form UI and hides the sign-up form UI.
     *
     * @param view The UI component that receives the click event.
     */
    public void onLoginClicked(View view) {
        showLoginForm();
    }

    /**
     * Shows the sign-up form UI and hides the login form UI.
     *
     * @param view The UI component that receives the click event.
     */
    public void onSignupClicked(View view) {
        showSignupForm();
    }

    /**
     * Triggers the display of a DatePickerDialog when the date of birth field is clicked.
     *
     * @param view The UI component that receives the click event.
     */
    public void onDateOfBirthClicked(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                LoginSignupActivity.this,
                (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    selectedMonth = selectedMonth + 1; // Month is 0-based in Calendar
                    String formattedDate = selectedYear + "-" +
                            String.format("%02d", selectedMonth) + "-" +
                            String.format("%02d", selectedDay);
                    ((EditText) view).setText(formattedDate);
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }
    /**
     * Submits the login form, authenticating the user with email and password.
     *
     * @param view The UI component that receives the click event.
     */
    public void onLoginSubmitClicked(View view) {
        EditText loginEmail = findViewById(R.id.loginEmail);
        EditText loginPassword = findViewById(R.id.loginPassword);

        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform login using Firebase Auth (assuming username is an email)
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in success
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        navigateToItemListActivity();
                    } else {
                        // If sign-in fails, display a message to the user.
                        Toast.makeText(LoginSignupActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Submits the sign-up form, registering the user with provided details.
     *
     * @param view The UI component that receives the click event.
     */
    public void onSignupSubmitClicked(View view) {
        // Validate input fields are filled
        if (validateSignupFields()) {
            // Save data to Firebase Firestore
            saveUserDataToFirebase();
        } else {
            // Show error to the user indicating that all fields are required
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Validates if all the required fields in the sign-up form are filled.
     *
     * @return true if all fields are filled, false otherwise.
     */
    private boolean validateSignupFields() {
        // Check that none of the EditText fields are empty
        return !signupFirstName.getText().toString().isEmpty() &&
                !signupLastName.getText().toString().isEmpty() &&
                !signupDateOfBirth.getText().toString().isEmpty() &&
                !signupUsername.getText().toString().isEmpty() &&
                !signupEmail.getText().toString().isEmpty() &&
                !signupCreatePassword.getText().toString().isEmpty() &&
                !signupRepeatPassword.getText().toString().isEmpty();
    }

    /**
     * Saves the new user's data to Firebase Firestore and creates an authentication account.
     */
    private void saveUserDataToFirebase() {
        String email = signupEmail.getText().toString().trim();
        String password = signupCreatePassword.getText().toString().trim();
        String confirmPassword = signupRepeatPassword.getText().toString().trim();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user with email and password in Firebase Auth
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            Map<String, Object> userDetail = new HashMap<>();
                            userDetail.put("firstName", signupFirstName.getText().toString().trim());
                            userDetail.put("lastName", signupLastName.getText().toString().trim());
                            userDetail.put("dateOfBirth", signupDateOfBirth.getText().toString().trim());
                            // Do not save the password to Firestore
                            // Remove the username if you are not using it

                            // Store user details in Firestore
                            firestoreDB.collection("users")
                                    .document(user.getUid())
                                    .set(userDetail)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firebase", "User details updated in Firestore");
                                        navigateToItemListActivity();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w("Firebase", "Error updating user details", e);
                                        Toast.makeText(LoginSignupActivity.this,
                                                "Failed to update user details: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        }
                    } else {
                        // If sign up fails, display a message to the user.
                        if (task.getException() != null) {
                            Log.w("FirebaseAuth", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginSignupActivity.this,
                                    "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
