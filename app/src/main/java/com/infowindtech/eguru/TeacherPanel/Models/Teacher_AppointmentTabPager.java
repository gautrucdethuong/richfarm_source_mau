package com.infowindtech.eguru.TeacherPanel.Models;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.infowindtech.eguru.TeacherPanel.Fragments.Teacher_Appointment_Fragment;
import com.infowindtech.eguru.TeacherPanel.Fragments.Teacher_Calender_Fragment;


public class Teacher_AppointmentTabPager  extends FragmentStatePagerAdapter {
    int tabCount;

    public Teacher_AppointmentTabPager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Teacher_Calender_Fragment();
            case 1:
                 Teacher_Appointment_Fragment fragment = new Teacher_Appointment_Fragment();
                 Bundle bundle=new Bundle();
                 bundle.putString("flag","0");
                 fragment.setArguments(bundle);
                 return fragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
