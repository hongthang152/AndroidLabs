package com.example.androidlabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TOOLBAR_PAGE = 500;

    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(ACTIVITY_NAME, "In function: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        findViewById(R.id.take_picture_btn).setOnClickListener(e -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        findViewById(R.id.go_to_weather_forecast_btn).setOnClickListener(e -> {
            Intent weatherForecastIntent = new Intent(getBaseContext(), WeatherForecastActivity.class);
            startActivity(weatherForecastIntent);
        });

        Intent fromMain = getIntent();
        ((EditText)findViewById(R.id.email_field)).setText(fromMain.getStringExtra("EMAIL"));

        findViewById(R.id.go_to_chat_btn).setOnClickListener(e -> {
            startActivity(new Intent(getBaseContext(), ChatRoomActivity.class));
        });

        findViewById(R.id.go_to_toolbar_page_btn).setOnClickListener(e -> {
            startActivityForResult(new Intent(getBaseContext(), TestToolbar.class), REQUEST_TOOLBAR_PAGE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(ACTIVITY_NAME, "In function: onActivityResult");

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageButton imgBtn = findViewById(R.id.take_picture_btn);

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgBtn.setImageBitmap(imageBitmap);
        } else if(requestCode == REQUEST_TOOLBAR_PAGE) {
            finish();
        }
    }
}
