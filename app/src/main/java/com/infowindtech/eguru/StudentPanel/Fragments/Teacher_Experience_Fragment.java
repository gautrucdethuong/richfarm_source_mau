package com.infowindtech.eguru.StudentPanel.Fragments;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Adapter.TeacherInfoAdapter;
import com.infowindtech.eguru.StudentPanel.Model.TeacherInfo;
import com.infowindtech.eguru.StudentPanel.Model.TeacherUserIdSession;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.FragExpTeacherBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Teacher_Experience_Fragment extends Fragment{
    FragExpTeacherBinding binding;
    ArrayList<TeacherInfo> infolist;
    TeacherInfoAdapter adapter;
    String user_id,message,langcode;
    TeacherUserIdSession teacherUserIdSession;
    UserSessionManager userSessionManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragExpTeacherBinding.inflate(inflater,container,false);
        teacherUserIdSession = new TeacherUserIdSession(getActivity());
        final HashMap<String, String> user = teacherUserIdSession.getUserDetails();
        user_id=user.get(teacherUserIdSession.KEY_USERID);
        userSessionManager = new UserSessionManager(getActivity());
        HashMap<String,String> lang=userSessionManager.getLanguage();
        langcode=lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(),langcode);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        binding.lvInfo.setLayoutManager(MyLayoutManager);
        binding.lvInfo.setItemAnimator(new DefaultItemAnimator());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        MyLayoutManager.setAutoMeasureEnabled(true);
        binding.lvInfo.setNestedScrollingEnabled(false);
        binding.lvInfo.setHasFixedSize(false);
        binding.tvNoexp.setVisibility(View.VISIBLE);
        GetteacherinfoTask();



        return binding.getRoot();
    }
    public void GetteacherinfoTask() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherProfileURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        infolist=new ArrayList<>();
                        try {
                            Log.d("RRRRRR", "teacherexperiencefragment  :  onResponse: "+response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            JSONObject obj=jsonObject.getJSONObject("data");
                            if (flag.equals("true")){
                                final String experience = obj.getString("teacher_experince");
                                if(experience.equals("")){
                                    binding.lvInfo.setVisibility(View.GONE);
                                    binding.tvNoexp.setVisibility(View.VISIBLE);
                                }else{
                                    binding.lvInfo.setVisibility(View.VISIBLE);
                                    binding.tvNoexp.setVisibility(View.GONE);
                                    infolist.add(new TeacherInfo(getString(R.string.Experience),experience));
                                    adapter=new TeacherInfoAdapter(infolist,getActivity());
                                    binding.lvInfo.setAdapter(adapter);
                                }
                            }else if (flag.equals("false")){
                                AlertClass.alertDialogShow(getActivity(),msg);
                                binding.tvNoexp.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception e) {
                            Log.d("RRRRRR", "studentsubjectfragment  :  exception: "+e);
                            AlertClass.alertDialogShow(getActivity(),e.getMessage());
                            binding.tvNoexp.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof NetworkError) {
                            message =getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(),message);
                        } else if (error instanceof ServerError) {
                            message =getString(R.string.servererror);
                            AlertClass.DispToast(getActivity(),message);
                        } else if (error instanceof AuthFailureError) {
                            message =getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(),message);
                        } else if (error instanceof ParseError) {
                            message =getString(R.string.parsingerror);
                            AlertClass.DispToast(getActivity(),message);
                        } else if (error instanceof NoConnectionError) {
                            message =getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(),message);
                        } else if (error instanceof TimeoutError) {
                            message =getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(),message);
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id);
                params.put("lang_code",langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
}
