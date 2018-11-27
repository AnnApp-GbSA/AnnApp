package com.pax.tk.annapp;

import java.io.Serializable;

public class Grade implements Serializable{

    //actual Grade
    private int grade;

    //Grade iswritten = true;
    private boolean iswritten;

    private float rating;

    private String note;

    private Subject subject;
    //private Calendar date;

    /**
     * creates a grade with a subject, the grade as Integer, a rating, a note and the boolean if it is written
     *
     * @param subject subject in which the grade was got
     * @param grade grade as Integer
     * @param iswritten true if the grade is written, false if not
     * @param rating rating of the grade compared to other grades as float
     * @param note note as String
     */
    public Grade(Subject subject, int grade, boolean iswritten, float rating, String note){
        this.subject = subject;
        this.grade = grade;
        this.iswritten = iswritten;
        this.rating = rating;
        this.note = note;
    }

    /**
     * get the subject of a Grade
     *
     * @return subject of the Grade
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * sets the subject of a Grade
     *
     * @param subject subject to be set
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     * get the grade of a Grade
     *
     * @return grade of the Grade
     */
    public int getGrade() {
        return grade;
    }

    /**
     * sets the grade of a Grade
     *
     * @param grade grade to be set
     */
    public void setGrade(int grade) {
        this.grade = grade;
    }

    /**
     * get if a Grade is written or not
     *
     * @return true if the Grade is written, false if not
     */
    public boolean iswritten() {
        return iswritten;
    }

    /**
     * sets if a Grade is written
     *
     * @param iswritten true if the Grade shall be set to written, false if it shall be set to oral
     */
    public void setIswritten(boolean iswritten) {
        this.iswritten = iswritten;
    }

    /**
     * get the rating of a Grade
     *
     * @return rating of the Grade
     */
    public float getRating() {
        return rating;
    }

    /**
     * sets the rating of a Grade
     *
     * @param rating rating to be set
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * get the note of a Grade
     *
     * @return note of the Grade
     */
    public String getNote() {
        return note;
    }

    /**
     * sets the note of a Grade
     *
     * @param note note to be set
     */
    public void setNote(String note) {
        this.note = note;
    }
}
