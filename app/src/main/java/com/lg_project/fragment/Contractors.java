package com.lg_project.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.tabs.TabLayout;
import com.lg_project.activity.MainActivity;
import com.lg_project.activity.MainActivity2;
import com.lg_project.R;
import com.lg_project.adapter.MyAdapter;

public class Contractors extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    private MyAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        MainActivity2.heading.setText(R.string.contractors);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contractors, container, false);
        return v;
    }


    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        //MainActivity2.l1.setVisibility(View.VISIBLE);

        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.available));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.available_soon));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.not_available));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

//        Available available=new Available();
//                    getFragmentManager().beginTransaction().replace(R.id.viewPager,available).commit();

//        Fragment fragment = new Available();
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment, fragment.getClass().getSimpleName()).commit();
//        viewPager.setOffscreenPageLimit(2);
        adapter = new MyAdapter(getContext(), getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
                    case 0: MainActivity2.heading.setText(R.string.contractors);
                            break;
                    case 1: MainActivity2.heading.setText(R.string.contractors);
                        break;
                    case 2: MainActivity2.heading.setText(R.string.contractors);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        MainActivity2.heading.setText(R.string.contractors);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
//                adapter.refreshFragment(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
//                Toast.makeText(getContext(), "tabReSelected:  " + tab.getText(), Toast.LENGTH_SHORT).show();
              /*  if(tab.getPosition()==0)
                {
                    Available available=new Available();
                    getFragmentManager().beginTransaction().replace(R.id.viewPager,available).commit();
                }*/
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if (viewPager != null && viewPager.getAdapter() != null) {
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }

}