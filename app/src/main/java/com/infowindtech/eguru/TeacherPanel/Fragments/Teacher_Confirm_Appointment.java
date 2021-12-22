package com.infowindtech.eguru.TeacherPanel.Fragments;


import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

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
import com.infowindtech.eguru.StudentPanel.Model.ClassInfoData;
import com.infowindtech.eguru.TeacherPanel.Adapters.TeacherConfirmAdapter;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.FragAppointmentListBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Teacher_Confirm_Appointment extends Fragment {
    FragAppointmentListBinding binding;
    ArrayList<ClassInfoData> list;
    TeacherConfirmAdapter adapter;
    String message, user_id, last_id, appointment_id, langcode;
    UserSessionManager userSessionManager;
    HashMap<String, String> user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragAppointmentListBinding.inflate(inflater, container, false);
        userSessionManager = new UserSessionManager(getActivity());
        if (userSessionManager.signIn())
            getActivity().finish();
        user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(), langcode);
        last_id = "";
        list = new ArrayList<>();
        list.clear();
        Appointmentlist();
        if (list.size() == 0) {
            binding.svListener.setVisibility(View.GONE);
            binding.tvNoclass.setVisibility(View.VISIBLE);
        }
        binding.svListener.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = binding.svListener.getText().toString().toLowerCase(Locale.getDefault());
                if (!text.equals("")) {
                    adapter.filter(text);
                }
            }
        });

        binding.recycleview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == scrollState) {
                    if (binding.recycleview.getLastVisiblePosition() == adapter.getCount() - 1) {
                        user_id = user.get(userSessionManager.KEY_USERID);
                        last_id = appointment_id;
                        Appointmentlist();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });


        //setUserVisibleHint(true);
        return binding.getRoot();
    }

    public void Appointmentlist() {
        final ProgressDialog dialogCom = new ProgressDialog(getActivity());
        dialogCom.setMessage(getActivity().getString(R.string.pleasewait));
        dialogCom.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.UnconfirmConfirmHistoryURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialogCom.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "teachercomfirmappointment: response" + response);

                            if(last_id.equals(""))
                            {
                                list.clear();
                            }
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            if (flag.equals("true")) {
                                binding.tvNoclass.setVisibility(View.GONE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                binding.svListener.setVisibility(View.VISIBLE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String user_id = object.getString("user_id");
                                    appointment_id = object.getString("appointment_id");
                                    String username = object.getString("username");
                                    String fullname = object.getString("full_name");
                                    String date_of_appointment = object.getString("date_of_apt");
                                    String timeappointment = object.getString("time_apt");
                                    String status = object.getString("appointment_status");
                                    String pay_status = object.getString("payment_status");
                                    String profile_image = object.getString("profile_image");
                                    String room_id = object.getString("room_id");

                                    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                    SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                    Date oneWayTripDate = input.parse(date_of_appointment);
                                    String new_order_date = output.format(oneWayTripDate);
                                    Log.d("SSSSSS", "onResponse: " + status + "   " + pay_status);
                                    ClassInfoData cd = new ClassInfoData();
                                    cd.setId(user_id);
                                    cd.setUser_pic(profile_image);
                                    cd.setSubject(fullname);
                                    cd.setDate(new_order_date);
                                    cd.setTime(timeappointment);
                                    cd.setAppointid(appointment_id);
                                    cd.setUsername(username);
                                    cd.setStatus(status);
                                    cd.setPayStatus(pay_status);
                                    cd.setRoom_id(room_id);

                                    list.add(cd);
                                    adapter = new TeacherConfirmAdapter(list, getActivity(), Teacher_Confirm_Appointment.this);
                                    adapter.notifyDataSetChanged();
                                    binding.recycleview.setAdapter(adapter);
                                    if (list.size() >= 10) {
                                        binding.recycleview.setSelection(list.size() - 10);
                                    }
                                }
                                last_id = appointment_id;
                            } else if (flag.equals("false")) {
                                dialogCom.dismiss();
                            }
                        } catch (Exception e) {
                            dialogCom.dismiss();
                            Log.d("RRRRR", "teachercomfirmappointment: exception" + e);
                            binding.svListener.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialogCom.dismiss();
                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(), message);
                        } else if (error instanceof ServerError) {
                            message = getString(R.string.servererror);
                            AlertClass.DispToast(getActivity(), message);
                        } else if (error instanceof AuthFailureError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(), message);
                        } else if (error instanceof ParseError) {
                            message = getString(R.string.parsingerror);
                            AlertClass.DispToast(getActivity(), message);
                        } else if (error instanceof NoConnectionError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(), message);
                        } else if (error instanceof TimeoutError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(), message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("teacher_id", user_id);
                params.put("status", "Confirm");
                params.put("last_id", last_id);
                params.put("lang_code", langcode);
                Log.d("teachercomfirm", "getParams: "+params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

}
