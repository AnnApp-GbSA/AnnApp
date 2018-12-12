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

    /**
     * creates a subject with a name, a teacher, a room and a rating for the written grades
     *
     * @param name name of the subject
     * @param rating rating for the written grades of this subject
     * @param teacher teacher of the subject
     * @param room room of the subject
     */
    public Subject(String name, int rating, String teacher, String room) {
        this.name = name;
        ratingSub = rating;
        this.teacher = teacher;
        this.room = room;
    }

    /**
     * gets the name of a subject
     *
     * @return name of the subject
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of a subject
     *
     * @param name name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets the rating of a subject
     *
     * @return rating of the subject
     */
    public int getRatingSub() {
        return ratingSub;
    }

    /**
     * sets the rating of a subject
     *
     * @param ratingSub rating to be set
     */
    public void setRatingSub(int ratingSub) {
        this.ratingSub = ratingSub;
    }

    /**
     * gets the teacher of a subject
     *
     * @return teacher of the subject
     */
    public String getTeacher() {
        return teacher;
    }

    /**
     * gets the room of a subject
     *
     * @return room of the subject
     */
    public String getRoom() {
        return room;
    }

    /**
     * sets the room of a subject
     *
     * @param room room to be set
     */
    public void setRoom(String room) {
        this.room = room;
    }

    /**
     * sets the teacher of a subject
     *
     * @param teacher teacher to be set
     */
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    /**
     * adds a task to the tasks of this subject
     *
     * @param task task to be added
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * removes a task from the tasks of this subject
     *
     * @param task task to be removed
     */
    public void removeTask(Task task) {
        tasks.remove(task);
    }

    /**
     * adds a lesson to the lessons of this subject
     *
     * @param lesson lesson to be added
     */
    public void addLesson(Lesson lesson){
        lessons.add(lesson);
    }

    /**
     * removes a lesson from the lessons of this subject
     *
     * @param lesson lesson to be removed
     */
    public void removeLesson(Lesson lesson){
        lessons.remove(lesson);
    }

    /**
     * removes all lessons from this subject
     */
    public void removeAllLessons(){
        lessons.clear();
    }

    /**
     * gets all lessons of this subject
     *
     * @return lessons of this subject as ArrayList<>
     */
    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    /**
     * sets the position of this subject
     *
     * @param position position to be set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * adds a grade to this subject
     *
     * @param grade grade to be added
     */
    public void addGrade(Grade grade) {
        //Adding new Grade
        grades.add(grade);
    }

    /**
     * removes a grade from this subject
     *
     * @param grade grade to be removed
     */
    public void removeGrade(Grade grade) {
        grades.remove(grade);
    }

    /**
     * gets the average of all grades of a subject
     *
     * @return average of all grades of the subject
     */
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

    /**
     * gets a grades of this subject
     *
     * @return all grades of this subject as ArrayList<>
     */
    public ArrayList<Grade> getAllGrades() {
        return grades;
    }

    /**
     * gets all tasks of this subject
     *
     * @return all tasks of this subject as ArrayList<>
     */
    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    /**
     * gets all tasks of this subject sorted by time
     *
     * @return all tasks of this subject as ArrayList<>
     */
    public ArrayList<Task> getAllTasksSorted() {
        System.out.println(tasks);
        Collections.sort(tasks);
        //tasks.stream().sorted().collect(Collectors.toList())
        return tasks;
    }

    /**
     * converts this subject into a String
     *
     * @return String
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * compares this subject with an other object
     *
     * @param o object this subject shall be compared with
     * @return true if this subject equals the object, false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        return name.equals(subject.name);
    }

    /**
     * creates a hash code for this subject
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * gets the next lesson after a time
     *
     * @param after time which of the next lesson shall be found
     * @param sls schoolLessonSystem which shall be used
     * @return next lesson
     */
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
