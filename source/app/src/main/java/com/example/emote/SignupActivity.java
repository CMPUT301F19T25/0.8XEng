package com.example.emote;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity to handle user signups
 */
public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText usernameText;
    EditText passwordText;
    EditText confirmPasswordText;
    Button createAccountButton;
    private FirebaseAuth mAuth;
    CountingIdlingResource expressoTestIdlingResouce = new CountingIdlingResource("signup");


    /**
     * The onCreate event handles by the activity, set up signup activity
     * @param savedInstanceState: the instance state passed to the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        usernameText = findViewById(R.id.signup_username);
        passwordText = findViewById(R.id.signup_password);
        confirmPasswordText = findViewById(R.id.signup_confirm_password);
        createAccountButton = findViewById(R.id.btn_create_account);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    /**
     * Event to handle when the signup fails
     * @param message: a message to print to the user
     */
    public void onSignupFailed(String message) {
        Toast.makeText(getBaseContext(), "Failed to create account: "+ message, Toast.LENGTH_LONG).show();
        createAccountButton.setEnabled(true);
    }

    /**
     * Event to handle when signup succeeds, return to LoginActivity
     */
    public void onSignupSuccess() {
        createAccountButton.setEnabled(true);
        setResult(RESULT_OK, null);
        FireStoreHandler fsh = new FireStoreHandler(EmoteApplication.getUsername());
        fsh.setupUser();
        finish();
    }

    /**
     * Sign up the user
     */
    public void signup(){
        Log.d(TAG, "SignupActivity");

        if (!validate()){
            onSignupFailed("Invalid Username or Password");
            return;
        }

        createAccountButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        expressoTestIdlingResouce.increment();

        LoginHelper.signupUser(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            EmoteApplication.setUsername(username);
                            progressDialog.dismiss();
                            onSignupSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            onSignupFailed(task.getException().getMessage());
                        }
                        expressoTestIdlingResouce.decrement();
                    }
                });
    }

    /**
     * validates the username and password for the user
     * @return boolean representing valid username and password
     */
    private boolean validate(){
        boolean valid = true;

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        if (username.isEmpty() || !username.matches("[a-zA-Z0-9]+")){
            usernameText.setError("Username can only contain letters or numbers!");
            valid = false;
        }

        if (password.isEmpty() || password.length() < 8) {
            passwordText.setError("Password should be more than 8 characters");
            valid = false;
        }

        if (!password.equals(confirmPassword)){
            confirmPasswordText.setError("Passwords don't match");
            valid = false;
        }

        return valid;
    }

    /**
     * returns the idling resources for the signup activity
     * @return the idling resources for expresso
     */
    public CountingIdlingResource returnIdlingResource() {
        return expressoTestIdlingResouce;
    }}
