package edu.uga.cs.statecapitalsquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
public class QuizData {

    public static final String DEBUG_TAG = "QuizData";

    private SQLiteDatabase db;
    private QuizDBHelper quizDbHelper;

    // Columns for retrieving data
    private static final String[] questionColumns = {
            QuizDBHelper.QUIZQUESTION_COLUMN_ID,
            QuizDBHelper.QUIZQUESTION_COLUMN_STATE_NAME,
            QuizDBHelper.QUIZQUESTION_COLUMN_CAPITAL_CITY,
            QuizDBHelper.QUIZQUESTION_COLUMN_CITY1,
            QuizDBHelper.QUIZQUESTION_COLUMN_CITY2,
            QuizDBHelper.QUIZQUESTION_COLUMN_STATEHOOD_YEAR,
            QuizDBHelper.QUIZQUESTION_COLUMN_CAPITAL_YEAR
    };

    private static final String[] quizColumns = {
            QuizDBHelper.QUIZZES_COLUMN_ID,
            QuizDBHelper.QUIZZES_COLUMN_QUIZ_DATE,
            QuizDBHelper.QUIZZES_COLUMN_QUESTION1_ID,
            QuizDBHelper.QUIZZES_COLUMN_QUESTION2_ID,
            QuizDBHelper.QUIZZES_COLUMN_QUESTION3_ID,
            QuizDBHelper.QUIZZES_COLUMN_QUESTION4_ID,
            QuizDBHelper.QUIZZES_COLUMN_QUESTION5_ID,
            QuizDBHelper.QUIZZES_COLUMN_QUESTION6_ID,
            QuizDBHelper.QUIZZES_COLUMN_QUIZ_SCORE,
            QuizDBHelper.QUIZZES_COLUMN_QUESTIONS_ANSWERED
    };

    public QuizData(Context context) {
        this.quizDbHelper = QuizDBHelper.getInstance(context);
    }

    public void open() {
        db = quizDbHelper.getWritableDatabase();
        Log.d(DEBUG_TAG, "QuizData: db open");
    }

    public void close() {
        if (quizDbHelper != null) {
            quizDbHelper.close();
            Log.d(DEBUG_TAG, "QuizData: db closed");
        }
    }

    public boolean isDBOpen() {
        return db.isOpen();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////
    // Get Method for gitting list of questions from the questions table in the db
    public List<Question> retrieveAllQuestions() {
        List<Question> questions = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;
        try {
            cursor = db.query(QuizDBHelper.TABLE_QUIZQUESTIONS, questionColumns, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    columnIndex= cursor.getColumnIndex(QuizDBHelper.QUIZQUESTION_COLUMN_ID);
                    int questionId = cursor.getInt(columnIndex);
                    columnIndex = cursor.getColumnIndex(QuizDBHelper.QUIZQUESTION_COLUMN_STATE_NAME);
                    String stateName = cursor.getString(columnIndex);
                    columnIndex = cursor.getColumnIndex(QuizDBHelper.QUIZQUESTION_COLUMN_CAPITAL_CITY);
                    String capitalCity = cursor.getString(columnIndex);
                    columnIndex = cursor.getColumnIndex(QuizDBHelper.QUIZQUESTION_COLUMN_CITY1);
                    String city1 = cursor.getString(columnIndex);
                    columnIndex = cursor.getColumnIndex(QuizDBHelper.QUIZQUESTION_COLUMN_CITY2);
                    String city2 = cursor.getString(columnIndex);
                    columnIndex = cursor.getColumnIndex(QuizDBHelper.QUIZQUESTION_COLUMN_STATEHOOD_YEAR);
                    int statehoodYear = cursor.getInt(columnIndex);
                    columnIndex = cursor.getColumnIndex(QuizDBHelper.QUIZQUESTION_COLUMN_CAPITAL_YEAR);
                    int capitalYear = cursor.getInt(columnIndex);
                    Question question = new Question(stateName, capitalCity, city1, city2, statehoodYear, capitalYear);
                    question.setQuestionId(questionId);
                    questions.add(question);
                    //Log.d(DEBUG_TAG, "Retrieved Question: " + question);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Exception caught: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return questions;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////
    // Get method that retrives  quiz from the quizzes table
    public List<Quiz> retrieveAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;
        Date quizDate;
        try {
            cursor = db.query(QuizDBHelper.TABLE_QUIZZES, quizColumns, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    columnIndex = cursor.getColumnIndex(QuizDBHelper.QUIZZES_COLUMN_ID);
                    int quizId = cursor.getInt(columnIndex);
                    columnIndex = cursor.getColumnIndex(QuizDBHelper.QUIZZES_COLUMN_QUIZ_DATE);
                    String dateStr = cursor.getString(columnIndex);
                    try {
                        //quizDate = Date.from(LocalDateTime.parse(dateStr, DATE_FORMATTER).atZone(ZoneId.systemDefault()).toInstant());
                        quizDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
                    }catch (ParseException e){
                        Log.d(DEBUG_TAG, "Date parsing error: " + e.getMessage());
                        continue;  // Skip this record if parsing fails
                    }
                    columnIndex = cursor.getColumnIndex(QuizDBHelper.QUIZZES_COLUMN_QUIZ_SCORE);
                    int quizScore = cursor.getInt(columnIndex);
                    columnIndex= cursor.getColumnIndex(QuizDBHelper.QUIZZES_COLUMN_QUESTIONS_ANSWERED);
                    int questionsAnswered = cursor.getInt(columnIndex);

                    Quiz quiz = new Quiz(quizDate, new ArrayList<>(), quizScore, questionsAnswered);
                    quiz.setQuizId(quizId);
                    quizzes.add(quiz);
                    //Log.d(DEBUG_TAG, "Retrieved Quiz: " + quiz);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Exception caught: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return quizzes;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    // Inserts a quiz Pojo into the quizzes table
    public Quiz storeQuiz(Quiz quiz) {
        ContentValues values = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(quiz.getQuizDate());
        values.put(QuizDBHelper.QUIZZES_COLUMN_QUIZ_DATE, formattedDate);
        values.put(QuizDBHelper.QUIZZES_COLUMN_QUIZ_SCORE, quiz.getQuizScore());
        values.put(QuizDBHelper.QUIZZES_COLUMN_QUESTIONS_ANSWERED, quiz.getQuestionsAnswered());

        long id = db.insert(QuizDBHelper.TABLE_QUIZZES, null, values);
        quiz.setQuizId((int) id);
        Log.d(DEBUG_TAG, "Stored new quiz with id: " + quiz.getQuizId());

        return quiz;
    }

}
