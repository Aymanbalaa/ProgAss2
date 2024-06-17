package com.example.progass2;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        long profileId = getIntent().getLongExtra("profileId", -1);
        Profile profile = DatabaseHelper.getInstance(this).getProfile(profileId);

        ((TextView) findViewById(R.id.nameTextView)).setText(profile.getName());
        ((TextView) findViewById(R.id.surnameTextView)).setText(profile.getSurname());
        ((TextView) findViewById(R.id.idTextView)).setText(String.valueOf(profile.getId()));
        ((TextView) findViewById(R.id.gpaTextView)).setText(String.format("%.1f", profile.getGpa()));
    }
}
