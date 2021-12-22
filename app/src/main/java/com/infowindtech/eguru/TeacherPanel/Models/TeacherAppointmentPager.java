package com.infowindtech.eguru.TeacherPanel.Models;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.infowindtech.eguru.TeacherPanel.Fragments.Teacher_Confirm_Appointment;
import com.infowindtech.eguru.TeacherPanel.Fragments.Teacher_Past_Appointment;
import com.infowindtech.eguru.TeacherPanel.Fragments.Teacher_Unconfirm_Appointment;

public class TeacherAppointmentPager extends FragmentStatePagerAdapter {
    int tabCount;

    public TeacherAppointmentPager(FragmentManager fm,int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                //Teacher_Unconfirm_Appointment unconf=new Teacher_Unconfirm_Appointment();
                return new Teacher_Unconfirm_Appointment() ;
            case 1:
                //Teacher_Confirm_Appointment conf=new Teacher_Confirm_Appointment();
                return new Teacher_Confirm_Appointment();
            case 2:
                //Teacher_Past_Appointment past = new Teacher_Past_Appointment();
                return new Teacher_Past_Appointment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
