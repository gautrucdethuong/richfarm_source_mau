package com.infowindtech.eguru.StudentPanel.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.StudentPanel.Model.TeacherlistData;
import com.infowindtech.eguru.StudentPanel.TeacherProfileActivity;
import com.infowindtech.eguru.databinding.ChoosePlaceTeacherlistBinding;


import java.util.List;

public class Choose_Studyplace_Adapter extends BaseAdapter{
    private List<TeacherlistData> mListenerList;
    Context mContext;
    Handler handler;
    ChoosePlaceTeacherlistBinding binding;
    public Choose_Studyplace_Adapter(List<TeacherlistData> mListenerList, Context mContext) {
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
            binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.choose_place_teacherlist, viewGroup, false);
            view = binding.getRoot();
            view.setTag(binding);
        }else{
            binding = (ChoosePlaceTeacherlistBinding) view.getTag();
        }
      //
        //  handler = new Handler();
        binding.tvClass.setText(mListenerList.get(i).getTname());
        binding.tvDate.setVisibility(View.GONE);
        binding.tvTime.setVisibility(View.GONE);
        binding.tvFee.setText(mListenerList.get(i).getTfee()+"/h");
        binding.rating.setVisibility(View.VISIBLE);
        binding.rating.setRating(Float.parseFloat(mListenerList.get(i).getTrating()));
        binding.rating.setEnabled(false);
      //  binding.tvQualityRating.setText(mListenerList.get(i).getTeacher_quality());
        Glide.with(mContext).load(mListenerList.get(i).getTimage())
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imProfile);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Intent in=new Intent(mContext, TeacherProfileActivity.class);
                        in.putExtra("id",mListenerList.get(i).getUserid());
                      //  in.putExtra("id",mListenerList.get(i).);
                        (mContext).startActivity(in);
            }
        });
        return binding.getRoot();
    }

}
