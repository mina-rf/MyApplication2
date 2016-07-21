package com.test.myapplication2.app;


//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.*;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by mina on 7/12/16.
 */

public class NewTaskFragment extends Fragment implements AdapterView.OnItemSelectedListener ,View.OnClickListener , CompoundButton.OnCheckedChangeListener {

    EditText name ;

    Spinner tagSpinner;
    EditText description;
    Button addTask;

    EditText date;
    DatePickerDialog dateDialoge ;
    SimpleDateFormat dateFormat ;

    EditText target;
    AlertDialog targetPicker;
    NumberPicker targetNumebrPicker;
    AlertDialog.Builder builder;

    CheckBox haveDeadLine;

    EditText time;
    TimePickerDialog timeDialoge;

    LinearLayout layout;

    TextView timeTV;
    TextView dateTV;

    LinearLayout timeLayout;
    LinearLayout dateLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.newtask_layout, container, false);
        initialize(view);

        layout = (LinearLayout) view.findViewById(R.id.newtask_layout);

//        date = (EditText) view.findViewById(R.id.data_picker);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
//        date.requestFocus();
//        date.setInputType(InputType.TYPE_NULL);

        addTask.setOnClickListener(this);

        target = (EditText) view.findViewById(R.id.target_picker);
        target.requestFocus();
        target.setInputType(InputType.TYPE_NULL);
        target.setOnClickListener(this);

        haveDeadLine = (CheckBox) view.findViewById(R.id.have_deadline);
        haveDeadLine.setOnCheckedChangeListener(this);

        showTargetPicker();
