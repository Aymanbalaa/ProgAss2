package com.example.progass2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ProfileActivity extends AppCompatActivity {

    private ListView accessListView;
    private long profileId;

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

        profileId = getIntent().getLongExtra("profileId", -1);
        Profile profile = DatabaseHelper.getInstance(this).getProfile(profileId);

        ((TextView) findViewById(R.id.nameTextView)).setText("Name: " + profile.getName());
        ((TextView) findViewById(R.id.surnameTextView)).setText("Surname: " + profile.getSurname());
        ((TextView) findViewById(R.id.idTextView)).setText("ID: " + profile.getId());
        ((TextView) findViewById(R.id.gpaTextView)).setText("GPA: " + String.format("%.1f", profile.getGpa()));

        accessListView = findViewById(R.id.accessListView);
        loadAccessHistory(profileId);

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProfile();
            }
        });
    }

    private void loadAccessHistory(long profileId) {
        List<Access> accessList = DatabaseHelper.getInstance(this).getAccessHistory(profileId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd @ HH:mm:ss", Locale.getDefault());
        List<String> displayInfo = accessList.stream()
                .map(a -> a.getAccessType() + " - " + sdf.format(a.getTimestamp()))
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayInfo);
        accessListView.setAdapter(adapter);
    }

    private void deleteProfile() {
        DatabaseHelper.getInstance(this).deleteProfile(profileId);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseHelper.getInstance(this).addAccess(profileId, "closed");
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
