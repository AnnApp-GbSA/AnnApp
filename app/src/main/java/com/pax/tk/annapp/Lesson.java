package com.pax.tk.annapp;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;

import static java.lang.Math.floor;

/**
 * Created by Tobias Kiehnlein on 03.01.2018.
 */

public class Lesson implements Serializable, Comparable<Lesson> {

    //the subject of this lesson
    private Subject subject;
    private int day;
    private int time;

    //the room in which this very lesson takes place
    private String room;

    /**
     * creates a lesson with a subject, room, day as Integer and time as Integer
     *
     * @param subject subject of the lesson
     * @param room room in which the lesson takes place
     * @param day day at which the lesson takes place
     * @param time time at which the lesson takes place
     */
    public Lesson(Subject subject, String room, int day, int time) {
        this.subject = subject;
        this.room = room;

        this.day = day;
        this.time = time;
    }

    /**
     * get the subject of a Lesson
     *
     * @return subject of the Lesson
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * sets the subject of a Lesson
     *
     * @param subject subject to be set
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     * get the room of a Lesson
     *
     * @return room of the Lesson
     */
    public String getRoom() {
        if (room == null)
            return subject.getRoom();
        return room;
    }

    /**
     * sets the room of a Lesson
     *
     * @param room room to be set
     */
    public void setRoom(String room) {
        this.room = room;
    }

    /**
     * get the day of a Lesson
     *
     * @return day of the Lesson as Integer
     */
    public int getDay() {
        return day;
    }

    /**
     * sets the day of a Lesson
     *
     * @param day day to be set as Integer
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * get the time of a Lesson
     *
     * @return time of the Lesson as Integer
     */
    public int getTime() {
        return time;
    }

    /**
     * sets the time of a Lesson
     *
     * @param time time to be set as Integer
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * get the next lesson
     *
     * @param base ...
     * @param sls SchoolLessonSystem
     * @return Calendar
     */
    public Calendar getNextLessonAfter(Calendar base, SchoolLessonSystem sls) {
        Calendar ret = (Calendar) base.clone();
        ret.set(Calendar.DAY_OF_WEEK, Util.calendarWeekdayByDayIndex(day));
        ret.set(Calendar.HOUR_OF_DAY, (int) Math.floor((double) /**sls.getSchoolstart()*/ /*TODO Change back to schoolstart time from sls*/ 240 / 60.0));
        ret.set(Calendar.MINUTE, /*sls.getSchoolstart()*/ 240 % 60);
        ret.add(Calendar.MINUTE, time * /*sls.getLessonLenght()*/45);
        /*for(Integer b:sls.getBreaksAfterLesson())
            if(b<time)
                ret.add(Calendar.MINUTE,sls.getBreakLenght());
        if(!ret.after(base))
            ret.add(Calendar.WEEK_OF_YEAR,1);*/
        if(ret.getTimeInMillis() < base.getTimeInMillis())
            ret.add(Calendar.DAY_OF_YEAR, 7);

        if(ret.getTimeInMillis() == base.getTimeInMillis())
            ret.add(Calendar.DAY_OF_YEAR, 7);

        return ret;
    }

    /**
     * compares a lesson to this Lesson
     *
     * @param lesson Lesson to be compared
     * @return 1 if this Lesson is after the other one, -1 if it is before and 0 if they are at the same time
     */
    @Override
    public int compareTo(@NonNull Lesson lesson) {
        if (day == lesson.day)
            if (time > lesson.time)
                return 1;
            else if (time < lesson.time)
                return -1;
            else
                return 0;
        else if (day > lesson.day)
            return 1;
        return -1;
    }

    /**
     * converts a Lesson into a String
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Lesson{" +
                "subject=" + subject +
                ", day=" + day +
                ", time=" + time +
                ", room='" + room + '\'' +
                '}';
    }
}
