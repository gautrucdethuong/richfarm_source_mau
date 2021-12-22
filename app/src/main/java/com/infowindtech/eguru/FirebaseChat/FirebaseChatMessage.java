package com.infowindtech.eguru.FirebaseChat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.R;

import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.ChatMsgBinding;
import com.infowindtech.eguru.databinding.FragChatMsglist1Binding;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class FirebaseChatMessage extends AppCompatActivity {
    ChatMsgBinding binding;
    String msg = "", sender_name = "", sender_username = "", sender_pic = "", sender_id = "", langcode = "", str_username = "", str_usertype;
    ArrayList<ChatModel> msglist;
    UserSessionManager userSessionManager;
    String str_userid, str_userprofile, name;

    private Handler m_handler;

    DatabaseReference myRef;
    FirebaseChatAdapter adapter1;
    ChildEventListener childEventListener;
    Boolean firstTime;
    ArrayList<String> keyList;
    String room_id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar

        binding=DataBindingUtil.setContentView(FirebaseChatMessage.this,R.layout.chat_msg);



        binding.imBackNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("KKDKDKKDKD", "onClick: ");

                onBackPressed();

            }
        });

        userSessionManager = new UserSessionManager(this);

        final HashMap<String, String> user = userSessionManager.getUserDetails();
        str_userid = user.get(userSessionManager.KEY_USERID);
        str_userprofile = user.get(userSessionManager.KEY_PROFILE);
        str_username = user.get(userSessionManager.KEY_USERNAME);
        str_usertype = user.get(userSessionManager.KEY_USERTYPE);

        Intent intent=getIntent();
        sender_id = intent.getStringExtra("sender_id");
        sender_name = intent.getStringExtra("sender_name");
        sender_username = intent.getStringExtra("sender_username");
        room_id = intent.getStringExtra("room_id");

        Log.d("room", "onCreateView: " + sender_id + "   " + str_userid+"     "+room_id);

        sender_pic = intent.getStringExtra("sender_pic");

        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(this, langcode);
        name = sender_name.substring(0, 1).toUpperCase() + sender_name.substring(1);
        binding.tvSendername.setText(name);
        binding.chatListView.setVisibility(View.INVISIBLE);
        binding.tvNomsg.setVisibility(View.VISIBLE);
       // binding.imOption.setVisibility(View.INVISIBLE);
        Glide.with(this).load(sender_pic)
                .thumbnail(0.5f)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.senderImg);

        firstTime = true;

        msglist = new ArrayList<ChatModel>();
        keyList = new ArrayList<String>();




        FirebaseApp.initializeApp(this);
       // FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference()
                .child("chat").child(room_id);


//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    String key = ds.getKey();
//                    ChatModel comment = dataSnapshot.getValue(ChatModel.class);
//                    Log.d("TAG", key + ": " + name);
//
//                    binding.chatListView.setVisibility(View.VISIBLE);
//                    binding.tvNomsg.setVisibility(View.INVISIBLE);
//                    // binding.imOption.setVisibility(View.VISIBLE);
//                    msglist.add(comment);
//                    keyList.add(key);
//                    adapter1 = new FirebaseChatAdapter(msglist, FirebaseChatMessage.this,
//                            FirebaseChatMessage.this, keyList);
//                    binding.chatListView.setAdapter(adapter1);
//                    refreshList();
//                    adapter1.notifyDataSetChanged();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d("TAG", databaseError.getMessage()); //Don't ignore errors!
//            }
//        };
//        myRef.addListenerForSingleValueEvent(valueEventListener);



        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String previousChildName) {

                final ChatModel comment = dataSnapshot.getValue(ChatModel.class);
                String keyModel = dataSnapshot.getKey();


                Log.d("ccccc", "firstitme: " + keyModel+"  "+comment+"  "+comment.equals(null)+"   "+comment.equals(""));


                if (comment.equals(null) || comment.equals("")) {

                    binding.chatListView.setVisibility(View.INVISIBLE);
                    binding.tvNomsg.setVisibility(View.VISIBLE);


                } else {

                    binding.chatListView.setVisibility(View.VISIBLE);
                    binding.tvNomsg.setVisibility(View.INVISIBLE);
                   // binding.imOption.setVisibility(View.VISIBLE);
                    msglist.add(comment);
                    keyList.add(keyModel);
                    adapter1 = new FirebaseChatAdapter(msglist, FirebaseChatMessage.this,
                            FirebaseChatMessage.this, keyList);
                    binding.chatListView.setAdapter(adapter1);
                    refreshList();
                    adapter1.notifyDataSetChanged();

                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addChildEventListener(childEventListener);



        Date cDate = new Date();
        final String nDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(cDate);
        binding.enterChat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = binding.chatEditText1.getText().toString().trim();
                if (msg.equals("")) {
                    Toast.makeText(FirebaseChatMessage.this, getString(R.string.nomsg), Toast.LENGTH_SHORT).show();
                } else {

                    DateFormat df = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                    String time = df.format(Calendar.getInstance().getTime());
                    SendChatMsgTask();
                    Log.d("RRRRRRR", "onClick: "+sender_id+"   "+str_userid);
                    ChatModel user = new ChatModel(sender_id, sender_name, str_username, str_userid, msg, nDate, time, "0");

                    myRef.push().setValue(user);

                    binding.chatEditText1.setText("");

                }
            }
        });


    }


    public void removeItem(ArrayList<String> key, int pos) {
        myRef.child(key.get(pos)).removeValue();
        adapter1.notifyDataSetChanged();
    }

    public void refreshList() {

        binding.chatListView.setSelection(msglist.size());
    }


    private void SendChatMsgTask() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.SendChatMsgURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                JSONObject obj = new JSONObject(response);
                                Log.d("RRRR", "onResponse: "+response);
                                String flag = obj.getString("status").toString();
//                                if (flag.equals("true")) {
//                                    GetAllChatMsgTask();
//                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_from_id", str_userid);
                params.put("user_to_id", sender_id);
                params.put("user_msg", msg);
                params.put("lang_code", langcode);
                Log.d("RRRR", "onResponse: "+params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FirebaseChatMessage.this);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }


}
