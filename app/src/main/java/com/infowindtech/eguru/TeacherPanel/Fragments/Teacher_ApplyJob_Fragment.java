package com.infowindtech.eguru.TeacherPanel.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.infowindtech.eguru.MainActivity;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Model.ClassInfoData;
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.TeacherPanel.Adapters.TeacherApplyJobAdapter;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.FragApplyJob1Binding;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Teacher_ApplyJob_Fragment extends Fragment{
    FragApplyJob1Binding binding;
    ArrayList<ClassInfoData> list;
    TeacherApplyJobAdapter adapter;
    UserSessionManager userSessionManager;
    String user_id,subject_id,city,langcode,active_status;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragApplyJob1Binding.inflate(inflater,container,false);
        userSessionManager = new UserSessionManager(getActivity());
        if (userSessionManager.signIn())
            getActivity().finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id=user.get(userSessionManager.KEY_USERID);
        subject_id=user.get(userSessionManager.KEY_SubId);
        city=user.get(userSessionManager.KEY_CITY);
        active_status=user.get(userSessionManager.KEY_Activation);
        HashMap<String,String> lang=userSessionManager.getLanguage();
        langcode=lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(),langcode);
        binding.tvHeading.setText(getString(R.string.Jobs));
        binding.tvNoclass.setVisibility(View.VISIBLE);

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


        if(active_status.equals("2")){
           // AlertClass.alertDialogShow(getActivity(),"Please Complete Your Profile");
        }else if(active_status.equals("1")){
            ApplyJoblistTask();
        }
        String androidId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(androidId)
                .build();
        binding.adView.loadAd(adRequest);
        binding.adView.setAdListener(new AdListener(){
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
    private void ApplyJoblistTask(){
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.pleasewait));
        pDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.JobSearchURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null !=response) {
                                Log.d("RRRRR", "teacherapplyjobfragment  job : response"+response);
                                pDialog.dismiss();
                                list=new ArrayList<>();
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String str_msg=obj.getString("message");
                                if (flag.equals("true")){
                                    binding.tvNoclass.setVisibility(View.GONE);
                                    JSONArray jsonArray=obj.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String user_id=object.getString("user_id");
                                            if(user_id.equals("0")){
                                            }else {
                                                String fullname = object.getString("full_name");
                                                String profile_image = object.getString("profile_image");
                                                String address = object.getString("address");
                                                String email = object.getString("email");
                                                ClassInfoData cd = new ClassInfoData();
                                                cd.setId(user_id);
                                                cd.setUser_pic(profile_image);
                                                cd.setSubject(fullname);
                                                cd.setDate(address);
                                                cd.setTime(email);
                                                list.add(cd);
                                                adapter=new TeacherApplyJobAdapter(list,getActivity());
                                                adapter.notifyDataSetChanged();
                                                binding.recycleview.setAdapter(adapter);
                                            }
                                    }
                                }else if (flag.equals("false")){

                                    pDialog.dismiss();
                                    binding.tvNoclass.setVisibility(View.VISIBLE);

                                }
                            }
                        }
                        catch(Exception e){
                            //AlertClass.alertDialogShow(getActivity(),e.getMessage());
                            Log.d("RRRRR", "teacherapplyjobfragment  job : exception"+e);
                            pDialog.hide();
                            binding.tvNoclass.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("RRRRR", "teacherapplyjobfragment: error"+error);
                        Toast.makeText(getActivity(),"volley error", Toast.LENGTH_LONG).show();
                        pDialog.hide();
                        binding.tvNoclass.setVisibility(View.VISIBLE);
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("teacher_id",user_id);
                params.put("subject_id",subject_id);
                params.put("city",city);
                params.put("lang_code",langcode);
                Log.d("RRRRR", "getParams: "+params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);

    }
}

