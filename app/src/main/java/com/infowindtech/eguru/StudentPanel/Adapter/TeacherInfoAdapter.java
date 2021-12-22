package com.infowindtech.eguru.StudentPanel.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.infowindtech.eguru.R;
import com.infowindtech.eguru.StudentPanel.Model.TeacherInfo;

import java.util.List;

public class TeacherInfoAdapter extends RecyclerView.Adapter<TeacherInfoAdapter.MyViewHolder>{
    private List<TeacherInfo> mListenerList;
    Context mContext;
    private LayoutInflater inflater;

    public TeacherInfoAdapter(List<TeacherInfo> mListenerList, Context mContext) {
        this.mListenerList = mListenerList;
        this.mContext = mContext;
        inflater= LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teacher_info_listitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
        holder.tvAttributName.setText(mListenerList.get(i).getAttribute());
        holder.tvValue.setText(mListenerList.get(i).getValue());
    }

    @Override
    public int getItemCount() {
        return mListenerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAttributName, tvValue;
        public MyViewHolder(View view) {
            super(view);
            tvAttributName =view.findViewById(R.id.tv_attribut_name);
            tvValue = view.findViewById(R.id.tv_value);
        }
    }
}
