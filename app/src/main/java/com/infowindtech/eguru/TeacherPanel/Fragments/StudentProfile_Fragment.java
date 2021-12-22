package com.infowindtech.eguru.TeacherPanel.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.FirebaseChat.FirebaseChatMessage;
import com.infowindtech.eguru.MainActivity;
import com.infowindtech.eguru.MineVolleyGlobal;
import com.infowindtech.eguru.MineVolleyListener;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.FragTeachersideuserprofile1Binding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentProfile_Fragment extends Fragment {
    FragTeachersideuserprofile1Binding binding;
    String message,student_id,str_usertype,from,full_name,profile,appointmentid;
    UserSessionManager userSessionManager;
    String user_id,classnamestr,classidstr,appointcount,langcode;
    ArrayList<String> classlist;
    String username1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= FragTeachersideuserprofile1Binding.inflate(inflater,container,false);
        student_id=getArguments().getString("studentid");
        from=getArguments().getString("from");
        appointmentid=getArguments().getString("appointment");
        userSessionManager = new UserSessionManager(getActivity());
        if (userSessionManager.signIn())
            getActivity().finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id=user.get(userSessionManager.KEY_USERID);
        str_usertype = user.get(userSessionManager.KEY_USERTYPE);
        HashMap<String,String> lang=userSessionManager.getLanguage();
        langcode=lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(),langcode);
        final HashMap<String, String> appointment = userSessionManager.getHomeScreen();
        appointcount=appointment.get(userSessionManager.KEY_HomeScreenCount);
        if(from.equals("Applyjob")){
            student_id=getArguments().getString("studentid");
            appointmentid=getArguments().getString("appointment");
            binding.placeLayout.setVisibility(View.GONE);
            binding.tvApply.setVisibility(View.VISIBLE);
            binding.tvCancel.setVisibility(View.VISIBLE);
            StudentInfoTask();
        }else if(from.equals("Appliedjob")){
            student_id=getArguments().getString("studentid");
            binding.placeLayout.setVisibility(View.GONE);
            StudentwithoutappointmentInfoTask();
            binding.tvApply.setVisibility(View.GONE);
            binding.tvCancel.setVisibility(View.GONE);
        }else if(from.equals("Appointment")){
            student_id=getArguments().getString("studentid");
            appointmentid=getArguments().getString("appointment");
            StudentInfoTask();
            binding.tvApply.setVisibility(View.GONE);
            binding.tvCancel.setVisibility(View.GONE);
        }
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm=getFragmentManager();
                fm.popBackStack();
            }
        });
        binding.tvHeading.setText(getString(R.string.student));
        binding.tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage(getString(R.string.areinterested));
                alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ApplyJobTask();
                    }
                });
                alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      dialogInterface.cancel();
                    }
                });
                alertDialog.show();

            }
        });
        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage(getString(R.string.arenotinterested));
                alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      CancelJobTask();
                    }
                });
                alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alertDialog.show();

            }
        });
        binding.imChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("RRRR", "onVolleyResponse: "+user_id+"  "+student_id+"  "+str_usertype);
                if (str_usertype.equals("1")) {

                    callAPII(user_id,student_id);

                } else {
                    callAPII(student_id,user_id);
                }

            }
        });
        return binding.getRoot();
    }


    public void callAPII(String fromid,String toid){
        HashMap<String,String> map=new HashMap<>();
        map.put("user_from_id",fromid);
        map.put("user_to_id",toid);
        Log.d("RRRR", "onVolleyResponse: " +map);

        new MineVolleyGlobal(getContext()).parseVollyStringRequest(ServiceClass.ChatConnection, 1, map, new MineVolleyListener() {
            @Override
            public void onVolleyResponse(String response) {
                Log.d("RRRR", "onVolleyResponse: "+response);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    String flag = obj.getString("status");
                    String msg = obj.getString("message");
                    String chatId = obj.getString("chat_id");
                    Log.d("RRRR", "RRRR: "+chatId);
                    String room_id = chatId ;
                    Log.d("RRRR", "RRRR: "+room_id);


                    Intent intent = new Intent(getContext(), FirebaseChatMessage.class);
                    intent.putExtra("sender_id", student_id);
                    intent.putExtra("sender_name", full_name);
                    intent.putExtra("sender_username", username1);
                    intent.putExtra("sender_pic", profile);
                    intent.putExtra("room_id", room_id);
                    startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }




    public void StudentInfoTask(){
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.StudentProfileURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        classlist=new ArrayList<>();
                        try {
                            Log.d("RRRR", "studentprofilefragment  :  onResponse: "+response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                JSONObject obj = jsonObject.getJSONObject("data");
                                    final String user_id = obj.getString("user_id");
                                    final String user_type = obj.getString("user_type");
                                    username1 = obj.getString("username");
                                    final String email = obj.getString("email");
                                    full_name = obj.getString("full_name");
                                    final String contact_number = obj.getString("contact_number");
                                    final String address = obj.getString("address");
                                    final String city = obj.getString("city");
                                    final String country = obj.getString("country");
                                    final String zipcode = obj.getString("zipcode");
                                    final String studentplace = obj.getString("student_place");
                                    final String subject = obj.getString("subject_name");
                                     profile = obj.getString("profile_image");
                                    final String nation=obj.getString("nationality");
                                    final JSONArray classes = obj.getJSONArray("class");
                                    ArrayList<String> classidlist=new ArrayList<>();
                                    for(int j=0;j<classes.length();j++){
                                        JSONObject jsonObject1=classes.getJSONObject(j);
                                        String class_id=jsonObject1.getString("class_id");
                                        String class_name=jsonObject1.getString("class_name");
                                        classlist.add(class_name);
                                        classidlist.add(class_id);
                                    }
                                    StringBuilder classname = new StringBuilder();
                                    for(String str :classlist){
                                        classname.append(str).append(",");
                                    }
                                    classnamestr =classname.toString();
                                    if (classnamestr.endsWith(",")) {
                                        classnamestr = classnamestr.substring(0,classnamestr.length() - 1);
                                    }
                                    StringBuilder classid = new StringBuilder();
                                    for(String str :classidlist){
                                        classid.append(str).append(",");
                                    }
                                    classidstr=classid.toString();
                                    if (classidstr.endsWith(",")) {
                                        classidstr = classidstr.substring(0,classidstr.length() - 1);
                                    }
                                    Glide.with(getActivity()).load(profile)
                                            .thumbnail(0.5f)

                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(binding.imProfile);
                                    binding.etFullname.setText(full_name);
                                    binding.tvName.setText(full_name);
                                    binding.etEmail.setText(email);
                                    binding.etAddress.setText(address+" "+city+" "+country+" "+zipcode);
                                    binding.etType.setText(contact_number);
                                    binding.etSubject.setText(subject);
                                    binding.etClass.setText(classnamestr);
                                    binding.etPlace.setText(studentplace);
                                    binding.etNation.setText(nation);

                            }else if (flag.equals("false")){
                                AlertClass.alertDialogShow(getActivity(),msg);
                            }
                        }catch (Exception e){
                            Log.d("RRRR", "studentprofilefragment  :  exception: "+e);

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
                params.put("user_id",student_id);
                params.put("appointment_id",appointmentid);
                params.put("lang_code",langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
    public void StudentwithoutappointmentInfoTask() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.StudentProfileURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        classlist=new ArrayList<>();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRR", "studentprofilefragment  :  onResponse: "+response);

                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                    JSONObject obj = jsonObject.getJSONObject("data");
                                    final String user_id = obj.getString("user_id");
                                    final String user_type = obj.getString("user_type");
                                    final String username1 = obj.getString("username");
                                    final String email = obj.getString("email");
                                    full_name = obj.getString("full_name");
                                    final String contact_number = obj.getString("contact_number");
                                    final String address = obj.getString("address");
                                    final String city = obj.getString("city");
                                    final String country = obj.getString("country");
                                    final String zipcode = obj.getString("zipcode");
                                    final String strplace = obj.getString("student_place");
                                    final String subject = obj.getString("subject_name");
                                    profile = obj.getString("profile_image");
                                    final String nation=obj.getString("nationality");
                                    final JSONArray classes = obj.getJSONArray("class");
                                    ArrayList<String> classidlist=new ArrayList<>();
                                    for(int j=0;j<classes.length();j++){
                                        JSONObject jsonObject1=classes.getJSONObject(j);
                                        String class_id=jsonObject1.getString("class_id");
                                        String class_name=jsonObject1.getString("class_name");
                                        classlist.add(class_name);
                                        classidlist.add(class_id);
                                    }
                                    StringBuilder classname = new StringBuilder();
                                    for(String str :classlist){
                                        classname.append(str).append(",");
                                    }
                                    classnamestr =classname.toString();
                                    if (classnamestr.endsWith(",")){
                                        classnamestr = classnamestr.substring(0,classnamestr.length() - 1);
                                    }
                                    StringBuilder classid = new StringBuilder();
                                    for(String str :classidlist){
                                        classid.append(str).append(",");
                                    }
                                    classidstr=classid.toString();
                                    if (classidstr.endsWith(",")){
                                        classidstr = classidstr.substring(0,classidstr.length() - 1);
                                    }
                                    Glide.with(getActivity()).load(profile)
                                            .thumbnail(0.5f)

                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(binding.imProfile);
                                    binding.etFullname.setText(full_name);
                                    binding.tvName.setText(full_name);
                                    binding.etEmail.setText(email);
                                    binding.etAddress.setText(address+" "+city+" "+country+" "+zipcode);
                                    binding.etType.setText(contact_number);
                                    binding.etSubject.setText(subject);
                                    binding.etClass.setText(classnamestr);
                                    binding.etNation.setText(nation);

                            }else if (flag.equals("false")){
                                AlertClass.alertDialogShow(getActivity(),msg);
                            }
                        }catch (Exception e) {
                            Log.d("RRRR", "studentprofilefragment  :  exception: "+e);

                            AlertClass.alertDialogShow(getActivity(),e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener(){
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
                params.put("user_id",student_id);
                params.put("lang_code",langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
    public void ApplyJobTask(){
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.InterestedURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRR", "studentprofilefragment  :  onResponse: "+response);

                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                    alertDialog.setMessage(msg);
                                    alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                           if(appointcount.equals("0")){
                                               Intent in=new Intent(getActivity(), MainActivity.class);
                                               in.putExtra("count", appointcount);
                                               startActivity(in);
                                           }else {
                                               Teacher_ApplyJob_Fragment fragment = new Teacher_ApplyJob_Fragment();
                                               Bundle bundle=new Bundle();
                                               bundle.putString("flag","0");
                                               fragment.setArguments(bundle);
                                               getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
                                           }

                                        }
                                    });
                                    alertDialog.show();

                            }else if (flag.equals("false")){
                                AlertClass.alertDialogShow(getActivity(),msg);
                            }
                        }catch (Exception e) {
                            Log.d("RRRR", "studentprofilefragment  :  exception: "+e);
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
                params.put("student_id",student_id);
                params.put("teacher_id",user_id);
                params.put("lang_code",langcode);
                Log.d("studentprofilefragment", "getParams: "+params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
    public void CancelJobTask(){
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.NotInterestedURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRR", "studentprofilefragment  :  onResponse: "+response);

                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                    alertDialog.setMessage(msg);
                                    alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(appointcount.equals("0")){
                                                Intent in=new Intent(getActivity(), MainActivity.class);
                                                in.putExtra("count", appointcount);
                                                startActivity(in);
                                            }else {
                                                Teacher_ApplyJob_Fragment fragment = new Teacher_ApplyJob_Fragment();
                                                Bundle bundle=new Bundle();
                                                bundle.putString("flag","0");
                                                fragment.setArguments(bundle);
                                                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
                                            }
                                        }
                                    });
                                    alertDialog.show();

                            }else if (flag.equals("false")){
                                AlertClass.alertDialogShow(getActivity(),msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "studentprofilefragment  :  exception: "+e);

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
                params.put("student_id",student_id);
                params.put("teacher_id",user_id);
                params.put("lang_code",langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

}
