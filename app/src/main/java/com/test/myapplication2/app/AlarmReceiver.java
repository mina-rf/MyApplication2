package com.test.myapplication2.app;

import android.app.Notification;
//import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;

import java.util.Calendar;

/**
 * Created by mina on 7/22/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("received alarm!" + intent.getStringExtra(TasksDBHelper.TASK_COLUMN_NAME));
        String name = intent.getStringExtra(TasksDBHelper.TASK_COLUMN_NAME);

        Calendar cal =Calendar.getInstance() ;
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);


        TasksDBHelper db = new TasksDBHelper(context);
        Cursor c = db.getTask(name);
        if (c.moveToFirst()){

            TaskInfo info = db.getTaskInfo(name);
            if (year == info.year &&
                    month == info.month &&
                    day == info.day &&
                    hour == info.hour &&
                    minute == info.minute){


                NotificationManager notif=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notify=new Notification.Builder(context).setContentTitle("Reminder For Task")
                        .setContentText(name).setSmallIcon(R.mipmap.ic_launcher).setDefaults(Notification.DEFAULT_SOUND).build();
//                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                builder.setSound(alarmSound);
                notif.notify(102, notify);


            }
        }


    }


}
