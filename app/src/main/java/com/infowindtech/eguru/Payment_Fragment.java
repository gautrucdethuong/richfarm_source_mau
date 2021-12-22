package com.infowindtech.eguru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.infowindtech.eguru.databinding.ActivityRazorPaymentBinding;
import com.infowindtech.eguru.databinding.FragStudentCalender1Binding;

public class Payment_Fragment extends Fragment {

    ActivityRazorPaymentBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityRazorPaymentBinding.inflate(inflater, container, false);


       return binding.getRoot();
    }
}