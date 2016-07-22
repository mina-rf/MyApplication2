package com.test.myapplication2.app;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.widget.*;
import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mina on 7/9/16.
 */
public class ToDoListFragment  extends Fragment implements View.OnClickListener {

    ListView taskList ;
    EditText mEditTest;

    List<TaskHolder> list = new ArrayList<TaskHolder>();
    CustomAdapter adapter;

    TasksDBHelper tasksDB ;

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d("context menu", "onCreateContextMenu ");
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0 , v.getId(),0,R.string.delete_task);
        menu.add(0 , v.getId() ,0 , "Edit");
        Log.d("context menu", "onCreateContextMenu ");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info  =  (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        TextView tv = (TextView) info.targetView.findViewById(R.id.task_name_inlist);
        if (item.getTitle().equals((getActivity().getString(R.string.delete_task)))){
            tasksDB.deleteTask(tv.getText().toString());
            ((ViewPager)getActivity().findViewById(R.id.pager)).getAdapter().notifyDataSetChanged();
            return true;
        }
        else if (item.getTitle().equals((getActivity().getString(R.string.edit_task)))){
            openEditPage(tv.getText().toString());
//            ((ViewPager)getActivity().findViewById(R.id.pager)).getAdapter().notifyDataSetChanged();
            return true;

        }
        else
            return super.onContextItemSelected(item);



    }

    private void openEditPage(String name) {
        TasksDBHelper db = new TasksDBHelper(getActivity());
        EventBus bus = EventBus.getDefault();
        bus.post(db.getTaskInfo(name));

        Bundle b = new Bundle();
        TaskInfo info = db.getTaskInfo(name);
        b.putString(db.TASK_COLUMN_NAME , info.name);
        b.putInt(db.TASK_COLUMN_DEADLINE_YEAR , info.year);
        b.putInt(db.TASK_COLUMN_DEADLINE_MONTH , info.month);
        b.putInt(db.TASK_COLUMN_DEADLINE_DAY , info.day);
        b.putInt(db.TASK_COLUMN_DEADLINE_HOUR , info.hour);
        b.putInt(db.TASK_COLUMN_DEADLINE_MINUTE, info.minute);
        b.putString(db.TASK_COLUMN_DESCRIPTION  , info.description);
        b.putInt(db.TASK_COLUMN_TARGET , info.target);
        b.putInt(db.TASK_COLUMN_TAG , info.color);
        b.putInt(db.TASK_COLUMN_HASDEADLINE , info.hasDeadline ? 1 : 0);
        b.putInt(db.TASK_COLUMN_DONE , info.done);

        NewTaskFragment f = new NewTaskFragment();
        f.setArguments(b);
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.root_frame,f);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view;
        view= inflater.inflate(R.layout.todolist_layout, container, false);

        
        tasksDB = new TasksDBHelper(getActivity());
        taskList = (ListView) view.findViewById(R.id.tasks_listView);
        adapter = new CustomAdapter(getActivity(), list, new CustomAdapter.ListAdapterListener() {
            @Override
            public void onClickAtOKButton(int position) {
                changePager();
            }
        });

        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.new_task);
        addButton.setOnClickListener(this);

        if (getArguments() != null) {
            if (getArguments().getInt("isEdit") == 0)
                addNewTaskToDB(getArguments());
            else
                editNewTask(getArguments() , getArguments().getString("nameBeforeEdit"));

        }

        //draw Views
        list.removeAll(list);
        Cursor c = tasksDB.getAllTasks();
        if (c .moveToFirst()) {

            while (c.isAfterLast() == false) {

                TaskHolder newTask = new TaskHolder();
                newTask.taskName =c.getString(c.getColumnIndex(TasksDBHelper.TASK_COLUMN_NAME));
                newTask.target = c.getInt(c.getColumnIndex(TasksDBHelper.TASK_COLUMN_TARGET));
                newTask.done = c.getInt(c.getColumnIndex(TasksDBHelper.TASK_COLUMN_DONE));
                newTask.color = c.getInt(c.getColumnIndex(TasksDBHelper.TASK_COLUMN_TAG));
                list.add(newTask);
                adapter.notifyDataSetChanged();

                c.moveToNext();
            }
        }


        taskList.setAdapter(adapter);
        this.setRetainInstance(true);

        taskList.setClickable(true);
        registerForContextMenu(taskList);

        taskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("on item long click listener");
                return false;
            }
        });
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("item clicked!");
            }
        });

        return view;

    }

    private void editNewTask(Bundle arguments, String nameBeforEdit) {
        TasksDBHelper db = new TasksDBHelper(getActivity());
        db.deleteTask(nameBeforEdit);
        System.out.println("deletdd"+nameBeforEdit);
        addNewTaskToDB(arguments);
        db.setDone(arguments.getString(TasksDBHelper.TASK_COLUMN_NAME) , arguments.getInt(TasksDBHelper.TASK_COLUMN_DONE));
    }

    private void changePager() {
        ((ViewPager)getActivity().findViewById(R.id.pager)).setCurrentItem(0);

    }

    private void addNewTaskToDB(Bundle arguments) {

        tasksDB.insertTask(arguments.getString(TasksDBHelper.TASK_COLUMN_NAME),
                arguments.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_YEAR),
                arguments.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_MONTH),
                arguments.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_DAY),
                arguments.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_HOUR),
                arguments.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_MINUTE),
                arguments.getString(TasksDBHelper.TASK_COLUMN_DESCRIPTION),
                arguments.getInt(TasksDBHelper.TASK_COLUMN_TAG),
                arguments.getInt(TasksDBHelper.TASK_COLUMN_TARGET),
                0 );

        System.out.println("year : "+  arguments.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_YEAR));

        if( arguments.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_YEAR) != 0){
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR , arguments.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_YEAR));
            cal.set(Calendar.MONTH , arguments.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_MONTH));
            cal.set(Calendar.DAY_OF_MONTH , arguments.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_DAY));
            cal.set(Calendar.HOUR_OF_DAY ,arguments.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_HOUR));
            cal.set(Calendar.MINUTE ,arguments.getInt(TasksDBHelper.TASK_COLUMN_DEADLINE_MINUTE));
            cal.set(Calendar.SECOND ,0);

            Intent intentAlarm = new Intent(getActivity(), AlarmReceiver.class);
            intentAlarm.putExtra(TasksDBHelper.TASK_COLUMN_NAME , arguments.getString(TasksDBHelper.TASK_COLUMN_NAME));
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(), PendingIntent.getBroadcast(getActivity(),1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
        }

    }

    public void addTask(View view){


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    private LinearLayout createNewTask (String text){
        ActionBar.LayoutParams lparams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(lparams);
        textView.setText("task: " + text);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(lparams);
        layout.addView(textView);

        return layout;
    }
    @Override
    public void onClick(View view) {

        Log.d("inam tag!", "onClick joz button");
        if (view.getId() == R.id.new_task){

            FragmentTransaction trans = getFragmentManager().beginTransaction();
        /*
         * IMPORTANT: We use the "root frame" defined in
         * "root_fragment.xml" as the reference to replace fragment
         */
            trans.replace(R.id.root_frame, new NewTaskFragment());

        /*
         * IMPORTANT: The following lines allow us to add the fragment
         * to the stack and return to it later, by pressing back
         */
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(null);

            trans.commit();


        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


}
