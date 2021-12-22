package com.infowindtech.eguru.TeacherPanel.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
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
import com.infowindtech.eguru.TeacherPanel.Adapters.Teacher_classInfo_Adapter;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.FragTeacherCalenderBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Teacher_Calender_Fragment extends Fragment{
    FragTeacherCalenderBinding binding;
    String message,user_id,selected_date,new_date;
    UserSessionManager userSessionManager;
    ArrayList<ClassInfoData> classInfoDataArrayList;
    Teacher_classInfo_Adapter adapter;
    String date_month_year,day,lastid,mDate,appointment_id,langcode;
    String flag ="abc",first,last_id,date="0",newuserid,newappointid,datejson="",lastId="";
    private Calendar _calendar;
    private int month, year;
    private static final String dateTemplate = "MMMM yyyy";
    private GridCellAdapter gridadapter;
    View savedView;
    Date cDate;
    String show_date ;
    List<String> appointdatelist;
    JSONArray appointmentjsonarray;
    public GregorianCalendar geocal;
    public   List<String> dayString,datelist;
    int scroll=0;
    Snackbar bar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragTeacherCalenderBinding.inflate(inflater,container,false);
        userSessionManager = new UserSessionManager(getActivity());
        if (userSessionManager.signIn())
            getActivity().finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id=user.get(userSessionManager.KEY_USERID);
        HashMap<String,String> lang=userSessionManager.getLanguage();
        langcode=lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(),langcode);
        newuserid=user_id;
        appointdatelist = new ArrayList<>();
        final HashMap<String, String> appointarray = userSessionManager.getAppointmentCalender();
        datejson = appointarray.get(userSessionManager.KEY_ArrayList);
        try{
                appointmentjsonarray = new JSONArray(datejson);
                for (int i = 0; i < appointmentjsonarray.length(); i++){
                    String date = appointmentjsonarray.getString(i);
                    appointdatelist.add(date);
                }
            } catch (Exception e) {
            }

        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        classInfoDataArrayList=new ArrayList<>();
        cDate = new Date();
        final String fDate = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(cDate);
        mDate = new SimpleDateFormat("MM",Locale.ENGLISH).format(cDate);
        selected_date=fDate;
        lastid="";
        AllAppointmentlist();
        if(classInfoDataArrayList.size()==0){
            binding.tvNoclass.setVisibility(View.VISIBLE);
        }
        show_date= new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH).format(cDate);
        binding.tvDate.setText("");
        day= new SimpleDateFormat("dd",Locale.ENGLISH).format(cDate);
        binding.tvSelectedDate.setText("-");
        binding.tvOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (binding.tvDate.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), getString(R.string.plzselectdate), Toast.LENGTH_SHORT).show();
                } else {
                    if (selected_date.equals(fDate)) {
                        classInfoDataArrayList = new ArrayList<>();
                        newuserid = user_id;
                        date = "1";
                        new_date = selected_date;
                        lastid = "";
                        Appointmentlist();
                        binding.tvOk.setTextColor(getResources().getColor(R.color.oreange));
                        binding.tvGetAll.setTextColor(getResources().getColor(R.color.black));
                        binding.tvDate.setText(show_date);
                        binding.tvSelectedDate.setText(day);
                    } else {
                        classInfoDataArrayList = new ArrayList<>();
                        newuserid = user_id;
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
                String show_date = new SimpleDateFormat("dd-MM-yyyy").format(cDate);
                binding.tvDate.setText("");
                day = new SimpleDateFormat("dd", Locale.ENGLISH).format(cDate);
                binding.tvSelectedDate.setText("-");
                if (savedView != null) {
                    savedView.setBackgroundColor(getResources().getColor(R.color.white));
                }
                lastid = "";
                date = "0";
                AllAppointmentlist();
                binding.tvOk.setTextColor(getResources().getColor(R.color.black));
                binding.tvGetAll.setTextColor(getResources().getColor(R.color.oreange));
            }
        });
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
                binding.currentMonth.setText(android.text.format.DateFormat.format("MMMM yyyy", geocal));
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
                binding.currentMonth.setText(android.text.format.DateFormat.format("MMMM yyyy", geocal));

            }
        });
        gridadapter = new GridCellAdapter(getActivity(), geocal);
        binding.calendar.setAdapter(gridadapter);
        binding.calendar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        binding.calendar.setSelection(R.drawable.selector);
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
                        if (binding.lvClassess.getLastVisiblePosition() == adapter.getCount() - 1) {
                            if (date.equals("1")) {
                                user_id = newuserid;
                                new_date = selected_date;
                                lastid = lastId;
                                Log.d("LLLLLLL", "onScrollChange: " + lastid);
                                Appointmentlist();
                            } else if (date.equals("0")) {
                                user_id = newuserid;
                                lastid = lastId;
                                Log.d("LLLLLLL", "onScrollChange: " + lastid);
                                AllAppointmentlist();
                            }
                        }

                    }
                }
            });
        }
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
            binding.tvDate.setText("-");
            day = new SimpleDateFormat("dd", Locale.ENGLISH).format(cDate);
            binding.tvSelectedDate.setText("");
            lastid = "";
            date = "0";
            AllAppointmentlist();
        }

    }

    public class GridCellAdapter extends BaseAdapter {
        private TextView gridcell;
        private Context mContext;
        private java.util.Calendar monthn;
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
            //Locale.getDefault();
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
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.calender_day_gridcell, parent, false);
            }
            gridcell = row.findViewById(R.id.calendar_day_gridcell);
            String[] day_color = dayString.get(position).split("-");
            final String theday = day_color[2];
            String themonth = day_color[1];
            String theyear = day_color[0];
            String fulldate = theday + "-" + themonth + "-" + theyear;
            datelist.add(fulldate);
            final String gridvalue = day_color[2].replaceFirst("^0*", "");
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


                    if (selected_date.equals(first)) {
                        classInfoDataArrayList = new ArrayList<>();
                        newuserid = user_id;
                        date = "1";
                        new_date = selected_date;
                        lastid = "";
                        Appointmentlist();
                        binding.tvOk.setTextColor(getResources().getColor(R.color.oreange));
                        binding.tvGetAll.setTextColor(getResources().getColor(R.color.black));
                        binding.tvDate.setText(show_date);
                        binding.tvSelectedDate.setText(day);
                    } else {
                        classInfoDataArrayList = new ArrayList<>();
                        newuserid = user_id;
                        date = "1";
                        new_date = selected_date;
                        lastid = "";
                        Appointmentlist();
                        binding.tvOk.setTextColor(getResources().getColor(R.color.oreange));
                        binding.tvGetAll.setTextColor(getResources().getColor(R.color.black));
                    }

                    Log.d("text", "onClick: " + gridvalue + "  " + theday + "   " + gridcell + "    " + first);
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

    public void AllAppointmentlist() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.UnconfirmConfirmHistoryURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "teachercalenderfragment all: response" + response);
                            if (lastid.equals("")) {
                                classInfoDataArrayList.clear();
                            }

                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            if (flag.equals("true")) {
                                binding.tvNoclass.setVisibility(View.GONE);
                                binding.lvClassess.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                if (jsonArray.length() == 0 && classInfoDataArrayList.size() != 0) {
                                    bar.dismiss();
                                    Toast.makeText(getActivity(), getString(R.string.nomore), Toast.LENGTH_SHORT).show();
                                } else {
                                    binding.tvGetAll.setTextColor(getResources().getColor(R.color.oreange));
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String user_id=object.getString("user_id");
                                        appointment_id=object.getString("appointment_id");
                                        String fullname = object.getString("full_name");
                                        String date_of_appointment = object.getString("date_of_apt");
                                        String timeappointment = object.getString("time_apt");
                                        String profile_image = object.getString("profile_image");
                                        String pay_status = object.getString("payment_status");
                                        String status = object.getString("appointment_status");
                                        String roomId = object.getString("room_id");
                                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                                        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
                                        Date oneWayTripDate = input.parse(date_of_appointment);
                                        String new_order_date = output.format(oneWayTripDate);
                                        ClassInfoData cd = new ClassInfoData();
                                        cd.setId(user_id);
                                        cd.setUser_pic(profile_image);
                                        cd.setSubject(fullname);
                                        cd.setDate(new_order_date);
                                        cd.setTime(timeappointment);
                                        cd.setPayStatus(pay_status);
                                        cd.setAppointid(appointment_id);
                                        cd.setStatus(status);
                                        cd.setRoom_id(roomId);
                                        classInfoDataArrayList.add(cd);
                                        lastId = appointment_id ;
                                        Log.d("LLLLLLL", "api: "+lastId+"   "+cd.getRoom_id());
                                        adapter = new Teacher_classInfo_Adapter(classInfoDataArrayList, getActivity(), Teacher_Calender_Fragment.this);
                                        binding.lvClassess.setAdapter(adapter);
                                        setListViewHeightBasedOnChildren(binding.lvClassess);
                                        if (classInfoDataArrayList.size() >= 10) {
                                            binding.lvClassess.setSelection(classInfoDataArrayList.size() - 10);
                                        }
                                    }
                                    lastid=appointment_id;
                                }

                            }else if (flag.equals("false")){
                                if(classInfoDataArrayList.size()==0) {
                                    binding.tvNoclass.setVisibility(View.VISIBLE);
                                }
                                else {
                                    binding.tvNoclass.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "teachercalenderfragment: exception" + e);
                            //AlertClass.alertDialogShow(getActivity(),e.getMessage());
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
                params.put("teacher_id", newuserid);
                params.put("status", "");
                params.put("last_id", lastid);
                params.put("date", "");
                params.put("lang_code", langcode);
                Log.d("LLLLLLL", "api: " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
    public void Appointmentlist(){
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.UnconfirmConfirmHistoryURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "teachercalenderfragment appo: response" + response);
                            if (lastid.equals("")) {
                                classInfoDataArrayList.clear();
                            }
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            if (flag.equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                binding.lvClassess.setVisibility(View.VISIBLE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String user_id = object.getString("user_id");
                                    newappointid = object.getString("appointment_id");
                                    String fullname = object.getString("full_name");
                                    String date_of_appointment = object.getString("date_of_apt");
                                    String timeappointment = object.getString("time_apt");
                                    String profile_image = object.getString("profile_image");
                                    String status = object.getString("appointment_status");
                                    String pay_status = object.getString("payment_status");
                                    String room_id = object.getString("room_id");
                                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                                        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
                                        Date oneWayTripDate = input.parse(date_of_appointment);
                                        String new_order_date = output.format(oneWayTripDate);
                                        ClassInfoData cd = new ClassInfoData();
                                        cd.setId(user_id);
                                        cd.setUser_pic(profile_image);
                                        cd.setSubject(fullname);
                                        cd.setDate(new_order_date);
                                        cd.setPayStatus(pay_status);
                                        cd.setTime(timeappointment);
                                        cd.setAppointid(newappointid);
                                        cd.setStatus(status);
                                        cd.setRoom_id(room_id);
                                        classInfoDataArrayList.add(cd);
                                        adapter = new Teacher_classInfo_Adapter(classInfoDataArrayList, getActivity(), Teacher_Calender_Fragment.this);
                                        binding.lvClassess.setAdapter(adapter);
                                        setListViewHeightBasedOnChildren(binding.lvClassess);
                                        if (classInfoDataArrayList.size() >= 10){
                                            binding.lvClassess.setSelection(classInfoDataArrayList.size() - 10);
                                        }
                                    }
                                    lastid = newappointid;
                            }else if (flag.equals("false")){
                                if(classInfoDataArrayList.size()==0){
                                    String str_msg =jsonObject.getString("message");
                                    Toast.makeText(getActivity(),str_msg, Toast.LENGTH_SHORT).show();
                                    binding.lvClassess.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "teachercalenderfragment: exception" + e);
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
                params.put("teacher_id", newuserid);
                params.put("status", "");
                params.put("last_id", lastid);
                params.put("date", new_date);
                params.put("lang_code", langcode);
                Log.d("llll", "getParams: " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
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
}
