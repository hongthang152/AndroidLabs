package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);
        findViewById(R.id.checkBox).setOnClickListener(v -> Toast.makeText(MainActivity.this, getResources().getText(R.string.toast_message), Toast.LENGTH_LONG).show());

        ((Switch) findViewById(R.id.switch1)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                Snackbar snackBar = Snackbar.make(findViewById(R.id.main_layout), getResources().getText(R.string.switch_messasge) + " " +
                        getResources().getText(isChecked ? R.string.message_on : R.string.message_off), Snackbar.LENGTH_LONG);
                snackBar.setAction(getResources().getText(R.string.undo), click -> buttonView.setChecked(!isChecked));
                snackBar.show();
                // do something, the isChecked will be
                // true if the switch is in the On position
            }
        });
    }
}
