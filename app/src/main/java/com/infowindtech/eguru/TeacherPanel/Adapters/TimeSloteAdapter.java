package com.infowindtech.eguru.TeacherPanel.Adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.infowindtech.eguru.TeacherPanel.Models.TimeSloteSpinnerData;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.databinding.SpinneritemBinding;

import java.util.List;

public class TimeSloteAdapter extends BaseAdapter {
    private List<TimeSloteSpinnerData> mListenerList;
    Context mContext;


    public TimeSloteAdapter(List<TimeSloteSpinnerData> mListenerList, Context mContext) {
        this.mListenerList = mListenerList;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return mListenerList.size();
    }

    @Override
    public Object getItem(int i) {
        return mListenerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SpinneritemBinding binding;
        if (view == null) {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(mContext),
                    R.layout.spinneritem, viewGroup, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (SpinneritemBinding) view.getTag();
        }
        binding.tvUser.setText(mListenerList.get(i).getTimeslote());
        return binding.getRoot();
    }
}
