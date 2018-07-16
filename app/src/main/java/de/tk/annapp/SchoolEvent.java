package de.tk.annapp;

import java.io.Serializable;

public class SchoolEvent implements Serializable {
    long startTime;
    String data;
    int color;

    public SchoolEvent(int color, long startTime, String data){
        this.startTime = startTime;
        this.data = data;
        this.color = color;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getData(){return data;}

    public void setData(String data){this.data = data;}

    public int getColor(){return color;}

    public void setColor(int color){this.color = color;}
}
