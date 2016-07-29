package com.test.myapplication2.app;


import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.*;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.*;
import android.widget.*;
import de.greenrobot.event.EventBus;

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
    Button cancel;

    EditText date;
    DatePickerDialog dateDialoge ;
    SimpleDateFormat dateFormat ;

    EventBus bus = EventBus.getDefault();

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

    List<String> colors;

    boolean inEditMode = false;
    boolean twice = false ;

    public static int RED =Color.parseColor("#DC143C");
    public static int ORANGE = Color.parseColor("#FF8C00");
    public static int YELLOW = Color.parseColor("#FFD700");
    public static int BLUE = Color.parseColor("#00BFFF");
    public static int PINK = Color.parseColor("#FF69B4");
    public static int GREEN = Color.parseColor("#32CD32");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        colors = new ArrayList<String>();
        colors.add("Red");
        colors.add("Orange");
        colors.add("Yellow");
        colors.add("Green");
        colors.add("Blue");
        colors.add("Pink");

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.newtask_layout, container, false);
        initialize(view);
        showTargetPicker();
        if(getArguments() != null){
            initializeForEdit();
        }

        return view;

    }

    private void initializeForEdit() {


        inEditMode = true;
        Bundle info = getArguments();
        name.setText(info.getString(TasksDBHelper.TASK_COLUMN_NAME));
        target.setText(Integer.toString(info.getInt(TasksDBHelper.TASK_COLUMN_TARGET)));
        tagSpinner.setSelection(getColorIndex(info.getInt(TasksDBHelper.TASK_COLUMN_TAG)));
        description.setText(info.getString(TasksDBHelper.TASK_COLUMN_DESCRIPTION));
        System.out.println("hasss : " + info.getInt(TasksDBHelper.TASK_COLUMN_HASDEADLINE));
        if (info.getInt(TasksDBHelper.TASK_COLUMN_HASDEADLINE) == 1){
            haveDeadLine.setChecked(true);
            Calendar now = Calendar.getInstance();
            createTimerEditText(now);
            createDateEditText(now);
            setDateAndTimePickers();
            date.setText(info.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_YEAR)
                    +"-"+info.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_MONTH)+"-"+
                    info.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_DAY));
            time.setText( info.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_HOUR)+":"
                    + info.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_MINUTE));
        }
    }

    private int getColorIndex(int color) {

        for (int i =0 ; i < 7 ; i++ ){
           if ( getColor(colors.get(i)) == color )
               return i;
        }
        return -1;
    }


    private void setDateField() {
        date.setOnClickListener(this);
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
        Calendar newCalendar = Calendar.getInstance();
        int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = newCalendar.get(Calendar.MINUTE);
        timeDialoge = new TimePickerDialog(getActivity() , new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                time.setText(selectedHour + ":" + selectedMinute);
            }

        } , hour , minute , true);



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

                    }
                });



    }


    private void initialize(View view) {

        name = (EditText) view.findViewById(R.id.task_name);
        layout = (LinearLayout) view.findViewById(R.id.newtask_layout);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        tagSpinner = (Spinner) view.findViewById(R.id.task_tag);
        description = (EditText) view.findViewById(R.id.task_description);
        addTask = (Button) view.findViewById(R.id.addTask);
        cancel = (Button)  view.findViewById(R.id.cancel_task);

        cancel.setOnClickListener(this);
        addTask.setOnClickListener(this);

        target = (EditText) view.findViewById(R.id.target_picker);
        target.requestFocus();
        target.setInputType(InputType.TYPE_NULL);
        target.setOnClickListener(this);

        haveDeadLine = (CheckBox) view.findViewById(R.id.have_deadline);
        haveDeadLine.setOnCheckedChangeListener(this);

        System.out.println("before listnere!");
        tagSpinner.setOnItemSelectedListener(this);
        MyArrayAdapter adapter = new MyArrayAdapter(getActivity() ,colors);

        tagSpinner.setAdapter(adapter);

        target.setText("5");
//        tagSpinner.setPrompt("Select a color tag");

    }


    @Override
    public void onClick(View view) {

        Log.d("tagggg", "onClick ");

        if (view.getId() == R.id.addTask){

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
            bundle.putInt(TasksDBHelper.TASK_COLUMN_HASDEADLINE , haveDeadLine.isChecked() == true ? 1 : 0);
            bundle.putInt("isEdit" , inEditMode == false ? 0 : 1 );

            if (inEditMode) {
                bundle.putString("nameBeforeEdit", getArguments().getString(TasksDBHelper.TASK_COLUMN_NAME));
                bundle.putInt(TasksDBHelper.TASK_COLUMN_DONE, getArguments().getInt(TasksDBHelper.TASK_COLUMN_DONE));
            }
            fragment.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.root_frame , fragment);
            transaction.commit();
        }
        else if (view.getId() == R.id.cancel_task){

            ToDoListFragment fragment = new ToDoListFragment();
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

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public static int getColor (String color){

        if (color.equals("Red"))
            return Color.parseColor("#DC143C");
        if (color.equals("Orange"))
            return Color.parseColor("#FF8C00");
        if (color.equals("Yellow"))
            return Color.parseColor("#FFD700");
        if (color.equals("Green"))
            return Color.parseColor("#32CD32");
        if (color.equals("Blue"))
            return Color.parseColor("#00BFFF");
        if (color.equals("Pink"))
            return Color.parseColor("#FF69B4");

        return Color.BLACK;

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b == true && (!inEditMode || twice ))  {

            Calendar now = Calendar.getInstance();

            createTimerEditText(now);

            createDateEditText(now);

            setDateAndTimePickers();

            System.out.println("checked");
        }
        else if (b == false){
            twice = true ;
            System.out.println("unchecked");
            ((ViewGroup) timeLayout.getParent()).removeView(timeLayout);
            ((ViewGroup) dateLayout.getParent()).removeView(dateLayout);
        }
    }

    private void setDateAndTimePickers() {
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

        LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT));
        params.setMargins(0,0,16,0);
        dateLayout.setLayoutParams(params);
        timeLayout.setLayoutParams(params);

        layout.addView(dateLayout , 1);
        layout.addView(timeLayout , 2);
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
            TextView tag = (TextView) view.findViewById((R.id.tagInSpinner));

            tv.setTextColor(Color.BLACK);
            tag.setBackgroundColor(getColor(colors.get(i)));
            tv.setText("    " +colors.get(i));
