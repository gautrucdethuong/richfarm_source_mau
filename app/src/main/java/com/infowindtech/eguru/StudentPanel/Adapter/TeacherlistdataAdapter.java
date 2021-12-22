package com.infowindtech.eguru.StudentPanel.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.FirebaseChat.FirebaseChatMessage;
import com.infowindtech.eguru.MineVolleyGlobal;
import com.infowindtech.eguru.MineVolleyListener;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Model.TeacherlistData;
import com.infowindtech.eguru.StudentPanel.TeacherProfileActivity;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.ParticularsubjteacherListitemBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class TeacherlistdataAdapter extends BaseAdapter {
    private List<TeacherlistData> mListenerList;
    Context mContext;
    ParticularsubjteacherListitemBinding binding;
    UserSessionManager userSessionManager;
    String user_id,senderid,str_usertype ,senderName,senderUsername,senderPic ;

    public TeacherlistdataAdapter(List<TeacherlistData> mListenerList, Context mContext) {
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
        userSessionManager = new UserSessionManager(mContext);
        if (userSessionManager.signIn())
            ((Activity) mContext).finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        str_usertype = user.get(userSessionManager.KEY_USERTYPE);
        if (view == null) {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(mContext),
                    R.layout.particularsubjteacher_listitem, viewGroup, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (ParticularsubjteacherListitemBinding) view.getTag();
        }
        binding.tvName.setText(mListenerList.get(i).getTname());
        if (mListenerList.get(i).getTagline().equals("")) {
            binding.tvEmail.setVisibility(View.GONE);
        } else {
            binding.tvEmail.setVisibility(View.VISIBLE);
            binding.tvEmail.setText(mListenerList.get(i).getTagline());
        }
        binding.tvContact.setText(mListenerList.get(i).getTmobile());
        binding.tvFee.setText(mListenerList.get(i).getTfee() + "/h");
        binding.rating.setRating(Float.parseFloat(mListenerList.get(i).getTrating()));
        binding.rating.setEnabled(false);
      //  binding.tvQualityRating.setText(mListenerList.get(i).getTeacher_quality());
        binding.imChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_id.equals("0")) {
                    AlertClass.signupdialog(mContext, mContext.getString(R.string.plzsignup));
                } else {
                    Log.d("RRRR", "onVolleyResponse: "+user_id+"  "+str_usertype+"  "+senderid+"   "+senderName);

                    senderid = mListenerList.get(i).getUserid() ;
                    senderName =  mListenerList.get(i).getTname();
                    senderUsername =  mListenerList.get(i).getTusername();
                    senderPic = mListenerList.get(i).getTimage();

                    if (str_usertype.equals("1")) {

                        callAPII(senderid,user_id);

                    } else {

                        callAPII(user_id,senderid);
                    }


                    //ChatConnection();
                      }
            }
        });
        Glide.with(mContext).load(mListenerList.get(i).getTimage())
                .thumbnail(0.5f)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imProfile);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext, TeacherProfileActivity.class);
                in.putExtra("id", mListenerList.get(i).getUserid());
                (mContext).startActivity(in);

            }
        });
        return binding.getRoot();
    }


    public void callAPII(String fromid,String toid){
        HashMap<String,String> map=new HashMap<>();
        map.put("user_from_id",fromid);
        map.put("user_to_id",toid);
        Log.d("RRRR", "onVolleyResponse: "+map);
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
                    intent.putExtra("sender_id", senderid);
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






    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}


