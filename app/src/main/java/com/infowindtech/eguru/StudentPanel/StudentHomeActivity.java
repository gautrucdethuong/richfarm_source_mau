package com.infowindtech.eguru.StudentPanel;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;

import com.infowindtech.eguru.AlertClass;

import com.infowindtech.eguru.FirebaseChat.ChatUsersListFragment;
import com.infowindtech.eguru.LoginActivity;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Fragments.Choose_StudyPlace_Fragment;
import com.infowindtech.eguru.StudentPanel.Fragments.Class_Info_Fragment;
import com.infowindtech.eguru.StudentPanel.Fragments.JobAppled_Teacherlist_Fragment;
import com.infowindtech.eguru.StudentPanel.Fragments.Parent_SubjectList_Fragment;
import com.infowindtech.eguru.StudentPanel.Fragments.Personal_Information_Fragment;
import com.infowindtech.eguru.StudentPanel.Fragments.Setting_Fragment;
import com.infowindtech.eguru.StudentPanel.Fragments.Transaction_Fragment;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.ActivityStudentHomeBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentHomeActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityStudentHomeBinding binding;
    UserSessionManager userSessionManager;
    String user_id, email, usertype, profile, appointmentcount = "0", langcode;
    public static boolean isDrawerOpen = false;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    Fragment fr;
    List<String> appointdatelist;
    JSONArray datearray;
    private Boolean saveLogin, savelang;
    FragmentManager fm = getSupportFragmentManager();

    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_home);

        Intent in = getIntent();
        appointmentcount = in.getStringExtra("count");
        if (appointmentcount == null) {
            appointmentcount = "0";
        }
        flag = in.getFlags();

        Log.d("appointmentcount", "onCreate: " + appointmentcount + "   "+flag);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        savelang = loginPreferences.getBoolean("saveLang", false);
        loginPrefsEditor = loginPreferences.edit();
        userSessionManager = new UserSessionManager(StudentHomeActivity.this);
        if (savelang == true) {
            langcode = loginPreferences.getString("lang", "");
            userSessionManager.selectedlanguage(langcode);
            AlertClass.setLocale(StudentHomeActivity.this, langcode);
        } else {
            langcode = "en";
            userSessionManager.selectedlanguage("en");
            AlertClass.setLocale(StudentHomeActivity.this, langcode);
        }


        if (userSessionManager.signIn())
            finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        email = user.get(userSessionManager.KEY_EMAIL);
        usertype = user.get(userSessionManager.KEY_USERTYPE);
        profile = user.get(userSessionManager.KEY_PROFILE);
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(StudentHomeActivity.this, langcode);
        binding.navView.setNavigationItemSelectedListener(StudentHomeActivity.this);
        View headerView = binding.navView.getHeaderView(0);
        Glide.with(StudentHomeActivity.this).load(profile)
                .thumbnail(0.5f)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((CircleImageView) headerView.findViewById(R.id.userprofile));
        TextView tv_email = headerView.findViewById(R.id.tvh);
        tv_email.setText(email);
        TextView fab = headerView.findViewById(R.id.fab);
        if (user_id.equals("0")) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSessionManager.logoutUser();
                clearDeviceToken();
                loginPrefsEditor.putBoolean("saveLang", false);
                loginPrefsEditor.putString("lang", "en");
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
                Intent in = new Intent(StudentHomeActivity.this, LoginActivity.class);
                startActivity(in);
            }
        });


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

        binding.header.imDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDrawerOpen) {
                    isDrawerOpen = false;
                    binding.drawerLayout.closeDrawers();
                } else {
                    isDrawerOpen = true;
                    binding.drawerLayout.openDrawer(binding.lldrawercontent);
                }
            }
        });
        binding.header.tvHeading.setVisibility(View.GONE);
        if (appointmentcount.equals("0") && flag == 0) {
            fr = new Parent_SubjectList_Fragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.subject_fragment, fr).commit();
        } else if (appointmentcount.equals("0") && flag != 0) {
            fr = new Class_Info_Fragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.subject_fragment, fr).commit();
        } else {
            fr = new Class_Info_Fragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.subject_fragment, fr).commit();
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        AlertClass.setLocale(StudentHomeActivity.this, langcode);
        int id = item.getItemId();
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }

        if (id == R.id.wallet) {
            item.setVisible(false);
        }
        if (id == R.id.transaction) {

            Transaction_Fragment fragment = new Transaction_Fragment();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
            isOpenDrawer();

        }

        if (id == R.id.account) {
            if (user_id.equals("0")) {
                isOpenDrawer();
                AlertClass.signupdialog(StudentHomeActivity.this, getString(R.string.plzsignup));
            } else {
                /*if(binding.header.tvHeading.getText().toString().equals(getString(R.string.Profile))){
                    isOpenDrawer();
                }else {*/
                if (fm.getBackStackEntryCount() >= 0) {
                    Personal_Information_Fragment fragment = new Personal_Information_Fragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
                    isOpenDrawer();
                    binding.header.tvHeading.setText(getString(R.string.Profile));
                }
            }
        }
        if (id == R.id.subjects) {
            if (fm.getBackStackEntryCount() >= 0) {
                Parent_SubjectList_Fragment fragment = new Parent_SubjectList_Fragment();
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
                isOpenDrawer();
                binding.header.tvHeading.setText(getString(R.string.Category));
            }

        }
        if (id == R.id.classes) {
            if (user_id.equals("0")) {
                isOpenDrawer();
                AlertClass.signupdialog(StudentHomeActivity.this, getString(R.string.plzsignup));
            } else {
               /* if (binding.header.tvHeading.getText().toString().equals(getString(R.string.classes))) {
                    isOpenDrawer();
                } else {*/
                if (fm.getBackStackEntryCount() >= 0) {
                    Class_Info_Fragment fragment = new Class_Info_Fragment();

                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
                    isOpenDrawer();
                    binding.header.tvHeading.setText(getString(R.string.classes));
                }
            }
           /* if(!appointmentcount.equals("0")){
                    final HashMap<String, String> user = userSessionManager.getUserDetails();
                    user_id = user.get(userSessionManager.KEY_USERID);
                    StudentAppointmentURL();
                    Intent in = new Intent(StudentHomeActivity.this, StudentHomeActivity.class);
                    in.putExtra("count", appointmentcount);
                    startActivity(in);

                }else {
                    Class_Info_Fragment fragment = new Class_Info_Fragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
                    isOpenDrawer();
                    binding.header.tvHeading.setText(getString(R.string.classes));
                }*/
        }
        if (id == R.id.jobapplied) {
            if (user_id.equals("0")) {
                isOpenDrawer();
                AlertClass.signupdialog(StudentHomeActivity.this, getString(R.string.plzsignup));
            } else {
               /* if (binding.header.tvHeading.getText().toString().equals(getString(R.string.Suggestedteachers))) {
                    isOpenDrawer();
                } else {*/
                if (fm.getBackStackEntryCount() >= 0) {
                    JobAppled_Teacherlist_Fragment fragment = new JobAppled_Teacherlist_Fragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
                    isOpenDrawer();
                    binding.header.tvHeading.setText(getString(R.string.Suggestedteachers));
                }
            }
        }
        if (id == R.id.studyplace) {
            if (user_id.equals("0")) {
                isOpenDrawer();
                AlertClass.signupdialog(StudentHomeActivity.this, getString(R.string.plzsignup));
            } else {
               /* if (binding.header.tvHeading.getText().toString().equals(getString(R.string.StudyPlaces))) {
                    isOpenDrawer();
                } else {*/
                if (fm.getBackStackEntryCount() >= 0) {
                    Choose_StudyPlace_Fragment fragment = new Choose_StudyPlace_Fragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
                    isOpenDrawer();
                    binding.header.tvHeading.setText(getString(R.string.StudyPlaces));
                }
            }
        }
        if (id == R.id.chat) {
            if (user_id.equals("0")) {
                isOpenDrawer();
                AlertClass.signupdialog(StudentHomeActivity.this, getString(R.string.plzsignup));
            } else {
              /*  if (binding.header.tvHeading.getText().toString().equals(getString(R.string.Chat))) {
                    isOpenDrawer();
                } else {*/
                if (fm.getBackStackEntryCount() >= 0) {
                    ChatUsersListFragment fragment = new ChatUsersListFragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
                    isOpenDrawer();
                    binding.header.tvHeading.setText(getString(R.string.Chat));
                }
            }
        }
        if (id == R.id.setting) {
            if (user_id.equals("0")) {
                isOpenDrawer();
                AlertClass.signupdialog(StudentHomeActivity.this, getString(R.string.plzsignup));
            } else {
                /*if (binding.header.tvHeading.getText().toString().equals(getString(R.string.Settings))) {
                    isOpenDrawer();
                } else {*/
                if (fm.getBackStackEntryCount() >= 0) {
                    Setting_Fragment fragment = new Setting_Fragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, fragment).commit();
                    isOpenDrawer();
                    binding.header.tvHeading.setText(getString(R.string.Settings));
                }
            }
        }
        if (id == R.id.logout) {
            userSessionManager.logoutUser();
            clearDeviceToken();
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();

        }
        return false;
    }

    public void isOpenDrawer() {
        if (isDrawerOpen) {
            isDrawerOpen = false;
            binding.drawerLayout.closeDrawers();
        }
    }


    public void changeBack()
    {

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }


        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            AlertDialog.Builder ab = new AlertDialog.Builder(StudentHomeActivity.this);
            ab.setMessage(getString(R.string.closeapp));
            ab.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
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

    private void StudentAppointmentURL() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.StudentAppointmentDateURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRRR", "studenthomeactivity  :  response: " + response);
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
                            Log.d("RRRRR", "teacherprofileactivity  :  exception: " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "teacherprofileactivity  :  error: " + error);
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentHomeActivity.this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        conf.setLayoutDirection(new Locale(lang));
        res.updateConfiguration(conf, dm);
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
                                Log.d("RRRRR", "studentHomeActivity  :  onResponse: " + response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                if (flag.equals("true")) {

                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "mainactivity: exception" + e);
                            AlertClass.alertDialogShow(StudentHomeActivity.this, e.getMessage());
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentHomeActivity.this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }


}

