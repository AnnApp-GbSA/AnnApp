package com.pax.qbt.annapp;

import java.util.Set;

/**
 * Created by Petrus on 26.03.2018.
 */

public class SchoolLessonSystem {
    private int schoolstart; //Minutes after midnight
    private int lessonLenght;
    private int breakLenght;
    private Set<Integer> breaksAfterLesson;

    public SchoolLessonSystem(int schoolstart, int lessonLenght, int breakLenght, Set<Integer> breaksAfterLesson){
        this.schoolstart = schoolstart;
        this.lessonLenght = lessonLenght;
        this.breakLenght = breakLenght;
        this.breaksAfterLesson = breaksAfterLesson;
    }

    public int getSchoolstart() {
        return schoolstart;
    }

    public void setSchoolstart(int schoolstart) {
        this.schoolstart = schoolstart;
    }

    public int getLessonLenght() {
        return lessonLenght;
    }

    public void setLessonLenght(int lessonLenght) {
        this.lessonLenght = lessonLenght;
    }

    public int getBreakLenght() {
        return breakLenght;
    }

    public void setBreakLenght(int breakLenght) {
        this.breakLenght = breakLenght;
    }
    public void addBreakAfterLesson(int time){
        if(!breaksAfterLesson.contains(time))
        breaksAfterLesson.add(time);
    }

    public Set<Integer> getBreaksAfterLesson() {
        return breaksAfterLesson;
    }
}
