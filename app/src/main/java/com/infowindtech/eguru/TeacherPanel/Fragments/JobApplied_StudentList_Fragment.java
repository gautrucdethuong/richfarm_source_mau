package com.infowindtech.eguru.TeacherPanel.Fragments;


import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.infowindtech.eguru.MainActivity;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Model.ClassInfoData;
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.TeacherPanel.Adapters.JobAppliedStudentList_Adapter;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.FragAppliedStudent1Binding;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class JobApplied_StudentList_Fragment extends Fragment{
    FragAppliedStudent1Binding binding;
    ArrayList<ClassInfoData> list;
    JobAppliedStudentList_Adapter adapter;
    UserSessionManager userSessionManager;
    String user_id,city,Client_id,ads_image,ads_url,langcode,active_status;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragAppliedStudent1Binding.inflate(inflater, container, false);
        userSessionManager = new UserSessionManager(getActivity());
        if (userSessionManager.signIn())
            getActivity().finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        city = user.get(userSessionManager.KEY_CITY);
        active_status= user.get(userSessionManager.KEY_Activation);
        HashMap<String,String> lang=userSessionManager.getLanguage();
        langcode=lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(),langcode);


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
        binding.tvHeading.setText(getString(R.string.AppliedJobs));
        binding.tvNoclass.setVisibility(View.VISIBLE);
        ApplyJoblistTask();
        AdminAdsTask();
        binding.imAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Admin_Ads_Fragment frag=new Admin_Ads_Fragment();
                Bundle bundle=new Bundle();
                bundle.putString("ad_url",ads_url);
                frag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.applyjob, frag).commit();
            }
        });
        return binding.getRoot();
    }
    private void ApplyJoblistTask(){
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.pleasewait));
        pDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.AppliedJobStudentListURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                pDialog.dismiss();
                                list = new ArrayList<>();
                                JSONObject obj = new JSONObject(response);
                                Log.d("RRRRR", "jobappliedstudentlistfragment   job :  onResponse: "+response);
                                String flag = obj.getString("status").toString();
                                String str_msg = obj.getString("message");
                                if (flag.equals("true")) {
                                    JSONArray jsonArray = obj.getJSONArray("data");
                                    binding.tvNoclass.setVisibility(View.GONE);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String user_id = object.getString("user_id");
                                           // String username = obj.getString("username");
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
                                           // cd.setUsername(username);
                                            list.add(cd);
                                            adapter = new JobAppliedStudentList_Adapter(list, getActivity());
                                            adapter.notifyDataSetChanged();
                                            binding.recycleview.setAdapter(adapter);
                                        }

                                } else if (flag.equals("false")) {
                                    pDialog.dismiss();
                                    binding.tvNoclass.setVisibility(View.VISIBLE);
                                    Toast.makeText(getActivity(), str_msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch (Exception e){
                            pDialog.hide();
                            Log.d("RRRRR", "jobappliedstudentlistfragment  job:  exception: "+e);
                            binding.tvNoclass.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "jobappliedstudentlistfragment job  :  error: "+error);

                        Toast.makeText(getActivity(), "volley error", Toast.LENGTH_LONG).show();
                        pDialog.hide();
                        binding.tvNoclass.setVisibility(View.VISIBLE);
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("teacher_id", user_id);
                params.put("city",city);
                params.put("lang_code",langcode);
                Log.d(TAG, "getParams: "+params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
    public void AdminAdsTask() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.AdminAdsURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try{
                            jsonObject = new JSONObject(response);
                            Log.d("RRRRR", "jobappliedstudentlistfragment  ad:  onResponse: "+response);

                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                             JSONObject jsonObject1=jsonObject.getJSONObject("data");
                             Client_id=jsonObject1.getString("client_id");
                             ads_image=jsonObject1.getString("ad_image");
                             ads_url=jsonObject1.getString("ad_url");
                                Glide.with(getActivity()).load(ads_image)
                                        .thumbnail(0.5f)

                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(binding.imAds);
                                binding.imAds.setBackgroundResource(R.drawable.black_rectagle_border);

                            }else if (flag.equals("false")){
                               // Snackbar.make(getView(),R.string.unableshowads,Snackbar.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Log.d("RRRRR", "jobappliedstudentlistfragment  ad:  exception: "+e);

                          //  Snackbar.make(getView(),R.string.unableshowads,Snackbar.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "jobappliedstudentlistfragment ad :  error: "+error);

                        Toast.makeText(getActivity(), getString(R.string.unableshowads), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("lang_code",langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
}
