package com.infowindtech.eguru.StudentPanel.Model;






import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.infowindtech.eguru.StudentPanel.Fragments.Teacher_Experience_Fragment;
import com.infowindtech.eguru.StudentPanel.Fragments.Teacher_PersonalInfo_Fragment;

public class TeacherProfilePager extends FragmentStatePagerAdapter {
    int tabCount;
    public TeacherProfilePager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Teacher_PersonalInfo_Fragment tab1 = new Teacher_PersonalInfo_Fragment();
                return tab1;
            case 1:
                Teacher_Experience_Fragment tab2 = new Teacher_Experience_Fragment();
                return tab2;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return tabCount;
    }
}
