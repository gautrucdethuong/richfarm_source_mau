package com.infowindtech.eguru.StudentPanel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.ExpandedGridView;
import com.infowindtech.eguru.FirebaseChat.FirebaseChatMessage;
import com.infowindtech.eguru.MineVolleyGlobal;
import com.infowindtech.eguru.MineVolleyListener;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Adapter.GridviewAppointmentslotAdapter;
import com.infowindtech.eguru.StudentPanel.Model.DrAppointmenttime;
import com.infowindtech.eguru.StudentPanel.Model.TeacherProfilePager;
import com.infowindtech.eguru.StudentPanel.Model.TeacherUserIdSession;
import com.infowindtech.eguru.TeacherPanel.Adapters.UserTypeAdapter;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.TeacherPanel.Models.UserType;
import com.infowindtech.eguru.databinding.FragTeacherProfile1Binding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TeacherProfileActivity extends FragmentActivity implements OnMapReadyCallback, TabLayout.OnTabSelectedListener {
    FragTeacherProfile1Binding binding;
    GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    double latitude, longitude;
    String user_id, str_usertype ,profilepic, message, name,username,rate, address, city, country, zipcode, id, teacherid, bookingdate, bookingtime, bookteacherid, bookstudentid, bookendtime, bookstarttime, teachingplace;
    private int mHour, mMinute;
    EditText et_time, et_end_time;
    TeacherUserIdSession teacherUserIdSession;
    UserSessionManager userSessionManager;
    String fullname, userpic, student_address, str_address, place, appointment_date, duration, firstduration, nDate, langcode, pickerdate, picDate;
    ArrayList<UserType> place_list;
    UserTypeAdapter place_adapter;
    SupportMapFragment mapFragment;
    int tdate, mdate, hour, minute, timeduration;
    EditText tv_place;
    EditText et_date;
    String[] timeaaray, durationarray, systemarray;
    double lati, longi;
    boolean dialogopen = false;
    long ldate;
    ImageView dialog_back, imAfternondown, imAfternonup, imEvedown, imEveup, imDown, imUp, im_closestarttime, im_closeendtime;
    TextView tv_dialog, mor_tv, after_tv, eve_tv, tv_starttime, tv_endtime, tv_nomorslot;
    GridviewAppointmentslotAdapter adapter, adapter1;
    public static int counter = 0;
    ArrayList<DrAppointmenttime> list, list3, list2;
    String confirm, df_medium_us_str, timeslote, selectmor, current_time, str_teacherid, strCurrencyCode;
    ExpandedGridView morninggridview, afternoongridview, eveninggridview;
    Date chosenDate;
    RelativeLayout mor_layout, after_layout, eve_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.frag_teacher_profile1);
        Intent in = getIntent();
        id = in.getStringExtra("id");
        teacherUserIdSession = new TeacherUserIdSession(TeacherProfileActivity.this);
        teacherUserIdSession.createUseridSession(id);
        userSessionManager = new UserSessionManager(TeacherProfileActivity.this);
        if (userSessionManager.signIn())
            TeacherProfileActivity.this.finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        fullname = user.get(userSessionManager.KEY_FULLNAME);
        userpic = user.get(userSessionManager.KEY_PROFILE);
        duration = user.get(userSessionManager.KEY_APPOINTMENT_DUR);
        student_address = user.get(userSessionManager.KEY_ADDRESS);
        str_usertype = user.get(userSessionManager.KEY_USERTYPE);

        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(TeacherProfileActivity.this, langcode);



