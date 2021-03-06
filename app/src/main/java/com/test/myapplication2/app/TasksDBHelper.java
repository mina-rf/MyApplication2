package com.test.myapplication2.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

/**
 * Created by mina on 7/13/16.
 */
public class TasksDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Data.db";
    public static final String TASK_TABLE_NAME = "tasks";
    public static final String TASK_COLUMN_NAME = "name";
    public static final String TASK_COLUMN_DEADLINE_YEAR = "deadline_year";
    public static final String TASK_COLUMN_DEADLINE_MONTH = "deadline_month";
    public static final String TASK_COLUMN_DEADLINE_DAY = "deadline_day";
    public static final String TASK_COLUMN_DEADLINE_HOUR = "deadline_hour";
    public static final String TASK_COLUMN_DEADLINE_MINUTE = "deadline_minute";
    public static final String TASK_COLUMN_DESCRIPTION = "description";
    public static final String TASK_COLUMN_TAG = "tag";
    public static final String TASK_COLUMN_TARGET = "target";
    public static final String TASK_COLUMN_DONE = "done";
    public static final String TASK_COLUMN_HASDEADLINE = "hasdeadline";
    public static  final String POMODORO_TABLE_NAME = "pomodoros";
    public static final String POMODORO_COLUMN_NUMBER =  "number";
    public static final String POMODORO_COLUMN_DATE = "Data";
    private static final int DATABASE_VERSION = 2;



    public TasksDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TASK_TABLE_NAME + "(" +
                TASK_COLUMN_NAME + " TEXT PRIMARY KEY, " +
                TASK_COLUMN_DEADLINE_YEAR + " INTEGER, " +
                TASK_COLUMN_DEADLINE_MONTH + " INTEGER, " +
                TASK_COLUMN_DEADLINE_DAY + " INTEGER, " +
                TASK_COLUMN_DEADLINE_HOUR + " INTEGER, " +
                TASK_COLUMN_DEADLINE_MINUTE + " INTEGER, " +
                TASK_COLUMN_DESCRIPTION + " TEXT, " +
                TASK_COLUMN_TARGET + " INTEGER, " +
                TASK_COLUMN_DONE + " INTEGER, " +
//                TASK_COLUMN_HASDEADLINE + "INTEGER,"+
                TASK_COLUMN_TAG + " INTEGER)"
        );


        sqLiteDatabase.execSQL("CREATE TABLE " + POMODORO_TABLE_NAME + "(" +
                POMODORO_COLUMN_DATE + " TEXT PRIMARY KEY, " +
                POMODORO_COLUMN_NUMBER + " INTEGER)"
        );




    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertTask(String name, Integer deadline_year , Integer deadline_month , Integer deadline_day ,
                              Integer deadline_hour , Integer deadline_minute , String description , Integer tag , Integer target  , Integer done ) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TASK_COLUMN_NAME, name);
        contentValues.put(TASK_COLUMN_DEADLINE_YEAR, deadline_year);
        contentValues.put(TASK_COLUMN_DEADLINE_MONTH, deadline_month);
        contentValues.put(TASK_COLUMN_DEADLINE_DAY, deadline_day);
        contentValues.put(TASK_COLUMN_DEADLINE_HOUR, deadline_hour);
        contentValues.put(TASK_COLUMN_DEADLINE_MINUTE, deadline_minute);
        contentValues.put(TASK_COLUMN_DESCRIPTION, description);
        contentValues.put(TASK_COLUMN_TARGET, target);
        contentValues.put(TASK_COLUMN_DONE, 0);
        contentValues.put(TASK_COLUMN_TAG, tag);
//        contentValues.put(TASK_COLUMN_HASDEADLINE , hasDeadline);

        db.insert(TASK_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateTask (String name, Integer deadline_year , Integer deadline_month , Integer deadline_day ,
                               Integer deadline_hour , Integer deadline_minute , Integer work_interval ,
                               Integer short_Break , String description , Integer tag , Integer target  , Integer done ,Integer hasDeadline) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_COLUMN_NAME, name);
        contentValues.put(TASK_COLUMN_DEADLINE_YEAR, deadline_year);
        contentValues.put(TASK_COLUMN_DEADLINE_MONTH, deadline_month);
        contentValues.put(TASK_COLUMN_DEADLINE_DAY, deadline_day);
        contentValues.put(TASK_COLUMN_DEADLINE_HOUR, deadline_hour);
        contentValues.put(TASK_COLUMN_DEADLINE_MINUTE, deadline_minute);
        contentValues.put(TASK_COLUMN_DESCRIPTION, description);
        contentValues.put(TASK_COLUMN_TARGET, target);
        contentValues.put(TASK_COLUMN_DONE, done);
        contentValues.put(TASK_COLUMN_TAG, tag);
