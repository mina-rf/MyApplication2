package com.test.myapplication2.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mina on 7/19/16.
 */
public class RootFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.root_fragment, container, false);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

    /*
     * When this container fragment is created, we fill it with our first
     * "real" fragment
     */
        ToDoListFragment toDoListFragment = new ToDoListFragment();
//        toDoListFragment.setArguments(savedInstanceState);
        transaction.replace(R.id.root_frame, toDoListFragment);

        transaction.commit();

        return view;
    }
}
