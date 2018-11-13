package com.pax.tk.annapp;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.github.sundeepk.compactcalendarview.domain.Event;
import com.pax.tk.annapp.Adapter.RVAdapterNews;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class SubjectManager {

    private static final SubjectManager subjectManager = new SubjectManager();

    Context context;

    String filename;

    private SchoolLessonSystem schoolLessonSystem = null;

    //Contains all subjects
    ArrayList<Subject> subjects = new ArrayList<Subject>();
    ArrayList<News> news = new ArrayList<>();
    Day[] days;
    Set<Event> events = new HashSet<>();
    //TODO: Set<Event> sortedEvents = new TreeSet<>(events);

    private SubjectManager() {
        System.out.println("Create SubjectManager...");
        days = new Day[]{new Day(0), new Day(1), new Day(2), new Day(3), new Day(4)};
    }

    //Returns the singelton subjectManager
    public static SubjectManager getInstance() {
        return subjectManager;
    }

    public void setContext(Context c) {
        this.context = c;
    }

    public void setSchoolLessonSystem(SchoolLessonSystem schoolLessonSystem) {
        if (schoolLessonSystem == null) {
            Set s = new HashSet<Integer>();
            s.add(2);
            s.add(4);
            int schoolstart = ((MainActivity) context).getPreferences(MODE_PRIVATE).getInt(context.getString(R.string.key_schoolstart), 480);
            int lessonTime = ((MainActivity) context).getPreferences(MODE_PRIVATE).getInt(context.getString(R.string.key_lessonTime), 45);
            int breakTime = ((MainActivity) context).getPreferences(MODE_PRIVATE).getInt(context.getString(R.string.key_breakTime), 15);
            setSchoolLessonSystem(new SchoolLessonSystem(schoolstart, lessonTime, breakTime, s));
        }
        this.schoolLessonSystem = schoolLessonSystem;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public void addSubject(Subject subject) {
        if (subjects.contains(subject))
            return;
        subjects.add(subject);
        sortSubjects();
        //save();
    }

    public ArrayList<News> getNews() {
        return news;
    }

    //Gives back the average of all subjects
    public float getWholeGradeAverage() {
        float wholeGradeAverage = 0;
        int emptySubjects = 0;

        //Goes through all subjects and get the GradePointaverage and adds it to the wholeAverageGrade
        for (Subject _subject : subjects) {
            wholeGradeAverage += _subject.getGradePointAverage();
            System.out.println(wholeGradeAverage);
        }

        for (Subject _subject : subjects) {
            if (_subject.grades.isEmpty()) {
                emptySubjects += 1;
            }
        }

        wholeGradeAverage /= (subjects.size() - emptySubjects);
        return Util.round(wholeGradeAverage, 2);
    }

    public int getLongestDaysLessons() {
        int i = 0;
        System.out.println(i);
        for (Day d :
                days) {
            int x = 0;
            for (Lesson l :
                    d.getLessons()) {
                x++;
            }

            Boolean subjectExists = false;
            for (int start = d.getLessons().size(); start > 0; start--) {
                if (d.getLesson(start) == null && subjectExists) {
                    x--;
                } else {
                    subjectExists = true;
                }
            }
            if (x > i)
                i = x;
        }
        return i;
    }

    public void load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(context.openFileInput("AnnApp"));
            try {
                subjects = (ArrayList<Subject>) ois.readObject();
            } catch (Exception e) {
            }
            try {
                days = (Day[]) ois.readObject();
            } catch (Exception e) {
            }
            try {
                news = (ArrayList<News>) ois.readObject();
            } catch (Exception e) {
            }
            System.out.println("loading:");
            System.out.println(subjects);
            System.out.println(days);
            System.out.println(news);
            ois.close();
        } catch (Exception e) {
            System.out.println("loading failed ---------------------------------------------------------------------------------------------------------");
            e.printStackTrace();
        }
    }

    public void save() {
        System.out.println("saving:");
        System.out.println(subjects);
        System.out.println(days);
        System.out.println(news);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput("AnnApp", MODE_PRIVATE));
            oos.writeObject(subjects);
            oos.writeObject(days);
            oos.writeObject(news);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setLesson(Lesson lesson) {
        int day = lesson.getDay();
        System.out.println(lesson.getDay());
        int time = lesson.getTime() - 1;
        System.out.println(lesson.getTime());
        System.out.println("Lesson: " + lesson.toString());
        if (lesson.getSubject() == null) {
            days[day].getLesson(time).getSubject().removeLesson(days[day].getLesson(time));
        } else if (days[day].getLesson(time) == null) {

        } else if (days[day].getLesson(time).getSubject() == null) {
            lesson.getSubject().addLesson(lesson);
        } else if (!days[day].getLesson(time).getSubject().equals(lesson.getSubject())) {
            days[day].getLesson(time).getSubject().removeLesson(days[day].getLesson(time));
            lesson.getSubject().addLesson(lesson);
        }
        System.out.println("InSubMan");
        days[day].setLesson(lesson);
        //save();
    }

    public void deleteLesson(Lesson lesson) {
        for (Day d :
                days) {
            for (Lesson l :
                    d.getLessons()) {
                if (l == lesson) {
                    d.getLessons().set(lesson.getTime(), null);
                    return;
                }
            }
        }
    }

    public void deleteAllLessons(Lesson lesson) {
        for (Day d :
                days) {
            for (Lesson l :
                    d.getLessons()) {
                if (l == null)
                    continue;
                if (l.getSubject() == lesson.getSubject())
                    d.getLessons().set(l.getTime(), null);
            }
        }
    }

    public void deleteSubject(Subject subject) {
        subjects.remove(subject);
    }

    public Day[] getDays() {
        return days;
    }

    public SchoolLessonSystem getSchoolLessonSystem() {
        return schoolLessonSystem;
    }

    public News getOneNews(int position) {
        return news.get(position);
    }

    public int getNewsCount() {
        return news.size();
    }

    public void mergeNews(ArrayList<News> news) {
//        ArrayList<News> reallyNewNews = new ArrayList<>();
//        for (News n : news) {
//            if (!this.news.contains(n)) {
//                System.out.println(n);
//                reallyNewNews.add(n);
//            } else
//                for (int i = 0; i < this.news.size(); i++)
//                    if (this.news.get(i).equals(n))
//                        this.news.set(i, n);
//        }
//        reallyNewNews.addAll(this.news);
//        this.news = reallyNewNews;

        //this.news = news;
        this.news = news;
    }

    public void addNews (News news){
        this.news.add(news);
    }

    public void sortSubjects() {
        Collections.sort(subjects,
                (o1, o2) -> o1.getName().compareTo(o2.getName()));
    }

    public Drawable getFromURl(String url) {
        if (url == null)
            return null;
        Drawable d = Drawable.createFromPath(context.getFilesDir().getAbsolutePath() + "/newsimage" + String.valueOf(url.hashCode()));
        if (d == null)
            System.out.println("Missing Image: \"" + url + "\" (" + String.valueOf(url.hashCode()) + ")");
        else
            return d;
        System.out.println("Image gets loaded \"" + url + "\" (" + String.valueOf(url.hashCode()) + ")");
        try {
            d = Util.drawableFromUrl(url);
            FileOutputStream fileOutStream = context.openFileOutput("newsimage" + String.valueOf(url.hashCode()), MODE_PRIVATE);
            ((BitmapDrawable) d).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fileOutStream);
            fileOutStream.close();
            System.out.println("Saved \"" + url + "\" (" + String.valueOf(url.hashCode()) + ")");
            //System.out.println("Worked= : "+t2.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed saving image \"" + url + "\" (" + String.valueOf(url.hashCode()) + ")");
        }
        return d;
    }

    public ArrayList<Lesson> getTodaysLessons() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                return days[0].getLessons();
            case Calendar.TUESDAY:
                return days[1].getLessons();
            case Calendar.WEDNESDAY:
                return days[2].getLessons();
            case Calendar.THURSDAY:
                return days[3].getLessons();
            case Calendar.FRIDAY:
                return days[4].getLessons();
            default:
                return null;

        }
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event){
        events.add(event);
    }

    public void removeEvent(Event event){
        events.remove(event);
    }
}
