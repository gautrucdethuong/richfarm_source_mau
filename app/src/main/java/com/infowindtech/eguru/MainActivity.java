package com.infowindtech.eguru;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import com.infowindtech.eguru.FirebaseChat.ChatUsersListFragment;
import com.infowindtech.eguru.StudentPanel.Fragments.Setting_Fragment;
import com.infowindtech.eguru.TeacherPanel.Fragments.JobApplied_StudentList_Fragment;
import com.infowindtech.eguru.TeacherPanel.Fragments.Teacher_ApplyJob_Fragment;
import com.infowindtech.eguru.TeacherPanel.Fragments.Teacher_AppointmentTab_Fragment;
import com.infowindtech.eguru.TeacherPanel.Fragments.Teacher_Profile_Fragment;
import com.infowindtech.eguru.TeacherPanel.Fragments.Wallet_Fragment;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding binding;
    UserSessionManager userSessionManager;
    String user_id, appointcount = "0", phone, email, usertype, address, city, country, zipcode, classes, subjects, name, profile, tagline, days, morning, afternoon, evening, langcode;
    public static boolean isDrawerOpen = false;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    TextView apointmentcount;
    Fragment fr;
    List<String> appointdatelist;
    JSONArray datearray;
    String active_status;
    int flag = 0;
    private Boolean saveLogin, savelang;
    FragmentManager fm = getSupportFragmentManager();

    static DrawerLayout drawer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Intent in = getIntent();
        appointcount = in.getStringExtra("count");

        if (appointcount == null) {
            appointcount = "0";

        }
        flag = in.getFlags();

        Log.d("appointmentcount", "onCreate: " + appointcount + "   " + flag);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        savelang = loginPreferences.getBoolean("saveLang", false);
        loginPrefsEditor = loginPreferences.edit();
        userSessionManager = new UserSessionManager(MainActivity.this);
        if (savelang == true) {
            langcode = loginPreferences.getString("lang", "");
            userSessionManager.selectedlanguage(langcode);
            AlertClass.setLocale(MainActivity.this, langcode);
        } else {
            langcode = "en";
            userSessionManager.selectedlanguage("en");
            AlertClass.setLocale(MainActivity.this, langcode);
        }
        if (userSessionManager.signIn())
            finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        name = user.get(userSessionManager.KEY_FULLNAME);
        phone = user.get(userSessionManager.KEY_PHONENUMBER);
        email = user.get(userSessionManager.KEY_EMAIL);
        usertype = user.get(userSessionManager.KEY_USERTYPE);
        address = user.get(userSessionManager.KEY_ADDRESS);
        city = user.get(userSessionManager.KEY_CITY);
        country = user.get(userSessionManager.KEY_COUNTRY);
        zipcode = user.get(userSessionManager.KEY_ZIPCODE);
        classes = user.get(userSessionManager.KEY_CLASSES);
        subjects = user.get(userSessionManager.KEY_SUBJECT);
        profile = user.get(userSessionManager.KEY_PROFILE);
        tagline = user.get(userSessionManager.KEY_Tagline);
        days = user.get(userSessionManager.KEY_DAYS);
        morning = user.get(userSessionManager.KEY_MORNING);
        afternoon = user.get(userSessionManager.KEY_AFTRNOON);
        evening = user.get(userSessionManager.KEY_EVENING);
        active_status = user.get(userSessionManager.KEY_Activation);
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(MainActivity.this, langcode);
        binding.navView.setNavigationItemSelectedListener(MainActivity.this);
        View headerView = binding.navView.getHeaderView(0);
        Glide.with(MainActivity.this).load(profile)
                .into((CircleImageView) headerView.findViewById(R.id.userprofile));
        TextView tv_email = headerView.findViewById(R.id.tvh);
        tv_email.setText(email);
        TextView tv_active = headerView.findViewById(R.id.tv_active);
        TextView tv_inactive = headerView.findViewById(R.id.tv_inactive);
        ImageView im_active = headerView.findViewById(R.id.im_active);
        ImageView im_inactive = headerView.findViewById(R.id.im_inactive);


        Log.d("AAAAA", "onCreate: " + subjects + " " + classes + "      " + usertype);


        binding.drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int arg0) {
            }

            @Override
            public void onDrawerSlide(View arg0, float arg1) {
            }

            @Override
            public void onDrawerOpened(View arg0) {
                isDrawerOpen = true;
            }

            @Override
            public void onDrawerClosed(View arg0) {
                isDrawerOpen = false;
            }

        });
        Appointmentcount();
        apointmentcount = binding.navView.getMenu().findItem(R.id.appointment).getActionView().findViewById(R.id.tv_unread_status);
        if (appointcount.equals("0") && flag == 0) {
            fr = new Teacher_ApplyJob_Fragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.teacher_fragment, fr).commit();
        } else if (appointcount.equals("0") && flag != 0) {
            fr = new Teacher_AppointmentTab_Fragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.teacher_fragment, fr).commit();
        } else {
            fr = new Teacher_AppointmentTab_Fragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.teacher_fragment, fr).commit();
        }
        if (active_status.equals("1")) {
            im_active.setVisibility(View.VISIBLE);
            tv_active.setVisibility(View.VISIBLE);
            im_inactive.setVisibility(View.GONE);
            tv_inactive.setVisibility(View.GONE);
        } else if (active_status.equals("2")) {
            im_inactive.setVisibility(View.VISIBLE);
            tv_inactive.setVisibility(View.VISIBLE);
            im_active.setVisibility(View.GONE);
            tv_active.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        AlertClass.setLocale(MainActivity.this, langcode);
        int id = item.getItemId();
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }


        if (id == R.id.account) {

            if (fm.getBackStackEntryCount() >= 0) {

                Teacher_Profile_Fragment fragment = new Teacher_Profile_Fragment();
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
                isOpenDrawer();

            }
            // }
        }
        if (id == R.id.appointment) {
            Appointmentcountread();

            Teacher_AppointmentTab_Fragment fragment = new Teacher_AppointmentTab_Fragment();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
            isOpenDrawer();


        }
        if (id == R.id.jobapply) {
            Teacher_ApplyJob_Fragment fragment = new Teacher_ApplyJob_Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("flag", "0");
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
            isOpenDrawer();


        }
        if (id == R.id.jobapplied) {

            JobApplied_StudentList_Fragment fragment = new JobApplied_StudentList_Fragment();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
            isOpenDrawer();


        }
        if (id == R.id.chat) {

            ChatUsersListFragment fragment = new ChatUsersListFragment();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
            isOpenDrawer();

        }
        if (id == R.id.wallet) {

            Wallet_Fragment fragment = new Wallet_Fragment();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
            isOpenDrawer();

        }
        if (id == R.id.setting) {

            Setting_Fragment fragment = new Setting_Fragment();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
            isOpenDrawer();
            //   binding.header.tvHeading.setText(getString(R.string.Settings));

        }
        if (id == R.id.logout) {
            userSessionManager.logoutUser();
            clearDeviceToken();
            loginPrefsEditor.putBoolean("saveLang", false);
            loginPrefsEditor.putString("lang", "en");
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
            Intent in = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(in);
        }
        return false;
    }

    public void isOpenDrawer() {
        if (isDrawerOpen) {
            isDrawerOpen = false;
            binding.drawerLayout.closeDrawers();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {


        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
            ab.setMessage(getString(R.string.closeapp));
            ab.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    MainActivity.super.onBackPressed();
                }
            });
            ab.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = ab.create();
            alert.show();
        }
    }

    private void Appointmentcount() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.Appointmentcount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRRR", "mainactivity  : onResponse: " + response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                if (flag.equals("true")) {
                                    JSONObject jsonObject = obj.getJSONObject("data");
                                    String count = jsonObject.getString("appointment_count");
                                    if (count.equals("0")) {
                                        apointmentcount.setVisibility(View.GONE);
                                    } else {
                                        apointmentcount.setText(count);
                                    }
                                } else {
                                    // AlertClass.alertDialogShow(MainActivity.this, msg);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "mainactivity: exception" + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "mainactivity: error" + error);
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
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private void Appointmentcountread() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.Appointmentcountread,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRRR", "mainactivity  :  onResponse: " + response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                if (flag.equals("true")) {
                                    Appointmentcount();
                                } else {
                                    // AlertClass.alertDialogShow(MainActivity.this, msg);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "mainactivity: exception" + e);
                            AlertClass.alertDialogShow(MainActivity.this, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "mainactivity: error" + error);
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
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
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
                                Log.d("RRRRR", "mainactivity  :  onResponse: " + response);
                                appointdatelist = new ArrayList<>();
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                JSONArray data = obj.getJSONArray("data");
                                if (flag.equals("true")) {
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject object = data.getJSONObject(i);
                                        String date = object.getString("date");
                                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                                        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
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
                            Log.d("RRRRR", "mainactivity: exception" + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "mainactivity: error" + error);
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
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }


    public void callDrawer() {

        binding.drawerLayout.openDrawer(Gravity.LEFT);
    }


    private void clearDeviceToken() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.ClearDeviceToken,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRRR", "mainactivity  :  onResponse: " + response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                if (flag.equals("true")) {

                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "mainactivity: exception" + e);
                            AlertClass.alertDialogShow(MainActivity.this, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "mainactivity: error" + error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }


}