//        contentValues.put(TASK_COLUMN_HASDEADLINE , hasDeadline);
        db.update(TASK_TABLE_NAME, contentValues, TASK_COLUMN_NAME + " = ? ", new String[] { name } );
        return true;
    }

    public boolean addDone (String name){
        ContentValues cv = new ContentValues();
        Cursor c = getTask(name);
        if (c .moveToFirst()) {
            cv.put(TASK_COLUMN_DONE, c.getInt(c.getColumnIndex(TasksDBHelper.TASK_COLUMN_DONE)) + 1);
            System.out.println("done : " +(c.getInt(c.getColumnIndex(TasksDBHelper.TASK_COLUMN_DONE))));
        }

        SQLiteDatabase db = getWritableDatabase();
        db.update(TASK_TABLE_NAME, cv, TASK_COLUMN_NAME + "= ?", new String[] {name});

        c = getTask(name);
        if (c .moveToFirst()) {
            System.out.println("done : " +(c.getInt(c.getColumnIndex(TasksDBHelper.TASK_COLUMN_DONE)) ));
        }
        return true;
    }



    public Cursor getTask(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + TASK_TABLE_NAME + " WHERE " +
                TASK_COLUMN_NAME + "=?", new String[] { name } );
        return res;
    }
    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + TASK_TABLE_NAME, null );
        return res;
    }

    public Integer deleteTask(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TASK_TABLE_NAME,
                TASK_COLUMN_NAME + " = ? ",
                new String[] { name });
    }

    public boolean insertDayStat(String date , Integer number){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(POMODORO_COLUMN_DATE ,date );
        cv.put(POMODORO_COLUMN_NUMBER,number);
        db.insert(POMODORO_TABLE_NAME , null , cv);
        return true;
    }

    public boolean addPomodoroInData (String date){
        ContentValues cv = new ContentValues();
        Cursor c = getStatInDate(date);
        if (c .moveToFirst()) {
            cv.put(POMODORO_COLUMN_NUMBER, c.getInt(c.getColumnIndex(TasksDBHelper.POMODORO_COLUMN_NUMBER)) + 1);
        }

        SQLiteDatabase db = getWritableDatabase();
        db.update(POMODORO_TABLE_NAME, cv, POMODORO_COLUMN_DATE + "= ?", new String[] {date});

        return true;
    }

    public Cursor getStatInDate (String date){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + POMODORO_TABLE_NAME + " WHERE " +
                POMODORO_COLUMN_DATE + "=?", new String[] { date } );
        return res;

    }

    public int getNumberOfPomodoroInDate (int year , int month , int day){

        String date = year+"-"+month+"-"+day;

        Cursor c = getStatInDate(date);
        ContentValues cv = new ContentValues();
        if (c.moveToFirst())
            return c.getInt(c.getColumnIndex(POMODORO_COLUMN_NUMBER));
        return 0;
    }

    public TaskInfo getTaskInfo (String name){
        Cursor c = getTask(name);
        TaskInfo info = null;
        if (c.moveToFirst()){
            boolean hasDeadline = c.getInt(c.getColumnIndex(TASK_COLUMN_DEADLINE_YEAR)) == 0 ? false : true;
            int year = c.getInt(c.getColumnIndex(TASK_COLUMN_DEADLINE_YEAR));
            int month = c.getInt(c.getColumnIndex(TASK_COLUMN_DEADLINE_MONTH));
            int day = c.getInt(c.getColumnIndex(TASK_COLUMN_DEADLINE_DAY));
            int hour = c.getInt(c.getColumnIndex(TASK_COLUMN_DEADLINE_HOUR));
            int minute = c.getInt(c.getColumnIndex(TASK_COLUMN_DEADLINE_MINUTE));
            int color = c.getInt(c.getColumnIndex(TASK_COLUMN_TAG));
            String description = c.getString(c.getColumnIndex(TASK_COLUMN_DESCRIPTION));
            int target = c.getInt(c.getColumnIndex(TASK_COLUMN_TARGET));
            int done = c.getInt(c.getColumnIndex(TASK_COLUMN_DONE));
            info = new TaskInfo(name,color,hasDeadline,year,month,day,hour,minute,description,target,done);
        }
        return info;
    }


    public void setDone(String name, int done) {

        ContentValues cv = new ContentValues();
        Cursor c = getTask(name);
        if (c .moveToFirst()) {
            cv.put(TASK_COLUMN_DONE, done);
            System.out.println("done : " +(c.getInt(c.getColumnIndex(TasksDBHelper.TASK_COLUMN_DONE))));
        }

        SQLiteDatabase db = getWritableDatabase();
        db.update(TASK_TABLE_NAME, cv, TASK_COLUMN_NAME + "= ?", new String[] {name});


    }
    int getNumberOfPomodoroWithTag (int color){

        SQLiteDatabase db = getWritableDatabase() ;
        Cursor c = db.rawQuery("SELECT * FROM " + TASK_TABLE_NAME + " WHERE "+TASK_COLUMN_TAG + " =? " , new String[]{Integer.toString(color)});
        int sum = 0;
        if (c.moveToFirst())
            while (c.isAfterLast() == false){
                 sum += c.getInt(c.getColumnIndex(TASK_COLUMN_DONE));
                 c.moveToNext();
                 System.out.println("loop?");
            }
        return sum;
    }
}
