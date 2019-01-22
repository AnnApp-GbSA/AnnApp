package com.pax.tk.annapp;

import java.util.Set;

public class SchoolLessonSystem {
    private int schoolstart; //Minutes after midnight
    private int lessonLenght;
    private int breakLenght;
    private Set<Integer> breaksAfterLesson;

    /**
     * creates a schoolLessonSystem with a start, a lesson length, a break length as Integers and a the breaks after a lesson as Set<>
     *
     * @param schoolstart start time
     * @param lessonLenght length of a lesson
     * @param breakLenght length of a break
     * @param breaksAfterLesson breaks after a lesson as Set<>
     */
    public SchoolLessonSystem(int schoolstart, int lessonLenght, int breakLenght, Set<Integer> breaksAfterLesson){
        this.schoolstart = schoolstart;
        this.lessonLenght = lessonLenght;
        this.breakLenght = breakLenght;
        this.breaksAfterLesson = breaksAfterLesson;
    }

    /**
     * gets the start time
     *
     * @return start time as Integer
     */
    public int getSchoolstart() {
        return schoolstart;
    }

    /**
     * sets the start time
     *
     * @param schoolstart start time to be set
     */
    public void setSchoolstart(int schoolstart) {
        this.schoolstart = schoolstart;
    }
}
