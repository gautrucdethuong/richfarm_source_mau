package com.infowindtech.eguru.TeacherPanel.Fragments;


import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infowindtech.eguru.R;
import com.infowindtech.eguru.TeacherPanel.Models.TeacherAppointmentPager;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.LayoutAppointmentTabsBinding;

public class Teacher_Appointment_Fragment extends Fragment implements TabLayout.OnTabSelectedListener {
    LayoutAppointmentTabsBinding binding;
    TeacherAppointmentPager adapter;
    String flag,langcode;
    UserSessionManager userSessionManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=LayoutAppointmentTabsBinding.inflate(inflater,container,false);
        flag=getArguments().getString("flag");
       /* if (userSessionManager.signIn())
            getActivity().finish();
        HashMap<String,String> lang=userSessionManager.getLanguage();
        langcode=lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(),langcode);*/
        binding.tabLayout.addTab( binding.tabLayout.newTab().setText(getString(R.string.unconfirm)), 0);
        binding.tabLayout.addTab( binding.tabLayout.newTab().setText(getString(R.string.confirm)), 1);
        binding.tabLayout.addTab( binding.tabLayout.newTab().setText(getString(R.string.past)), 2);
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new TeacherAppointmentPager(getChildFragmentManager(),  binding.tabLayout.getTabCount());
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(3);
        binding.tabLayout.setOnTabSelectedListener(this);
        binding.pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener( binding.tabLayout));
        TabLayout.Tab tab =  binding.tabLayout.getTabAt(Integer.parseInt(flag));
        tab.select();
        for(int i=0; i < binding.tabLayout.getTabCount(); i++) {
            View tab1 = ((ViewGroup) binding.tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab1.getLayoutParams();
            p.setMargins(10, 10, 10, 10);
            tab1.requestLayout();
        }
        return binding.getRoot();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        binding.pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


}
