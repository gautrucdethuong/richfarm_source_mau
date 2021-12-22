package com.infowindtech.eguru.TeacherPanel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.infowindtech.eguru.TeacherPanel.Models.UserType;
import com.infowindtech.eguru.R;

import java.util.ArrayList;
import java.util.List;

public class UserTypeAdapter extends BaseAdapter {
    private List<UserType> mListenerList;
    public ArrayList<UserType> arraylistListener;
    Context mContext;

    public UserTypeAdapter(List<UserType> mListenerList, Context mContext) {
        this.mListenerList = mListenerList;
        this.mContext = mContext;
        this.arraylistListener = new ArrayList<UserType>();
        this.arraylistListener.addAll(mListenerList);
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

        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.spinneritem, null);
            holder = new ViewHolder();
            holder.type = view.findViewById(R.id.tv_user);
            holder.id = view.findViewById(R.id.tv_id);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.type.setText(mListenerList.get(i).getType());
        return view;
    }

    public class ViewHolder {
        TextView type;
        TextView id;
    }
}
