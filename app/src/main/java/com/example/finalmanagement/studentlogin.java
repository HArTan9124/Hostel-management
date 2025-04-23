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

public class studentlogin extends AppCompatActivity {

    EditText name, uid, course, email, year, password;
    Button submitBtn;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If already logged in, skip login
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, StudentLanding.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_studentlogin);

        name = findViewById(R.id.Name);
        uid = findViewById(R.id.UID);
        course = findViewById(R.id.Department);
        email = findViewById(R.id.studentmail);
        year = findViewById(R.id.Year);
        password = findViewById(R.id.studentPassword);
        submitBtn = findViewById(R.id.stubtn);

        mAuth = FirebaseAuth.getInstance();

        submitBtn.setOnClickListener(v -> registerStudent());
    }

    private void registerStudent() {
        String sName = name.getText().toString();
        String sUID = uid.getText().toString();
        String sCourse = course.getText().toString();
        String sEmail = email.getText().toString();
        String sYear = year.getText().toString();
        String sPass = password.getText().toString();

        if (sName.isEmpty() || sUID.isEmpty() || sCourse.isEmpty() || sEmail.isEmpty() || sYear.isEmpty() || sPass.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(sEmail, sPass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        HashMap<String, String> studentData = new HashMap<>();
                        studentData.put("name", sName);
                        studentData.put("uid", sUID);
                        studentData.put("course", sCourse);
                        studentData.put("email", sEmail);
                        studentData.put("year", sYear);

                        FirebaseDatabase.getInstance().getReference()
                                .child("Users").child("Student").child(uid).setValue(studentData)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Student Registered", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, StudentLanding.class));
                                    finish();
                                }).addOnFailureListener(e ->
                                        Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
                                );
                    } else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
