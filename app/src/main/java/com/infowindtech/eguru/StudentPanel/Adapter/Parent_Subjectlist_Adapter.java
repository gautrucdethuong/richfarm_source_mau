package com.infowindtech.eguru.StudentPanel.Adapter;


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
import com.infowindtech.eguru.StudentPanel.Fragments.Student_Subject_Fragment;

import com.infowindtech.eguru.StudentPanel.Model.SubjectData;
import com.infowindtech.eguru.databinding.StudentSubjectGvitemBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Parent_Subjectlist_Adapter extends BaseAdapter {
    private List<SubjectData> mListenerList;
    private ArrayList<SubjectData> arraylist;
    Context mContext;
    StudentSubjectGvitemBinding binding;
    public Parent_Subjectlist_Adapter(List<SubjectData> mListenerList, Context mContext) {
        this.mListenerList = mListenerList;
        this.mContext = mContext;
        this.arraylist = new ArrayList<SubjectData>();
        this.arraylist.addAll(mListenerList);
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
                    R.layout.student_subject_gvitem, viewGroup, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (StudentSubjectGvitemBinding) view.getTag();
        }
        binding.tvSubject.setVisibility(View.GONE);
        binding.imSubject.setVisibility(View.GONE);
        Glide.with(mContext).load(mListenerList.get(i).getImage())
                .thumbnail(0.5f)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imMainSubject);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Student_Subject_Fragment frag=new Student_Subject_Fragment();
                Bundle bundle=new Bundle();
                bundle.putString("subjectid",mListenerList.get(i).getId());
                bundle.putString("subjectname",mListenerList.get(i).getType());
                frag.setArguments(bundle);
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout,frag).commit();
            }
        });
        return binding.getRoot();
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mListenerList.clear();
        if (charText.length() == 0) {
            mListenerList.addAll(arraylist);
        }
        else
        {
            for (SubjectData wp : arraylist)
            {
                if (wp.getType().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    mListenerList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}

