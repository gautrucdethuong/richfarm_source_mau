package com.infowindtech.eguru;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.infowindtech.eguru.StudentPanel.Model.TeacherInfo;
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.TeacherPanel.Fragments.Personal_Registration_Fragment;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.FragFirstrunBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FirstRunClass extends Fragment{
    FragFirstrunBinding binding;
    ArrayList<String> teachlist,subidlist,classlist;
    String message,user_id,langcode,str_sub,str_sub_ids,strfromArrayList,strfromArrayList1,classnamestr,classidstr,morning;
    ArrayList<TeacherInfo> teacherInfos;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin,savelang;
    private SharedPreferences.Editor loginPrefsEditor;
    UserSessionManager userSessionManager;
    String token;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragFirstrunBinding.inflate(inflater,container,false);
        userSessionManager = new UserSessionManager(getActivity());
        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        savelang=loginPreferences.getBoolean("saveLang", false);
        loginPrefsEditor = loginPreferences.edit();
        binding.tvChoseLang.setText("Would you like to choose");
        binding.english.setText(getString(R.string.student));
        binding.arabic.setText(getString(R.string.teacher));
        String language= Locale.getDefault().getDisplayLanguage();
        if(language.equals("English")){
            langcode="en";
        }else {
            langcode="ar";
        }


        token = FirebaseInstanceId.getInstance().getToken();
        Log.d("CCDDDDDD", "onCreate: "+token);

        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.english.isChecked()){
                    getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit()
                            .putBoolean("isFirstRun", false)
                            .apply();
                    LoginTask();
                }else if(binding.arabic.isChecked()){
                    getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit()
                            .putBoolean("isFirstRun", false)
                            .apply();
                    Intent in=new Intent(getActivity(),Personal_Registration_Fragment.class);
                    in.putExtra("go","teacher");
                    startActivity(in);
                }else {
                    Toast.makeText(getActivity(), "Please choose one option.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return binding.getRoot();
    }
    public void LoginTask(){
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.LoginURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        Log.d("RRRRR", "runfirst: response"+response);
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        teacherInfos=new ArrayList<>();
                        teachlist=new ArrayList<>();
                        subidlist=new ArrayList<>();
                        classlist=new ArrayList<>();
                        try {
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                JSONObject obj = jsonObject.getJSONObject("data");
                                user_id = obj.getString("user_id");
                                final String user_type = obj.getString("user_type");
                                final String username1 = obj.getString("username");
                                final String email = obj.getString("email");
                                final String full_name = obj.getString("full_name");
                                final String contact_number = obj.getString("contact_number");
                                final String address = obj.getString("address");
                                final String city = obj.getString("city");
                                final String country = obj.getString("country");
                                final String zipcode = obj.getString("zipcode");
                                final JSONArray classes = obj.getJSONArray("class");
                                final String subject = obj.getString("subject");
                                final String profile = obj.getString("profile_image");
                                JSONArray jsonArray=obj.getJSONArray("subject_array");
                                final String institute=obj.getString("institute_name");
                                final String education=obj.getString("qualification");
                                final String exp=obj.getString("teacher_experince");
                                final String appointment_dur=obj.getString("duration");
                                final String im_id_copy=obj.getString("identity_card");
                                final String im_certificate=obj.getString("certificate");
                                final String morning=obj.getString("availability_morning");
                                final String afternoon=obj.getString("availability_afternoon");
                                final String evening=obj.getString("availability_evening");
                                final String days=obj.getString("availability_days");
                                final String fees=obj.getString("teacher_fees");
                                final String timeslote=morning+","+afternoon+","+evening;
                                final String techer_gender_choice=obj.getString("teaching_gender_choice_id");
                                final String latitude=obj.getString("latitude");
                                final String longitude=obj.getString("longitude");
                                final String date_of_birth=obj.getString("date_of_birth");
                                final String gender=obj.getString("gender");
                                final String passingyear=obj.getString("year_of_passing");
                                final String teacherplaceid=obj.getString("teaching_place_id");
                                final String tagline=obj.getString("user_tagline");
                                final String nation=obj.getString("nationality");
                                final String currency_code=obj.getString("teacher_currency_code");
                                final String paypal_email=obj.getString("paypal_email");
                                final String payment_method_id=obj.getString("payment_method_id");
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
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    String sub_id=jsonObject1.getString("id");
                                    String sub_name=jsonObject1.getString("subject");
                                    TeacherInfo ts=new TeacherInfo(sub_id,sub_name);
                                    teacherInfos.add(ts);
                                    TeacherInfo subinfo=teacherInfos.get(i);
                                    String subjects=subinfo.getValue();
                                    String subjects_id=subinfo.getAttribute();
                                    teachlist.add(subjects);
                                    subidlist.add(subjects_id);
                                }
                                String str_data=teachlist.toString();
                                str_sub= str_data.substring(1,str_data.length()-1);
                                String str_sub_id=subidlist.toString();
                                str_sub_ids= str_sub_id.substring(1,str_sub_id.length()-1);
                                JSONArray teaching_place = obj.getJSONArray("teaching_place");
                                ArrayList<String> list=new ArrayList<>();
                                for(int i=0;i<teaching_place.length();i++){
                                    JSONObject placejson=teaching_place.getJSONObject(i);
                                    String place=placejson.getString("Place");
                                    list.add(place);
                                }
                                StringBuilder sb = new StringBuilder();
                                for(String str : list){
                                    sb.append(str).append(",");
                                }
                                strfromArrayList = sb.toString();
                                if (strfromArrayList.endsWith(",")) {
                                    strfromArrayList = strfromArrayList.substring(0,strfromArrayList.length() - 1);
                                }
                                JSONArray genderchoicearray=obj.getJSONArray("teacher_gender_choice");
                                ArrayList<String> genderlist=new ArrayList<>();
                                for(int i=0;i<genderchoicearray.length();i++){
                                    JSONObject placejson=genderchoicearray.getJSONObject(i);
                                    String choice=placejson.getString("Gender");
                                    genderlist.add(choice);
                                }
                                StringBuilder sb1 = new StringBuilder();
                                for(String str : genderlist){
                                    sb1.append(str).append(",");
                                }
                                strfromArrayList1 = sb1.toString();
                                if (strfromArrayList1.endsWith(",")) {
                                    strfromArrayList1 = strfromArrayList1.substring(0,strfromArrayList1.length() - 1);
                                }
                                if(user_type.equals("2")){
                                    userSessionManager.createUserLoginSession(user_id, user_type, username1, email, contact_number, full_name, address, city, country, zipcode,str_sub, classnamestr,profile,subject,institute,education,exp,timeslote,appointment_dur,im_id_copy,im_certificate,morning,afternoon
                                            ,evening,days,strfromArrayList,fees,techer_gender_choice,latitude,longitude,date_of_birth,gender,passingyear,teacherplaceid,tagline,strfromArrayList1,classidstr,nation,"1",currency_code,paypal_email,payment_method_id);
                                    userSessionManager.createHomeScreen("0");
                                    loginPrefsEditor.putBoolean("saveLogin", true);
                                    loginPrefsEditor.putString("mobile_no","default");
                                    loginPrefsEditor.putString("password", "default@123");
                                    loginPrefsEditor.commit();
                                    Intent in = new Intent(getActivity(),StudentHomeActivity.class);
                                    in.putExtra("count","0");
                                    startActivity(in);
                                }
                            }else if (flag.equals("false")){
                                AlertClass.alertDialogShow(getActivity(),msg);
                            }
                        } catch (Exception e) {
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
                params.put("email_user_name","default");
                params.put("password","default@123");
                params.put("lang_code",langcode);
                params.put("device_token",token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
}
