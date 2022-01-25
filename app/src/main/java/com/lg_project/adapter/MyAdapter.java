package com.lg_project.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.lg_project.fragment.Available;
import com.lg_project.fragment.AvailableSoon;
import com.lg_project.fragment.NotAvailable;

import org.jetbrains.annotations.NotNull;

public class MyAdapter extends FragmentStatePagerAdapter {

    private Context myContext;
    int totalTabs;
    Available tab1;
    NotAvailable tab3;
    AvailableSoon tab2;


    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                tab1 = new Available();
                return tab1;
            case 1:
                tab2 = new AvailableSoon();
                return tab2;
            case 2:
                tab3 = new NotAvailable();
                return tab3;
            default:
                return null;
        }
    }
//    public void refreshFragment(int position) {
//        switch (position) {
//            case 1:
//                tab1.AvailableData();
//                break;
//            case 2:
//                tab2.AvailableData();
//                break;
//            case 3:
//                tab3.AvailableData();
//                break;
//
//        }
//    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }


}