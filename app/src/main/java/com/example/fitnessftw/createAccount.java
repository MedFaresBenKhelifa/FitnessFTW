package com.example.fitnessftw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class createAccount extends AppCompatActivity {
    private EditText signupEmail, signupPassword, signupConfirm;
    private DbHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupConfirm = findViewById(R.id.signup_confirm);
        CheckBox chkRead = findViewById(R.id.terms);

        databaseHelper = new DbHelper(this);

        findViewById(R.id.signup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signupEmail.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();
                String confirmPassword = signupConfirm.getText().toString().trim();

                // Email validation
                if (email.isEmpty() || !isValidEmail(email)) {
                    signupEmail.setError("Enter a valid email address");
                    signupEmail.requestFocus();
                    return;
                }

                // Password validation
                if (password.isEmpty() || !isValidPassword(password)) {
                    signupPassword.setError("Password must be at least 6 characters long");
                    signupPassword.requestFocus();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    signupConfirm.setError("Passwords do not match");
                    signupConfirm.requestFocus();
                    return;
                }

                if (!chkRead.isChecked()) {
                    Toast.makeText(createAccount.this, "Please agree to the terms", Toast.LENGTH_SHORT).show();
                    return;
                }

                // All validations passed, proceed with signup
                boolean checkUserEmail = databaseHelper.checkEmail(email);
                if (!checkUserEmail) {
                    boolean insert = databaseHelper.insertData(email, password);
                    if (insert) {
                        Toast.makeText(createAccount.this, "Signup Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), signUp.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(createAccount.this, "Signup Failed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(createAccount.this, "User already exists! Please login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }
}
