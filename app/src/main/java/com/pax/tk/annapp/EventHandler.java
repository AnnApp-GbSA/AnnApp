package com.pax.tk.annapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.URL;

public class EventHandler{
    SubjectManager subjectManager;

    public EventHandler(){
        subjectManager = SubjectManager.getInstance();
    }

    public static void loadSchoolEvents(){
        try{
            URL url = new URL("https://calendar.google.com/calendar/ical/o5bthi1gtvamdjhed61rot1e74%40group.calendar.google.com/public/basic.ics");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
