package com.pax.tk.annapp;


import android.graphics.Color;
import android.os.AsyncTask;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventHandler{
    Manager manager;

    /**
     * creates an event handler
     */
    public EventHandler(){
        manager = Manager.getInstance();
    }

    /**
     * loads the school events by calling a AsyncTask
     */
    public static void loadSchoolEvents(){

        new LoadSchoolEvents().execute((Void) null);

    }

    /**
     * transforms a date as String into a date as Long in milliseconds
     *
     * @param date date to transform as String
     * @return date as Long in milliseconds
     * @throws ParseException ...
     */
    public static long timeInMillis(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        Date datum = null;
        try {
            datum = (Date) formatter.parse(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        long timestamp = datum.getTime();
        return timestamp;
    }

    private static class LoadSchoolEvents extends AsyncTask<Void, Void, Void>{



        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param voids The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Void... voids) {

            try{

                Manager manager = Manager.getInstance();

                Object[] currentEvents = (Object[]) manager.getSchoolEvents().toArray();
                System.out.println("Eventssize: "+ currentEvents.length);

                URL url = new URL("https://calendar.google.com/calendar/ical/o5bthi1gtvamdjhed61rot1e74%40group.calendar.google.com/public/basic.ics");

                //loading Events
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String input;
                ArrayList<String> inputs = new ArrayList<>();
                while ((input = bufferedReader.readLine()) != null) {
                    inputs.add(input);
                }
                bufferedReader.close();

                //Managing Events
                ArrayList<Event> eventsToGet = new ArrayList<>();

                long startDate = -1;
                long endDate = -1;
                int startMonth = -1;
                int endMonth = -1;
                int startHour = -1;
                int endHour = -1;
                String uid = null;
                String location = null;
                String summary = null;

                for (String s :
                        inputs) {
                    if (s.startsWith("BEGIN:VEVENT")) {
                        startDate = -1;
                        endDate = -1;
                        startMonth = -1;
                        endMonth = -1;
                        startHour = -1;
                        endHour = -1;
                        uid = null;
                        location = null;
                        summary = null;
                    } else if (s.startsWith("DTSTART:")) {
                        String x = s.replace("DTSTART:", "");
                        try {
                            int year = Integer.valueOf(x.substring(0, 4));
                            startMonth = Integer.valueOf(x.substring(4, 6));
                            int day = Integer.valueOf(x.substring(6, 8));
                            String minutes = x.substring(11, 13);
                            int hours = Integer.valueOf(x.substring(9, 11));
                            if (startMonth > 3 && startMonth < 11) {
                                startHour = (hours + 2);
                            } else if (startMonth > 10 || startMonth < 4) {
                                startHour = (hours + 1);
                            } else {
                                System.out.println("Fehler bei Berechnung der Startzeit");
                            }
                            String date = day + "." + startMonth + "." + year + " " + startHour + ":" + minutes;
                            startDate = timeInMillis(date);
                        } catch (Exception e) {
                        }
                    } else if (s.startsWith("DTSTART;VALUE=DATE:")) {
                        String x = s.replace("DTSTART;VALUE=DATE:", "");
                        try {
                            int year = Integer.valueOf(x.substring(0, 4));
                            int month = Integer.valueOf(x.substring(4, 6));
                            int day = Integer.valueOf(x.substring(6, 8));
                            String date = day + "." + month + "." + year + " " + "00:00";
                            startDate = timeInMillis(date);
                        } catch (Exception e) {
                        }
                    } else if (s.startsWith("DTEND:")) {
                        String x = s.replace("DTEND:", "");
                        try {
                            int year = Integer.valueOf(x.substring(0, 4));
                            endMonth = Integer.valueOf(x.substring(4, 6));
                            int day = Integer.valueOf(x.substring(6, 8));
                            String minutes = x.substring(11, 13);
                            int hours = Integer.valueOf(x.substring(9, 11));
                            if (endMonth > 3 && endMonth < 11) {
                                endHour = (hours + 2);
                            } else if (endMonth > 10 || endMonth < 4) {
                                endHour = (hours + 1);
                            } else {
                                System.out.println("Fehler bei Berechnung der Endzeit");
                            }
                            String date = day + "." + endMonth + "." + year + " " + endHour + ":" + minutes;
                            endDate = timeInMillis(date);
                        } catch (Exception e) {
                        }
                    } else if (s.startsWith("DTEND;VALUE=DATE:")) {
                        String x = s.replace("DTEND;VALUE=DATE:", "");
                        try {
                            int year = Integer.valueOf(x.substring(0, 4));
                            int month = Integer.valueOf(x.substring(4, 6));
                            int day = Integer.valueOf(x.substring(6, 8));
                            String date = day + "." + month + "." + year + " " + "00:00";
                            endDate = timeInMillis(date);
                        } catch (Exception e) {
                        }
                    } else if (s.startsWith("UID:")) {
                        uid = s.replace("UID:", "");
                    } else if (s.startsWith("SUMMARY:")) {
                        summary = s.replace("SUMMARY:", "");
                    } else if (s.startsWith("LOCATION:")) {
                        location = s.replace("LOCATION:", "");
                    } else if (s.startsWith("END:VEVENT")) {
                        //TODO really add to calendar
                        eventsToGet.add(new Event(Color.GREEN, startDate, endDate + "°°" + location + "°°" + summary + "°°" + uid));

                        for (int i = 0; i<currentEvents.length; i++){
                            Event e = (Event) currentEvents[i];
                            if (((String) e.getData()).contains(uid))
                                manager.removeSchoolEvent(e);
                        }

                        manager.addSchoolEvent(new Event(Color.GREEN, startDate, endDate + "°°" + location + "°°" + summary + "°°" + uid));
                    }
                }

                System.out.println(eventsToGet);

            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }

}
