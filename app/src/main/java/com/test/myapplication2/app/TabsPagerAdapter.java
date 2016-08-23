package com.test.myapplication2.app;

//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by mina on 7/9/16.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public int getItemPosition(Object object) {
        Fragment fragment = (Fragment) object;
        if(fragment instanceof  PomodoroRootFragment){
            System.out.println("possssiiii");
            return POSITION_UNCHANGED;
        }
        return POSITION_NONE;
    }



    @Override
    public Fragment getItem(int index)
    {
        switch (index)
        {
            case 0:
                return new PomodoroRootFragment();
            case 1:
                return new RootFragment();
            case 2:
                return new StatisticsFragment();
        }

        return null;
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String[] tabs = {"","","",""};
        return tabs[position];
    }

}



