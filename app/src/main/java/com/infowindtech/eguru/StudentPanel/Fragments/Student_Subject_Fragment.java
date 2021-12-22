package com.infowindtech.eguru.StudentPanel.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Adapter.Student_Subject_Adapter;
import com.infowindtech.eguru.StudentPanel.Model.SubjectData;
import com.infowindtech.eguru.TeacherPanel.Fragments.Admin_Ads_Fragment;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.FragStudentSubject1Binding;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Student_Subject_Fragment extends Fragment{
    FragStudentSubject1Binding binding;
    String message,subject_id,subject_name;
    ArrayList<SubjectData> subjectlist;
    Student_Subject_Adapter subjectAdapter;
    String user_id,city,Client_id,ads_image,ads_url,langcode;
    UserSessionManager userSessionManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragStudentSubject1Binding.inflate(inflater,container,false);
        subject_id=getArguments().getString("subjectid");
        subject_name=getArguments().getString("subjectname");
        userSessionManager = new UserSessionManager(getActivity());
        HashMap<String,String> lang=userSessionManager.getLanguage();
        langcode=lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(),langcode);
        GetSubjectTask();
        binding.tvHeading.setText(subject_name);
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertClass.hideKeybord(getActivity(),view);
                FragmentManager fm=getFragmentManager();
                fm.popBackStack();
            }
        });



        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String text =binding.etSearch.getText().toString().toLowerCase(Locale.getDefault());
                subjectAdapter.filter(text);
            }
        });
        AdminAdsTask();
        binding.imAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Admin_Ads_Fragment frag=new Admin_Ads_Fragment();
                Bundle bundle=new Bundle();
                bundle.putString("ad_url",ads_url);
                frag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.subject, frag).commit();
            }
        });
        return binding.getRoot();
    }
    public void GetSubjectTask() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.SubSubjectURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        subjectlist=new ArrayList<>();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRRR", "studentsubjectfragment  :  onResponse: "+response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject obj=jsonArray.getJSONObject(i);
                                    String subject_id=obj.getString("Subject_id");
                                    String subject=obj.getString("Sub_subject");
                                    String Subject_image=obj.getString("Subject_image");
                                    if(Subject_image.equals("")){
                                     Subject_image="http://img.freepik.com/free-icon/blank-opened-book-pages_318-69387.jpg?size=338c&ext=jpg";
                                        SubjectData subj8=new SubjectData(subject,subject_id,Subject_image);
                                        subjectlist.add(subj8);
                                        subjectAdapter=new Student_Subject_Adapter(subjectlist,getActivity());
                                        binding.gvSubjects.setAdapter(subjectAdapter);
                                    }else {
                                        SubjectData subj8=new SubjectData(subject,subject_id,Subject_image);
                                        subjectlist.add(subj8);
                                        subjectAdapter=new Student_Subject_Adapter(subjectlist,getActivity());
                                        binding.gvSubjects.setAdapter(subjectAdapter);
                                    }

                                }
                            }else if (flag.equals("false")){
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                alertDialog.setMessage(getString(R.string.subjectnotavailable));
                                alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FragmentManager fm=getFragmentManager();
                                        fm.popBackStack();
                                        dialogInterface.cancel();
                                    }
                                });
                                alertDialog.show();
                            }
                        } catch (Exception e) {
                            Log.d("RRRRRR", "studentsubjectfragment  :  exception: "+e);
                            AlertClass.alertDialogShow(getActivity(),e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Log.d("RRRRRR", "studentsubjectfragment  :  error: "+error);
                        if (error instanceof NetworkError) {
                            message =getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(),message);
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parent_subject",subject_id);
                params.put("lang_code",langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
    public void AdminAdsTask(){
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.AdminAdsURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRRR", "studentsubjectfragment  :  onResponse: "+response);
                            jsonObject = new JSONObject(response);
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
                        } catch (Exception e) {
                            Log.d("RRRRRR", "studentsubjectfragment  :  exception: "+e);
                           //Snackbar.make(getView(),R.string.unableshowads,Snackbar.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Snackbar.make(getView(),R.string.unableshowads,Snackbar.LENGTH_SHORT).show();
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

