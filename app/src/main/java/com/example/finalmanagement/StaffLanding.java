package com.example.finalmanagement;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.*;

public class StaffLanding extends AppCompatActivity {

    private RecyclerView rvStudents;
    private Button btnSubmit, logoutBtn, navbar;
    private StudentAdapter adapter;
    private List<Student> studentList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private FirebaseFirestore firestore;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_landing);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        // Initialize Views
        rvStudents = findViewById(R.id.rv_students);
        btnSubmit = findViewById(R.id.btn_submit);
        logoutBtn = findViewById(R.id.btn_logout);
        navbar = findViewById(R.id.navbar);
        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.navview);

        // Setup RecyclerView
        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(studentList);
        rvStudents.setAdapter(adapter);

        // Firebase Realtime Database reference (for loading students)
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Student");

        // Firestore instance (for saving attendance)
        firestore = FirebaseFirestore.getInstance();

        // Fetch students
        fetchStudentsFromFirebase();

        // Attendance submission
        btnSubmit.setOnClickListener(v -> exportAttendanceToFirestore(adapter.getStudentList()));

        // Logout button
        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, stafflogin.class));
            finish();
        });

        // Navigation drawer toggle
        navbar.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Handle nav menu clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.Edit) {
                Toast.makeText(this, "Day out clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StaffLanding.this, DayOut.class);
                startActivity(intent);
                finish();
                return true;

            } else if (id == R.id.Profile) {
                Toast.makeText(this, "Night out clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StaffLanding.this, NightOut.class);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.setting) {
                Toast.makeText(this, "Complaints clicked", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.Logout) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, stafflogin.class));
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void fetchStudentsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                studentList.clear();
                for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                    if (studentSnapshot.hasChild("name")) {
                        String name = studentSnapshot.child("name").getValue(String.class);
                        if (name != null && !name.isEmpty()) {
                            studentList.add(new Student(name));
                            Log.d("StudentLoaded", name);
                        }
                    } else {
                        Log.e("FirebaseError", "Missing 'name' field in: " + studentSnapshot.toString());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(StaffLanding.this, "Failed to load students: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void exportAttendanceToFirestore(List<Student> students) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        for (Student student : students) {
            Map<String, Object> attendanceData = new HashMap<>();
            attendanceData.put("name", student.getName());
            attendanceData.put("date", date);
            attendanceData.put("status", student.isPresent() ? "Present" : "Absent");

            firestore.collection("attendance")
                    .add(attendanceData)
                    .addOnSuccessListener(documentReference ->
                            Log.d("Firestore", "Attendance added for: " + student.getName()))
                    .addOnFailureListener(e ->
                            Log.e("Firestore", "Error adding attendance", e));
        }

        Toast.makeText(this, "Attendance submitted to Firestore", Toast.LENGTH_SHORT).show();
    }
}
