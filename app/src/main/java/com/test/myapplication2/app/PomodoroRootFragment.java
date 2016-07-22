package com.test.myapplication2.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by a on 7/22/16.
 */
public class PomodoroRootFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pomodoro_root, container, false);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.pomodoro_root, new PomodoroFragment());

        transaction.commitAllowingStateLoss();

        return view;
    }
}