//        setDateTimeField();
        return view;

    }


    private void setDateField() {
        date.setOnClickListener(this);
        System.out.println("listner set");
        Calendar newCalendar = Calendar.getInstance();
        dateDialoge = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormat.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    private void setTimeField() {
        time.setOnClickListener(this);
        System.out.println("listner set");
        Calendar newCalendar = Calendar.getInstance();
        int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = newCalendar.get(Calendar.MINUTE);
        timeDialoge = new TimePickerDialog(getActivity() , new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                time.setText(selectedHour + ":" + selectedMinute);
            }

        } , hour , minute , true);
        timeDialoge.setTitle("Select Time");



    }

    public void showTargetPicker()
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.number_picker, null);

        builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setTitle("Choose number of intervals :");

        targetNumebrPicker = (NumberPicker) dialogView.findViewById(R.id.numberPicker);
        targetNumebrPicker.setMaxValue(10);
        targetNumebrPicker.setMinValue(1);
        targetNumebrPicker.setWrapSelectorWheel(false);
        targetNumebrPicker.setOnClickListener(this);
        builder.setPositiveButton("OK", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        target.setText(String.valueOf(targetNumebrPicker.getValue()));
                        ;
                    }
                });



    }


    private void initialize(View view) {

        name = (EditText) view.findViewById(R.id.task_name);
//        deadLineYear = (NumberPicker) view.findViewById(R.id.task_deadline_year);
//        deadLineMonth = (NumberPicker) view.findViewById(R.id.task_deadline_month);
//        deadLineDay = (NumberPicker) view.findViewById(R.id.task_deadline_day);
//        deadLineHour = (NumberPicker) view.findViewById(R.id.task_deadline_hour);
//        deadLineMinute  = (NumberPicker) view.findViewById(R.id.task_deadline_minute);
//        target = (NumberPicker) view.findViewById(R.id.task_target);
        tagSpinner = (Spinner) view.findViewById(R.id.task_tag);
        description = (EditText) view.findViewById(R.id.task_description);
        addTask = (Button) view.findViewById(R.id.addTask);


        List<String> colors = new ArrayList<String>();
        colors.add("RED");
        colors.add("ORANGE");
        colors.add("YELLOW");
        colors.add("GREEN");
        colors.add("BLUE");
        colors.add("PINK");

        tagSpinner.setOnItemSelectedListener(this);
        MyArrayAdapter adapter = new MyArrayAdapter(getActivity() ,colors);

        tagSpinner.setAdapter(adapter);
//        tagSpinner.setPrompt("Select a color tag");

    }


    @Override
    public void onClick(View view) {

        Log.d("tagggg", "onClick ");

        if (view.getId() == R.id.addTask){

            Log.d("tagggg", "onClick ");

            ToDoListFragment fragment = new ToDoListFragment();
            Bundle bundle = new Bundle();

            bundle.putString(TasksDBHelper.TASK_COLUMN_NAME , name.getText().toString());
            bundle.putInt(TasksDBHelper.TASK_COLUMN_DEADLINE_YEAR , getYear());
            bundle.putInt(TasksDBHelper.TASK_COLUMN_DEADLINE_MONTH , getMonth());
            bundle.putInt(TasksDBHelper.TASK_COLUMN_DEADLINE_DAY , getDay());
            bundle.putInt(TasksDBHelper.TASK_COLUMN_DEADLINE_HOUR , getHour());
            bundle.putInt(TasksDBHelper.TASK_COLUMN_DEADLINE_MINUTE , getMinute());
            bundle.putInt(TasksDBHelper.TASK_COLUMN_TARGET , Integer.valueOf(target.getText().toString()));
            bundle.putInt(TasksDBHelper.TASK_COLUMN_TAG , getColor(tagSpinner.getSelectedItem().toString()));
            bundle.putString(TasksDBHelper.TASK_COLUMN_DESCRIPTION , String.valueOf(description.getText()));

            fragment.setArguments(bundle);

            //Inflate the fragment
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.root_frame , fragment);
            transaction.commit();
        }

        else if (view.getId() == R.id.target_picker){
            showTargetPicker();
            builder.show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        String item = adapterView.getItemAtPosition(i).toString();
//
        // Showing selected spinner item
        System.out.println("hahahahahahahahha");
        Toast.makeText(adapterView.getContext(), "Selected: " , Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public static int getColor (String color){

        if (color.equals("RED"))
            return Color.parseColor("#DC143C");
        if (color.equals("ORANGE"))
            return Color.parseColor("#FF8C00");
        if (color.equals("YELLOW"))
            return Color.parseColor("#FFD700");
        if (color.equals("GREEN"))
            return Color.parseColor("#32CD32");
        if (color.equals("BLUE"))
            return Color.parseColor("#00BFFF");
        if (color.equals("PINK"))
            return Color.parseColor("#FF69B4");

        return Color.BLACK;

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b == true) {

            Calendar now = Calendar.getInstance();

            createTimerEditText(now);

            createDateEditText(now);

            dateTV = new TextView(getActivity());
            dateTV.setText("Date:  ");
            dateTV.setTextSize(16);

            dateLayout = new LinearLayout(getActivity());
            dateLayout.setOrientation(LinearLayout.HORIZONTAL);
            dateLayout.addView(dateTV);
            dateLayout.addView(date);

            timeTV = new TextView(getActivity());
            timeTV.setText("Time:  ");
            timeTV.setTextSize(16);

            timeLayout = new LinearLayout(getActivity());
            timeLayout.setOrientation(LinearLayout.HORIZONTAL);
            timeLayout.addView(timeTV);
            timeLayout.addView(time);

            layout.addView(dateLayout , 1);
            layout.addView(timeLayout , 2);

            System.out.println("checked");
        }
        else {
            System.out.println("unchecked");
            ((ViewGroup) timeLayout.getParent()).removeView(timeLayout);
            ((ViewGroup) dateLayout.getParent()).removeView(dateLayout);
        }
    }

    private void createDateEditText(Calendar now) {
        date = new EditText(getActivity());
        date.requestFocus();
        date.setOnClickListener(this);
        setDateField();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialoge.show();
            }
        });


        date.setText(now.get(Calendar.YEAR)+"-"+now.get(Calendar.MONTH)+"-"+now.get(Calendar.DAY_OF_MONTH));
        date.setInputType(InputType.TYPE_NULL);
    }

    private void createTimerEditText(Calendar now) {
        time = new EditText(getActivity());
        time.requestFocus();
        time.setOnClickListener(this);
        setTimeField();
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialoge.show();
            }
        });
        time.setInputType(InputType.TYPE_NULL);
        time.setText(now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE));
    }


    private class MyArrayAdapter extends BaseAdapter {



        Context context;
        List<String> colors;
        LayoutInflater inflater;

        public MyArrayAdapter(Context applicationContext, List<String> colors) {
            this.context = applicationContext;
            this.colors = colors;
            inflater = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return colors.size();
        }

        @Override
        public Object getItem(int i) {

            System.out.println("itemmmm");
            return colors.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.tag , null);
            TextView tv = (TextView) view.findViewById(R.id.color_name);
            Button bt = (Button) view.findViewById((R.id.tagInSpinner));

            tv.setTextColor(Color.BLACK);
            bt.setBackgroundColor(getColor(colors.get(i)));
            tv.setText(colors.get(i));
//            tv.setMinimumWidth(100);
//            view.setBackgroundColor(getColor(colors.get(i)));

            return view;
        }



    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.setting_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private int getYear(){
        if (haveDeadLine.isActivated()){
            return Integer.valueOf(date.getText().toString().split("-")[0]);
        }
        else
            return 0;

    }

    private int getMonth(){
        if (haveDeadLine.isActivated()){
            return Integer.valueOf(date.getText().toString().split("-")[1]);
        }
        else
            return 0;

    }

    private int getDay(){
        if (haveDeadLine.isActivated()){
            return Integer.valueOf(date.getText().toString().split("-")[2]);
        }
        else
            return 0;

    }
    private int getHour(){
        if (haveDeadLine.isActivated()){
            return Integer.valueOf(time.getText().toString().split(":")[0]);
        }
        else
            return 0;

    }

    private int getMinute(){
        if (haveDeadLine.isActivated()){
            return Integer.valueOf(time.getText().toString().split("-")[1]);
        }
        else
            return 0;

    }


}


