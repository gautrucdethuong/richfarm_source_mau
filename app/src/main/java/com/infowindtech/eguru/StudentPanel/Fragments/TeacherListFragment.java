package com.infowindtech.eguru.StudentPanel.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;

import com.infowindtech.eguru.StudentPanel.Adapter.TeacherlistdataAdapter;
import com.infowindtech.eguru.StudentPanel.Model.TeacherlistData;

import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.FragParticularsubjteacherList1Binding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherListFragment extends Fragment {
    FragParticularsubjteacherList1Binding binding;
    String message, subject_id, lastid, user_id, subjectid, city, langcode;
    ArrayList<TeacherlistData> list;
    TeacherlistdataAdapter adapter;
    UserSessionManager userSessionManager;
    Snackbar bar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragParticularsubjteacherList1Binding.inflate(inflater, container, false);
        subject_id = getArguments().getString("subjectid");
        userSessionManager = new UserSessionManager(getActivity());
        if (userSessionManager.signIn())
            getActivity().finish();



        final HashMap<String, String> user = userSessionManager.getUserDetails();
        city = user.get(userSessionManager.KEY_CITY);
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(), langcode);
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        });
        binding.tvHeading.setText(getString(R.string.teacherlist));
        list = new ArrayList<>();
        subjectid = subject_id;
        lastid = "";

        Log.d("SSSSS", "onResponse: "+subjectid);
        GetTeacherlistTask();
        binding.lvTeachers.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == scrollState) {
                    if (binding.lvTeachers.getLastVisiblePosition() == adapter.getCount() - 1) {
                        subjectid = subject_id;
                        lastid = user_id;
                        GetTeacherlistTask();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        return binding.getRoot();
    }

    public void GetTeacherlistTask() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherListFilterbystudentURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRRR", "teacherlistfragment  :  onResponse: "+response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                if (jsonArray.length() == 0) {
                                    AlertClass.alertDialogShow(getActivity(), getString(R.string.noteacheravailable));
                                } else {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        user_id = obj.getString("user_id");
                                        String username = obj.getString("username");
                                        String name = obj.getString("full_name");
                                        String email = obj.getString("email");
                                        String contact_number = obj.getString("contact_number");
                                        String teacher_fees = obj.getString("teacher_fees");
                                        String rating = obj.getString("rating");
                                        String profile_image = obj.getString("profile_image");
                                        String tagline = obj.getString("user_tagline");
                                        String teacher_quality = obj.getString("user_teacher_quality");
                                        list.add(new TeacherlistData(user_id,username, name, email, contact_number, profile_image, teacher_fees, rating, tagline, teacher_quality));
                                        adapter = new TeacherlistdataAdapter(list, getActivity());
                                        binding.lvTeachers.setAdapter(adapter);
                                        if (list.size() >= 10) {
                                            binding.lvTeachers.setSelection(list.size() - 10);
                                        }
                                    }
                                    lastid = user_id;
                                }
                            } else if (flag.equals("false")) {
                                if (lastid.equals("")) {
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                    binding.tvNoclass.setVisibility(View.VISIBLE);
                                } else if (list.size() != 0) {
                                    binding.tvNoclass.setVisibility(View.GONE);
                                    //Toast.makeText(getActivity(), "No More Items.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRRR", "teacherlistfragment  :  exception: "+e);
                            // AlertClass.alertDialogShow(getActivity(), e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRRR", "teacherlistfragment  :  error: "+error);
                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(), message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("subject_id", subject_id);
                params.put("last_id", lastid);
                params.put("city", city);
                params.put("lang_code", langcode);
                Log.d("TAG", "getParams: "+params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
}
