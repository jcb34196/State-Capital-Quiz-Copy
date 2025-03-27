package edu.uga.cs.statecapitalsquiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PastResultsActivity extends AppCompatActivity {

    private QuizDBHelper dbHelper;
    private ListView resultsListView;
    private QuizData quizData;
    private ArrayAdapter<String> resultsAdapter;
    private List<String> quizResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_results);

        resultsListView = findViewById(R.id.resultsListView);
        Button backButton = findViewById(R.id.backButton);

        dbHelper = QuizDBHelper.getInstance(this);
        quizData = new QuizData(this);
        quizResults = new ArrayList<>();

        resultsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, quizResults);
        resultsListView.setAdapter(resultsAdapter);

        new LoadQuizResultsTask().execute();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastResultsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class LoadQuizResultsTask extends AsyncTask<Void, Void, List<Quiz>> {
        @Override
        protected List<Quiz> doInBackground(Void... params) {
            quizData.open();
            return quizData.retrieveAllQuizzes();
        }

        @Override
        protected void onPostExecute(List<Quiz> results) {
            if (results != null && !results.isEmpty()) {
                Collections.reverse(results);  // Reverse the list to show the newest first
                for (Quiz result : results) {
                    String displayText = "Date: " + result.getQuizDate() + ", Score: " + result.getQuizScore();
                    quizResults.add(displayText);
                }
                resultsAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(PastResultsActivity.this, "No past results found", Toast.LENGTH_SHORT).show();
            }
            quizData.close();
        }
    }
}





