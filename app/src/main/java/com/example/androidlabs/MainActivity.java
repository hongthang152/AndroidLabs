package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_preference_email), Context.MODE_PRIVATE);
        String email = sharedPref.getString(getString(R.string.shared_preference_email), "");
        EditText emailField = findViewById(R.id.email_field);
        emailField.setText(email);

        findViewById(R.id.login_button).setOnClickListener(e -> {
            SharedPreferences sharedPrefLogin = getApplicationContext().getSharedPreferences(getString(R.string.shared_preference_email), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefLogin.edit();
            editor.putString(getString(R.string.shared_preference_email), ((EditText)findViewById(R.id.email_field)).getText().toString());
            editor.commit();

            Intent goToProfile = new Intent(getBaseContext(), ProfileActivity.class);
            goToProfile.putExtra("EMAIL", emailField.getText().toString());
            startActivity(goToProfile);
        });
    }
}
