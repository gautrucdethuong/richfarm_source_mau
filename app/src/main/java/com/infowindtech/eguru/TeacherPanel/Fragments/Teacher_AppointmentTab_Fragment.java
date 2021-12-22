package com.infowindtech.eguru.TeacherPanel.Fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infowindtech.eguru.MainActivity;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.TeacherPanel.Models.Teacher_AppointmentTabPager;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.FragAppointmentTab1Binding;

public class Teacher_AppointmentTab_Fragment extends Fragment implements TabLayout.OnTabSelectedListener {
    FragAppointmentTab1Binding binding;
    Teacher_AppointmentTabPager adapter;
    String flag, langcode;
    UserSessionManager userSessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragAppointmentTab1Binding.inflate(inflater, container, false);


        binding.imDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    ((MainActivity)getActivity()).callDrawer();

                }catch (Exception ex){
                    ((StudentHomeActivity)getActivity()).callDrawer();
                }

            }
        });

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.calender)), 0);
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.appoint)), 1);
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new Teacher_AppointmentTabPager(getChildFragmentManager(), binding.tabLayout.getTabCount());
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(2);
        binding.tabLayout.setOnTabSelectedListener(this);
        binding.pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tvHeading.setText(getString(R.string.calender));


        for(int i=0; i < binding.tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) binding.tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(20, 10, 20, 10);
            tab.requestLayout();
        }


        return binding.getRoot();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        binding.pager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 0) {
            binding.tvHeading.setText(getString(R.string.calender));
        } else if (tab.getPosition() == 1) {
            binding.tvHeading.setText(getString(R.string.appoint));
        }
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
