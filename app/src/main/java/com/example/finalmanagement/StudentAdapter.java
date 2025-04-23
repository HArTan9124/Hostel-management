package com.example.finalmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;

    public StudentAdapter(List<Student> students) {
        this.studentList = students;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.tvName.setText(student.getName());

        holder.rgAttendance.setOnCheckedChangeListener(null); // avoid triggering listener during recycling

        if (student.isPresent()) {
            holder.rbPresent.setChecked(true);
        } else {
            holder.rbAbsent.setChecked(true);
        }

        holder.rgAttendance.setOnCheckedChangeListener((group, checkedId) -> {
            student.setPresent(checkedId == R.id.rb_present);
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        RadioGroup rgAttendance;
        RadioButton rbPresent, rbAbsent;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_student_name);
            rgAttendance = itemView.findViewById(R.id.rg_attendance);
            rbPresent = itemView.findViewById(R.id.rb_present);
            rbAbsent = itemView.findViewById(R.id.rb_absent);
        }
    }
}
