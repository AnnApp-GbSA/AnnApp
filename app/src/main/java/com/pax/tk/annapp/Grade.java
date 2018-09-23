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

    public Grade(Subject subject, int grade, boolean iswritten, float rating, String note){
        this.subject = subject;
        this.grade = grade;
        this.iswritten = iswritten;
        this.rating = rating;
        this.note = note;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public boolean iswritten() {
        return iswritten;
    }

    public void setIswritten(boolean iswritten) {
        this.iswritten = iswritten;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
