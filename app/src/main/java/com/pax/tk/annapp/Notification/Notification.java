package com.pax.tk.annapp.Notification;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Notification implements Serializable {

    private String eventText;
    private String subjectName;
    private Calendar date;
    private int ID;

    public Notification (String eventText, String subjectName, int ID, Calendar date){
        this.eventText = eventText;
        this.ID = ID;
        this.subjectName = subjectName;
        this.date = date;
    }

    public Notification(){

    }

    public String getEventText() {
        return eventText;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Calendar getDate() {
        return date;
    }

        //this.date = date;
    public void setDate(long date){
        Calendar now = Calendar.getInstance();
        now.setTime(new Date(date));
        this.date = now;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "eventText='" + eventText + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", ID='" + String.valueOf(ID) + '\'' +
                ", date='" + String.valueOf(date) + '\'' +
                '}';
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", ID);
            jsonObject.put("subjectName", subjectName);
            jsonObject.put("date", date.getTimeInMillis());
            jsonObject.put("eventText", eventText);
        } catch (JSONException e) {
            throw new IllegalStateException("Failed to convert the object to JSON");
        }
        return jsonObject.toString();
    }

    /**
     * Parses a Json string to an {@link Notification} instance.
     *
     * @param string The String representation of an alarm
     * @return an instance of {@link Notification}
     */
    public static Notification fromJson(String string) {
        JSONObject jsonObject;
        Notification notification = new Notification();
        try {
            jsonObject = new JSONObject(string);
            notification.setEventText(jsonObject.getString("eventText"));
            notification.setDate(jsonObject.getLong("date"));
            notification.setID(jsonObject.getInt("ID"));
            notification.setSubjectName(jsonObject.getString("subjectName"));
        } catch (JSONException e) {
            throw new IllegalArgumentException("Failed to parse the String: " + string);
        }

        return notification;
    }
}
