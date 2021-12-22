package com.infowindtech.eguru.FirebaseChat;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.MainActivity;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.FragChatUsersBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatUsersListFragment extends Fragment {
    FragChatUsersBinding binding;
    ChatUserListAdapter adapter;
    ArrayList<ChatUserListData> user_list;
    RequestQueue requestQueue;
    UserSessionManager userSessionManager;
    String str_userid, langcode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragChatUsersBinding.inflate(inflater, container, false);
        userSessionManager = new UserSessionManager(getContext());
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        str_userid = user.get(userSessionManager.KEY_USERID);
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(), langcode);

        binding.imDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    ((MainActivity)getActivity()).callDrawer();

                }catch (Exception ex){
                    ((StudentHomeActivity)getActivity()).callDrawer();
                }


            }
        });



        binding.tvHeading.setText(getString(R.string.Chat));
        binding.tvNoclass.setVisibility(View.VISIBLE);
        ChatUserListTask();


        String androidId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(androidId).build();
        binding.adView.loadAd(adRequest);
        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getActivity(), getString(R.string.addfailed) + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
        return binding.getRoot();
    }
/*
    @Override
    public void onResume() {
        super.onResume();
        binding.adView.setVisibility(View.VISIBLE);
        Log.d("printmsg", "REsume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("printmsg", "PAuse");
        binding.adView.setVisibility(View.GONE);
    }
    */

    public void visibilitygone() {
        binding.adView.setVisibility(View.GONE);
    }

    public void ChatUserListTask() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.ChatUSERListURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            if (null != response) {
                                JSONObject obj = new JSONObject(response);
                                Log.d("RRRRR", "chatuserlistfragment  :  onResponse: "+response);

                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                user_list = new ArrayList<>();
                                if (flag.equals("true")) {
                                    JSONArray jarr = obj.getJSONArray("data");
                                    if (jarr.length() == 0) {
                                        binding.tvNoclass.setVisibility(View.VISIBLE);
                                    } else {
                                        binding.tvNoclass.setVisibility(View.GONE);
                                        for (int i = 0; i < jarr.length(); i++) {
                                            JSONObject job = jarr.getJSONObject(i);
                                            String str_userfromid = job.getString("user_from_id");
                                            String str_name = job.getString("full_name");
                                            String str_username = job.getString("user_name");
                                            String str_profile = job.getString("profile_image");
                                            String str_usertoid = job.getString("user_to_id");
                                            int str_unreadmsg = job.getInt("unread_msg_count");
                                            String str_chatId = job.getString("chat_id");
                                            ChatUserListData user1 = new ChatUserListData();
                                            user1.setName(str_name);
                                            user1.setUserfromid(str_userfromid);
                                            user1.setUsertoid(str_usertoid);
                                            user1.setProfile(str_profile);
                                            user1.setUsername(str_username);
                                            user1.setUnreadmsg(str_unreadmsg);
                                            user1.setChatId(str_chatId);
                                            user_list.add(user1);
                                            adapter = new ChatUserListAdapter(user_list, getActivity(), ChatUsersListFragment.this);
                                            adapter.notifyDataSetChanged();
                                            binding.lvChat.setAdapter(adapter);
                                        }
                                    }
                                } else if (flag.equals("false")) {
                                    AlertClass.DispToast(getActivity(), msg);
                                    binding.tvNoclass.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "chatuserlistfragment  :  exception: "+e);
                            AlertClass.DispToast(getActivity(), e.getMessage());
                            binding.tvNoclass.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "chatuserlistfragment  :  error: "+error);
                        //Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        binding.tvNoclass.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", str_userid);
                params.put("lang_code", langcode);
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }



    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }
}
