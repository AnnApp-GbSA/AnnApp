package de.tk.annapp;

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

    public Task(String task, Calendar date, String kind, Subject subject, Calendar due){
        this.task = task;
        this.date=date;
        this.due = due;
        this.kind = kind;
        this.subject = subject;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Calendar getDue() {
        return due;
    }

    public void setDue(Calendar due) {
        this.due = due;
    }

    @Override
    public int compareTo(@NonNull Task task) { //TODO Check order
        return getDue().after(task.getDue())?1:
                getDue().before(task.getDue())?-1:
                getDate().after(task.getDate())?1:
                        getDate().after(task.getDate())?-1:0;
    }

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