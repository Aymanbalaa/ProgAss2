package com.example.progass2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private ListView profileListView;
    private ArrayAdapter<String> adapter;
    private FloatingActionButton addButton;
    private boolean isById = false;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = DatabaseHelper.getInstance(this);
        profileListView = findViewById(R.id.profileListView);
        addButton = findViewById(R.id.addButton);

        loadProfiles();

        profileListView.setOnItemClickListener((parent, view, position, id) -> {
            long profileId = dbHelper.getAllProfiles(isById).get(position).getId();
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("profileId", profileId);
            startActivity(intent);
        });

        addButton.setOnClickListener(v -> new InsertProfileDialogFragment().show(getSupportFragmentManager(), "InsertProfile"));
    }

    private void loadProfiles() {
        List<Profile> profiles = dbHelper.getAllProfiles(isById);
        List<String> displayInfo = profiles.stream()
                .map(p -> isById ? String.valueOf(p.getId()) : p.getSurname() + ", " + p.getName())
                .collect(Collectors.toList());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayInfo);
        profileListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toggleDisplayMode) {
            isById = !isById;
            loadProfiles();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
