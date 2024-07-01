package com.example.progass2;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        long profileId = getIntent().getLongExtra("profileId", -1);
        Profile profile = DatabaseHelper.getInstance(this).getProfile(profileId);

        ((TextView) findViewById(R.id.nameTextView)).setText("Name: " + profile.getName());
        ((TextView) findViewById(R.id.surnameTextView)).setText("Surname: " + profile.getSurname());
        ((TextView) findViewById(R.id.idTextView)).setText("ID: "+ profile.getId());
        ((TextView) findViewById(R.id.gpaTextView)).setText("GPA: "+ String.format("%.1f", profile.getGpa()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
