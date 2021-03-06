package com.test.myapplication2.app;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.NumberPicker;
import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by mina on 7/9/16.
 */
public class StatisticsFragment extends Fragment implements View.OnClickListener {

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_layout, container, false);

        int values[] = getBarValues();
        String days[] = getDays();
        BarChart mBarChart = (BarChart) view.findViewById(R.id.barchart);


        mBarChart.addBar(new BarModel(days[6] ,values[6],  0xFFFFCC80 ));
        mBarChart.addBar(new BarModel(days[5] ,values[5], 0xFFFFB74D));
        mBarChart.addBar(new BarModel(days[4] ,values[4], 0xFFFFA726));
        mBarChart.addBar(new BarModel(days[3] ,values[3], 0xFFFF9800));
        mBarChart.addBar(new BarModel(days[2] ,values[2],  0xFFFB8C00));
        mBarChart.addBar(new BarModel(days[1] ,values[1], 0xFFF57C00));
        mBarChart.addBar(new BarModel(days[0] ,values[0],  0xFFEF6C00));

//        mBarChart.
        mBarChart.startAnimation();

        int stat[] = getTagStats();
        PieChart mPieChart = (PieChart) view.findViewById(R.id.piechart);

        mPieChart.addPieSlice(new PieModel("Red", stat[0], NewTaskFragment.RED));
        mPieChart.addPieSlice(new PieModel("Blue", stat[1], NewTaskFragment.BLUE));
        mPieChart.addPieSlice(new PieModel("Orange", stat[2], NewTaskFragment.ORANGE));
        mPieChart.addPieSlice(new PieModel("Pink", stat[3], NewTaskFragment.PINK));
        mPieChart.addPieSlice(new PieModel("Yellow", stat[4], NewTaskFragment.YELLOW));
        mPieChart.addPieSlice(new PieModel("Green", stat[5], NewTaskFragment.GREEN));

        mPieChart.startAnimation();

        return view;
    }

    @Override
    public void onClick(View view) {


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Statistics");
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    private int[] getBarValues(){
        TasksDBHelper db = new TasksDBHelper(getActivity());
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int[] values = new int[7];
        values[0] = db.getNumberOfPomodoroInDate(year , month , day);

        for(int i = 1 ; i<7 ; i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE , -i);
            values[i]=db.getNumberOfPomodoroInDate(cal.get(Calendar.YEAR),
                                                    cal.get(Calendar.MONTH),
                                                    cal.get(Calendar.DAY_OF_MONTH));
        }

        System.out.println(Arrays.toString(values));
        return values;

    }

    private int[] getTagStats (){

        int ans[] = new int[6];
        TasksDBHelper db =new TasksDBHelper(getActivity());
        ans[0] = db.getNumberOfPomodoroWithTag(NewTaskFragment.RED);
        ans[1] = db.getNumberOfPomodoroWithTag(NewTaskFragment.BLUE);
        ans[2] = db.getNumberOfPomodoroWithTag(NewTaskFragment.ORANGE);
        ans[3] = db.getNumberOfPomodoroWithTag(NewTaskFragment.PINK);
        ans[4] = db.getNumberOfPomodoroWithTag(NewTaskFragment.YELLOW);
        ans[5] = db.getNumberOfPomodoroWithTag(NewTaskFragment.GREEN);

        return ans;
    }

    private String[] getDays(){

        String[] days = new String[7];
        String[] namesOfDays =  {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        for (int i=0 ; i <7 ; i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE , -i);
            days[i] =namesOfDays[cal.get(Calendar.DAY_OF_WEEK)-1];
        }

        System.out.println(Arrays.toString(days));
        return days;
    }
}
