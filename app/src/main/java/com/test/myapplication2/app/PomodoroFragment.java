package com.test.myapplication2.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;

import de.greenrobot.event.*;

import java.sql.Time;
import java.util.Calendar;

public class PomodoroFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    TimerView timerView;
    long workTime = 10 * 1000;
    long shortBreakTime = 5 * 1000;
    long longBreakTime = 7 * 1000;
    long time;
    int breaksInRow = 2;
    int breaksNum;
    boolean isBreak;
    boolean isRunning;
    String taskName = null;
    EventBus bus = EventBus.getDefault();
    private long MAX_CLICK_DURATION = 200;

    @Override
    public void onClick(View view) {

    }

    private void startTimerService() {
        Intent intent = new Intent(getActivity(), TimerService.class);
        intent.setAction("");
        intent.putExtra("time", time);
        intent.putExtra("isBreak", isBreak);
        intent.putExtra("num", breaksNum);
        getActivity().startService(intent);
        isRunning = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //registering broadcast receiver
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(TimerService.BROADCAST_TIME));
        isBreak = getActivity().getIntent().getBooleanExtra("salam", false);
        breaksNum = getActivity().getIntent().getIntExtra("num", 0);
        isRunning = getActivity().getIntent().getBooleanExtra("run", false);
        bus.register(this);
        if (!isRunning) onFinishTimer();
        else breakOrWork();

        setHasOptionsMenu(true);

    }

    public void onEvent(StartTaskEvent event) {
        System.out.println(event.task);
        ((ViewPager) getActivity().findViewById(R.id.pager)).setCurrentItem(0);
        startTimerService();
        taskName = event.task;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pomodoro_layout, container, false);
        timerView = (TimerView) view.findViewById(R.id.timeView);

        timerView.setOnTouchListener(this);
        //read the value of Pomodoro from Shared Preferences
        setValues();
//        breakOrWork();
        drawTimer(time, 0);
        return view;

    }

    private void setValues() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int time = preferences.getInt("work_interval", 9);
        int breakT = preferences.getInt("short_break", 2);
//        breaksInRow = preferences.getInt("breaks_num", 2);
//        workTime = time * 60 * 1000 * 5;
//        shortBreakTime = breakT * 60 * 1000 * 5;
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    private void updateUI(Intent intent) {
        long l = intent.getLongExtra("counter", 0L);
        int angle = 360 - (int) ((l / 1000 * 1000) * 360 / time);
        drawTimer(l, angle);

        if (angle == 360) {
            isBreak = intent.getBooleanExtra("isBreak", false);
            isRunning = intent.getBooleanExtra("run", false);
            onFinishTimer();
            drawTimer(time, 0);
            String text = isBreak ? getString(R.string.break_time_message) : getString(R.string.work_time_message);
            showDialog(text);
        }
    }

    private void UpdateDB() {
        TasksDBHelper db = new TasksDBHelper(getActivity());

        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);

        String date = year + "-" + month + "-" + day;
        Cursor c = db.getStatInDate(date);

        if (c.moveToFirst()) {
            db.addPomodoroInData(date);
        } else {
            db.insertDayStat(date, 1);
        }
        if (taskName != null) {
            db.addDone(taskName);
            ((ViewPager) getActivity().findViewById(R.id.pager)).getAdapter().notifyDataSetChanged();
            taskName = null;
        }
    }

    private void breakOrWork() {
        if (isBreak) {
//            breaksNum++;
            if (breaksNum == breaksInRow) {
//                Toast.makeText(getActivity(), "Time to take a looooonnnnggggg break", Toast.LENGTH_LONG).show();
                breaksNum = 0;
                time = longBreakTime;
            } else {
//                Toast.makeText(getActivity(), "Time to take a break", Toast.LENGTH_LONG).show();
                time = shortBreakTime;
            }
        } else {
            time = workTime;
        }

    }

    private void drawTimer(long time, int angle) {
        timerView.setAngle(angle);
        if (time == this.time)
            timerView.setTime(getString(R.string.start_timer));
        else
            timerView.setTime(getTime(time));
        timerView.invalidate();
    }


    public static String getTime(long time) {
        int min = (int) (time / (1000 * 60));
        int second = (int) ((time % (60 * 1000)) / 1000);
        String output = String.format("%02d:%02d", min, second);
        return output;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = timerView.getCenterX();
        int y = timerView.getCenterY();
        int r = timerView.getRadious();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (Math.sqrt((x - motionEvent.getX()) * (x - motionEvent.getX()) + (y - motionEvent.getY()) * (y - motionEvent.getY())) < r) {
                    System.out.println("circle clicked");
//                    timerClicked();
                }
                break;
            case MotionEvent.ACTION_UP:
                long clickDuration = motionEvent.getEventTime() - motionEvent.getDownTime();
                if (clickDuration < MAX_CLICK_DURATION) {
                    timerClicked();
                }
                break;
            default:
                break;
        }

        return true;
    }

    private void timerClicked() {
        if (!isRunning) {
            startTimerService();
        } else {
            //TODO:
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setMessage(getString(R.string.stop_pomodoro_dialog));

            alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    getActivity().stopService(new Intent(getActivity(), TimerService.class));
                    drawTimer(time, 0);
                    isRunning = false;
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO:RESUME
//                    Toast.makeText(getActivity(),"You clicked no button",Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }

    private void onFinishTimer() {
        if (isBreak) {
            breaksNum++;
            UpdateDB();
        }
        breakOrWork();

    }

    private void showDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((NotificationManager) getActivity().getSystemService(MainActivity.NOTIFICATION_SERVICE)).cancel(1338);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
