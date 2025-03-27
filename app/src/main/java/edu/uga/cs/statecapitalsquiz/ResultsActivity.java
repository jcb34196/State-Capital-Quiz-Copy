package edu.uga.cs.statecapitalsquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView scoreTextView = findViewById(R.id.scoreTextView);
        int score = getIntent().getIntExtra("quizScore", 0);
        scoreTextView.setText("Your Score: " + score);

        // New Quiz button
        Button retryButton = findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, QuizActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Go to Home button
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // New button to view past results
        Button previousResultsButton = findViewById(R.id.previousResultsButton);
        previousResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, PastResultsActivity.class);
                startActivity(intent);
            }
        });
    }
}