//TODO
      //  binding.toolbarDrname.setText(getString(R.string.Profile));
        binding.mapView.onCreate(null);
        binding.mapView.getMapAsync(this);
        GetteacherinfoTask();
        final Dialog dialog = new Dialog(TeacherProfileActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.date_time_dialog);
        tv_place = dialog.findViewById(R.id.tv_address);
        dialog_back = dialog.findViewById(R.id.im_back);
        tv_dialog = dialog.findViewById(R.id.tv_heading);


        Locale.getDefault();
        final Calendar mcurrentTime = Calendar.getInstance();
        hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        minute = mcurrentTime.get(Calendar.MINUTE);
        chosenDate = mcurrentTime.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        current_time = simpleDateFormat.format(calendar.getTime());
        binding.appointLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_id.equals("0")) {
                    AlertClass.signupdialog(TeacherProfileActivity.this, getString(R.string.plzsignup));
                } else {
                    dialog.show();
                    dialogopen = true;
                    dialog_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                    tv_dialog.setText(getString(R.string.Appointment));
                    mor_layout = dialog.findViewById(R.id.mor_layout);
                    after_layout = dialog.findViewById(R.id.afternoon_layout);
                    eve_layout = dialog.findViewById(R.id.eve_layout);
                    morninggridview = dialog.findViewById(R.id.gv_morning);
                    afternoongridview = dialog.findViewById(R.id.gv_afternoon);
                    eveninggridview = dialog.findViewById(R.id.gv_evening);
                    mor_tv = dialog.findViewById(R.id.tv_morning);
                    after_tv = dialog.findViewById(R.id.tv_afternoon);
                    eve_tv = dialog.findViewById(R.id.tv_evening);
                    imAfternondown = dialog.findViewById(R.id.im_afternondown);
                    imAfternonup = dialog.findViewById(R.id.im_afternonup);
                    imEvedown = dialog.findViewById(R.id.im_evedown);
                    imEveup = dialog.findViewById(R.id.im_eveup);
                    imDown = dialog.findViewById(R.id.im_down);
                    imUp = dialog.findViewById(R.id.im_up);
                    tv_starttime = dialog.findViewById(R.id.tv_starttime);
                    tv_endtime = dialog.findViewById(R.id.tv_endtime);
                    final RelativeLayout teacher_havetimeslot = dialog.findViewById(R.id.have_class);
                    final RelativeLayout teacher_havenotimeslot = dialog.findViewById(R.id.notavailable_layout);
                    final View timesloteview = dialog.findViewById(R.id.available_timeslote);
                    im_closestarttime = dialog.findViewById(R.id.im_cancel_starttime);
                    im_closeendtime = dialog.findViewById(R.id.im_cancel_endtime);
                    tv_nomorslot = dialog.findViewById(R.id.tv_nomorslot);
                    Date cDate = new Date();
                    nDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(cDate);
                    String fDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(cDate);
                    appointment_date = fDate;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.ENGLISH);
                    df_medium_us_str = dateFormat.format(chosenDate);
                    Log.d("EEE", df_medium_us_str);
                    et_date = dialog.findViewById(R.id.in_date);
                    et_date.setText(nDate);
                    teacherid = str_teacherid;
                    TodayTimeSlotTask();
                    if (timeslote.contains(df_medium_us_str)) {
                        teacher_havetimeslot.setVisibility(View.VISIBLE);
                        teacher_havenotimeslot.setVisibility(View.GONE);
                    } else {
                        teacher_havetimeslot.setVisibility(View.GONE);
                        teacher_havenotimeslot.setVisibility(View.VISIBLE);
                    }
                    tdate = Integer.parseInt(new SimpleDateFormat("dd", Locale.ENGLISH).format(cDate));
                    mdate = Integer.parseInt(new SimpleDateFormat("MM", Locale.ENGLISH).format(cDate));
                    Spinner spn_place = dialog.findViewById(R.id.user);
                    et_end_time = dialog.findViewById(R.id.in_end_time);

                    et_time = dialog.findViewById(R.id.in_time);
                    TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
                    TextView tv_ok = dialog.findViewById(R.id.tv_ok);
                    final RelativeLayout maplayout = dialog.findViewById(R.id.map_layout);

                    binding.imBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            et_date.setText("");
                            et_time.setText("");
                            et_end_time.setText("");
                            tv_place.setText("");
                            tv_place.setVisibility(View.GONE);
                            finish();
                        }
                    });
                    mor_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!et_time.equals("")) {
                                et_time.setText("");
                                im_closestarttime.setVisibility(View.INVISIBLE);
                            }
                            if (!et_end_time.equals("")) {
                                et_end_time.setText("");
                                im_closeendtime.setVisibility(View.INVISIBLE);
                            }
                            if (afternoongridview.getVisibility() == GridView.VISIBLE) {
                                imAfternondown.setVisibility(View.VISIBLE);
                                imAfternonup.setVisibility(View.INVISIBLE);
                                afternoongridview.setVisibility(View.GONE);
                            }
                            if (eveninggridview.getVisibility() == GridView.VISIBLE) {
                                imEvedown.setVisibility(View.VISIBLE);
                                imEveup.setVisibility(View.INVISIBLE);
                                eveninggridview.setVisibility(View.GONE);
                            }
                            morninggridview.setVisibility(View.VISIBLE);
                            imDown.setVisibility(View.INVISIBLE);
                            imUp.setVisibility(View.VISIBLE);
                            imUp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    morninggridview.setVisibility(View.GONE);
                                    imDown.setVisibility(View.VISIBLE);
                                    imUp.setVisibility(View.INVISIBLE);
                                }
                            });

                        }
                    });
                    after_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!et_time.equals("")) {
                                et_time.setText("");
                                im_closestarttime.setVisibility(View.INVISIBLE);
                            }
                            if (!et_end_time.equals("")) {
                                et_end_time.setText("");
                                im_closeendtime.setVisibility(View.INVISIBLE);
                            }
                            if (morninggridview.getVisibility() == GridView.VISIBLE) {
                                imDown.setVisibility(View.VISIBLE);
                                imUp.setVisibility(View.INVISIBLE);
                                morninggridview.setVisibility(View.GONE);
                            }
                            if (eveninggridview.getVisibility() == GridView.VISIBLE) {
                                imEvedown.setVisibility(View.VISIBLE);
                                imEveup.setVisibility(View.INVISIBLE);
                                eveninggridview.setVisibility(View.GONE);
                            }
                            afternoongridview.setVisibility(View.VISIBLE);
                            imAfternondown.setVisibility(View.INVISIBLE);
                            imAfternonup.setVisibility(View.VISIBLE);
                            imAfternonup.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    afternoongridview.setVisibility(View.GONE);
                                    imAfternondown.setVisibility(View.VISIBLE);
                                    imAfternonup.setVisibility(View.INVISIBLE);
                                }
                            });

                        }
                    });
                    eve_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!et_time.equals("")) {
                                et_time.setText("");
                                im_closestarttime.setVisibility(View.INVISIBLE);
                            }
                            if (!et_end_time.equals("")) {
                                et_end_time.setText("");
                                im_closeendtime.setVisibility(View.INVISIBLE);
                            }
                            if (morninggridview.getVisibility() == GridView.VISIBLE) {
                                imDown.setVisibility(View.VISIBLE);
                                imUp.setVisibility(View.INVISIBLE);
                                morninggridview.setVisibility(View.GONE);
                            }
                            if (afternoongridview.getVisibility() == GridView.VISIBLE) {
                                imAfternondown.setVisibility(View.VISIBLE);
                                imAfternonup.setVisibility(View.INVISIBLE);
                                afternoongridview.setVisibility(View.GONE);
                            }
                            eveninggridview.setVisibility(View.VISIBLE);
                            imEvedown.setVisibility(View.INVISIBLE);
                            imEveup.setVisibility(View.VISIBLE);
                            imEveup.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    eveninggridview.setVisibility(View.GONE);
                                    imEvedown.setVisibility(View.VISIBLE);
                                    imEveup.setVisibility(View.INVISIBLE);
                                }
                            });

                        }
                    });
                    et_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!et_time.equals("")) {
                                et_time.setText("");
                                im_closestarttime.setVisibility(View.INVISIBLE);
                            }
                            if (!et_end_time.equals("")) {
                                et_end_time.setText("");
                                im_closeendtime.setVisibility(View.INVISIBLE);
                            }
                            final Calendar calendar = Calendar.getInstance(Locale.getDefault());
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            final Date chosenDate = calendar.getTime();
                            final Dialog dialog1 = new Dialog(TeacherProfileActivity.this);
                            dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                            dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                            dialog1.setContentView(R.layout.datepickerdialog);
                            dialog1.show();
                            final DatePicker simpleDatePicker = dialog1.findViewById(R.id.simpleDatePicker); // initiate a date picker
                            TextView tv_ok = dialog1.findViewById(R.id.tv_ok);
                            TextView tv_cancel = dialog1.findViewById(R.id.tv_cancel);
                            simpleDatePicker.setSpinnersShown(false);
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                                simpleDatePicker.setMinDate(calendar.getTimeInMillis());
                                simpleDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                                    @Override
                                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                                        int newm = i1 + 1;
                                        String date = i2 + "-" + newm + "-" + i;
                                        try {
                                            Date selDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(date);
                                            pickerdate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(selDate);
                                            appointment_date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(selDate);
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.ENGLISH);
                                            df_medium_us_str = dateFormat.format(selDate);
                                            Log.d("EEE", df_medium_us_str);
                                            if (timeslote.contains(df_medium_us_str)) {
                                                teacher_havetimeslot.setVisibility(View.VISIBLE);
                                                teacher_havenotimeslot.setVisibility(View.GONE);
                                            } else {
                                                teacher_havetimeslot.setVisibility(View.GONE);
                                                teacher_havenotimeslot.setVisibility(View.VISIBLE);

                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                });
                            } else {
                                final int minYear = calendar.get(Calendar.YEAR);
                                final int minMonth = calendar.get(Calendar.MONTH);
                                final int minDay = calendar.get(Calendar.DAY_OF_MONTH);
                                simpleDatePicker.init(minYear, minMonth, minDay, new DatePicker.OnDateChangedListener() {
                                    @Override
                                    public void onDateChanged(DatePicker view, int year,
                                                              int month, int day) {
                                        Calendar newDate = Calendar.getInstance();
                                        newDate.set(year, month, day);
                                        int newm = month + 1;
                                        if (calendar.after(newDate)) {
                                            view.init(minYear, minMonth, minDay, this);
                                        }
                                        String date = day + "-" + newm + "-" + year;
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                            Date strDate = sdf.parse(date);
                                            if (System.currentTimeMillis() > strDate.getTime()) {
                                                Date cDate = new Date();
                                                nDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(cDate);
                                                String fDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(cDate);
                                                pickerdate = nDate;
                                                appointment_date = fDate;
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.ENGLISH);
                                                df_medium_us_str = dateFormat.format(cDate);
                                                Log.d("EEE", df_medium_us_str);
                                                if (timeslote.contains(df_medium_us_str)) {
                                                    teacher_havetimeslot.setVisibility(View.VISIBLE);
                                                    teacher_havenotimeslot.setVisibility(View.GONE);
                                                } else {
                                                    teacher_havetimeslot.setVisibility(View.GONE);
                                                    teacher_havenotimeslot.setVisibility(View.VISIBLE);

                                                }
                                            } else {
                                                String date1 = day + "-" + newm + "-" + year;
                                                Date selDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(date1);
                                                pickerdate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(selDate);
                                                appointment_date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(selDate);
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.ENGLISH);
                                                df_medium_us_str = dateFormat.format(selDate);
                                                if (timeslote.contains(df_medium_us_str)) {
                                                    teacher_havetimeslot.setVisibility(View.VISIBLE);
                                                    teacher_havenotimeslot.setVisibility(View.GONE);
                                                } else {
                                                    teacher_havetimeslot.setVisibility(View.GONE);
                                                    teacher_havenotimeslot.setVisibility(View.VISIBLE);

                                                }
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                });
                            }
                            tv_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        dialog1.cancel();
                                        dialogopen = false;
                                        et_date.setText(pickerdate);
                                        teacherid = str_teacherid;
                                        Date selDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(et_date.getText().toString());
                                        appointment_date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(selDate);
                                        ShowTimeSlotTask();
                                    } catch (Exception e) {

                                    }
                                }
                            });
                            tv_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    et_date.setText("");
                                    et_time.setText("");
                                    et_end_time.setText("");
                                    tv_place.setText("");
                                    tv_place.setVisibility(View.GONE);
                                    dialog1.cancel();
                                    dialogopen = false;
                                }
                            });
                        }
                    });
                    tv_starttime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tv_starttime.setVisibility(View.INVISIBLE);
                            et_time.setVisibility(View.VISIBLE);
                        }
                    });
                    im_closestarttime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            im_closestarttime.setVisibility(View.INVISIBLE);
                            et_time.setText("");
                        }
                    });
                    tv_endtime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tv_endtime.setVisibility(View.INVISIBLE);
                            et_end_time.setVisibility(View.VISIBLE);

                        }
                    });
                    im_closeendtime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            im_closeendtime.setVisibility(View.INVISIBLE);
                            et_end_time.setText("");
                        }
                    });
                    tv_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            et_date.setText("");
                            et_time.setText("");
                            et_end_time.setText("");
                            tv_place.setText("");
                            tv_place.setVisibility(View.GONE);
                            dialog.cancel();
                        }
                    });
                    tv_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bookstudentid = user_id;
                            bookteacherid = teacherid;
                            bookingdate = appointment_date;
                            bookstarttime = et_time.getText().toString().trim();
                            bookendtime = et_end_time.getText().toString().trim();
                            try {
                                bookingtime = bookstarttime + "-" + bookendtime;
                                str_address = tv_place.getText().toString().trim();
                                if (bookingdate.equals("")) {
                                    Toast.makeText(TeacherProfileActivity.this, getString(R.string.selectdate), Toast.LENGTH_SHORT).show();
                                } else if (bookstarttime.equals("")) {
                                    Toast.makeText(TeacherProfileActivity.this, getString(R.string.selectstarttime), Toast.LENGTH_SHORT).show();
                                } else if (bookendtime.equals("")) {
                                    Toast.makeText(TeacherProfileActivity.this, getString(R.string.selectendtime), Toast.LENGTH_SHORT).show();
                                } else if (place.equals(getString(R.string.place))) {
                                    Toast.makeText(TeacherProfileActivity.this, getString(R.string.selectplace), Toast.LENGTH_SHORT).show();
                                }
//                                else if (str_address.equals("")) {
//                                    Toast.makeText(TeacherProfileActivity.this, getString(R.string.enteraddress), Toast.LENGTH_SHORT).show();
//                                }
                                else {
                                    bookclassappointment();
                                    dialog.cancel();
                                }
                            } catch (Exception e) {

                            }

                        }
                    });
                    place_adapter = new UserTypeAdapter(place_list, TeacherProfileActivity.this);
                    spn_place.setAdapter(place_adapter);
                    spn_place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            UserType userType = place_list.get(i);
                            place = userType.getType();
                            if (place.equals(getString(R.string.TeacherHome))) {
                                maplayout.setVisibility(View.GONE);
                                mapFragment.getView().setVisibility(View.GONE);
                                tv_place.setText(address);
                                Log.d("ssssaaaaa", "onItemSelected: "+address);
                            } else if (place.equals(getString(R.string.StudentHome))) {
                                maplayout.setVisibility(View.GONE);
                                mapFragment.getView().setVisibility(View.GONE);
                                tv_place.setText(student_address);
                            } else if (place.equals(getString(R.string.PublicPlace))) {
                                maplayout.setVisibility(View.VISIBLE);
                                mapFragment.getView().setVisibility(View.VISIBLE);
                                tv_place.setText("");

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }
        });
        binding.chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("RRRR", "onVolleyResponse: "+user_id+"  "+str_teacherid+"  "+str_usertype);
                if (user_id.equals("0")) {
                    AlertClass.signupdialog(TeacherProfileActivity.this, getString(R.string.plzsignup));
                } else {
                    if (str_usertype.equals("1")) {
                        callAPII(str_teacherid,user_id);

                    } else {
                        callAPII(user_id,str_teacherid);
                    }

                    }
            }
        });
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.infotab)), 0);
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.exptab)), 1);
        TeacherProfilePager adapter = new TeacherProfilePager(getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(2);
        binding.tabLayout.setOnTabSelectedListener(this);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.appbar.setExpanded(true);

        for(int i=0; i < binding.tabLayout.getTabCount(); i++) {
            View tab1 = ((ViewGroup) binding.tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab1.getLayoutParams();
            p.setMargins(10, 10, 10, 10);
            tab1.requestLayout();
        }

        //TODO
//        binding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = false;
//            int scrollRange = -1;
//
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                if (scrollRange + verticalOffset == 0) {
//                    binding.toolbarDrname.setVisibility(View.VISIBLE);
//                    binding.toolbarImg.setVisibility(View.VISIBLE);
//                    binding.toolbarDrname.setText(name);
//                    Glide.with(TeacherProfileActivity.this).load(profilepic)
//                            .thumbnail(0.5f)
//                            .crossFade()
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(binding.toolbarImg);
//                    binding.toolbar.setBackgroundColor(getResources().getColor(R.color.red));
//                    isShow = true;
//                } else if (isShow) {
//                    binding.toolbar.setBackgroundColor(getResources().getColor(R.color.blacktransparent));
//                    binding.collapsingToolbar.setTitle(" ");
//                    binding.toolbarDrname.setVisibility(View.GONE);
//                    binding.toolbarImg.setVisibility(View.GONE);
//                    isShow = false;
//                }
//            }
//        });



        mapFragment = (SupportMapFragment) TeacherProfileActivity.this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }






    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        Geocoder coder = new Geocoder(this);
        try {
            Log.d("sssss", "onMapClick: " + student_address);
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(student_address, 50);
            for(Address add : adresses) {
                // if (statement) {//Controls to ensure it is right address such as country etc.
                double longitude = add.getLongitude();
                double latitude = add.getLatitude();

                Log.i("Lat", "" + latitude+"    "+adresses);
                Log.i("Lng", "" + longitude);
                LatLng latLng = new LatLng(latitude, longitude);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                googleMap.addMarker(markerOptions);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));


                if (tv_place.getVisibility() == View.VISIBLE) {
                    tv_place.setText(address);
                    Log.d("ssssseeee", "onMapClick: " + address);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }








//                Geocoder geocoder;
//                List<Address> addresses;
//                geocoder = new Geocoder(TeacherProfileActivity.this, Locale.getDefault());
//                try {
//                    Log.d("sssss", "onMapClick: ");
//                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
//                    String address = addresses.get(0).getAddressLine(0);
//                    if (tv_place.getVisibility() == View.VISIBLE) {
//                        tv_place.setText(address);
//                        Log.d("sssss", "onMapClick: " + address);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

    }



    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        binding.viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void GetteacherinfoTask() {
        final ProgressDialog dialog = new ProgressDialog(TeacherProfileActivity.this);
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherProfileURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "teacherprofileactivity tea :  response: "+response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                JSONObject obj = jsonObject.getJSONObject("data");
                                str_teacherid = obj.getString("user_id");
                                username = obj.getString("username");
                                name = obj.getString("full_name");
                                rate = obj.getString("rating");
                                address = obj.getString("address");
                                city = obj.getString("city");
                                country = obj.getString("country");
                                zipcode = obj.getString("zipcode");
                                profilepic = obj.getString("profile_image");
                                duration = obj.getString("duration");
                                timeslote = obj.getString("availability_days");
                                final String teacher_fees = obj.getString("teacher_fees");
                                String teacher_quality = obj.getString("user_teacher_quality");
                                binding.tvTeacherName.setText(name);
                                Glide.with(TeacherProfileActivity.this).load(profilepic)
                                        .thumbnail(0.5f)

                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(binding.imProfile);
                                binding.rating.setRating(Float.parseFloat(rate));
                                binding.rating.setEnabled(false);
                                binding.tvFees.setText(teacher_fees);

                                String[] currency = teacher_fees.split(" ");
                                strCurrencyCode = currency[0];
                                if(strCurrencyCode.equals("INR"))
                                {
                                    binding.imCurrency.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.rupee));
                                }else if (strCurrencyCode.equals("USD"))
                                {
                                    binding.imCurrency.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.dollor));
                                }else {
                                    AlertClass.DispToast(getApplicationContext(),"CHECK CURRENCY CODE");
                                }

                               // binding.tvQuality.setText(teacher_quality);
                                getLatLongFromPlace(address + "," + city + "," + "," + country);
                                JSONArray placearray = obj.getJSONArray("teaching_place");
                                place_list = new ArrayList<>();
                                place_list.add(new UserType(getString(R.string.place), "", true));
                                for (int i = 0; i < placearray.length(); i++) {
                                    JSONObject placeobj = placearray.getJSONObject(i);
                                    teachingplace = placeobj.getString("Place");
                                    place_list.add(new UserType(teachingplace, "", true));
                                }




                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(TeacherProfileActivity.this, msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "teacherprofileactivity  :  Exception: "+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(TeacherProfileActivity.this, message);
                        } else if (error instanceof ServerError) {
                            message = getString(R.string.servererror);
                            AlertClass.DispToast(TeacherProfileActivity.this, message);
                        } else if (error instanceof AuthFailureError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(TeacherProfileActivity.this, message);
                        } else if (error instanceof ParseError) {
                            message = getString(R.string.parsingerror);
                            AlertClass.DispToast(TeacherProfileActivity.this, message);
                        } else if (error instanceof NoConnectionError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(TeacherProfileActivity.this, message);
                        } else if (error instanceof TimeoutError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(TeacherProfileActivity.this, message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", id);
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(TeacherProfileActivity.this);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public void bookclassappointment() {
        final ProgressDialog dialog = new ProgressDialog(TeacherProfileActivity.this);
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TakeAppointmentURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "teacherprofileactivity class :  response: "+response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                AlertDialog.Builder ab = new AlertDialog.Builder(TeacherProfileActivity.this);
                                ab.setMessage(msg);
                                ab.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        finish();
                                    }
                                });
                                AlertDialog alert = ab.create();
                                alert.show();
                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(TeacherProfileActivity.this, msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "teacherprofileactivity  :  Exception: "+e);
                            AlertClass.alertDialogShow(TeacherProfileActivity.this, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(TeacherProfileActivity.this, message);
                        } else if (error instanceof ServerError) {
                            message = getString(R.string.servererror);
                            AlertClass.DispToast(TeacherProfileActivity.this, message);
                        } else if (error instanceof AuthFailureError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(TeacherProfileActivity.this, message);
                        } else if (error instanceof ParseError) {
                            message = getString(R.string.parsingerror);
                            AlertClass.DispToast(TeacherProfileActivity.this, message);
                        } else if (error instanceof NoConnectionError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(TeacherProfileActivity.this, message);
                        } else if (error instanceof TimeoutError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(TeacherProfileActivity.this, message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("student_id", bookstudentid);
                params.put("teacher_id", bookteacherid);
                params.put("date_of_appointment", bookingdate);
                params.put("time_of_appointment", bookingtime);
                params.put("address", str_address);
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(TeacherProfileActivity.this);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public void getLatLongFromPlace(String place) {
        try {
            Geocoder selected_place_geocoder = new Geocoder(TeacherProfileActivity.this);
            List<Address> address;
            address = selected_place_geocoder.getFromLocationName(place, 5);
            if (address == null) {
            } else {
                Address location = address.get(0);
                lati = location.getLatitude();
                longi = location.getLongitude();
                latitude = lati;
                longitude = longi;
                final LatLng point = new LatLng(lati, longi);
                MarkerOptions options = new MarkerOptions()
                        .position(point);
                mMap.addMarker(options);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lati, longi), 16.0f));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowTimeSlotTask() {
        final ProgressDialog pDialog = new ProgressDialog(TeacherProfileActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.BookedAppointmentURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                pDialog.dismiss();
                                Log.d("RRRRR", "teacherprofileactivity time :  response: "+response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                Log.d("response_get", response);

                                if (flag.equals("true")) {
                                    JSONObject data = obj.getJSONObject("data");
                                    String drid = data.getString("teacher_id");
                                    JSONArray morarray = data.getJSONArray("teacher_morning_array");
                                    JSONArray afterarray = data.getJSONArray("teacher_afternoon_array");
                                    JSONArray evenarray = data.getJSONArray("teacher_evening_array");
                                    list = new ArrayList<DrAppointmenttime>();
                                    ArrayList<DrAppointmenttime> dr_arr = new ArrayList<>();
                                    ArrayList<DrAppointmenttime> mdr_arr = new ArrayList<>();
                                    list2 = new ArrayList<DrAppointmenttime>();
                                    list3 = new ArrayList<DrAppointmenttime>();
                                    mor_tv.setVisibility(View.VISIBLE);
                                    after_tv.setVisibility(View.VISIBLE);
                                    eve_tv.setVisibility(View.VISIBLE);
                                    ArrayList<String> arr_str = new ArrayList<>();
                                    ArrayList<String> arr_mine = new ArrayList<>();
                                    ArrayList<String> arr_eve = new ArrayList<>();
                                    ArrayList<String> mor_list = new ArrayList<>();
                                    if (msg.equals("Teacher Booked Time slots")) {
                                        for (int i = 0; i < morarray.length(); i++) {
                                            JSONObject json = morarray.getJSONObject(i);
                                            String morning = json.getString("morning_time");
                                            if (!morning.equals("")) {
                                                String str_show = morning + " AM";
                                                arr_str.add(str_show);
                                            }
                                        }
                                        JSONArray confirmarray = data.getJSONArray("confirmed_appoint_arr");
                                        for (int i = 0; i < confirmarray.length(); i++) {
                                            JSONObject jsonObject = confirmarray.getJSONObject(i);
                                            confirm = jsonObject.getString("booked_time");
                                            if (!confirm.equals("")) {
                                                JSONObject dataobj = jsonObject.getJSONObject("data");
                                                JSONArray splite_array = dataobj.getJSONArray("split_time");
                                                for (int k = 0; k < splite_array.length(); k++) {
                                                    JSONObject spliteobj = splite_array.getJSONObject(k);
                                                    String splite_time = spliteobj.getString("time");
                                                    mor_list.add(splite_time);
                                                    Log.d("morlist", splite_time);
                                                    DrAppointmenttime drbean = new DrAppointmenttime();
                                                    drbean.setBooktime(splite_time);
                                                    dr_arr.add(drbean);
                                                }
                                            }

                                        }
                                        for (int j = 0; j < dr_arr.size(); j++) {
                                            String str_dr = dr_arr.get(j).getBooktime();
                                            Log.d("StringAArryy", str_dr);
                                            if (arr_str.contains(str_dr)) {
                                                arr_str.remove(str_dr);
                                            }
                                        }
                                        Log.d("Array", arr_str.toString());
                                        for (int i = 0; i < arr_str.size(); i++) {
                                            String str_mine = arr_str.get(i);
                                            DrAppointmenttime drBean = new DrAppointmenttime();
                                            drBean.setBgroup(str_mine);
                                            list.add(drBean);
                                            Log.d("mine111", list.toString());
                                        }
                                        adapter = new GridviewAppointmentslotAdapter(list, TeacherProfileActivity.this, TeacherProfileActivity.this);
                                        morninggridview.setAdapter(adapter);
                                        morninggridview.setExpanded(true);
                                        for (int j = 0; j < afterarray.length(); j++) {
                                            JSONObject json = afterarray.getJSONObject(j);
                                            String afternoon = json.getString("afternoon_time");
                                            if (!afternoon.equals("")) {
                                                String str_show = afternoon + " PM";
                                                arr_mine.add(str_show);
                                            }
                                        }
                                        for (int i = 0; i < dr_arr.size(); i++) {
                                            String str_dr = dr_arr.get(i).getBooktime();
                                            Log.d("StringAArryy", str_dr);
                                            if (arr_mine.contains(str_dr)) {
                                                arr_mine.remove(str_dr);
                                            }

                                        }
                                        for (int i = 0; i < arr_mine.size(); i++) {
                                            String str_mine = arr_mine.get(i);
                                            DrAppointmenttime drBean = new DrAppointmenttime();
                                            drBean.setBgroup(str_mine);
                                            list2.add(drBean);
                                            Log.d("mine111", list2.toString());
                                            adapter = new GridviewAppointmentslotAdapter(list2, TeacherProfileActivity.this, TeacherProfileActivity.this);
                                            afternoongridview.setAdapter(adapter);
                                            afternoongridview.setExpanded(true);

                                        }
                                        for (int j = 0; j < evenarray.length(); j++) {
                                            JSONObject json = evenarray.getJSONObject(j);
                                            String evening = json.getString("evening_time");
                                            if (!evening.equals("")) {
                                                String str_show = evening + " PM";
                                                arr_eve.add(str_show);
                                            }
                                        }

                                        for (int i = 0; i < dr_arr.size(); i++) {
                                            String str_dr = dr_arr.get(i).getBooktime();
                                            Log.d("StringAArryy", str_dr);
                                            if (arr_eve.contains(str_dr)) {
                                                arr_eve.remove(str_dr);
                                            }
                                        }
                                        for (int j = 0; j < arr_eve.size(); j++) {
                                            String str_mine = arr_eve.get(j);
                                            DrAppointmenttime drBean = new DrAppointmenttime();
                                            drBean.setBgroup(str_mine);
                                            list3.add(drBean);
                                            Log.d("mine112", list3.toString());
                                            adapter1 = new GridviewAppointmentslotAdapter(list3, TeacherProfileActivity.this, TeacherProfileActivity.this);
                                            eveninggridview.setAdapter(adapter1);
                                            eveninggridview.setExpanded(true);
                                        }
                                    } else {
                                        for (int i = 0; i < morarray.length(); i++) {
                                            DrAppointmenttime bp = new DrAppointmenttime();
                                            JSONObject json = morarray.getJSONObject(i);
                                            String morning = json.getString("morning_time");
                                            bp.setBgroup(morning + " AM");
                                            bp.setBooktime("0.00 AM");
                                            list.add(bp);
                                            adapter = new GridviewAppointmentslotAdapter(list, TeacherProfileActivity.this, TeacherProfileActivity.this);
                                            morninggridview.setAdapter(adapter);
                                            morninggridview.setExpanded(true);

                                        }
                                        for (int j = 0; j < afterarray.length(); j++) {
                                            DrAppointmenttime bp = new DrAppointmenttime();
                                            JSONObject json = afterarray.getJSONObject(j);
                                            String afternoon = json.getString("afternoon_time");
                                            bp.setBgroup(afternoon + " PM");
                                            bp.setBooktime("0.00 AM");
                                            list2.add(bp);
                                            adapter = new GridviewAppointmentslotAdapter(list2, TeacherProfileActivity.this, TeacherProfileActivity.this);
                                            afternoongridview.setAdapter(adapter);
                                            afternoongridview.setExpanded(true);

                                        }
                                        for (int k = 0; k < evenarray.length(); k++) {
                                            DrAppointmenttime bp = new DrAppointmenttime();
                                            JSONObject json = evenarray.getJSONObject(k);
                                            String evening = json.getString("evening_time");
                                            bp.setBgroup(evening + " PM");
                                            bp.setBooktime("0.00 AM");
                                            list3.add(bp);
                                            adapter = new GridviewAppointmentslotAdapter(list3, TeacherProfileActivity.this, TeacherProfileActivity.this);
                                            eveninggridview.setAdapter(adapter);
                                            eveninggridview.setExpanded(true);
                                        }
                                    }
                                } else {
                                    Toast.makeText(TeacherProfileActivity.this, msg, Toast.LENGTH_LONG).show();

                                }
                                pDialog.dismiss();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e1) {
                            Log.d("RRRRR", "teacherprofileactivity  :  Exception: "+e1);
                            AlertClass.alertDialogShow(TeacherProfileActivity.this, "" + e1.getMessage());
                            pDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "teacherprofileactivity  :  error: "+error);
                        Toast.makeText(TeacherProfileActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("teacher_id", teacherid);
                params.put("date", appointment_date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(TeacherProfileActivity.this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private void TodayTimeSlotTask() {
        final ProgressDialog pDialog = new ProgressDialog(TeacherProfileActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.BookedAppointmentURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                pDialog.dismiss();
                                Log.d("RRRRR", "teacherprofileactivity today :  response: "+response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                if (flag.equals("true")) {
                                    JSONObject data = obj.getJSONObject("data");
                                    String drid = data.getString("teacher_id");
                                    JSONArray morarray = data.getJSONArray("teacher_morning_array");
                                    JSONArray afterarray = data.getJSONArray("teacher_afternoon_array");
                                    JSONArray evenarray = data.getJSONArray("teacher_evening_array");
                                    list = new ArrayList<DrAppointmenttime>();
                                    ArrayList<DrAppointmenttime> dr_arr = new ArrayList<>();
                                    ArrayList<DrAppointmenttime> mdr_arr = new ArrayList<>();
                                    list2 = new ArrayList<DrAppointmenttime>();
                                    list3 = new ArrayList<DrAppointmenttime>();
                                    mor_tv.setVisibility(View.VISIBLE);
                                    after_tv.setVisibility(View.VISIBLE);
                                    eve_tv.setVisibility(View.VISIBLE);
                                    ArrayList<String> arr_str = new ArrayList<>();
                                    ArrayList<String> arr_mine = new ArrayList<>();
                                    ArrayList<String> arr_eve = new ArrayList<>();
                                    ArrayList<String> mor_list = new ArrayList<>();
                                    if (msg.equals("Teacher Booked Time slots")) {
                                        try {
                                            for (int i = 0; i < morarray.length(); i++) {
                                                JSONObject json = morarray.getJSONObject(i);
                                                String morning = json.getString("morning_time");
                                                if (!morning.equals("")) {
                                                    String str_show = morning + " AM";
                                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                                                    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                                                    Date date1 = simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
                                                    Date date2 = simpleDateFormat.parse(str_show);
                                                    if (date2.before(date1)) {

                                                    } else {
                                                        arr_str.add(str_show);
                                                    }
                                                }

                                            }
                                        } catch (Exception e) {

                                        }

                                        JSONArray confirmarray = data.getJSONArray("confirmed_appoint_arr");
                                        for (int i = 0; i < confirmarray.length(); i++) {
                                            JSONObject jsonObject = confirmarray.getJSONObject(i);
                                            confirm = jsonObject.getString("booked_time");
                                            if (!confirm.equals("")) {
                                                JSONObject dataobj = jsonObject.getJSONObject("data");
                                                JSONArray splite_array = dataobj.getJSONArray("split_time");
                                                for (int k = 0; k < splite_array.length(); k++) {
                                                    JSONObject spliteobj = splite_array.getJSONObject(k);
                                                    String splite_time = spliteobj.getString("time");
                                                    mor_list.add(splite_time);
                                                    Log.d("morlist", splite_time);
                                                    DrAppointmenttime drbean = new DrAppointmenttime();
                                                    drbean.setBooktime(splite_time);
                                                    dr_arr.add(drbean);
                                                }
                                            }

                                        }
                                        for (int j = 0; j < dr_arr.size(); j++) {
                                            String str_dr = dr_arr.get(j).getBooktime();
                                            Log.d("StringAArryy", str_dr);
                                            if (arr_str.contains(str_dr)) {
                                                arr_str.remove(str_dr);
                                            }

                                        }
                                        Log.d("Array", arr_str.toString());
                                        for (int i = 0; i < arr_str.size(); i++) {
                                            String str_mine = arr_str.get(i);
                                            DrAppointmenttime drBean = new DrAppointmenttime();
                                            drBean.setBgroup(str_mine);
                                            list.add(drBean);
                                            Log.d("mine111", list.toString());
                                        }
                                        adapter = new GridviewAppointmentslotAdapter(list, TeacherProfileActivity.this, TeacherProfileActivity.this);
                                        morninggridview.setAdapter(adapter);
                                        morninggridview.setExpanded(true);
                                        try {
                                            for (int j = 0; j < afterarray.length(); j++) {
                                                JSONObject json = afterarray.getJSONObject(j);
                                                String afternoon = json.getString("afternoon_time");
                                                if (!afternoon.equals("")) {
                                                    String str_show = afternoon + " PM";
                                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                                                    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                                                    Date date1 = simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
                                                    Date date2 = simpleDateFormat.parse(str_show);
                                                    if (date2.before(date1)) {

                                                    } else {
                                                        arr_mine.add(str_show);
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            Log.d("RRRRR", "teacherprofileactivity  :  Exception: "+e);
                                        }
                                        for (int i = 0; i < dr_arr.size(); i++) {
                                            String str_dr = dr_arr.get(i).getBooktime();
                                            Log.d("StringAArryy", str_dr);
                                            if (arr_mine.contains(str_dr)) {
                                                arr_mine.remove(str_dr);
                                            }

                                        }
                                        for (int i = 0; i < arr_mine.size(); i++) {
                                            String str_mine = arr_mine.get(i);
                                            DrAppointmenttime drBean = new DrAppointmenttime();
                                            drBean.setBgroup(str_mine);
                                            list2.add(drBean);
                                            Log.d("mine111", list2.toString());
                                            adapter = new GridviewAppointmentslotAdapter(list2, TeacherProfileActivity.this, TeacherProfileActivity.this);
                                            afternoongridview.setAdapter(adapter);
                                            afternoongridview.setExpanded(true);

                                        }
                                        try {
                                            for (int j = 0; j < evenarray.length(); j++) {
                                                JSONObject json = evenarray.getJSONObject(j);
                                                String evening = json.getString("evening_time");
                                                if (!evening.equals("")) {
                                                    String str_show = evening + " PM";
                                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                                                    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                                                    Date date1 = simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
                                                    Date date2 = simpleDateFormat.parse(str_show);
                                                    if (date2.before(date1)) {

                                                    } else {
                                                        arr_eve.add(str_show);
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {

                                        }
                                        for (int i = 0; i < dr_arr.size(); i++) {
                                            String str_dr = dr_arr.get(i).getBooktime();
                                            Log.d("StringAArryy", str_dr);
                                            if (arr_eve.contains(str_dr)) {
                                                arr_eve.remove(str_dr);
                                            }
                                        }
                                        for (int j = 0; j < arr_eve.size(); j++) {
                                            String str_mine = arr_eve.get(j);
                                            DrAppointmenttime drBean = new DrAppointmenttime();
                                            drBean.setBgroup(str_mine);
                                            list3.add(drBean);
                                            Log.d("mine112", list3.toString());
                                            adapter1 = new GridviewAppointmentslotAdapter(list3, TeacherProfileActivity.this, TeacherProfileActivity.this);
                                            eveninggridview.setAdapter(adapter1);
                                            eveninggridview.setExpanded(true);
                                        }
                                    } else {
                                        for (int i = 0; i < morarray.length(); i++) {
                                            DrAppointmenttime bp = new DrAppointmenttime();
                                            JSONObject json = morarray.getJSONObject(i);
                                            String morning = json.getString("morning_time");
                                            bp.setBgroup(morning + " AM");
                                            bp.setBooktime("0.00 AM");
                                            list.add(bp);
                                            adapter = new GridviewAppointmentslotAdapter(list, TeacherProfileActivity.this, TeacherProfileActivity.this);
                                            morninggridview.setAdapter(adapter);
                                            morninggridview.setExpanded(true);
                                        }
                                        for (int j = 0; j < afterarray.length(); j++) {
                                            DrAppointmenttime bp = new DrAppointmenttime();
                                            JSONObject json = afterarray.getJSONObject(j);
                                            String afternoon = json.getString("afternoon_time");
                                            bp.setBgroup(afternoon + " PM");
                                            bp.setBooktime("0.00 AM");
                                            list2.add(bp);
                                            adapter = new GridviewAppointmentslotAdapter(list2, TeacherProfileActivity.this, TeacherProfileActivity.this);
                                            afternoongridview.setAdapter(adapter);
                                            afternoongridview.setExpanded(true);

                                        }
                                        for (int k = 0; k < evenarray.length(); k++) {
                                            DrAppointmenttime bp = new DrAppointmenttime();
                                            JSONObject json = evenarray.getJSONObject(k);
                                            String evening = json.getString("eveing_time");
                                            bp.setBgroup(evening + " PM");
                                            bp.setBooktime("0.00 AM");
                                            list3.add(bp);
                                            adapter = new GridviewAppointmentslotAdapter(list3, TeacherProfileActivity.this, TeacherProfileActivity.this);
                                            eveninggridview.setAdapter(adapter);
                                            eveninggridview.setExpanded(true);
                                        }
                                    }
                                    pDialog.dismiss();
                                } else {
                                    pDialog.dismiss();
                                    Toast.makeText(TeacherProfileActivity.this, msg, Toast.LENGTH_LONG).show();

                                }
                                pDialog.dismiss();
                            }
                        } catch (JSONException e1) {
                            Log.d("RRRRR", "teacherprofileactivity  :  Exception: "+e1);
                            AlertClass.alertDialogShow(TeacherProfileActivity.this, "" + e1.getMessage());
                            pDialog.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "teacherprofileactivity  :  error: "+error);
                        Toast.makeText(TeacherProfileActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("teacher_id", teacherid);
                params.put("date", appointment_date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(TeacherProfileActivity.this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    public void selectstarttime(String time) {
        if (!et_time.getText().toString().equals("")) {
            if (et_end_time.getVisibility() == View.VISIBLE) {
                if (et_time.getText().toString().equals(time)) {
                    Toast.makeText(TeacherProfileActivity.this, getString(R.string.selectnexthour), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                        Date date1 = simpleDateFormat.parse(et_time.getText().toString());
                        Date date2 = simpleDateFormat.parse(time);
                        if (date2.before(date1)) {
                            Toast.makeText(TeacherProfileActivity.this, getString(R.string.selectnexthour), Toast.LENGTH_SHORT).show();
                        } else {
                            et_end_time.setText(time);
                            im_closeendtime.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        } else if (et_time.getVisibility() == View.VISIBLE && et_time.getText().toString().equals("")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            calendar.add(Calendar.HOUR, 1);
            if (et_date.getText().toString().trim().equals(nDate)) {
                try {
                    Log.d("atime", "" + calendar.getTime());
                    Date date1 = simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
                    Date date2 = simpleDateFormat.parse(time);
                    if (date2.before(date1)) {
                        Toast.makeText(TeacherProfileActivity.this, "Please Select Time After 1 Hour.", Toast.LENGTH_SHORT).show();
                    } else if (!et_end_time.getText().toString().equals("")) {
                        try {
                            Date date3 = simpleDateFormat.parse(et_end_time.getText().toString());
                            Date date4 = simpleDateFormat.parse(time);
                            if (date3.before(date4)) {
                                Toast.makeText(TeacherProfileActivity.this, getString(R.string.selectasduration) + " " + duration + " " + getString(R.string.duration), Toast.LENGTH_SHORT).show();
                            } else if (et_end_time.getText().toString().equals(time)) {
                                Toast.makeText(TeacherProfileActivity.this, getString(R.string.selectasduration) + " " + duration + " " + getString(R.string.duration), Toast.LENGTH_SHORT).show();
                            } else {
                                et_time.setText(time);
                                im_closestarttime.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {

                        }

                    } else {
                        et_time.setText(time);
                        im_closestarttime.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {

                }
            } else {
                if (!et_end_time.getText().toString().equals("")) {
                    try {
                        Date date3 = simpleDateFormat.parse(et_end_time.getText().toString());
                        Date date4 = simpleDateFormat.parse(time);
                        if (date3.before(date4)) {
                            Toast.makeText(TeacherProfileActivity.this, getString(R.string.selectasduration) + " " + duration + " " + getString(R.string.duration), Toast.LENGTH_SHORT).show();
                        } else if (et_end_time.getText().toString().equals(time)) {
                            Toast.makeText(TeacherProfileActivity.this, getString(R.string.selectasduration) + " " + duration + " " + getString(R.string.duration), Toast.LENGTH_SHORT).show();
                        } else {
                            et_time.setText(time);
                            im_closestarttime.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {

                    }
                } else {
                    et_time.setText(time);
                    im_closestarttime.setVisibility(View.VISIBLE);
                }

            }
        }
    }



    public void callAPII(String fromid,String toid){
        HashMap<String,String> map=new HashMap<>();
        map.put("user_from_id",fromid);
        map.put("user_to_id",toid);

        Log.d("RRRR", "onVolleyResponse: "+map);
        new MineVolleyGlobal(TeacherProfileActivity.this).parseVollyStringRequest(ServiceClass.ChatConnection, 1, map, new MineVolleyListener() {
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
                    Log.d("RRRR", "onVolleyResponse: "+room_id);

                    Intent intent = new Intent(getApplicationContext(), FirebaseChatMessage.class);
                    intent.putExtra("sender_id", str_teacherid);
                    intent.putExtra("sender_name", name);
                    intent.putExtra("sender_username", username);
                    intent.putExtra("sender_pic", profilepic);
                    intent.putExtra("room_id", room_id);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }









}



















