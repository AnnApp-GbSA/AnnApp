package com.pax.qbt.annapp;

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

    public Lesson(Subject subject, String room, int day, int time) {
        this.subject = subject;
        this.room = room;

        this.day = day;
        this.time = time;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getRoom() {
        if (room == null)
            return subject.getRoom();
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

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
        return ret;
    }

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
