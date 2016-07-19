package com.test.myapplication2.app;

import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by mina on 7/10/16.
 * Completed by melika :)
 */
public class TimerService extends Service {

    public static final String BROADCAST_TIME = "com.test.myapplication2.app.displayevent";
    Intent myintent;
    CountDownTimer timer;
    boolean isBreak;
    int num;

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("service started");
        long time = intent.getLongExtra("time", 0);
        isBreak = intent.getBooleanExtra("isBreak", false);
        num = intent.getIntExtra("num", 0);

        Notification notif = getNotification("remaining time: " + PomodoroFragment.getTime(time), null);
        ((NotificationManager) getSystemService(MainActivity.NOTIFICATION_SERVICE)).cancel(1338);
        timer = new CountDownTimer(time, 1000) {

            @Override
            public void onTick(long l) {
                myintent.putExtra("counter", l);
                sendBroadcast(myintent);
                updateNotification(l);
            }

            @Override
            public void onFinish() {
                myintent.putExtra("counter", 0L);
                isBreak = !isBreak;
                myintent.putExtra("isBreak", isBreak);
                sendBroadcast(myintent);
                finishedNotification();
                stopSelf();

            }
        }.start();

        startForeground(1337, notif);

        return START_STICKY;
    }

    private Notification getNotification(String text, Uri sound) {
        Intent startIntent = new Intent(this, MainActivity.class);
        startIntent.putExtra("salam", isBreak);
        startIntent.putExtra("num", num);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Pomo.Do")
                .setContentText(text)
                .setContentIntent(pIntent)
                .setSound(sound)
                .build();
    }

    private void finishedNotification() {

        SharedPreferences getAlarms = PreferenceManager.
                getDefaultSharedPreferences(getBaseContext());
        Uri uri = null;

//        if (getAlarms.getBoolean("playing", true)) {
            String alarms = getAlarms.getString("ringtone", "default ringtone");
            System.out.println(alarms);
            uri = Uri.parse(alarms);
//        }
//        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if (alarmUri == null) {
//            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        }

        Notification notification = getNotification("Time to take a break!", uri);

        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(MainActivity.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1338, notification);
    }

    private void updateNotification(long l) {

        Notification notification = getNotification("remaining time: " + PomodoroFragment.getTime(l), null);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(MainActivity.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1337, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        myintent = new Intent(BROADCAST_TIME);
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
