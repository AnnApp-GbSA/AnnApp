package com.pax.tk.annapp;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Jakob on 13.01.2018.
 */

public class Task implements Serializable, Comparable<Task>{

    private String task;
    private Calendar date;
    private Subject subject;
    private String kind;
    private Calendar due;

    /**
     * creates a task with an information, a date, the kind of the task, a subject and the due in it
     *
     * @param task description of the task
     * @param date date of the task
     * @param kind kind of the task
     * @param subject subject of the task
     * @param due due of the task
     */
    public Task(String task, Calendar date, String kind, Subject subject, Calendar due){
        this.task = task;
        this.date=date;
        this.due = due;
        this.kind = kind;
        this.subject = subject;
    }

    /**
     * gets the task
     *
     * @return description as String
     */
    public String getTask() {
        return task;
    }

    /**
     * sets the task
     *
     * @param task description to be set
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * gets the date
     *
     * @return date of the task
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * sets the date
     *
     * @param date date to be set
     */
    public void setDate(Calendar date) {
        this.date = date;
    }

    /**
     * gets the subject
     *
     * @return subject of the task
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * sets the subject
     *
     * @param subject subject to be set
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     * gets the kind
     *
     * @return kind of the task
     */
    public String getKind() {
        return kind;
    }

    /**
     * sets the kind
     *
     * @param kind kind to be set
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * gets the due
     *
     * @return due of the task
     */
    public Calendar getDue() {
        return due;
    }

    /**
     * sets the due
     *
     * @param due due to be set
     */
    public void setDue(Calendar due) {
        this.due = due;
    }

    /**
     * compares the task to another one
     *
     * @param task task to to compare with the first one
     * @return due between the two tasks
     */
    @Override
    public int compareTo(@NonNull Task task) { //TODO Check order
        return getDue().after(task.getDue())?1:
                getDue().before(task.getDue())?-1:
                getDate().after(task.getDate())?1:
                        getDate().after(task.getDate())?-1:0;
    }

    /**
     * converts the task into a String
     *
     * @return String with all information of the task in it
     */
    @Override
    public String toString() {
        return "Task{" +
                "task='" + task + '\'' +
                ", date=" + date +
                ", subject=" + subject +
                ", kind='" + kind + '\'' +
                ", due=" + due +
                '}';
    }
}