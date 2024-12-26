package com.example.finalwmp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SubjectsActivity extends AppCompatActivity {

    DatabaseHelper db;
    ListView listViewSubjects;
    Button buttonConfirm;
    String username;
    ArrayList<Integer> selectedSubjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        db = new DatabaseHelper(this);
        listViewSubjects = findViewById(R.id.listViewSubjects);
        buttonConfirm = findViewById(R.id.buttonConfirm);

        username = getIntent().getStringExtra("username");

        // Load all subjects into the ListView
        Cursor cursor = db.getSubjects();
        ArrayList<String> subjects = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SUBJECT_NAME);
                int creditsIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SUBJECT_CREDITS);

                // Check if columns exist in the cursor
                if (nameIndex != -1 && creditsIndex != -1) {
                    String name = cursor.getString(nameIndex);
                    int credits = cursor.getInt(creditsIndex);
                    subjects.add(name + " (" + credits + " credits)");
                } else {
                    Log.e("SubjectsActivity", "Column not found in cursor");
                }
            }
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, subjects);
        listViewSubjects.setAdapter(adapter);
        listViewSubjects.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listViewSubjects.setOnItemClickListener((parent, view, position, id) -> {
            // Toggle selection for the clicked item
            if (selectedSubjects.contains(position)) {
                selectedSubjects.remove((Integer) position);
            } else {
                selectedSubjects.add(position);
            }
        });

        buttonConfirm.setOnClickListener(v -> {
            if (selectedSubjects.isEmpty()) {
                Toast.makeText(SubjectsActivity.this, "You must select at least one subject.", Toast.LENGTH_SHORT).show();
                return;
            }

            int totalCredits = 0;
            ArrayList<String> enrolledSubjects = new ArrayList<>();

            for (int i = 0; i < selectedSubjects.size(); i++) {
                int subjectIndex = selectedSubjects.get(i);

                Cursor subjectCursor = db.getSubjects();
                if (subjectCursor != null && subjectCursor.moveToPosition(subjectIndex)) {
                    int nameIndex = subjectCursor.getColumnIndex(DatabaseHelper.COLUMN_SUBJECT_NAME);
                    int creditsIndex = subjectCursor.getColumnIndex(DatabaseHelper.COLUMN_SUBJECT_CREDITS);

                    // Check if columns exist in the cursor
                    if (nameIndex != -1 && creditsIndex != -1) {
                        String subjectName = subjectCursor.getString(nameIndex);
                        int credits = subjectCursor.getInt(creditsIndex);

                        totalCredits += credits;
                        enrolledSubjects.add(subjectName + " - " + credits + " Credits");
                    } else {
                        Log.e("SubjectsActivity", "Column not found in cursor");
                    }
                }
                if (subjectCursor != null) {
                    subjectCursor.close();
                }
            }

            if (totalCredits > 24) {
                Toast.makeText(SubjectsActivity.this, "Total credits cannot exceed 24.", Toast.LENGTH_SHORT).show();
            } else {
                // Navigate to SummaryActivity
                Intent intent = new Intent(SubjectsActivity.this, SummaryActivity.class);
                intent.putStringArrayListExtra("enrolledSubjects", enrolledSubjects);
                intent.putExtra("totalCredits", totalCredits);
                startActivity(intent);
            }
        });
    }
}
