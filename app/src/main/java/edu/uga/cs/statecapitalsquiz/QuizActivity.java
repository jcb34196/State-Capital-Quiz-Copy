package edu.uga.cs.statecapitalsquiz;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private Quiz quiz;
    private ViewPager2 viewPager;
    private QuizPagerAdapter quizPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Load questions from database
        List<Question> questions = loadQuestionsFromDatabase();
        List<Question> selectedQuestions = selectRandomQuestions(questions, 7);

        // Initialize quiz
        quiz = new Quiz(new Date(), selectedQuestions, 0, 0);

        // Set up ViewPager2 with QuizPagerAdapter
        viewPager = findViewById(R.id.viewPager);
        quizPagerAdapter = new QuizPagerAdapter(this, quiz);
        viewPager.setAdapter(quizPagerAdapter);

        // Listener to track the last question and display results
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == quiz.getTotalQuestions() - 1) {
                    showQuizResults();
                }
            }
        });
    }

    public void recordAnswer(Question question, String selectedAnswer) {
        boolean isCorrect = question.getCapitalCity().equals(selectedAnswer);
        if (isCorrect) {
            quiz.incrementScore();
        }
        quiz.submitAnswer(viewPager.getCurrentItem(), String.valueOf(isCorrect));
    }

    private List<Question> loadQuestionsFromDatabase() {
        QuizDBHelper dbHelper = QuizDBHelper.getInstance(this);
        return dbHelper.getAllQuestions();
    }

    private List<Question> selectRandomQuestions(List<Question> questions, int count) {
        List<Question> selectedQuestions = new ArrayList<>();
        Random random = new Random();
        HashSet<Integer> selectedIndices = new HashSet<>();
        while (selectedIndices.size() < count) {
            int randomIndex = random.nextInt(questions.size());
            if (!selectedIndices.contains(randomIndex)) {
                selectedIndices.add(randomIndex);
                selectedQuestions.add(questions.get(randomIndex));
            }
        }
        return selectedQuestions;
    }

    private void showQuizResults() {
        int score = quiz.getQuizScore();
        Intent intent = new Intent(this, ResultsActivity.class);
        QuizDBHelper.getInstance(this).saveQuizResult(score);
        intent.putExtra("quizScore", score);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        Log.d( TAG, "QuizActivity.onResume()" );
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d( TAG, "QuizActivity.onPause()" );
        super.onPause();
    }
}




