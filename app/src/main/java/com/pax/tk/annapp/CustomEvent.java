package com.pax.tk.annapp;

import android.support.annotation.Nullable;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.io.Serializable;

public class CustomEvent implements Serializable {

    private int color;
    private long timeInMillis;
    private Object data;

    public CustomEvent(int color, long timeInMillis) {
        this.color = color;
        this.timeInMillis = timeInMillis;
    }

    public CustomEvent(int color, long timeInMillis, Object data) {
        this.color = color;
        this.timeInMillis = timeInMillis;
        this.data = data;
    }

    public void parseEvent(Event event){
        int color = event.getColor();
        long timeInMillis = event.getTimeInMillis();
        Object data = event.getData();
    }

    public int getColor() {
        return color;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    @Nullable
    public Object getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomEvent event = (CustomEvent) o;

        if (color != event.color) return false;
        if (timeInMillis != event.timeInMillis) return false;
        if (data != null ? !data.equals(event.data) : event.data != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = color;
        result = 31 * result + (int) (timeInMillis ^ (timeInMillis >>> 32));
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CustomEvent{" +
                "color=" + color +
                ", timeInMillis=" + timeInMillis +
                ", data=" + data +
                '}';
    }
}
