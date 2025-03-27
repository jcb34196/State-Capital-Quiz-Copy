package edu.uga.cs.statecapitalsquiz;

import java.io.Serializable;

public class Question implements Serializable {
    private int questionId;
    private String stateName;
    private String capitalCity;
    private String city1;
    private String city2;
    private int statehoodYear;
    private int capitalYear;

    // Constructor with default ID set to -1
    public Question(String stateName, String capitalCity, String city1, String city2, int statehoodYear, int capitalYear) {
        this.questionId = -1; // ID will be set later using the setter method
        this.stateName = stateName;
        this.capitalCity = capitalCity;
        this.city1 = city1;
        this.city2 = city2;
        this.statehoodYear = statehoodYear;
        this.capitalYear = capitalYear;
    }

    // Getters and Setters
    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(String capitalCity) {
        this.capitalCity = capitalCity;
    }

    public String getCity1() {
        return city1;
    }

    public void setCity1(String city1) {
        this.city1 = city1;
    }

    public String getCity2() {
        return city2;
    }

    public void setCity2(String city2) {
        this.city2 = city2;
    }

    public int getStatehoodYear() {
        return statehoodYear;
    }

    public void setStatehoodYear(int statehoodYear) {
        this.statehoodYear = statehoodYear;
    }

    public int getCapitalYear() {
        return capitalYear;
    }

    public void setCapitalYear(int capitalYear) {
        this.capitalYear = capitalYear;
    }
}

