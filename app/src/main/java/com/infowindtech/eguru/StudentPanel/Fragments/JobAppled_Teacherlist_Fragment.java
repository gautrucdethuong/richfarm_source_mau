package com.infowindtech.eguru.StudentPanel.Fragments;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

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
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.FragJobAppliedTeacherlist1Binding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JobAppled_Teacherlist_Fragment extends Fragment {
    FragJobAppliedTeacherlist1Binding binding;
    String message, lastid, user_id, jobid, city, langcode, active_status;
    ArrayList<TeacherlistData> list;
    TeacherlistdataAdapter adapter;
    UserSessionManager userSessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragJobAppliedTeacherlist1Binding.inflate(inflater, container, false);
        binding.tvHeading.setText(getString(R.string.Suggestedteachers));
        userSessionManager = new UserSessionManager(getActivity());
        if (userSessionManager.signIn())
            getActivity().finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        city = user.get(userSessionManager.KEY_CITY);
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(), langcode);
        jobid = "";
        binding.tvNoclass.setVisibility(View.VISIBLE);
        GetTeacherlistTask();

        binding.imDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StudentHomeActivity) getActivity()).callDrawer();
            }
        });

        binding.lvTeachers.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == scrollState) {
                    if (binding.lvTeachers.getLastVisiblePosition() == adapter.getCount() - 1) {
                        user_id = user.get(userSessionManager.KEY_USERID);
                        jobid = lastid;
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
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.JobAppliedTeacherListURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        list = new ArrayList<>();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            Log.d("RRRRRR", "jobappliedteacherlistfragment  :  response" + response);
                            if (flag.equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                binding.tvNoclass.setVisibility(View.GONE);
                                if (jsonArray.length() == 0) {
                                    AlertClass.alertDialogShow(getActivity(), "Teacher Not Available");
                                } else {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        lastid = obj.getString("teacher_job_apply_id");
                                        String user_id = obj.getString("user_id");
                                        String username = obj.getString("username");
                                        String name = obj.getString("full_name");
                                        String contact_number = obj.getString("contact_number");
                                        String teacher_fees = obj.getString("teacher_fees");
                                        String rating = obj.getString("rating");
                                        String profile_image = obj.getString("profile_image");
                                        String teacher_quality = obj.getString("user_teacher_quality");
                                        list.add(new TeacherlistData(user_id,username, name, "", contact_number, profile_image, teacher_fees, rating, "", teacher_quality));
                                        adapter = new TeacherlistdataAdapter(list, getActivity());
                                        binding.lvTeachers.setAdapter(adapter);
                                        if (list.size() >= 10) {
                                            binding.lvTeachers.setSelection(list.size() - 10);
                                        }
                                    }
                                    jobid = lastid;
                                }
                            } else if (flag.equals("false")) {
                                //AlertClass.alertDialogShow(getActivity(),getString(R.string.noteacheravailable));
                                //binding.tvNoclass.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            Log.d("RRRRRR", "jobappliedteacherlistfragment   :  exception" + e);
                            //AlertClass.alertDialogShow(getActivity(),e.getMessage());
                            //binding.tvNoclass.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRRR", "jobappliedteacherlistfragment :  error" + error);
                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(), message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("student_id", user_id);
                params.put("last_id", jobid);
                params.put("city", city);
                params.put("lang_code", langcode);
                Log.d("print_params", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
}
