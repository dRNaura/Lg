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
import com.lg_project.adapter.MyAdapterHome;
import com.lg_project.modelclass.SocketManager;

import io.socket.client.Socket;


public class Home extends Fragment {

    TabLayout tabLayout2;
    ViewPager viewPager2;
    MyAdapterHome adapter;
    Socket mSocket;
    private SocketManager socketManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        MainActivity2.heading.setText(R.string.contractors);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        MainActivity2.heading.setText(R.string.contractors);
        tabLayout2=(TabLayout)v.findViewById(R.id.tabLayout2);
        viewPager2=(ViewPager)v.findViewById(R.id.viewPager2);

        tabLayout2.addTab(tabLayout2.newTab().setText(R.string.contractors).setIcon(R.drawable.contractor_active));
        tabLayout2.addTab(tabLayout2.newTab().setText(R.string.msgs).setIcon(R.drawable.message_inactive));
        tabLayout2.addTab(tabLayout2.newTab().setText(R.string.notification).setIcon(R.drawable.notification_inactive));
        tabLayout2.setTabGravity(TabLayout.GRAVITY_FILL);

//        viewPager2.setOffscreenPageLimit(2);
        adapter = new MyAdapterHome(getContext(), getChildFragmentManager(), tabLayout2.getTabCount());
        viewPager2.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        viewPager2.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout2));
        viewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
                    case 0: MainActivity2.heading.setText(R.string.contractors);
                          break;
                    case 1: MainActivity2.heading.setText(R.string.msgs);
                        break;
                    case 2: MainActivity2.heading.setText(R.string.notification);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        MainActivity2.heading.setText(R.string.contractors);
        tabLayout2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

               // Toast.makeText(getContext(), ""+tab.getPosition(), Toast.LENGTH_SHORT).show();
                viewPager2.setCurrentItem(tab.getPosition());

                if(tab.getPosition()==0)
                {
                  //  MainActivity2.heading.setText(R.string.contractors);
                  //  MainActivity2.l1.setVisibility(View.VISIBLE);
                    tab.setIcon(R.drawable.contractor_active);

                   // new Contractors();
                }

                if(tab.getPosition()==1)
                {
                   // MainActivity2.heading.setText(R.string.msgs);
                //    MainActivity2.l1.setVisibility(View.VISIBLE);
                    tab.setIcon(R.drawable.message_active);
                   // new Messages();
                }

               if(tab.getPosition()==2)
                {
                   // MainActivity2.heading.setText(R.string.notification);
                //    MainActivity2.l1.setVisibility(View.VISIBLE);
                    tab.setIcon(R.drawable.notification_active);
                  //  new Notifications();
                }

               /* Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new Available();
                        break;
                    case 1:
                        fragment = new AvailableSoon();
                        break;
                    case 2:
                        fragment = new NotAvailable();
                        break;
                }
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.viewPager, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
*/

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                switch (tab.getPosition())
                {
                    case 0: tab.setIcon(R.drawable.contractor_inactive);
                        break;
                    case 1: tab.setIcon(R.drawable.message_inactive);
                        break;
                    case 2: tab.setIcon(R.drawable.notification_inactive);
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                Toast.makeText(getContext(), "tabReSelected:  " + tab.getText(), Toast.LENGTH_SHORT).show();
                if(tab.getText().equals("Contractors"))
                {
                    MainActivity2.l1.setVisibility(View.VISIBLE);
                }
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewPager2 != null && viewPager2.getAdapter() != null) {
            viewPager2.getAdapter().notifyDataSetChanged();
        }
    }


}