package com.example.finalmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DayOut extends AppCompatActivity {

    private TextInputEditText studentNameEditText, dpUIDEditText, reasonEditText, dateEditText;
    private Button submitButton;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_out); // Change if your XML file is named differently

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize views
        studentNameEditText = findViewById(R.id.StudentName);
        dpUIDEditText = findViewById(R.id.DpUID);
        reasonEditText = findViewById(R.id.Reason);
        dateEditText = findViewById(R.id.dateofdp);
        submitButton = findViewById(R.id.dayoutsub); // Button from your XML

        // Set up click listener
        submitButton.setOnClickListener(v -> submitDayOutForm());
    }

    private void submitDayOutForm() {
        String name = studentNameEditText.getText().toString().trim();
        String uid = dpUIDEditText.getText().toString().trim();
        String reason = reasonEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();

        if (name.isEmpty() || uid.isEmpty() || reason.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create data map
        Map<String, Object> dayOutData = new HashMap<>();
        dayOutData.put("name", name);
        dayOutData.put("uid", uid);
        dayOutData.put("reason", reason);
        dayOutData.put("date", date);

        // Save to Firestore
        firestore.collection("DayOut")
                .add(dayOutData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(DayOut.this, "Day Out submitted!", Toast.LENGTH_SHORT).show();
                    clearForm(); // Optional: Clear fields after submission
                })
                .addOnFailureListener(e ->
                        Toast.makeText(DayOut.this, "Failed to submit: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void clearForm() {
        studentNameEditText.setText("");
        dpUIDEditText.setText("");
        reasonEditText.setText("");
        dateEditText.setText("");
    }
}
