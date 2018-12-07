package com.pax.tk.annapp;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.pax.tk.annapp.Notification.Notification;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class Manager {

    private static final Manager manager = new Manager();

    Context context;

    String filename;

    CompactCalendarView compactCalendarView;

    private SchoolLessonSystem schoolLessonSystem = null;

    //Contains all subjects
    ArrayList<Subject> subjects = new ArrayList<Subject>();
    ArrayList<News> news = new ArrayList<>();
    Day[] days;
    public Set<Event> schoolEvents = new HashSet<>();
    //TODO: Set<Event> sortedEvents = new TreeSet<>(schoolEvents);
    public Set<Event> privateEvents = new HashSet<>();

    public int test = 0;

    /**
     * creates a manager
     */
    private Manager() {
        System.out.println("Create Manager...");
        days = new Day[]{new Day(0), new Day(1), new Day(2), new Day(3), new Day(4)};
    }

    /**
     * get the singleton manager
     *
     * @return singleton manager
     */
    public static Manager getInstance() {
        return manager;
    }

    /**
     * sets the context
     *
     * @param c Context to be set
     */
    public void setContext(Context c) {
        this.context = c;
    }

    /**
     * sets the school lesson system
     *
     * @param schoolLessonSystem SchoolLessonSystem to be set
     */
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

    /**
     * get subjects
     *
     * @return subjects in an ArrayList<>
     */
    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    /**
     * adds subject if subjects does not contain it already
     *
     * @param subject Subject to add
     */
    public void addSubject(Subject subject) {
        if (subjects.contains(subject))
            return;
        subjects.add(subject);
        sortSubjects();
        //save();
    }

    /**
     * get news
     *
     * @return news in an ArrayList<>
     */
    public ArrayList<News> getNews() {
        return news;
    }

    /**
     * get the grade average of all subjects
     *
     * @return grade average of all subjects
     */
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

    /**
     * get the lessons of the longest day
     *
     * @return lessons of the longest day as Integer
     */
    public int getLongestDaysLessons() {

        int longestLength = 0;
        int length;
        int puffer;

        for (Day d :
                days) {

            length = 0;
            puffer = 0;

            for (Lesson l :
                    d.getLessons()) {
                if (l == null) {
                    puffer += 1;
                } else {
                    length += 1;
                    length += puffer;
                    puffer = 0;
                }
            }

            if (length > longestLength) {
                longestLength = length;
            }

        }

        return longestLength;
    }

    /**
     * loads subjects, days and news
     */
    public void load() {
        loadSchoolEvents();
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
            try {
                schoolEvents = (Set<Event>) ois.readObject();
            } catch (Exception e) {
            }
            System.out.println("loading:");
            System.out.println(subjects);
            System.out.println(days);
            System.out.println(news);
            ois.close();
            for (News n :
                    news) {
                n.setImage(getFromURl(n.getImageurl()));
            }
        } catch (Exception e) {
            System.out.println("loading failed ---------------------------------------------------------------------------------------------------------");
            e.printStackTrace();
        }
    }


    /**
     * saves subjects, days and news
     */
    public void save() {
        System.out.println("saving:");
        System.out.println(subjects);
        System.out.println(days);
        System.out.println(news);
        saveSchoolEvents();
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

    /**
     * loads school events
     */
    private void loadSchoolEvents() {
        ArrayList<CustomEvent> schoolEventsPuffer = (ArrayList<CustomEvent>) (new Util()).load(context, "schoolEvents");
        if (schoolEventsPuffer == null)
            schoolEventsPuffer = new ArrayList<>();
        for (CustomEvent ce :
                schoolEventsPuffer) {
            schoolEvents.add(new Event(ce.getColor(), ce.getTimeInMillis(), ce.getData()));
        }

        ArrayList<CustomEvent> privateEventsPuffer = (ArrayList<CustomEvent>) (new Util()).load(context, "privateEvents");
        if (privateEventsPuffer == null)
            privateEventsPuffer = new ArrayList<>();
        for (CustomEvent ce :
                privateEventsPuffer) {
            privateEvents.add(new Event(ce.getColor(), ce.getTimeInMillis(), ce.getData()));
        }
    }

    /**
     * saves school events
     */
    private void saveSchoolEvents() {
        ArrayList<CustomEvent> customSchoolEvents = new ArrayList<>();
        for (Event event :
                schoolEvents) {
            customSchoolEvents.add(new CustomEvent(event.getColor(), event.getTimeInMillis(), event.getData()));
        }
        (new Util()).save(context, customSchoolEvents, "schoolEvents");

        ArrayList<CustomEvent> customPrivateEvents = new ArrayList<>();
        for (Event event :
                privateEvents) {
            customPrivateEvents.add(new CustomEvent(event.getColor(), event.getTimeInMillis(), event.getData()));
        }
        (new Util()).save(context, customPrivateEvents, "privateEvents");
    }

    /**
     * sets a lesson
     *
     * @param lesson Lesson to be set
     */
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

    /**
     * deletes a lesson
     *
     * @param lesson Lesson to delete
     */
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

    /**
     * deletes all lessons of a Lesson
     *
     * @param lesson Lesson to delete everywhere
     */
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

    /**
     * deletes a subject
     *
     * @param subject Subject to delete
     */
    public void deleteSubject(Subject subject) {
        subjects.remove(subject);
    }

    /**
     * get days
     *
     * @return days as Day[]
     */
    public Day[] getDays() {
        return days;
    }

    /**
     * get school lesson system
     *
     * @return SchoolLessonSystem
     */
    public SchoolLessonSystem getSchoolLessonSystem() {
        return schoolLessonSystem;
    }

    /**
     * get news at a position
     *
     * @param position position of the news
     * @return News
     */
    public News getOneNews(int position) {
        return news.get(position);
    }

    /**
     * get size of news
     *
     * @return size of news
     */
    public int getNewsCount() {
        return news.size();
    }

    /**
     * updates the news with new news
     *
     * @param news new news
     */
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

    /**
     * sorts the subjects by name
     */
    public void sortSubjects() {
        Collections.sort(subjects,
                (o1, o2) -> o1.getName().compareTo(o2.getName()));
    }

    /**
     * get image from url
     *
     * @param url url from which the image is as String
     * @return image as Drawable
     */
    public Drawable getFromURl(String url) {
        if (url == null)
            return null;
        Drawable d = null;

        try {

            d = Drawable.createFromPath(context.getFilesDir().getAbsolutePath() + "/newsimage" + String.valueOf(url.hashCode()));

            if (d == null)
                System.out.println("Missing Image: \"" + url + "\" (" + String.valueOf(url.hashCode()) + ")");
            else
                return d;
            System.out.println("Image gets loaded \"" + url + "\" (" + String.valueOf(url.hashCode()) + ")");

            d = Util.drawableFromUrl(url);
            FileOutputStream fileOutStream = context.openFileOutput("newsimage" + String.valueOf(url.hashCode()), MODE_PRIVATE);
            ((BitmapDrawable) d).getBitmap().compress(Bitmap.CompressFormat.PNG, 10, fileOutStream);
            fileOutStream.close();
            System.out.println("Saved \"" + url + "\" (" + String.valueOf(url.hashCode()) + ")");
            //System.out.println("Worked= : "+t2.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed saving image \"" + url + "\" (" + String.valueOf(url.hashCode()) + ")");
        }
        return d;
    }

    /**
     * get the school events
     *
     * @return school events in a Set<>
     */
    public Set<Event> getSchoolEvents() {
        return schoolEvents;
    }

    /**
     * adds a event to the school events
     *
     * @param event Event to add
     */
    public void addSchoolEvent(Event event) {
        int color = Color.GREEN;

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.mm.yyyy");
            String endTime = ((String) event.getData()).split("°°")[0];
            if (!simpleDateFormat.format(new Date(Long.valueOf(endTime))).equals(simpleDateFormat.format(event.getTimeInMillis())) && (event.getTimeInMillis() == Long.valueOf(endTime) && (new SimpleDateFormat("kk:mm")).format(new Date(event.getTimeInMillis())).contains("24:00") || (new SimpleDateFormat("kk:mm")).format(new Date(Long.valueOf(endTime))).contains("24:00"))) {

                color = Color.RED;

                Event endEvent = new Event(color, Long.valueOf(endTime), event.getData());
                if (!compactCalendarView.getEvents(new Date(endEvent.getTimeInMillis())).contains(endEvent))
                    compactCalendarView.addEvent(endEvent);
                Long currentDate = Long.valueOf(endTime);
                for (int i = 1; currentDate - i * 86400000L > event.getTimeInMillis(); i++) {
                    if (!compactCalendarView.getEvents(currentDate - i * 86400000L).contains(new Event(color, currentDate - i * 86400000L, event.getData())))
                        compactCalendarView.addEvent(new Event(color, currentDate - i * 86400000L, event.getData()));
                }
            }
            //TODO check for n-day event
            if (!compactCalendarView.getEvents(new Date(event.getTimeInMillis())).contains(event))
                compactCalendarView.addEvent(new Event(color, event.getTimeInMillis(), event.getData()));
        } catch (Exception e) {
        }

        schoolEvents.add(new Event(color, event.getTimeInMillis(), event.getData()));
    }

    /**
     * removes a school event
     *
     * @param event Event to remove
     */
    public void removeSchoolEvent(Event event) {
        schoolEvents.remove(event);
        //TODO test for n-day event
        try {
            compactCalendarView.removeEvent(event);
        } catch (Exception e) {
        }
    }

    /**
     * removes a private event
     *
     * @param event Event to remove
     */
    public void removePrivateEvent(Event event) {
        privateEvents.remove(event);
        //TODO test for n-day event
        try {
            compactCalendarView.removeEvent(event);
        } catch (Exception e) {
        }
    }

    /**
     * adds a private event
     *
     * @param event Event to add
     */
    public void addPrivateEvent(Event event) {
        System.out.println("Adding private event:" + event.getData());
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.mm.yyyy");
            String endTime = ((String) event.getData()).split("°°")[0];
            System.out.println(endTime);
            if (!simpleDateFormat.format(new Date(Long.valueOf(endTime))).equals(simpleDateFormat.format(event.getTimeInMillis())) && (event.getTimeInMillis() == Long.valueOf(endTime) && (new SimpleDateFormat("kk:mm")).format(new Date(event.getTimeInMillis())).contains("24:00") || (new SimpleDateFormat("kk:mm")).format(new Date(Long.valueOf(endTime))).contains("24:00"))) {
                Event endEvent = new Event(event.getColor(), Long.valueOf(endTime), event.getData());
                System.out.println("adding n-day private event");
                if (!compactCalendarView.getEvents(new Date(endEvent.getTimeInMillis())).contains(endEvent))
                    compactCalendarView.addEvent(endEvent);
                Long currentDate = Long.valueOf(endTime);
                for (int i = 1; currentDate - i * 86400000L > event.getTimeInMillis(); i++) {
                    compactCalendarView.addEvent(new Event(event.getColor(), currentDate - i * 86400000L, event.getData()));
                }
            }
            //TODO check for n-day event
            if (!compactCalendarView.getEvents(new Date(event.getTimeInMillis())).contains(event))
                compactCalendarView.addEvent(new Event(event.getColor(), event.getTimeInMillis(), event.getData()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        privateEvents.add(new Event(event.getColor(), event.getTimeInMillis(), event.getData()));
    }

    /**
     * get the private events
     *
     * @return private events in a Set<>
     */
    public Set<Event> getPrivateEvents() {
        return privateEvents;
    }

    /**
     * sets the compact calendar view
     *
     * @param compactCalendarView CompactCalendarView to set
     */
    public void setCompactCalendarView(CompactCalendarView compactCalendarView) {
        this.compactCalendarView = compactCalendarView;
    }

}
