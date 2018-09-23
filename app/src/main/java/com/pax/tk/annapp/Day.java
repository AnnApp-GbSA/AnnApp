package com.pax.tk.annapp;

import java.io.Serializable;
import java.util.ArrayList;


public class Day implements Serializable {

    int number;
    private ArrayList<Lesson> lessons = new ArrayList<>();

    public Day(int number) {
        this.number = number;
    }

    public void setLesson(Lesson lesson) {

        while (lessons.size() <= lesson.getTime())
            lessons.add(new Lesson(null, null,number, lessons.size()));

        lessons.set(lesson.getTime(), lesson);
    }

    public Lesson getLesson(int time) {
        if (time < lessons.size())
            return lessons.get(time);
        return new Lesson(null, null, number, time);
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }


}


