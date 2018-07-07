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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
