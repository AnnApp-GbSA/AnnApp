package com.pax.tk.annapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class Subject implements Serializable {

    //Contains all grades of one Subject
    ArrayList<Grade> grades = new ArrayList<>();

    //Contains all tasks of one Subject
    ArrayList<Task> tasks = new ArrayList<>();

    private ArrayList<Lesson> lessons = new ArrayList<>();

    int position;

    //name of the Subject
    private String name;

    //determines how much a written Grade is worth
    //int the Subject class
    //(1 or 2)
    private int ratingSub;

    //name of the Teacher
    private String teacher;

    //Name of the room the subject normally takes place
    private String room;

    //Junge, junge, wer hat hier geschlampt?
    public Subject(String name, int rating, String teacher, String room) {
        this.name = name;
        ratingSub = rating;
        this.teacher = teacher;
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRatingSub() {
        return ratingSub;
    }

    public void setRatingSub(int ratingSub) {
        this.ratingSub = ratingSub;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public void addLesson(Lesson lesson){
        lessons.add(lesson);
    }

    public void removeLesson(Lesson lesson){
        lessons.remove(lesson);
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void addGrade(Grade grade) {
        //Adding new Grade
        grades.add(grade);
    }

    public void removeGrade(Grade grade) {
        grades.remove(grade);
    }

    //Returns the gradePointAverage
    public float getGradePointAverage() {
        float gradePointAverage;
        float writtenGradeAverage = 0f;
        float writtenGrades = 0f;
        float vocalGradeAverage = 0f;
        float vocalGrades = 0f;

        //loops through all 'grades' in grades
        for (Grade _grade : grades) {
            //Adds the Grade to the average
            if (_grade.iswritten()) {
                writtenGradeAverage += _grade.getGrade() * _grade.getRating();
                writtenGrades += _grade.getRating();
            } else if (!_grade.iswritten()) {
                vocalGradeAverage += _grade.getGrade() * _grade.getRating();
                vocalGrades += _grade.getRating();
            }
        }

        writtenGradeAverage /= writtenGrades;
        vocalGradeAverage /= vocalGrades;

        if (Float.isNaN(writtenGradeAverage)) {
            gradePointAverage = vocalGradeAverage;
        } else if (Float.isNaN(vocalGradeAverage)) {
            gradePointAverage = writtenGradeAverage;
        } else {
            gradePointAverage = (ratingSub * writtenGradeAverage + vocalGradeAverage) / (ratingSub + 1);
        }

        return Util.round(gradePointAverage, 2);
    }

    //Returns all Grades
    public ArrayList<Grade> getAllGrades() {
        return grades;
    }

    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    public ArrayList<Task> getAllTasksSorted() {
        System.out.println(tasks);
        Collections.sort(tasks);
        //tasks.stream().sorted().collect(Collectors.toList())
        return tasks;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        return name.equals(subject.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Calendar getNextLessonAfter(Calendar after,SchoolLessonSystem sls){
        if(lessons.isEmpty())
            return after;
        Calendar ret = (Calendar) after.clone();
        ret.add(Calendar.YEAR,5);
        for (Lesson lesson : lessons) {
            Calendar tmp = lesson.getNextLessonAfter(after, sls);

            if (ret.after(tmp))
                ret = tmp;
        }
        return ret;
    }
}
