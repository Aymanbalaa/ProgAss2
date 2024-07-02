package com.example.progass2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    private ListView profileListView;
    private ArrayAdapter<String> adapter;
    private FloatingActionButton addButton;
    private Button toggleButton;
    private boolean isById = false;
    private DatabaseHelper dbHelper;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = DatabaseHelper.getInstance(this);

        profileListView = findViewById(R.id.profileListView);
        addButton = findViewById(R.id.addButton);
        toggleButton = findViewById(R.id.toggleButton);

        updateHeaderText();
        loadProfiles();

        profileListView.setOnItemClickListener((parent, view, position, id) -> {
            long profileId = dbHelper.getAllProfiles(isById).get(position).getId();
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("profileId", profileId);
            startActivityForResult(intent, 1);
        });

        addButton.setOnClickListener(v -> new InsertProfileDialogFragment().show(getSupportFragmentManager(), "InsertProfile"));

        toggleButton.setOnClickListener(v -> {
            isById = !isById;
            loadProfiles();
            updateHeaderText();
        });
    }

    private void updateHeaderText() {
        String mode = isById ? "by ID" : "by Surname";
        ((TextView) findViewById(R.id.totalProfiles)).setText(dbHelper.getAllProfiles(isById).size() + " profiles, " + mode);
    }

    public void loadProfiles() {
        List<Profile> profiles = dbHelper.getAllProfiles(isById);
        List<String> displayInfo = IntStream.range(0, profiles.size())
                .mapToObj(i -> (i + 1) + ". " + (isById ? profiles.get(i).getId() : profiles.get(i).getSurname() + ", " + profiles.get(i).getName()))
                .collect(Collectors.toList());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayInfo);
        profileListView.setAdapter(adapter);
        updateHeaderText();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadProfiles();
        }
    }
}
