package com.test.myapplication2.app;

import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;
import de.greenrobot.event.*;

public class PomodoroFragment extends Fragment implements View.OnClickListener {
    Button button;
    TimerView timerView;
    long workTime = 4 * 1000;
    long shortBreakTime = 5 * 1000;
    long longBreakTime = 7 * 1000;
    long time;
    int breaksInRow = 2;
    int breaksNum;
    boolean isBreak;
    String taskName = null;
    EventBus bus = EventBus.getDefault();

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button2) {
            //starting the foreground service
            Log.d("start button", "onClick ");
            //TODO: check if another service is running by that time!
            startTimerService();
        }
    }

    private void startTimerService() {
        Intent intent = new Intent(getActivity(), TimerService.class);
        intent.setAction("");
        intent.putExtra("time", time);
        intent.putExtra("isBreak", isBreak);
        intent.putExtra("num", breaksNum);
        getActivity().startService(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //registering broadcast receiver
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(TimerService.BROADCAST_TIME));
        isBreak = getActivity().getIntent().getBooleanExtra("salam", false);
        breaksNum = getActivity().getIntent().getIntExtra("num", 0);
        bus.register(this);
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
        button = (Button) view.findViewById(R.id.button2);
        timerView = (TimerView) view.findViewById(R.id.timeView);

        button.setOnClickListener(this);

        //read the value of Pomodoro from Shared Preferences
        setValues();
        breakOrWork();
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
            breakOrWork();
            if (taskName != null) {
                TasksDBHelper db = new TasksDBHelper(getActivity());
                db.addDone(taskName);
                ((ViewPager)getActivity().findViewById(R.id.pager)).getAdapter().notifyDataSetChanged();
                taskName = null;
            }

        }
    }

    private void breakOrWork() {
        if (isBreak) {
            breaksNum++;
            if (breaksNum == breaksInRow) {
                Toast.makeText(getActivity(), "Time to take a looooonnnnggggg break", Toast.LENGTH_LONG).show();
                breaksNum = 0;
                time = longBreakTime;
            } else {
                Toast.makeText(getActivity(), "Time to take a break", Toast.LENGTH_LONG).show();
                time = shortBreakTime;
            }
        } else {
            time = workTime;
        }
        drawTimer(time, 0);
    }

    private void drawTimer(long time, int angle) {
        timerView.setAngle(angle);
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
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