//            tv.setMinimumWidth(100);
//            view.setBackgroundColor(getColor(colors.get(i)));

            return view;
        }



    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("New Task");
        inflater.inflate(R.menu.setting_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private int getYear(){
        if (haveDeadLine.isChecked()){
            System.out.println("yyyyy" +Integer.valueOf(date.getText().toString().split("-")[0]));
            return Integer.valueOf(date.getText().toString().split("-")[0]);

        }
        else
            return 0;

    }

    private int getMonth(){
        if (haveDeadLine.isChecked()){
            return Integer.valueOf(date.getText().toString().split("-")[1]);
        }
        else
            return 0;

    }

    private int getDay(){
        if (haveDeadLine.isChecked()){
            return Integer.valueOf(date.getText().toString().split("-")[2]);
        }
        else
            return 0;

    }
    private int getHour(){
        if (haveDeadLine.isChecked()){
            return Integer.valueOf(time.getText().toString().split(":")[0]);
        }
        else
            return 0;

    }

    private int getMinute(){
        if (haveDeadLine.isChecked()){
            return Integer.valueOf(time.getText().toString().split(":")[1]);
        }
        else
            return 0;

    }


//    public void onEvent(TaskInfo event) {
//        System.out.println(event.name);
//        name.setText(event.name);
////        target.setText(event.target);
////        tagSpinner.setSelection(getColoIndex(event.color));
//        description.setText(event.description);
//        if (event.hasDeadline){
//            Calendar now = Calendar.getInstance();
//            createTimerEditText(now);
//            createDateEditText(now);
//            setDateAndTimePickers();
//            date.setText(event.year+"-"+event.month+"-"+event.day);
//            time.setText(event.hour+":"+event.minute);
//        }
//        System.out.println("end of event bus");
//
//    }


}


