package com.example.progass2;



import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class InsertProfileDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Insert Profile");
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_insert_profile, null))
                .setPositiveButton("Save", (dialog, id) -> saveProfile())
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        return builder.create();
    }

    private void saveProfile() {
        EditText nameEditText = getDialog().findViewById(R.id.nameEditText);
        EditText surnameEditText = getDialog().findViewById(R.id.surnameEditText);
        EditText idEditText = getDialog().findViewById(R.id.idEditText);
        EditText gpaEditText = getDialog().findViewById(R.id.gpaEditText);

        // Insert saving logic here
    }
}
