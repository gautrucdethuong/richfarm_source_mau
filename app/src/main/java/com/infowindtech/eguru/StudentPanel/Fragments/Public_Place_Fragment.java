package com.infowindtech.eguru.StudentPanel.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.infowindtech.eguru.StudentPanel.Adapter.TeacherlistdataAdapter;
import com.infowindtech.eguru.StudentPanel.Model.TeacherlistData;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.FragPublicplacelistBinding;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Public_Place_Fragment extends Fragment{
    FragPublicplacelistBinding binding;
    String message,user_id,userlat,userlong;
    ArrayList<TeacherlistData> list;
    TeacherlistdataAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragPublicplacelistBinding.inflate(inflater,container,false);
        userlong=getArguments().getString("longitude");
        userlat=getArguments().getString("latitude");
        binding.header.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm=getFragmentManager();
                fm.popBackStack();
            }
        });



        binding.header.tvHeading.setText(getString(R.string.teacherlist));
        GetTeacherlocationTask();
        return  binding.getRoot();
    }
    public void GetTeacherlocationTask() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherMarkerLocationURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        list=new ArrayList<>();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRRR", "publicplacefragment  :  onResponse: "+response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    user_id = obj.getString("user_id");
                                    String name = obj.getString("full_name");
                                    String teacher_fees = obj.getString("teacher_fees");
                                    String rating = obj.getString("rating_total");
                                    String profile_image = obj.getString("profile_image");
                                    list.add(new TeacherlistData(user_id,"", name,"","", profile_image, teacher_fees, rating,"",""));
                                    adapter = new TeacherlistdataAdapter(list, getActivity());
                                    binding.lvPlaces.setAdapter(adapter);
                                }

                            }else if (flag.equals("false")){
                                AlertClass.alertDialogShow(getActivity(),msg);
                            }
                        }catch (Exception e) {
                            Log.d("RRRRRR", "publicplacefragment  :  exception: "+e);
                            AlertClass.alertDialogShow(getActivity(),e.getMessage());
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
                params.put("longitude",userlong);
                params.put("latitude",userlat);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
}
