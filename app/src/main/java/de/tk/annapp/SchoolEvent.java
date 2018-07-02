package de.tk.annapp;

public class SchoolEvent {
    long startTime;
    long endTime;
    String location;
    String summary;

    public SchoolEvent(long startTime, long endTime, String location, String summary){
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.summary = summary;
    }
}
