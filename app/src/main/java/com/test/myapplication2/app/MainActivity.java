package com.test.myapplication2.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v4.app.FragmentActivity;
//import android.support.v13.view.ViewPager;
//import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.test.myapplication2.app.TabsPagerAdapter;

public class MainActivity extends AppCompatActivity{
    /**
     * Called when the activity is first created.
     */

    private ViewPager viewPager;
    private ActionBar actionBar;
    private TabsPagerAdapter mAdapter;
    private  boolean b;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TabsPagerAdapter adapter = new TabsPagerAdapter(getFragmentManager());
//        ViewPager pager = (ViewPager) findViewById(R.id.pager);
//        pager.setAdapter(adapter);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
//        viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_alarm_check_grey600_24dp);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_calendar_check_grey600_24dp);
        tabLayout.getTabAt(2).setIcon(R.mipmap.ic_chart_bar_grey600_24dp);
//        tabLayout.getTabAt(3).setIcon(R.mipmap.ic_tune_grey600_24dp);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStackImmediate();
        else super.onBackPressed();
    }
}
