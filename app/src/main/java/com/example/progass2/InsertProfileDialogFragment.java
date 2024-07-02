package com.example.progass2;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class InsertProfileDialogFragment extends DialogFragment {

    private EditText idEditText, nameEditText, surnameEditText, gpaEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Insert Profile");
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_insert_profile, null))
                .setPositiveButton("Save", (dialog, id) -> saveProfile())
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        Dialog dialog = getDialog();
        if (dialog != null) {
            idEditText = dialog.findViewById(R.id.idEditText);
            nameEditText = dialog.findViewById(R.id.nameEditText);
            surnameEditText = dialog.findViewById(R.id.surnameEditText);
            gpaEditText = dialog.findViewById(R.id.gpaEditText);
        }
    }

    private void saveProfile() {
        long id = 0;
        try {
            id = Long.parseLong(idEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid ID", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = nameEditText.getText().toString().trim();
        String surname = surnameEditText.getText().toString().trim();
        float gpa = 0;
        try {
            gpa = Float.parseFloat(gpaEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid GPA", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.isEmpty() || surname.isEmpty() || gpa < 0.0 || gpa > 4.3 || id > 99999999 || id < 10000000) {
            Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseHelper dbHelper = DatabaseHelper.getInstance(getContext());
            Profile existingProfile = dbHelper.getProfile(id);
            if (existingProfile != null) {
                Toast.makeText(getContext(), "ID already exists", Toast.LENGTH_SHORT).show();
                return;
            }
            long newId = dbHelper.addProfile(id, name, surname, gpa);
            if (newId != -1) {
                ((MainActivity) getActivity()).loadProfiles();
            } else {
                Toast.makeText(getContext(), "Failed to add profile", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
