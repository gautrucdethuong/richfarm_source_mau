package com.infowindtech.eguru.StudentPanel.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

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
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.PaypalRazorPaymentActivity;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Adapter.Class_Info_Adapter;
import com.infowindtech.eguru.StudentPanel.Model.ClassInfoData;

import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.FragStudentCalender1Binding;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Class_Info_Fragment extends Fragment {

    String appointmentId = "", fees = "";

    boolean backpress = false;

    FragStudentCalender1Binding binding;
    String message, user_id, selected_date, new_date, newuserid;
    UserSessionManager userSessionManager;
    ArrayList<ClassInfoData> classInfoDataArrayList;
    Class_Info_Adapter adapter;
    String date_month_year, day, newappointid, lastid, appointid;
    String flag = "abc", first, mDate, datejson = "";
    private Calendar _calendar;
    private int month, year;
    private static final String dateTemplate = "MMMM yyyy";
    private GridCellAdapter gridadapter;
    View savedView;
    String date = "0";
    Date cDate;
    List<String> appointdatelist;
    JSONArray appointmentjsonarray;
    public List<String> dayString, datelist;
    public GregorianCalendar geocal;
    int scroll = 0;
    Snackbar bar;
    String show_date, langcode;
    static Class_Info_Fragment class_info_fragment;
    String name;

    public static Class_Info_Fragment getInstance() {
        return class_info_fragment;
    }

    public Class_Info_Fragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragStudentCalender1Binding.inflate(inflater, container, false);
        userSessionManager = new UserSessionManager(getActivity());
        if (userSessionManager.signIn())
            getActivity().finish();

        class_info_fragment = this;

        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(), langcode);
        appointdatelist = new ArrayList<>();
        final HashMap<String, String> appointarray = userSessionManager.getAppointmentCalender();
        datejson = appointarray.get(userSessionManager.KEY_ArrayList);
        try {
            appointmentjsonarray = new JSONArray(datejson);
            for (int i = 0; i < appointmentjsonarray.length(); i++) {
                String date = appointmentjsonarray.getString(i);
                appointdatelist.add(date);
            }
        } catch (Exception e) {
        }

        binding.imDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StudentHomeActivity) getActivity()).callDrawer();
            }
        });

        newuserid = user_id;
        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        classInfoDataArrayList = new ArrayList<>();
        cDate = new Date();
        final String fDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(cDate);
        mDate = new SimpleDateFormat("MM", Locale.ENGLISH).format(cDate);
        selected_date = fDate;
        lastid = "";
        date = "0";
        AllAppointmentlist(lastid);
        show_date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(cDate);
        binding.tvDate.setText("");
        day = new SimpleDateFormat("dd", Locale.ENGLISH).format(cDate);
        binding.tvSelectedDate.setText("-");
        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.tvDate.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), getString(R.string.plzselectdate), Toast.LENGTH_SHORT).show();
                } else {
                    classInfoDataArrayList = new ArrayList<>();
                    if (selected_date.equals(fDate)) {
                        date = "1";
                        new_date = selected_date;
                        lastid = "";
                        Appointmentlist();
                        binding.tvOk.setTextColor(getResources().getColor(R.color.oreange));
                        binding.tvGetAll.setTextColor(getResources().getColor(R.color.black));
                        binding.tvDate.setText(show_date);
                        binding.tvSelectedDate.setText(day);
                    } else {
                        date = "1";
                        new_date = selected_date;
                        lastid = "";
                        Appointmentlist();
                        binding.tvOk.setTextColor(getResources().getColor(R.color.oreange));
                        binding.tvGetAll.setTextColor(getResources().getColor(R.color.black));
                    }
                }
            }
        });
        binding.tvGetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classInfoDataArrayList = new ArrayList<>();
                String show_date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(cDate);
                binding.tvDate.setText("");
                day = new SimpleDateFormat("dd", Locale.ENGLISH).format(cDate);
                binding.tvSelectedDate.setText("-");
                if (savedView != null) {
                    savedView.setBackgroundColor(getResources().getColor(R.color.white));
                }
                lastid = "";
                date = "0";
                selected_date = show_date;
                AllAppointmentlist(lastid);
                binding.tvOk.setTextColor(getResources().getColor(R.color.black));
                binding.tvGetAll.setTextColor(getResources().getColor(R.color.oreange));

            }
        });
        binding.tvHeading.setText(getString(R.string.classes));
        binding.currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));
        geocal = (GregorianCalendar) GregorianCalendar.getInstance(Locale.ENGLISH);
        binding.premonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (geocal.get(GregorianCalendar.MONTH) == geocal
                        .getActualMinimum(GregorianCalendar.MONTH)) {
                    geocal.set((geocal.get(GregorianCalendar.YEAR) - 1),
                            geocal.getActualMaximum(GregorianCalendar.MONTH), 1);
                } else {
                    geocal.set(GregorianCalendar.MONTH,
                            geocal.get(GregorianCalendar.MONTH) - 1);
                }
                gridadapter.refreshDays();
                gridadapter.notifyDataSetChanged();
                binding.currentMonth.setText(DateFormat.format("MMMM yyyy", geocal));
            }
        });
        binding.nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (geocal.get(GregorianCalendar.MONTH) == geocal
                        .getActualMaximum(GregorianCalendar.MONTH)) {
                    geocal.set((geocal.get(GregorianCalendar.YEAR) + 1),
                            geocal.getActualMinimum(GregorianCalendar.MONTH), 1);
                } else {
                    geocal.set(GregorianCalendar.MONTH,
                            geocal.get(GregorianCalendar.MONTH) + 1);
                }
                gridadapter.refreshDays();
                gridadapter.notifyDataSetChanged();
                binding.currentMonth.setText(DateFormat.format("MMMM yyyy", geocal));
            }
        });
        gridadapter = new GridCellAdapter(getActivity(), geocal);
        gridadapter.notifyDataSetChanged();
        binding.calendar.setAdapter(gridadapter);
        binding.calendar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.appbar.setExpanded(true);
        binding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                } else if (isShow) {
                    isShow = false;
                }
            }
        });
        NestedScrollView scroller = binding.scroll;
        final String TAG = "tag";
        if (scroller != null) {
            binding.scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY) {
                        //Log.i(TAG, "Scroll DOWN");
                        scroll = 0;
                    }
                    if (scrollY < oldScrollY) {
                        //Log.i(TAG, "Scroll UP");
                        scroll = 0;
                    }
                    if (scrollY == 0) {
                        Log.i(TAG, "TOP SCROLL");
                        scroll = 0;
                    }
                    if (scrollY == (view.getChildAt(0).getMeasuredHeight() - view.getMeasuredHeight())) {
                        scroll = 1;
                        bar = Snackbar.make(view, getString(R.string.pleasewait), Snackbar.LENGTH_LONG);
                        ViewGroup contentLay = (ViewGroup) bar.getView().findViewById(R.id.snackbar_text).getParent();
                        ProgressBar item = new ProgressBar(getActivity());
                        contentLay.addView(item, 0);
                        bar.show();
                        if (binding.lvStudentClasses.getLastVisiblePosition() == adapter.getCount() - 1) {
                            if (date.equals("1")) {
                                user_id = newuserid;
                                new_date = selected_date;
                                lastid = appointid;
                                Appointmentlist();
                            } else if (date.equals("0")) {
                                lastid = newappointid;
                                AllAppointmentlist(lastid);
                            }
                        }
                    }
                }
            });
        }

        binding.tvOpencal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // binding.scrollView.setVisibility(View.VISIBLE);
                binding.tvOpencal.setVisibility(View.GONE);
            }
        });
        return binding.getRoot();
    }

    public void clearlist() {
        classInfoDataArrayList.clear();
    }

    public void setdata() {
        if (date.equals("1")) {
            classInfoDataArrayList = new ArrayList<>();
            user_id = newuserid;
            date = "1";
            new_date = selected_date;
            lastid = "";
            Appointmentlist();
        } else if (date.equals("0")) {
            classInfoDataArrayList = new ArrayList<>();
            user_id = newuserid;
            binding.tvDate.setText("");
            day = new SimpleDateFormat("dd", Locale.ENGLISH).format(cDate);
            binding.tvSelectedDate.setText("-");
            lastid = "";
            date = "0";
            AllAppointmentlist(lastid);
        }
    }

    //1

    public void Appointmentlist() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.StudentHireListURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRRR", "classinfofragment  app:  onResponse: " + response);

                            if (lastid.equals("")) {
                                classInfoDataArrayList.clear();
                            }

                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {


                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("lenght", "" + jsonArray.length());

                                binding.tvNoclass.setVisibility(View.GONE);
                                binding.lvStudentClasses.setVisibility(View.VISIBLE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String fullname = object.getString("full_name");
                                    String date_of_appointment = object.getString("date_of_appointment");
                                    String timeappointment = object.getString("time_appointment");
                                    String status = object.getString("status");
                                    String teacherid = object.getString("teacher_id");
                                    appointid = object.getString("appointment_id");
                                    String ratingstatus = object.getString("teacher_rating");
                                    String fees = object.getString("teacher_fees");
                                    String pay_status = object.getString("payment_status");
                                    String pic = object.getString("profile_image");
                                    String is_online = object.getString("is_online");
                                    String room_id = object.getString("room_id");



                                    //String teacher_quality = object.getString("user_teacher_quality");
                                    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                    SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                    Date oneWayTripDate = input.parse(date_of_appointment);
                                    String new_order_date = output.format(oneWayTripDate);
                                    ClassInfoData cd = new ClassInfoData();
                                    cd.setSubject(fullname);
                                    cd.setDate(new_order_date);
                                    cd.setTime(timeappointment);
                                    cd.setId(status);
                                    cd.setTeacher_id(teacherid);
                                    cd.setAppointid(appointid);
                                    cd.setRatingstauts(ratingstatus);
                                    cd.setPayStatus(pay_status);
                                    cd.setUser_pic(pic);
                                    cd.setStatus(fees);
                                    cd.setIs_online(is_online);
                                    cd.setRoom_id(room_id);


                                    //cd.setTeacher_quality(teacher_quality);
                                    classInfoDataArrayList.add(cd);
                                    adapter = new Class_Info_Adapter(classInfoDataArrayList, getActivity(), Class_Info_Fragment.this);
                                    binding.lvStudentClasses.setAdapter(adapter);

                                    setListViewHeightBasedOnChildren(binding.lvStudentClasses);
                                    if (classInfoDataArrayList.size() >= 10) {
                                        binding.lvStudentClasses.setSelection(classInfoDataArrayList.size() - 10);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                lastid = appointid;


                                if (jsonArray.length() == 0 && classInfoDataArrayList.size() == 0) {
                                    binding.lvStudentClasses.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), getString(R.string.noclassavailable), Toast.LENGTH_SHORT).show();
                                } else if (jsonArray.length() == 0 && classInfoDataArrayList.size() != 0) {
                                    bar.dismiss();
                                    Toast.makeText(getActivity(), getString(R.string.nomore), Toast.LENGTH_SHORT).show();
                                }

                            } else if (flag.equals("false")) {


//                                if(classInfoDataArrayList.size()!=0){
//                                    Toast.makeText(getActivity(),  getString(R.string.nomore), Toast.LENGTH_SHORT).show();
//                                }else {
//                                    binding.lvStudentClasses.setVisibility(View.GONE);
//                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
//                                }
//                            }
                                Log.d("Ppopopo", "onResponse: " + classInfoDataArrayList.size());

                                if (lastid.equals("")) {
                                    binding.lvStudentClasses.setVisibility(View.GONE);
                                    binding.tvNoclass.setVisibility(View.VISIBLE);
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                                } else if (classInfoDataArrayList.size() != 0) {
                                    binding.scroll.setVisibility(View.GONE);
                                    binding.tvNoclass.setVisibility(View.GONE);

                                    Toast.makeText(getActivity(), getString(R.string.nomore), Toast.LENGTH_SHORT).show();

                                }
                            }


                        } catch (Exception e) {
                            Log.d("RRRRRR", "classinfofragment  :  exception: " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                params.put("student_id", user_id);
                params.put("status", "");
                params.put("date_of_appointment", new_date);
                params.put("time_of_appointment", "");
                params.put("last_id", lastid);
                params.put("lang_code", langcode);
                Log.d("PPPPP", "getParams: " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    //dollor
    public void AllAppointmentlist(final String last) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.StudentHireListURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        Log.d("RRRRRR", "classinfofragment all  :  onResponse: " + response);

                        if (last.equals("")) {
                            classInfoDataArrayList.clear();
                        }


                        try {
                            jsonObject = new JSONObject(response);

                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                binding.tvNoclass.setVisibility(View.GONE);
                                binding.lvStudentClasses.setVisibility(View.VISIBLE);
                                binding.tvGetAll.setTextColor(getResources().getColor(R.color.oreange));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String fullname = object.getString("full_name");
                                    String date_of_appointment = object.getString("date_of_appointment");
                                    String timeappointment = object.getString("time_appointment");
                                    String pic = object.getString("profile_image");
                                    String status = object.getString("status");
                                    String teacherid = object.getString("teacher_id");
                                    newappointid = object.getString("appointment_id");
                                    String pay_status = object.getString("payment_status");
                                    String ratingstatus = object.getString("teacher_rating");
                                    String fees = object.getString("teacher_fees");
                                    String is_online = object.getString("is_online");
                                    String room_id = object.getString("room_id");


                                    String pastDate = "";
//
//                                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//                                    int date = date_of_appointment.compareTo(currentDate);
//                                    Log.d("DATE", "getView: " + date);
//                                    switch (date) {
//                                        case -1: //date1<date2 = -1
//                                            pastDate = "-1";
//                                            break;
//                                        case 1: //date1>date2 = 1
//                                            pastDate = "1";
//                                            break;
//
//
//                                        case 0: //date1==date2= 0
//                                            pastDate = "0";
//                                            break;
//
//                                        default:
//                                            pastDate ="";
//                                            break;
//                                    }
//
//                                    String pastDate = "";
//                                    String valid_until = date_of_appointment;
//                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
//                                    Date strDate = null;
//                                    try {
//                                        strDate = sdf.parse(valid_until);
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    Log.d("TAG", "onResponse: "+new Date()+"  "+date_of_appointment+"     "+sdf+"  "+strDate);
//
//                                    if (Calendar.getInstance().after(strDate)) {
//                                        pastDate = "1";
//                                    } else if (Calendar.getInstance().before(strDate)) {
//                                        pastDate = "-1";
//                                    } else if (Calendar.getInstance().equals(strDate)) {
//                                        pastDate = "0";
//                                    } else {
//                                        pastDate = "";
//                                    }





                                    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                    SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                    Date oneWayTripDate = input.parse(date_of_appointment);
                                    String new_order_date = output.format(oneWayTripDate);
                                    ClassInfoData cd = new ClassInfoData();
                                    cd.setSubject(fullname);
                                    cd.setDate(new_order_date);
                                    cd.setTime(timeappointment);
                                    cd.setId(status);
                                    cd.setTeacher_id(teacherid);
                                    cd.setAppointid(newappointid);
                                    cd.setRatingstauts(ratingstatus);
                                    cd.setPayStatus(pay_status);
                                    cd.setUser_pic(pic);
                                    cd.setStatus(fees);
                                    cd.setIs_online(is_online);
                                    cd.setRoom_id(room_id);

                                    classInfoDataArrayList.add(cd);
                                    adapter = new Class_Info_Adapter(classInfoDataArrayList, getActivity(), Class_Info_Fragment.this);
                                    binding.lvStudentClasses.setAdapter(adapter);

                                    setListViewHeightBasedOnChildren(binding.lvStudentClasses);
                                    if (classInfoDataArrayList.size() >= 10) {
                                        binding.lvStudentClasses.setSelection(classInfoDataArrayList.size() - 10);
                                    }
                                }
                                Log.d("PPPPPPP", "onResponse: " + newappointid);
                                adapter.notifyDataSetChanged();
                                lastid = newappointid;
                                Log.d("CCCCCCCParaa", "api: " + lastid);
                                if (jsonArray.length() == 0 && classInfoDataArrayList.size() == 0) {
                                    binding.tvNoclass.setVisibility(View.VISIBLE);
                                    binding.lvStudentClasses.setVisibility(View.GONE);
                                    bar.dismiss();
                                }
                                if (jsonArray.length() == 0 && classInfoDataArrayList.size() != 0) {
                                    bar.dismiss();
                                    Toast.makeText(getActivity(), getString(R.string.nomore), Toast.LENGTH_SHORT).show();
                                }

                               /* Log.d("lastid",lastid);
                                for(int k=0;k<classInfoDataArrayList.size();k++){
                                    String id=classInfoDataArrayList.get(k).getAppointid();
                                    String date=classInfoDataArrayList.get(k).getDate();
                                    Log.d("class",id+","+date);
                                }
                                Log.d("lenght",String.valueOf(classInfoDataArrayList.size()));*/

                            } else if (flag.equals("false")) {

                                Log.d("Ppopopo", "onResponse: " + classInfoDataArrayList.size());

                                if (lastid.equals("")) {
                                    binding.lvStudentClasses.setVisibility(View.GONE);
                                    binding.tvNoclass.setVisibility(View.VISIBLE);
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                                } else if (classInfoDataArrayList.size() == 0) {
                                    binding.scroll.setVisibility(View.GONE);
                                    binding.tvNoclass.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), getString(R.string.nomore), Toast.LENGTH_SHORT).show();

                                }
                            }

                        } catch (Exception e) {
                            Log.d("RRRRRR", "classinfofragment  :  exception: " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                params.put("student_id", user_id);
                params.put("status", "");
                params.put("date_of_appointment", "");
                params.put("time_of_appointment", "");
                params.put("last_id", last);
                params.put("lang_code", langcode);
                Log.d("CCCCCCCParaa", "getParams: " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);

    }

//https://edu.framework.infowindtech.biz/demo/?action=student_hire_list

    //dollor
    public class GridCellAdapter extends BaseAdapter {
        private TextView gridcell;
        private Context mContext;
        private Calendar monthn;
        public GregorianCalendar pmonth; // calendar instance for previous month
        public GregorianCalendar pmonthmaxset;
        private GregorianCalendar selectedDate;
        int firstDay;
        int maxWeeknumber;
        int maxP;
        int calMaxP;
        int mnthlength;
        String itemvalue, curentDateString;
        java.text.DateFormat df;
        String date;
        private ArrayList<String> items;

        public GridCellAdapter(Context context, GregorianCalendar monthCalendar) {
            super();
            dayString = new ArrayList<String>();
            monthn = monthCalendar;
            selectedDate = (GregorianCalendar) monthCalendar.clone();
            mContext = context;
            monthn.set(GregorianCalendar.DAY_OF_MONTH, 1);


            this.items = new ArrayList<String>();
            df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            curentDateString = df.format(selectedDate.getTime());
            datelist = new ArrayList<>();
            refreshDays();
        }

        public String getItem(int position) {
            return dayString.get(position);
        }

        @Override
        public int getCount() {
            return dayString.size();
        }

        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            return map;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.calender_day_gridcell, parent, false);
            }
            gridcell = row.findViewById(R.id.calendar_day_gridcell);
            String[] day_color = dayString.get(position).split("-");
            String theday = day_color[2];
            String themonth = day_color[1];
            String theyear = day_color[0];
            String fulldate = theday + "-" + themonth + "-" + theyear;
            datelist.add(fulldate);
            String gridvalue = day_color[2].replaceFirst("^0*", "");
            if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
                gridcell.setTextColor(Color.GRAY);
                gridcell.setClickable(false);
                gridcell.setFocusable(false);
            } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
                gridcell.setTextColor(Color.GRAY);
                gridcell.setClickable(false);
                gridcell.setFocusable(false);
            } else {
                gridcell.setTextColor(Color.BLACK);
            }
            if (day_color[2].equals(day) && themonth.equals(mDate)) {
                gridcell.setTextColor(getResources().getColor(R.color.oreange));
            }
            gridcell.setText(gridvalue);
            date = dayString.get(position);

            if (date.length() == 1) {
                date = "0" + date;
            }
            String monthStr = "" + (monthn.get(GregorianCalendar.MONTH) + 1);
            if (monthStr.length() == 1) {
                monthStr = "0" + monthStr;
            }
            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            for (int n = 0; n < datelist.size(); n++) {
                for (int m = 0; m < appointdatelist.size(); m++) {
                    String[] appointarray = appointdatelist.get(m).split("-");
                    String firstarray = appointarray[0];
                    String secondarray = appointarray[1];
                    String thirdarray = appointarray[2];
                    if (firstarray.equals(day_color[2]) && secondarray.equals(day_color[1]) && thirdarray.equals(day_color[0])) {
                        gridcell.setTextColor(getResources().getColor(R.color.green));
                    }
                }
            }
            gridcell.setBackgroundColor(getResources().getColor(R.color.white));
            gridcell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    date_month_year = (String) view.getTag();
                    binding.tvDate.setText(date_month_year);
                    String[] parts = date_month_year.split("-");
                    first = parts[0];
                    String second = parts[1];
                    String third = parts[2];
                    binding.tvSelectedDate.setText(first);
                    selected_date = third + "-" + second + "-" + first;
                    view.setBackgroundColor(getResources().getColor(R.color.blue_circle_in_image));

                    Log.d("AAAAAAAAAAA", "onClick: " + selected_date);

                    if (selected_date.equals(first)) {
                        date = "1";
                        new_date = selected_date;
                        lastid = "";
                        Appointmentlist();
                        binding.tvOk.setTextColor(getResources().getColor(R.color.oreange));
                        binding.tvGetAll.setTextColor(getResources().getColor(R.color.black));
                        binding.tvDate.setText(show_date);
                        binding.tvSelectedDate.setText(day);
                    } else {
                        date = "1";
                        new_date = selected_date;
                        lastid = "";
                        Appointmentlist();
                        binding.tvOk.setTextColor(getResources().getColor(R.color.oreange));
                        binding.tvGetAll.setTextColor(getResources().getColor(R.color.black));
                    }


                    if (savedView == null) {
                        savedView = view;
                    } else {
                        savedView.setBackgroundColor(getResources().getColor(R.color.white));
                        savedView = view;
                    }
                }
            });
            return row;
        }

        public void refreshDays() {
            items.clear();
            dayString.clear();
            Locale.getDefault();
            pmonth = (GregorianCalendar) monthn.clone();
            firstDay = monthn.get(GregorianCalendar.DAY_OF_WEEK);
            maxWeeknumber = monthn.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
            mnthlength = maxWeeknumber * 7;
            maxP = getMaxP(); // previous month maximum day 31,30....
            calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,corr ...
            pmonthmaxset = (GregorianCalendar) pmonth.clone();
            pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);
            for (int n = 0; n < mnthlength; n++) {
                itemvalue = df.format(pmonthmaxset.getTime());
                pmonthmaxset.add(GregorianCalendar.DATE, 1);
                dayString.add(itemvalue);
            }
        }

        private int getMaxP() {
            int maxP;
            if (monthn.get(GregorianCalendar.MONTH) == monthn
                    .getActualMinimum(GregorianCalendar.MONTH)) {
                pmonth.set((monthn.get(GregorianCalendar.YEAR) - 1),
                        monthn.getActualMaximum(GregorianCalendar.MONTH), 1);
            } else {
                pmonth.set(GregorianCalendar.MONTH,
                        monthn.get(GregorianCalendar.MONTH) - 1);
            }
            maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

            return maxP;
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public void onItemClick(final String appointid, final String fee, final String name) {

        appointmentId = appointid;
        fees = fee;
        this.name = name;

        Intent i = new Intent(getContext(), PaypalRazorPaymentActivity.class);
        i.putExtra("user_id", user_id);
        i.putExtra("appoint_id", appointmentId);
        i.putExtra("fee", fees);
        i.putExtra("name", name);
        startActivity(i);


//        final Dialog dialog_selected = new Dialog(getContext());
//
//        layoutBinder = DataBindingUtil.inflate(LayoutInflater.from(dialog_selected.getContext()), R.layout.layout_selection_dialog, null, false);
//        dialog_selected.setContentView(layoutBinder.getRoot());
//        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
//        lWindowParams.copyFrom(dialog_selected.getWindow().getAttributes());
//        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        dialog_selected.getWindow().setAttributes(lWindowParams);
//        dialog_selected.show();
//
//
//        layoutBinder.tvPaypal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog_selected.dismiss();
//                PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
//                Intent intent = new Intent(getActivity(), PaymentActivity.class);
//                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
//            }
//        });
//
//
//        layoutBinder.tvRazor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog_selected.dismiss();
//                Intent i = new Intent(getContext(),RazorPaymentActivity.class);
//                i.putExtra("user_id",user_id);
//                i.putExtra("appoint_id",appointmentId);
//                startActivity(i);
//            }
//        });


    }


//    private PayPalPayment getThingToBuy(String paymentIntent) {
//        return new PayPalPayment(new BigDecimal("0.01"), "USD", "sample item",
//                paymentIntent);
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == REQUEST_CODE_PAYMENT) {
//            if (resultCode == RESULT_OK) {
//                PaymentConfirmation confirm =
//                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//                if (confirm != null) {
//                    try {
//                        Log.i(TAG, confirm.toJSONObject().toString(4));
//                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
//                        String mTransactionState = confirm.getProofOfPayment().getState();
//                        if (mTransactionState != null && mTransactionState.equalsIgnoreCase("approved")) {
//
//                            String transactionId = confirm.getProofOfPayment().getTransactionId();
//                            //  String appointmentId = confirm.getProofOfPayment().getPaymentId();
//                            String time = confirm.getProofOfPayment().getCreateTime();
//                            String status = confirm.getProofOfPayment().getState();
//                            String amount = confirm.getPayment().toJSONObject().getString("amount");
//                            String currency = confirm.getPayment().toJSONObject().getString("currency_code");
//                            String dateStr = getConvertDate(time);
//
//                            callOrderNowService(user_id, appointmentId, transactionId, amount, status, currency, dateStr);
//
//                        }
//
//
//                    } catch (JSONException e) {
//                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
//                    }
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Log.i(TAG, "The user canceled.");
//            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//                Log.i(TAG, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
//            }
//        }
//
//    }
//
//    private String getConvertDate(String date_server) {
//        String serverdateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
//        SimpleDateFormat formatter = new SimpleDateFormat(serverdateFormat, Locale.UK);
//        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
//        Date value = null;
//        try {
//            value = formatter.parse(date_server);
//
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        df.setTimeZone(TimeZone.getDefault());
//        String formattedDate = df.format(value);
//        return formattedDate;
//    }


//    public void callOrderNowService(final String user_id, final String appointmentId, final String transactionId, final String amount, final String status, String currency, final String dateStr) {
//        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.order,
//                new Response.Listener<String>() {
//                    @RequiresApi(api = Build.VERSION_CODES.M)
//                    @Override
//                    public void onResponse(String response) {
//                        JSONObject jsonObject = null;
//                        try {
//                            Log.d("RRRRR", "classinfofragment  :  response: " + response);
//                            jsonObject = new JSONObject(response);
//                            String flag = jsonObject.getString("status");
//                            final String msg = jsonObject.getString("message");
//                            if (flag.equals("true")) {
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
//
//                            } else if (flag.equals("false")) {
//                                AlertClass.alertDialogShow(getActivity(), msg);
//                            }
//                        } catch (Exception e) {
//                            Log.d("RRRRR", "classinfofragment  :  exception: " + e);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if (error instanceof NetworkError) {
//                            message = getString(R.string.networkerror);
//                            AlertClass.DispToast(getActivity(), message);
//                        } else if (error instanceof ServerError) {
//                            message = getString(R.string.servererror);
//                            AlertClass.DispToast(getActivity(), message);
//                        } else if (error instanceof AuthFailureError) {
//                            message = getString(R.string.networkerror);
//                            AlertClass.DispToast(getActivity(), message);
//                        } else if (error instanceof ParseError) {
//                            message = getString(R.string.parsingerror);
//                            AlertClass.DispToast(getActivity(), message);
//                        } else if (error instanceof NoConnectionError) {
//                            message = getString(R.string.networkerror);
//                            AlertClass.DispToast(getActivity(), message);
//                        } else if (error instanceof TimeoutError) {
//                            message = getString(R.string.networkerror);
//                            AlertClass.DispToast(getActivity(), message);
//                        }
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("user_id", user_id);
//                params.put("appointment_id", appointmentId);
//                params.put("transaction_id", transactionId);
//                params.put("amount", amount);
//                params.put("type", "paypal");
//                params.put("status", status);
//                params.put("created_at", dateStr);
//                return params;
//
//
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        stringRequest.setShouldCache(true);
//        requestQueue.add(stringRequest);
//    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of LoginFragment");
        super.onResume();
        // AllAppointmentlist();
    }
}

