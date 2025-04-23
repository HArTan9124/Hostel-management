package com.example.finalmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class bothlogin extends AppCompatActivity {
    private Button studentbtn;
    private Button staffbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bothlogin);

        studentbtn = findViewById(R.id.StudentLogin);
        staffbtn = findViewById(R.id.StaffLogin);

        studentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bothlogin.this, studentlogin.class);
                startActivity(intent);
            }
        });

        staffbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bothlogin.this, stafflogin.class);
                startActivity(intent);
            }
        });
    }
}
