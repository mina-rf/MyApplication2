package com.test.myapplication2.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mina on 7/13/16.
 */
public class TasksDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Tasks.db";
    private static final int DATABASE_VERSION = 1;
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
                TASK_COLUMN_TAG + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertTask(String name, Integer deadline_year , Integer deadline_month , Integer deadline_day ,
                              Integer deadline_hour , Integer deadline_minute , String description , Integer tag , Integer target  , Integer done) {
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

        db.insert(TASK_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateTask (String name, Integer deadline_year , Integer deadline_month , Integer deadline_day ,
                               Integer deadline_hour , Integer deadline_minute , Integer work_interval ,
                               Integer short_Break , String description , Integer tag , Integer target  , Integer done) {
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
        db.update(TASK_TABLE_NAME, contentValues, TASK_COLUMN_NAME + " = ? ", new String[] { name } );
        return true;
    }

    public boolean addDone (String name){
        ContentValues cv = new ContentValues();
        Cursor c = getPerson(name);
        if (c .moveToFirst()) {
            cv.put(TASK_COLUMN_DONE, c.getInt(c.getColumnIndex(TasksDBHelper.TASK_COLUMN_DONE)) + 1);
            System.out.println("done : " +(c.getInt(c.getColumnIndex(TasksDBHelper.TASK_COLUMN_DONE))));
        }

        SQLiteDatabase db = getWritableDatabase();
        db.update(TASK_TABLE_NAME, cv, TASK_COLUMN_NAME + "= ?", new String[] {name});

        c = getPerson(name);
        if (c .moveToFirst()) {
            System.out.println("done : " +(c.getInt(c.getColumnIndex(TasksDBHelper.TASK_COLUMN_DONE)) ));
        }
        return true;
    }



    public Cursor getPerson(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + TASK_TABLE_NAME + " WHERE " +
                TASK_COLUMN_NAME + "=?", new String[] { name } );
        return res;
    }
    public Cursor getAllPersons() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + TASK_TABLE_NAME, null );
        return res;
    }

    public Integer deletePerson(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TASK_TABLE_NAME,
                TASK_COLUMN_NAME + " = ? ",
                new String[] { name });
    }





}
