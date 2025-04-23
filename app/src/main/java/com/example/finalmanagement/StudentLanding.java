package com.example.finalmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class StudentLanding extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Button navbarBtn, logoutBtn;
    private CircularProgressIndicator attendanceProgress;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private String currentStudentName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_landing);

        // Initialize UI
        drawerLayout = findViewById(R.id.main);
        navbarBtn = findViewById(R.id.navbar);
        logoutBtn = findViewById(R.id.btn_logout_stu);
        attendanceProgress = findViewById(R.id.attendenceprogress);

        // Navigation drawer toggle
        navbarBtn.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
            } else {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        // Logout button
        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, studentlogin.class));
            finish();
        });

        // Get student name then fetch attendance
        getStudentNameAndFetchAttendance();
    }

    private void getStudentNameAndFetchAttendance() {
        String uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference()
                .child("Users").child("Student").child(uid).child("name")
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        currentStudentName = snapshot.getValue(String.class);
                        fetchAttendanceData();
                    }
                });
    }

    private void fetchAttendanceData() {
        CollectionReference dayOutRef = db.collection("DayOut");
        dayOutRef.whereEqualTo("name", currentStudentName).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int count = 0;
                    for (QueryDocumentSnapshot ignored : queryDocumentSnapshots) {
                        count++;
                    }
                    updateProgress(count);
                })
                .addOnFailureListener(e -> updateProgress(0));
    }

    private void updateProgress(int count) {
        int percentage = Math.min((count * 100) / 30, 100); // Max 30 entries = 100%
        attendanceProgress.setProgress(percentage);

        TextView percentText = findViewById(R.id.attendence);
        if (percentText != null) {
            percentText.setText(percentage + "%");
        }
    }
}
