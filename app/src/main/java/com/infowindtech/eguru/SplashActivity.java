package com.infowindtech.eguru;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import com.infowindtech.eguru.StudentPanel.Model.TeacherInfo;
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.ActivitySplashBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SplashActivity extends FragmentActivity {
    ActivitySplashBinding binding;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin, savelang;
    private SharedPreferences.Editor loginPrefsEditor;
    UserSessionManager userSessionManager;
    String str_user_name_sh, str_user_password_sh, message, str_sub, str_sub_ids, strfromArrayList, strfromArrayList1, classnamestr, classidstr;
    ArrayList<TeacherInfo> teacherInfos;
    ArrayList<String> teachlist, subidlist, classlist;
    String user_id, appoinmtmentcount, langcode, morning, afternoon, evening, days, language;
    List<String> appointdatelist;
    JSONArray datearray;

    String token;
    FirebaseApp firebaseApp;


    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        userSessionManager = new UserSessionManager(getApplicationContext());
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        savelang = loginPreferences.getBoolean("saveLang", false);
        loginPrefsEditor = loginPreferences.edit();


        FirebaseApp.initializeApp(this);



        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("DEVICETOKEN", "getInstanceId failed", task.getException());
                            return;
                        }
                        token = task.getResult().getToken();

                        Log.d("DEVICETOKEN", token);
                    }
                });



        if (savelang == true) {
            langcode = loginPreferences.getString("lang", "");
            userSessionManager.selectedlanguage(langcode);
            AlertClass.setLocale(SplashActivity.this, langcode);
        } else {
            langcode = "en";
            userSessionManager.selectedlanguage("en");
            AlertClass.setLocale(SplashActivity.this, langcode);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (saveLogin == true) {
                    str_user_name_sh = loginPreferences.getString("mobile_no", "");
                    str_user_password_sh = loginPreferences.getString("password", "");
                    LoginTask();
                } else {
                    LoginScreen();
                }
            }
        }, 2000);
    }

    public void LoginScreen() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
    }

    public void LoginTask() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.LoginURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        teacherInfos = new ArrayList<>();
                        teachlist = new ArrayList<>();
                        subidlist = new ArrayList<>();
                        classlist = new ArrayList<>();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "splashactivity  login :  response " + response);
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
                                JSONArray jsonArray = obj.getJSONArray("subject_array");
                                final String institute = obj.getString("institute_name");
                                final String education = obj.getString("qualification");
                                final String exp = obj.getString("teacher_experince");
                                final String appointment_dur = obj.getString("duration");
                                final String im_id_copy = obj.getString("identity_card");
                                final String im_certificate = obj.getString("certificate");
                                final String morningt = obj.getString("availability_morning");
                                final String currency_code = obj.getString("teacher_currency_code");
                                final String paypal_email=obj.getString("paypal_email");
                                final String payment_method_id=obj.getString("payment_method_id");
                                if (morningt.equals("")) {
                                    morning = getString(R.string.blankmor);
                                } else {
                                    String[] mor = obj.getString("availability_morning").split(",");
                                    String last_mor = obj.getString("availability_morning").substring(obj.getString("availability_morning").lastIndexOf(',') + 1);
                                    morning = "Morning" + "(" + mor[0] + " A.M " + " to " + " " + last_mor + " A.M " + ")";
                                }
                                final String afternoont = obj.getString("availability_afternoon");
                                if (afternoont.equals("")) {
                                    afternoon = getString(R.string.blankafter);
                                } else {
                                    String[] after = obj.getString("availability_afternoon").split(",");
                                    String last_after = obj.getString("availability_afternoon").substring(obj.getString("availability_afternoon").lastIndexOf(',') + 1);
                                    afternoon = "Afternoon" + "(" + after[0] + " P.M " + " to " + " " + last_after + " P.M " + ")";
                                }
                                final String eveningt = obj.getString("availability_evening");
                                if (eveningt.equals("")) {
                                    evening = getString(R.string.blankeve);
                                } else {
                                    String[] eve = obj.getString("availability_evening").split(",");
                                    String last_eve = obj.getString("availability_evening").substring(obj.getString("availability_evening").lastIndexOf(',') + 1);
                                    evening = "Evening" + "(" + eve[0] + " P.M " + " to " + " " + last_eve + " P.M " + ")";
                                }
                                final String active_status = obj.getString("status");
                                final String dayst = obj.getString("availability_days");
                                if (dayst.contains("Mon") || dayst.contains("Tue") || dayst.contains("Wed") || dayst.contains("Thu") || dayst.contains("Fri") || dayst.contains("Sat") || dayst.contains("Sun")) {
                                    days = dayst.replace("Mon", getString(R.string.Mon));
                                    days = days.replace("Tue", getString(R.string.Tue));
                                    days = days.replace("Wed", getString(R.string.Wed));
                                    days = days.replace("Thu", getString(R.string.Thu));
                                    days = days.replace("Fri", getString(R.string.Fri));
                                    days = days.replace("Sat", getString(R.string.Sat));
                                    days = days.replace("Sun", getString(R.string.Sun));
                                } else {
                                    days = obj.getString("availability_days");
                                }
                                final String fees = obj.getString("teacher_fees");
                                final String timeslote = morning + "," + afternoon + "," + evening;
                                final String techer_gender_choice = obj.getString("teaching_gender_choice_id");
                                final String latitude = obj.getString("latitude");
                                final String longitude = obj.getString("longitude");
                                final String date_of_birth = obj.getString("date_of_birth");
                                final String gender = obj.getString("gender");
                                final String passingyear = obj.getString("year_of_passing");
                                final String teacherplaceid = obj.getString("teaching_place_id");
                                final String tagline = obj.getString("user_tagline");
                                final String nation = obj.getString("nationality");
                                JSONArray genderchoicearray = obj.getJSONArray("teacher_gender_choice");
                                ArrayList<String> classidlist = new ArrayList<>();
                                for (int j = 0; j < classes.length(); j++) {
                                    JSONObject jsonObject1 = classes.getJSONObject(j);
                                    String class_id = jsonObject1.getString("class_id");
                                    String class_name = jsonObject1.getString("class_name");
                                    classlist.add(class_name);
                                    classidlist.add(class_id);
                                }
                                StringBuilder classname = new StringBuilder();
                                for (String str : classlist) {
                                    classname.append(str).append(",");
                                }
                                classnamestr = classname.toString();
                                if (classnamestr.endsWith(",")) {
                                    classnamestr = classnamestr.substring(0, classnamestr.length() - 1);
                                }
                                StringBuilder classid = new StringBuilder();
                                for (String str : classidlist) {
                                    classid.append(str).append(",");
                                }
                                classidstr = classid.toString();
                                if (classidstr.endsWith(",")) {
                                    classidstr = classidstr.substring(0, classidstr.length() - 1);
                                }
                                ArrayList<String> genderlist = new ArrayList<>();
                                for (int i = 0; i < genderchoicearray.length(); i++) {
                                    JSONObject placejson = genderchoicearray.getJSONObject(i);
                                    String choice = placejson.getString("Gender");
                                    genderlist.add(choice);
                                }
                                StringBuilder sb1 = new StringBuilder();
                                for (String str : genderlist) {
                                    sb1.append(str).append(",");
                                }
                                strfromArrayList1 = sb1.toString();
                                if (strfromArrayList1.endsWith(",")) {
                                    strfromArrayList1 = strfromArrayList1.substring(0, strfromArrayList1.length() - 1);
                                }
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String sub_id = jsonObject1.getString("id");
                                    String sub_name = jsonObject1.getString("subject");
                                    TeacherInfo ts = new TeacherInfo(sub_id, sub_name);
                                    teacherInfos.add(ts);
                                    TeacherInfo subinfo = teacherInfos.get(i);
                                    String subjects = subinfo.getValue();
                                    String subjects_id = subinfo.getAttribute();
                                    teachlist.add(subjects);
                                    subidlist.add(subjects_id);
                                }
                                String str_data = teachlist.toString();
                                str_sub = str_data.substring(1, str_data.length() - 1);
                                String str_sub_id = subidlist.toString();
                                str_sub_ids = str_sub_id.substring(1, str_sub_id.length() - 1);
                                JSONArray teaching_place = obj.getJSONArray("teaching_place");
                                ArrayList<String> list = new ArrayList<>();
                                for (int i = 0; i < teaching_place.length(); i++) {
                                    JSONObject placejson = teaching_place.getJSONObject(i);
                                    String place = placejson.getString("Place");
                                    list.add(place);
                                }
                                StringBuilder sb = new StringBuilder();
                                for (String str : list) {
                                    sb.append(str).append(",");
                                }
                                strfromArrayList = sb.toString();
                                if (strfromArrayList.endsWith(",")) {
                                    strfromArrayList = strfromArrayList.substring(0, strfromArrayList.length() - 1);
                                }
                                userSessionManager.createUserLoginSession(user_id, user_type, username1, email, contact_number, full_name, address, city, country, zipcode, str_sub, classnamestr, profile, subject, institute, education, exp, timeslote, appointment_dur, im_id_copy, im_certificate, morning, afternoon
                                        , evening, days, strfromArrayList, fees, techer_gender_choice, latitude, longitude, date_of_birth, gender, passingyear, teacherplaceid, tagline, strfromArrayList1, classidstr, nation, active_status, currency_code,paypal_email,payment_method_id);
                                if (user_type.equals("1")) {
                                    TeacherHomeScreenURL();
                                    TeacherAppointmentURL();
                                } else if (user_type.equals("2")) {
                                    HomeScreenURL();
                                    StudentAppointmentURL();
                                }

                            } else if (flag.equals("false")) {

                                LoginScreen();
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "splashactivity login : exception" + e);
                            LoginScreen();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LoginScreen();
                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(SplashActivity.this, message);
                        } else if (error instanceof ServerError) {
                            message = getString(R.string.servererror);
                            AlertClass.DispToast(SplashActivity.this, message);
                        } else if (error instanceof AuthFailureError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(SplashActivity.this, message);
                        } else if (error instanceof ParseError) {
                            message = getString(R.string.parsingerror);
                            AlertClass.DispToast(SplashActivity.this, message);
                        } else if (error instanceof NoConnectionError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(SplashActivity.this, message);
                        } else if (error instanceof TimeoutError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(SplashActivity.this, message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_user_name", str_user_name_sh);
                params.put("password", str_user_password_sh);
                params.put("lang_code", langcode);
                params.put("device_token", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    private void HomeScreenURL() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.HomeScreenURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRRR", "splashactivity home :  onResponse: " + response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                if (flag.equals("true")) {
                                    appoinmtmentcount = obj.getString("data").toString();
                                    userSessionManager.createHomeScreen(appoinmtmentcount);
                                    Intent in = new Intent(SplashActivity.this, StudentHomeActivity.class);
                                    in.putExtra("count", appoinmtmentcount);
                                    startActivity(in);
                                } else if (flag.equals("false")) {
                                    appoinmtmentcount = obj.getString("data").toString();
                                    userSessionManager.createHomeScreen(appoinmtmentcount);
                                    Intent in = new Intent(SplashActivity.this, StudentHomeActivity.class);
                                    in.putExtra("count", appoinmtmentcount);
                                    startActivity(in);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "splashactivity home: exception" + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "splashactivity home: exception" + error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("student_id", user_id);
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private void TeacherHomeScreenURL() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherHomeScreenURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRRR", "splashactivity teacherhome :  onResponse: " + response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                if (flag.equals("true")) {
                                    appoinmtmentcount = obj.getString("data").toString();
                                    userSessionManager.createHomeScreen(appoinmtmentcount);
                                    Intent in = new Intent(SplashActivity.this, MainActivity.class);
                                    in.putExtra("count", appoinmtmentcount);
                                    startActivity(in);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "splashactivity teacherhome: exception" + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "splashactivity teacherhome: exception" + error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("teacher_id", user_id);
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private void StudentAppointmentURL() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.StudentAppointmentDateURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                appointdatelist = new ArrayList<>();
                                JSONObject obj = new JSONObject(response);
                                Log.d("RRRRR", "splashactivity   app:  onResponse: " + response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                JSONArray data = obj.getJSONArray("data");
                                if (flag.equals("true")) {
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject object = data.getJSONObject(i);
                                        String date = object.getString("date");
                                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                        Date oneWayTripDate = input.parse(date);
                                        String new_order_date = output.format(oneWayTripDate);
                                        appointdatelist.add(new_order_date);
                                    }
                                    datearray = new JSONArray(appointdatelist);
                                    userSessionManager.createAppointmentCalender(datearray);
                                } else if (flag.equals("false")) {
                                    datearray = new JSONArray(appointdatelist);
                                    userSessionManager.createAppointmentCalender(datearray);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "splashactivity app: exception" + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "splashactivity  app: exception" + error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("student_id", user_id);
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private void TeacherAppointmentURL() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherAppointmentDateURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                appointdatelist = new ArrayList<>();
                                JSONObject obj = new JSONObject(response);
                                Log.d("RRRRR", "splashactivity appdate :  onResponse: " + response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                JSONArray data = obj.getJSONArray("data");
                                if (flag.equals("true")) {
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject object = data.getJSONObject(i);
                                        String date = object.getString("date");
                                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                        Date oneWayTripDate = input.parse(date);
                                        String new_order_date = output.format(oneWayTripDate);
                                        appointdatelist.add(new_order_date);
                                    }
                                    datearray = new JSONArray(appointdatelist);
                                    userSessionManager.createAppointmentCalender(datearray);
                                } else if (flag.equals("false")) {
                                    datearray = new JSONArray(appointdatelist);
                                    userSessionManager.createAppointmentCalender(datearray);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "splashactivity appdate: exception" + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "splashactivity appdate: exception" + error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("teacher_id", user_id);
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun) {
            FirstRunClass frag = new FirstRunClass();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.splash, frag).commit();
        } else {

        }
    }
}
