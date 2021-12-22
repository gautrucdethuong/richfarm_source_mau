package com.infowindtech.eguru.TeacherPanel.Adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.infowindtech.eguru.FirebaseChat.FirebaseChatMessage;
import com.infowindtech.eguru.MineVolleyGlobal;
import com.infowindtech.eguru.MineVolleyListener;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Model.ClassInfoData;
import com.infowindtech.eguru.TeacherPanel.Fragments.StudentProfile_Fragment;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.AppointmentListitemBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TeacherPastAdapter extends BaseAdapter {
    private List<ClassInfoData> mListenerList;
    Context mContext;
    AppointmentListitemBinding binding;
    String senderId ,senderName,senderUsername,senderPic,str_usertype,str_userId ;
    private ArrayList<ClassInfoData> arraylist;
    String status;
    UserSessionManager userSessionManager;
    public TeacherPastAdapter(List<ClassInfoData> mListenerList, Context mContext){
        this.mListenerList = mListenerList;
        this.mContext = mContext;
        this.arraylist = new ArrayList<ClassInfoData>();
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
                    R.layout.appointment_listitem, viewGroup, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (AppointmentListitemBinding) view.getTag();
        }
        userSessionManager = new UserSessionManager(mContext);
        if (userSessionManager.signIn())
            ((Activity) mContext).finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        str_usertype = user.get(userSessionManager.KEY_USERTYPE);
        str_userId = user.get(userSessionManager.KEY_USERID);

        binding.tvName.setText(mListenerList.get(i).getSubject());
        binding.tvDate.setText(""+mListenerList.get(i).getDate());
        binding.tvTime.setText(""+mListenerList.get(i).getTime());
        binding.tvPastStatus.setVisibility(View.VISIBLE);
        status=mListenerList.get(i).getStatus();
        if(status.equals("Cancel")){
            binding.tvPastStatus.setText(mContext.getString(R.string.Canceled));
            binding.tvPastStatus.setTextColor(ContextCompat.getColor(mContext, R.color.oreange));
            binding.borderlayout.setBackgroundResource(R.drawable.red_rectangle_border);
        }else if(status.equals("Completed")){
            binding.tvPastStatus.setText(mContext.getString(R.string.Completed));
            binding.tvPastStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
        }
        binding.actionLayout.setVisibility(View.GONE);
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
                bundle.putString("from","Appointment");
                bundle.putString("appointment",mListenerList.get(i).getAppointid());
                frag.setArguments(bundle);
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout,frag).commit();
            }
        });
        binding.imChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                senderId = mListenerList.get(i).getId();
                senderName = mListenerList.get(i).getSubject();
                senderUsername = mListenerList.get(i).getUsername();
                senderPic = mListenerList.get(i).getUser_pic();


                if (str_usertype.equals("1")) {

                    callAPII(str_userId,senderId);

                } else {
                    callAPII(senderId,str_userId);
                }
                       }
        });
        return binding.getRoot();
    }





    public void callAPII(String fromid,String toid){
        HashMap<String,String> map=new HashMap<>();
        map.put("user_from_id",fromid);
        map.put("user_to_id",toid);

        new MineVolleyGlobal(mContext).parseVollyStringRequest(ServiceClass.ChatConnection, 1, map, new MineVolleyListener() {
            @Override
            public void onVolleyResponse(String response) {
                Log.d("RRRR", "onVolleyResponse: "+response);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    String flag = obj.getString("status");
                    String msg = obj.getString("message");
                    String chatId = obj.getString("chat_id");
                    Log.d("RRRR", "RRRR: "+chatId);
                    String room_id = chatId ;
                    Log.d("RRRR", "onVolleyResponse: "+room_id);


                    Intent intent = new Intent(mContext, FirebaseChatMessage.class);
                    intent.putExtra("sender_id", senderId);
                    intent.putExtra("sender_name", senderName);
                    intent.putExtra("sender_username", senderUsername);
                    intent.putExtra("sender_pic", senderPic);
                    intent.putExtra("room_id", room_id);

                    mContext.startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }




    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        mListenerList.clear();
        if (charText.length() == 0) {
            mListenerList.addAll(arraylist);
        }
        else
        {
            for (ClassInfoData wp : arraylist)
            {
                if (wp.getDate().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    mListenerList.add(wp);
                }else if(wp.getSubject().toLowerCase(Locale.getDefault()).contains(charText)){
                    mListenerList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
