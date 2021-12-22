package com.infowindtech.eguru.StudentPanel.Adapter;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.StudentPanel.Fragments.TeacherListFragment;
import com.infowindtech.eguru.StudentPanel.Model.SubjectData;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.StudentSubjectGvitemBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Student_Subject_Adapter extends BaseAdapter{
    private List<SubjectData> mListenerList;
    private ArrayList<SubjectData> arraylist;
    Context mContext;
    StudentSubjectGvitemBinding binding;
    UserSessionManager userSessionManager;
    String user_id;
    public Student_Subject_Adapter(List<SubjectData> mListenerList, Context mContext) {
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
        userSessionManager = new UserSessionManager(mContext);
        if (userSessionManager.signIn())
            ((Activity) mContext).finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        if (view == null) {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(mContext),
                    R.layout.student_subject_gvitem, viewGroup, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (StudentSubjectGvitemBinding) view.getTag();
        }
        binding.imMainSubject.setVisibility(View.GONE);
        binding.tvSubject.setText(mListenerList.get(i).getType());
        Glide.with(mContext).load(mListenerList.get(i).getImage())
                .thumbnail(0.5f)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imSubject);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_id.equals("0")) {
                    AlertClass.signupdialog(mContext,mContext.getString(R.string.plzsignup));
                } else {
                    TeacherListFragment frag = new TeacherListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("subjectid", mListenerList.get(i).getId());
                    frag.setArguments(bundle);
                    ((Activity) mContext).getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.subject, frag).commit();
                }
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
