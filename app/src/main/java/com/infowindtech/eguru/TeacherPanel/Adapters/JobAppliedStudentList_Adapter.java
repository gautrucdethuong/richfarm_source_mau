package com.infowindtech.eguru.TeacherPanel.Adapters;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.StudentPanel.Model.ClassInfoData;
import com.infowindtech.eguru.TeacherPanel.Fragments.StudentProfile_Fragment;
import com.infowindtech.eguru.databinding.ApplyJobListitemBinding;

import java.util.List;

public class JobAppliedStudentList_Adapter extends BaseAdapter{
    private List<ClassInfoData> mListenerList;
    Context mContext;
    ApplyJobListitemBinding binding;

    public JobAppliedStudentList_Adapter(List<ClassInfoData> mListenerList, Context mContext) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(mContext),
                    R.layout.apply_job_listitem, viewGroup, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (ApplyJobListitemBinding) view.getTag();
        }
        binding.tvName.setText(mListenerList.get(i).getSubject());
        binding.tvPhone.setText(mListenerList.get(i).getTime());
        binding.tvAddress.setText(mListenerList.get(i).getDate());
        Glide.with(mContext).load(mListenerList.get(i).getUser_pic())
                .thumbnail(0.5f)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imProfile);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudentProfile_Fragment frag=new StudentProfile_Fragment();
                Bundle bundle=new Bundle();
                bundle.putString("studentid",mListenerList.get(i).getId());
                bundle.putString("from","Appliedjob");
                bundle.putString("appointment","");
                frag.setArguments(bundle);
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.applyjob,frag).commit();
            }
        });
        return binding.getRoot();
    }
}
