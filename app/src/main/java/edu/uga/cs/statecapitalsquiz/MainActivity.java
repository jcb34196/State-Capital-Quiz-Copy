package edu.uga.cs.statecapitalsquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private QuizData quizData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Start Quiz button
        Button startQuizButton = findViewById(R.id.startQuizButton);
        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start QuizActivity when button is clicked
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });

        // Initialize "View Past Results" button
        Button viewPastResultsButton = findViewById(R.id.viewResultsButton);
        viewPastResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start PastResultsActivity when button is clicked
                Intent intent = new Intent(MainActivity.this, PastResultsActivity.class);
                startActivity(intent);
            }
        });


        // Initialize the database helper
        QuizDBHelper dbHelper = QuizDBHelper.getInstance(this);

        // Populate the quiz questions table asynchronously when the app starts
        new AsyncTask<Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    dbHelper.populateQuizQuestionsTable();
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error populating quiz questions: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                //Toast.makeText(MainActivity.this, "Quiz questions populated successfully!", Toast.LENGTH_SHORT).show();
            }
        }.execute();

        // Initialize QuizData
        quizData = new QuizData(this);

        // Open the database asynchronously
        new AsyncTask<Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                quizData.open();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // Call the other asynchronous tasks after opening the database
                retrieveAllQuestions(); // testing method to retrieve questions
                //createAndStoreQuiz(new Date(), 6, 6);  // test data for first quiz
                //createAndStoreQuiz(new Date(), 2, 6);  // test data for another quiz
                reviewAllQuizzes(); // testing method to retrieve all quizzes
                //
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (quizData != null) {
            quizData.close();  // Close the database connection when the activity is destroyed
            Log.d("QuizDataTest", "Database connection closed.");
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (quizData != null) {
            quizData.close();
            Log.d("QuizDataTest", "Database connection closed in onPause.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (quizData != null) {
            quizData.open();
            Log.d("QuizDataTest", "Database connection reopened in onResume.");
        }
    }

    // Helper Functions that retrieve data from the questions table asynchronously
    private void retrieveAllQuestions() {
        new AsyncTask<Void, List<Question>>() {
            @Override
            protected List<Question> doInBackground(Void... params) {
                return quizData.retrieveAllQuestions();
            }

            @Override
            protected void onPostExecute(List<Question> questions) {
                for (Question question : questions) {
                    Log.d("QuizDataTest", "Question ID: " + question.getQuestionId() +
                            ", State Name: " + question.getStateName() +
                            ", Capital City: " + question.getCapitalCity() +
                            ", City1: " + question.getCity1() +
                            ", City2: " + question.getCity2() +
                            ", Statehood Year: " + question.getStatehoodYear() +
                            ", Capital Year: " + question.getCapitalYear());
                }
            }
        }.execute();
    }

    // Helper Functions that send data to the quiz table asynchronously
    private void createAndStoreQuiz(Date quizDate, int correctAnswers, int totalNumQuestions) {
        new AsyncTask<Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                List<Question> questions = quizData.retrieveAllQuestions();
                Quiz newQuiz = new Quiz(quizDate, questions, correctAnswers, totalNumQuestions);
                quizData.storeQuiz(newQuiz);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                Log.d("QuizDataTest", "Quiz stored successfully.");
            }
        }.execute();
    }

    // Helper Functions that retrieve data from the quiz table asynchronously
    private void reviewAllQuizzes() {
        new AsyncTask<Void, List<Quiz>>() {
            @Override
            protected List<Quiz> doInBackground(Void... params) {
                return quizData.retrieveAllQuizzes();
            }

            @Override
            protected void onPostExecute(List<Quiz> quizzes) {
                if (quizzes != null && !quizzes.isEmpty()) {
                    for (Quiz quiz : quizzes) {
                        Log.d("QuizDataTest", "Quiz ID: " + quiz.getQuizId() + ", Date: " + quiz.getQuizDate() + ", Score: " + quiz.getQuizScore());
                    }
                } else {
                    Log.d("QuizDataTest", "No quizzes found.");
                }
            }
        }.execute();
    }
}

