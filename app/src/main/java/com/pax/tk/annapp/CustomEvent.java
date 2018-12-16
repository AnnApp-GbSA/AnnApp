package com.pax.tk.annapp;


import com.github.sundeepk.compactcalendarview.domain.Event;

import java.io.Serializable;

public class CustomEvent implements Serializable {

    private int color;
    private long timeInMillis;
    private Object data;

    /**
     * creates a custom event with a color and a start time
     *
     * @param color color which will be shown for this event as Integer
     * @param timeInMillis start time in milliseconds
     */
    public CustomEvent(int color, long timeInMillis) {
        this.color = color;
        this.timeInMillis = timeInMillis;
    }

    /**
     * creates a custom event with a color, start time and extra information
     *
     * @param color color which will be shown for this event as Integer
     * @param timeInMillis start time in milliseconds
     * @param data extra information
     */
    public CustomEvent(int color, long timeInMillis, Object data) {
        this.color = color;
        this.timeInMillis = timeInMillis;
        this.data = data;
    }

    /**
     * get the color of a CustomEvent
     *
     * @return color as Integer
     */
    public int getColor() {
        return color;
    }

    /**
     * get the start time of a CustomEvent
     *
     * @return start time in milliseconds
     */
    public long getTimeInMillis() {
        return timeInMillis;
    }

    /**
     * get the extra information of a CustomEvent
     *
     * @return extra information
     */
    public Object getData() {
        return data;
    }

    /**
     * checks if an object is equal to this CustomEvent
     *
     * @param o object to compare with this CustomEvent
     * @return true if it equals, false if not
     */
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

    /**
     * creates a hash code for this CustomEvent
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = color;
        result = 31 * result + (int) (timeInMillis ^ (timeInMillis >>> 32));
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    /**
     * converts a CustomEvent into a String
     *
     * @return String
     */
    @Override
    public String toString() {
        return "CustomEvent{" +
                "color=" + color +
                ", timeInMillis=" + timeInMillis +
                ", data=" + data +
                '}';
    }
}
