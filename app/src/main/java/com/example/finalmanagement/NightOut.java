package com.example.finalmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NightOut extends AppCompatActivity {

    private TextInputEditText studentNameEditText, npUIDEditText, reasonEditText, dateEditText, returnDateEditText;
    private Button submitButton;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night_out); // Make sure this matches your XML file name

        firestore = FirebaseFirestore.getInstance();

        // Initialize views
        studentNameEditText = findViewById(R.id.StudentName);
        npUIDEditText = findViewById(R.id.NpUID);
        reasonEditText = findViewById(R.id.Reason);
        dateEditText = findViewById(R.id.dateofnp);
        returnDateEditText = findViewById(R.id.returnDate);
        submitButton = findViewById(R.id.nightoutsub);

        submitButton.setOnClickListener(v -> submitNightOutForm());
    }

    private void submitNightOutForm() {
        String name = studentNameEditText.getText().toString().trim();
        String uid = npUIDEditText.getText().toString().trim();
        String reason = reasonEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String returnDate = returnDateEditText.getText().toString().trim();

        if (name.isEmpty() || uid.isEmpty() || reason.isEmpty() || date.isEmpty() || returnDate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> nightOutData = new HashMap<>();
        nightOutData.put("name", name);
        nightOutData.put("uid", uid);
        nightOutData.put("reason", reason);
        nightOutData.put("date", date);
        nightOutData.put("returnDate", returnDate);

        firestore.collection("NightOut")
                .add(nightOutData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(NightOut.this, "Night Out submitted!", Toast.LENGTH_SHORT).show();
                    clearForm();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(NightOut.this, "Failed to submit: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void clearForm() {
        studentNameEditText.setText("");
        npUIDEditText.setText("");
        reasonEditText.setText("");
        dateEditText.setText("");
        returnDateEditText.setText("");
    }
}
