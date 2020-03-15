package com.example.androidlabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class TestToolbar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        this.toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(this.toolbar);
        this.drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, this.toolbar, R.string.open, R.string.close);
        this.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        this.navView = findViewById(R.id.nav_view);
        this.navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Toast.makeText(this, getText(R.string.you_clicked_on_item_1), Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                //launch another Activity
                Toast.makeText(this, getText(R.string.you_clicked_on_item_2), Toast.LENGTH_SHORT).show();
                break;
            case R.id.help:
                Toast.makeText(this, getText(R.string.you_clicked_on_item_3), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        Toast.makeText(this, getText(R.string.you_clicked_on_the_overflow_menu), Toast.LENGTH_SHORT).show();
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chat_page: {
                Intent intent = new Intent(this, ChatRoomActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.weather_forecast: {
                Intent intent = new Intent(this, WeatherForecastActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.go_back_to_login: {
                finish();
                break;
            }
        }
        //close navigation drawer
//        mDrawerLayout.closeDrawer(GravityCompat.START);
//        return true;

        return false;
    }
}
