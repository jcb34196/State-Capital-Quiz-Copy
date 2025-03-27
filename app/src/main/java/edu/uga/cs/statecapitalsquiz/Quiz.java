package edu.uga.cs.statecapitalsquiz;

import java.util.Date;
import java.util.List;

public class Quiz {
    private int quizId;
    private Date quizDate;
    private List<Question> questions;
    private int quizScore;
    private int questionsAnswered;

    // Constructor with default ID set to -1
    public Quiz(Date quizDate, List<Question> questions, int quizScore, int questionsAnswered) {
        this.quizId = -1; // ID will be set later using the setter method
        this.quizDate = quizDate;
        this.questions = questions;
        this.quizScore = quizScore;
        this.questionsAnswered = questionsAnswered;
    }

    // Getters and Setters
    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public Date getQuizDate() {
        return quizDate;
    }

    public void setQuizDate(Date quizDate) {
        this.quizDate = quizDate;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(int quizScore) {
        this.quizScore = quizScore;
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public void setQuestionsAnswered(int questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }

    public void submitAnswer(int questionIndex, String selectedAnswer) {
        // Check if the answer is correct by comparing answerIndex with the correct answer index
        Question question = questions.get(questionIndex);

        if (question.getCapitalCity().equalsIgnoreCase(selectedAnswer)) {
            quizScore++; // Increment score if answer is correct
        }

        questionsAnswered++; // Increment the number of questions answered
    }

    // Method to get a question by index
    public Question getQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            return questions.get(index);
        } else {
            return null; // Or handle this in a way that suits your app
        }
    }

    // Method to get total number of questions
    public int getTotalQuestions() {
        return questions.size();
    }

    public void incrementScore() {
        quizScore++;
    }
}
