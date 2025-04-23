package com.example.finalmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class stafflogin extends AppCompatActivity {

    EditText name, eid, jobRole, email, year, password;
    Button submitBtn;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stafflogin);

        name = findViewById(R.id.staffName);
        eid = findViewById(R.id.EID);
        jobRole = findViewById(R.id.JobRole);
        email = findViewById(R.id.staffmail);
        year = findViewById(R.id.Yearofjoining);
        password = findViewById(R.id.Password);
        submitBtn = findViewById(R.id.staffbtn);

        mAuth = FirebaseAuth.getInstance();

        submitBtn.setOnClickListener(v -> registerStaff());
    }

    // 👇 Automatically go to next screen if user is already logged in
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, StaffLanding.class);
            startActivity(intent);
            finish();
        }
    }

    private void registerStaff() {
        String sName = name.getText().toString();
        String sEID = eid.getText().toString();
        String sJob = jobRole.getText().toString();
        String sEmail = email.getText().toString();
        String sYear = year.getText().toString();
        String sPass = password.getText().toString();

        if (sName.isEmpty() || sEID.isEmpty() || sJob.isEmpty() || sEmail.isEmpty() || sYear.isEmpty() || sPass.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(sEmail, sPass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        HashMap<String, String> staffData = new HashMap<>();
                        staffData.put("name", sName);
                        staffData.put("eid", sEID);
                        staffData.put("jobRole", sJob);
                        staffData.put("email", sEmail);
                        staffData.put("yearOfJoining", sYear);

                        FirebaseDatabase.getInstance().getReference()
                                .child("Users").child("Staff").child(uid).setValue(staffData)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Staff Registered", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(this, StaffLanding.class);
                                    startActivity(i);
                                    finish();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
                                );
                    } else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
