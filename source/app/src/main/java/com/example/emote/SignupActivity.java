package com.example.emote;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText usernameText;
    EditText passwordText;
    EditText confirmPasswordText;
    Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Failed to create account", Toast.LENGTH_LONG).show();
        createAccountButton.setEnabled(true);
    }

    public void onSignupSuccess() {
        createAccountButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void signup(){
        Log.d(TAG, "SignupActivity");

        if (!validate()){
            onSignupFailed();
            return;
        }

        createAccountButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        // TODO: Check if username exists etc

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // call either onSignupSuccess or onSignupFailed
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    private boolean validate(){
        boolean valid = true;

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        if (username.isEmpty() || !username.matches("[a-zA-Z0-9]+")){
            usernameText.setError("Username can only contain letters or numbers!");
            valid = false;
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("Password should be more than 8 alphanumeric characters");
            valid = false;
        }

        if (!password.equals(confirmPassword)){
            confirmPasswordText.setError("Passwords don't match");
            valid = false;
        }

        return valid;
    }
}
