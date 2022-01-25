package com.lg_project.adapter;


import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.lg_project.activity.MainActivity2;
import com.lg_project.R;
import com.lg_project.fragment.Contractors;
import com.lg_project.fragment.Messages;
import com.lg_project.fragment.Notifications;

public class MyAdapterHome extends FragmentStatePagerAdapter {

    private Context myContext;
    int totalTabs;

    public MyAdapterHome(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MainActivity2.heading.setText(R.string.contractors);
                Contractors homeFragment = new Contractors();
                return homeFragment;
            case 1:
                //MainActivity2.heading.setText(R.string.msgs);
                Messages sportFragment = new Messages();
                return sportFragment;
            case 2:
                //MainActivity2.heading.setText(R.string.notification);
                Notifications notification = new Notifications();
                return notification;
        }
        return null;
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }


}