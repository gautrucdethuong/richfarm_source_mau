package com.infowindtech.eguru.StudentPanel.Fragments;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.FragPersonalinfo1Binding;

import java.util.HashMap;

public class Personal_Information_Fragment extends Fragment{
    FragPersonalinfo1Binding binding;
    UserSessionManager userSessionManager;
    String user_id, phone, email, usertype, address, city, country, zipcode, classes, subjects, name, profile,langcode;
    public static boolean isDrawerOpen = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragPersonalinfo1Binding.inflate(inflater,container,false);
        binding.tvHeading.setText(getString(R.string.Profile));
        userSessionManager = new UserSessionManager(getActivity());
        if (userSessionManager.signIn())
            getActivity().finish();

        binding.imDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StudentHomeActivity) getActivity()).callDrawer();
            }
        });

        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        name = user.get(userSessionManager.KEY_FULLNAME);
        phone = user.get(userSessionManager.KEY_PHONENUMBER);
        email = user.get(userSessionManager.KEY_EMAIL);
        usertype = user.get(userSessionManager.KEY_USERTYPE);
        address = user.get(userSessionManager.KEY_ADDRESS);
        city = user.get(userSessionManager.KEY_CITY);
        country = user.get(userSessionManager.KEY_COUNTRY);
        zipcode = user.get(userSessionManager.KEY_ZIPCODE);
        classes = user.get(userSessionManager.KEY_CLASSES);
        subjects = user.get(userSessionManager.KEY_SUBJECT);
        profile = user.get(userSessionManager.KEY_PROFILE);
        HashMap<String,String> lang=userSessionManager.getLanguage();
        langcode=lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(),langcode);
        binding.tvName.setText(toTitleCase(name));
        binding.tvEmail.setText(email);
        binding.tvPhone.setText(phone);
        binding.tvUserName.setText(toTitleCase(name));
        binding.tvEmail1.setText(email);
        binding.tvMobile.setText(phone);
        binding.tvSubjects.setText(subjects);
        binding.tvClasses.setText(classes);
        if (usertype.equals("1")){
            binding.tvType.setText(getString(R.string.teacher));
        }else{
            binding.tvType.setText(getString(R.string.student));
        }
        binding.tvAddress.setText(address);
        Glide.with(getActivity()).load(profile)
                .thumbnail(0.5f)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imProfile);
         return binding.getRoot();
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
