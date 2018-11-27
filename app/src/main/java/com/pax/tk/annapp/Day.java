package com.pax.tk.annapp;

import java.io.Serializable;
import java.util.ArrayList;


public class Day implements Serializable {

    int number;
    private ArrayList<Lesson> lessons = new ArrayList<>();

    /**
     * creates a day with a number
     *
     * @param number number of the day
     */
    public Day(int number) {
        this.number = number;
    }

    /**
     * sets a lesson for this day
     *
     * @param lesson lesson to be set
     */
    public void setLesson(Lesson lesson) {

        while (lessons.size() <= lesson.getTime())
            lessons.add(new Lesson(null, null,number, lessons.size()));

        lessons.set(lesson.getTime(), lesson);
    }

    /**
     * get lesson for a time or creates a lesson for this time
     *
     * @param time time of the lesson
     * @return lesson at time or creates a new lesson at the time
     */
    public Lesson getLesson(int time) {
        if (time < lessons.size())
            return lessons.get(time);
        return new Lesson(null, null, number, time);
    }

    /**
     * get all lessons for this day
     *
     * @return lessons
     */
    public ArrayList<Lesson> getLessons() {
        return lessons;
    }


}


