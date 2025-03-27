package edu.uga.cs.statecapitalsquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuizDBHelper extends SQLiteOpenHelper {
    private static final String DEBUG_TAG = "QuizDBHelper";
    private static final String DB_NAME = "StateCapital.db";
    private static final int DB_VERSION = 1;
    private Context context;
    // Define table and column names for questions table/////////////////////////////////
    public static final String TABLE_QUIZQUESTIONS = "quizquestions";
    public static final String QUIZQUESTION_COLUMN_ID = "question_id";
    public static final String QUIZQUESTION_COLUMN_STATE_NAME = "state_name";
    public static final String QUIZQUESTION_COLUMN_CAPITAL_CITY = "capital_city";
    public static final String QUIZQUESTION_COLUMN_CITY1 = "city_1";
    public static final String QUIZQUESTION_COLUMN_CITY2 = "city_2";
    public static final String QUIZQUESTION_COLUMN_STATEHOOD_YEAR = "statehood_year";
    public static final String QUIZQUESTION_COLUMN_CAPITAL_YEAR = "capital_year";
    // Define table and column names for quizzes/////////////////////////////////////////
    public static final String TABLE_QUIZZES = "quizzes";
    public static final String QUIZZES_COLUMN_ID = "quiz_id";
    public static final String QUIZZES_COLUMN_QUIZ_DATE = "quiz_date";
    public static final String QUIZZES_COLUMN_QUESTION1_ID = "question_1_id";
    public static final String QUIZZES_COLUMN_QUESTION2_ID = "question_2_id";
    public static final String QUIZZES_COLUMN_QUESTION3_ID = "question_3_id";
    public static final String QUIZZES_COLUMN_QUESTION4_ID = "question_4_id";
    public static final String QUIZZES_COLUMN_QUESTION5_ID = "question_5_id";
    public static final String QUIZZES_COLUMN_QUESTION6_ID = "question_6_id";
    public static final String QUIZZES_COLUMN_QUIZ_SCORE = "quiz_score";
    public static final String QUIZZES_COLUMN_QUESTIONS_ANSWERED = "questions_answered";
    /////////////////////////////////////////////////////////////////////////////////////
    // Helper Instance
    private static QuizDBHelper helperInstance;

    /////////////////////////////////////////////////////////////////////////////////////
    // Create Tables
    // Create QuizQuestions Table////////////////////////////////////////////////////////
    private static final String Create_QUIZQuestions =
            "create table " + TABLE_QUIZQUESTIONS + " ("
                + QUIZQUESTION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QUIZQUESTION_COLUMN_STATE_NAME + " TEXT NOT NULL, "
                + QUIZQUESTION_COLUMN_CAPITAL_CITY + " TEXT NOT NULL, "
                + QUIZQUESTION_COLUMN_CITY1 + " TEXT NOT NULL, "
                + QUIZQUESTION_COLUMN_CITY2 + " TEXT NOT NULL, "
                + QUIZQUESTION_COLUMN_STATEHOOD_YEAR + " INTEGER, "
                + QUIZQUESTION_COLUMN_CAPITAL_YEAR + " INTEGER)";
    // Create Quizzes Table//////////////////////////////////////////////////////////////
    private static final String Create_Quizzes =
            "create table " + TABLE_QUIZZES + " ("
                + QUIZZES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QUIZZES_COLUMN_QUIZ_DATE + " TEXT NOT NULL, "
                + QUIZZES_COLUMN_QUESTION1_ID + " INTEGER, "
                + QUIZZES_COLUMN_QUESTION2_ID + " INTEGER, "
                + QUIZZES_COLUMN_QUESTION3_ID + " INTEGER, "
                + QUIZZES_COLUMN_QUESTION4_ID + " INTEGER, "
                + QUIZZES_COLUMN_QUESTION5_ID + " INTEGER, "
                + QUIZZES_COLUMN_QUESTION6_ID + " INTEGER, "
                + QUIZZES_COLUMN_QUIZ_SCORE + " INTEGER DEFAULT 0, "
                + QUIZZES_COLUMN_QUESTIONS_ANSWERED + " INTEGER DEFAULT 0, "
                + "FOREIGN KEY (" + QUIZZES_COLUMN_QUESTION1_ID + ") REFERENCES " + TABLE_QUIZQUESTIONS + "(" + QUIZQUESTION_COLUMN_ID + "), "
                + "FOREIGN KEY (" + QUIZZES_COLUMN_QUESTION2_ID + ") REFERENCES " + TABLE_QUIZQUESTIONS + "(" + QUIZQUESTION_COLUMN_ID + "), "
                + "FOREIGN KEY (" + QUIZZES_COLUMN_QUESTION3_ID + ") REFERENCES " + TABLE_QUIZQUESTIONS + "(" + QUIZQUESTION_COLUMN_ID + "), "
                + "FOREIGN KEY (" + QUIZZES_COLUMN_QUESTION4_ID + ") REFERENCES " + TABLE_QUIZQUESTIONS + "(" + QUIZQUESTION_COLUMN_ID + "), "
                + "FOREIGN KEY (" + QUIZZES_COLUMN_QUESTION5_ID + ") REFERENCES " + TABLE_QUIZQUESTIONS + "(" + QUIZQUESTION_COLUMN_ID + "), "
                + "FOREIGN KEY (" + QUIZZES_COLUMN_QUESTION6_ID + ") REFERENCES " + TABLE_QUIZQUESTIONS + "(" + QUIZQUESTION_COLUMN_ID + "))";

    ////////////////////////////////////////////////////////////////////////////////////////////
    // Private Constructor, creates database
    private QuizDBHelper( Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        this.context = context.getApplicationContext();  // Safely assign the context
    }
    // Access method to a single instance of the class
    // Synchronized so only one thread can execute this method at a time
    public static synchronized  QuizDBHelper getInstance( Context context){
        if (helperInstance == null){ // check if instance exist, if not create instance
            helperInstance = new QuizDBHelper(context.getApplicationContext());
        }
        return helperInstance;
    }
    // creates Data base if one does not exist
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_QUIZQuestions);
        db.execSQL(Create_Quizzes);
        Log.d( DEBUG_TAG, "Table " + TABLE_QUIZQUESTIONS + " created");
        Log.d( DEBUG_TAG, "Table " + TABLE_QUIZZES + " created");
    }
    // updates versions
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "drop table if exists " + TABLE_QUIZQUESTIONS);
        db.execSQL( "drop table if exists " + TABLE_QUIZZES);
        onCreate(db);
        Log.d( DEBUG_TAG, "Table " + TABLE_QUIZQUESTIONS + " updated");
        Log.d( DEBUG_TAG, "Table " + TABLE_QUIZZES + " updated");
    }

    public void populateQuizQuestionsTable() {
        if (context == null) {
            Log.e(DEBUG_TAG, "Context is null, cannot populate quiz questions");
            return;  // Exit if context is null
        }

        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the quizquestions table is empty
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_QUIZQUESTIONS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        // If the table is empty, populate it
        if (count == 0) {
            db.beginTransaction();
            try {
                InputStream is = context.getResources().openRawResource(R.raw.state_capitals);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                reader.readLine();  // Skip the header line

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 7) {
                        ContentValues values = new ContentValues();
                        values.put(QUIZQUESTION_COLUMN_STATE_NAME, parts[0].trim());
                        values.put(QUIZQUESTION_COLUMN_CAPITAL_CITY, parts[1].trim());
                        values.put(QUIZQUESTION_COLUMN_CITY1, parts[2].trim());
                        values.put(QUIZQUESTION_COLUMN_CITY2, parts[3].trim());
                        values.put(QUIZQUESTION_COLUMN_STATEHOOD_YEAR, Integer.parseInt(parts[4].trim()));
                        values.put(QUIZQUESTION_COLUMN_CAPITAL_YEAR, Integer.parseInt(parts[5].trim()));

                        db.insert(TABLE_QUIZQUESTIONS, null, values);
                        Log.d(DEBUG_TAG, "Inserted question for state: " + parts[0].trim());
                    }
                }

                db.setTransactionSuccessful();
                Log.d(DEBUG_TAG, "Quiz questions populated successfully!");
            } catch (Exception e) {
                Log.d(DEBUG_TAG, "Error while populating quiz questions: " + e.getMessage());
            } finally {
                db.endTransaction();
            }
        } else {
            Log.d(DEBUG_TAG, "Quiz questions already populated.");
        }
    }

    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUIZQUESTIONS, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String stateName = cursor.getString(cursor.getColumnIndex(QUIZQUESTION_COLUMN_STATE_NAME));
                String capitalCity = cursor.getString(cursor.getColumnIndex(QUIZQUESTION_COLUMN_CAPITAL_CITY));
                String city1 = cursor.getString(cursor.getColumnIndex(QUIZQUESTION_COLUMN_CITY1));
                String city2 = cursor.getString(cursor.getColumnIndex(QUIZQUESTION_COLUMN_CITY2));
                int statehoodYear = cursor.getInt(cursor.getColumnIndex(QUIZQUESTION_COLUMN_STATEHOOD_YEAR));
                int capitalYear = cursor.getInt(cursor.getColumnIndex(QUIZQUESTION_COLUMN_CAPITAL_YEAR));

                Question question = new Question(stateName, capitalCity, city1, city2, statehoodYear, capitalYear);
                questions.add(question);
            }
            cursor.close();
        }

        return questions;
    }

    public void saveQuizResult(int quizScore) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Get the current date and time as a string
        String quizDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date());

        // Prepare the content values to insert into the quizzes table
        ContentValues values = new ContentValues();
        values.put(QUIZZES_COLUMN_QUIZ_DATE, quizDate);
        values.put(QUIZZES_COLUMN_QUIZ_SCORE, quizScore);

        Log.d(DEBUG_TAG, "Quiz Date saved: " + quizDate);
        Log.d(DEBUG_TAG, "Quiz Score saved: " + quizScore);

        // Insert the quiz result into the database
        long newRowId = db.insert(TABLE_QUIZZES, null, values);
        if (newRowId != -1) {
            Log.d(DEBUG_TAG, "Quiz result saved successfully with ID: " + newRowId);
        } else {
            Log.e(DEBUG_TAG, "Error saving quiz result");
        }
    }


}
