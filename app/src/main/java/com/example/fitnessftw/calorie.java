package com.example.fitnessftw;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.os.Bundle;

public class calorie extends AppCompatActivity {
    private EditText ageEditText, weightEditText, heightEditText;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);
        ageEditText = findViewById(R.id.Age);
        weightEditText = findViewById(R.id.weight);
        heightEditText = findViewById(R.id.height);
        genderRadioGroup = findViewById(R.id.radio_group_gender);
        maleRadioButton = findViewById(R.id.radio_male);

        findViewById(R.id.calc).setOnClickListener(view -> calculateCalories());
    }
    private void calculateCalories() {
        String ageStr = ageEditText.getText().toString();
        String weightStr = weightEditText.getText().toString();
        String heightStr = heightEditText.getText().toString();

        if (ageStr.isEmpty() || weightStr.isEmpty() || heightStr.isEmpty()) {
            Toast.makeText(this, "Please enter values for age, weight, and height.", Toast.LENGTH_SHORT).show();
            return;
        }

        double age = Double.parseDouble(ageStr);
        double weight = Double.parseDouble(weightStr);
        double height = Double.parseDouble(heightStr);

        int genderRadioId = genderRadioGroup.getCheckedRadioButtonId();

        if (genderRadioId == -1) {
            Toast.makeText(this, "Please select a gender.", Toast.LENGTH_SHORT).show();
            return;
        }

        double bmr;
        if (maleRadioButton.getId() == genderRadioId) {
            bmr = ((10 * weight) + (6.25 * height) - (5 * age)) + 5;
        } else {
            bmr = ((10 * weight) + (6.25 * height) - (5 * age))-161;
        }
        Toast.makeText(this, "Your Basal Metabolic Rate is: " + bmr, Toast.LENGTH_SHORT).show();

    }
}