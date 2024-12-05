package com.example.fitnessftw;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signUp extends AppCompatActivity {
    private TextView loginEmail, loginPassword, create_account_text;
    private CheckBox rememberMe;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private DbHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        Button signUpButton = findViewById(R.id.login_button);
        TextView createAccountText = findViewById(R.id.create_account_text);
        CheckBox rememberMe = findViewById(R.id.rem);

        databaseHelper = new DbHelper(this);

        // Initialize shared preferences
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        // Check if "Remember Me" is checked and pre-fill login credentials if available
        if (loginPreferences.getBoolean("rememberMe", false)) {
            String savedEmail = loginPreferences.getString("email", "");
            String savedPassword = loginPreferences.getString("password", "");
            loginEmail.setText(savedEmail);
            loginPassword.setText(savedPassword);
            rememberMe.setChecked(true);
        }

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                if (email.isEmpty() || !isValidEmail(email)) {
                    loginEmail.setError("Enter a valid email address");
                    loginEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    loginPassword.setError("Password is required");
                    loginPassword.requestFocus();
                    return;
                }

                boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);

                if (checkCredentials) {
                    Toast.makeText(signUp.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), accueil.class);
                    startActivity(intent);

                    // Save login credentials if "Remember Me" is checked
                    if (rememberMe.isChecked()) {
                        loginPrefsEditor.putBoolean("rememberMe", true);
                        loginPrefsEditor.putString("email", email);
                        loginPrefsEditor.putString("password", password);
                        loginPrefsEditor.apply();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.apply();
                    }
                } else {
                    Toast.makeText(signUp.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signUp.this, createAccount.class);
                startActivity(intent);
            }
        });

        // Listen for checkbox changes and save the state accordingly
        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                loginPrefsEditor.putBoolean("rememberMe", isChecked);
                loginPrefsEditor.apply();
            }
        });
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                email.contains("@") &&
                email.contains(".");
    }
}
