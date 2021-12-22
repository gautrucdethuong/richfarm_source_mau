package com.infowindtech.eguru.StudentPanel.Adapter;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.infowindtech.eguru.R;
import com.infowindtech.eguru.StudentPanel.Model.DrAppointmenttime;
import com.infowindtech.eguru.StudentPanel.TeacherProfileActivity;
import com.infowindtech.eguru.databinding.GvappointmentslotitemBinding;

import java.util.ArrayList;
import java.util.List;

public class GridviewAppointmentslotAdapter extends BaseAdapter {
    public List<DrAppointmenttime> mListenerList;
    Context mContext;
    TeacherProfileActivity activity;
    private ArrayList<DrAppointmenttime> arrayList;
    View savedView;
    public GridviewAppointmentslotAdapter(List<DrAppointmenttime> mListenerList, Context mContext, TeacherProfileActivity activity) {
        this.mListenerList = mListenerList;
        this.mContext = mContext;
        this.activity = activity;
        this.arrayList=new ArrayList<>();
        this.arrayList.addAll(mListenerList);
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        GvappointmentslotitemBinding binding;
        if (view == null) {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(mContext),
                    R.layout.gvappointmentslotitem, viewGroup, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (GvappointmentslotitemBinding) view.getTag();
        }
        final String confirmdata = mListenerList.get(i).getBgroup();
        binding.tvTime.setText(confirmdata);
        /*if (savedView != null) {
            savedView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }*/
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String confirmdata = mListenerList.get(i).getBgroup();
                activity.selectstarttime(confirmdata);
                view.setBackgroundColor(mContext.getResources().getColor(R.color.blue_circle_in_image));
                if (savedView == null){
                    savedView = view;
                } else {
                    savedView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    savedView = view;
                }
            }
        });
        return binding.getRoot();
    }

}