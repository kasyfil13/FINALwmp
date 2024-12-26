package com.example.finalwmp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {
    ListView listViewSummary;
    TextView textViewTotalCredits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        listViewSummary = findViewById(R.id.listViewSummary);
        textViewTotalCredits = findViewById(R.id.textViewTotalCredits);

        // Get the data passed from SubjectsActivity
        ArrayList<String> enrolledSubjects = getIntent().getStringArrayListExtra("enrolledSubjects");
        int totalCredits = getIntent().getIntExtra("totalCredits", 0);

        // Populate the ListView with enrolled subjects
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, enrolledSubjects);
        listViewSummary.setAdapter(adapter);

        // Display the total credits
        textViewTotalCredits.setText("Total Credits: " + totalCredits);
    }
}
