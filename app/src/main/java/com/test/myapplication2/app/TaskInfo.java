package com.test.myapplication2.app;

/**
 * Created by mina on 7/22/16.
 */
public class TaskInfo {

    public TaskInfo(String name, int color, boolean  hasDeadline, int year, int month, int day, int hour, int minute, String description, int target, int done) {
        this.name = name;
        this.color = color;
        this.hasDeadline = hasDeadline;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.description = description;
        this.target = target;
        this.done = done;
    }

    public String name;
    public int color;
    public boolean hasDeadline;
    public int year;
    public int month;
    public int day;
    public  int hour;
    public int minute;
    public String description;
    public int target;
    public int done;
}
