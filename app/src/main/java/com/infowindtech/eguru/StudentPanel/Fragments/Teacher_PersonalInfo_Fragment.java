package com.infowindtech.eguru.StudentPanel.Fragments;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.infowindtech.eguru.StudentPanel.Adapter.TeacherInfoAdapter;
import com.infowindtech.eguru.StudentPanel.Model.TeacherInfo;
import com.infowindtech.eguru.StudentPanel.Model.TeacherUserIdSession;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.FragPersonalInfoTeacherBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Teacher_PersonalInfo_Fragment extends Fragment {
    FragPersonalInfoTeacherBinding binding;
    ArrayList<TeacherInfo> infolist;
    TeacherInfoAdapter adapter;
    String user_id,message,username,name,rate,address,city,country,zipcode,id,teacherid,classnamestr,classidstr,langcode,morning,afternoon,evening,days;
    TeacherUserIdSession teacherUserIdSession;
    ArrayList<String> classlist;
    UserSessionManager userSessionManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragPersonalInfoTeacherBinding.inflate(inflater,container,false);
        teacherUserIdSession = new TeacherUserIdSession(getActivity());
        final HashMap<String, String> user = teacherUserIdSession.getUserDetails();
        user_id=user.get(teacherUserIdSession.KEY_USERID);
        userSessionManager = new UserSessionManager(getActivity());
        HashMap<String,String> lang=userSessionManager.getLanguage();
        langcode=lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(),langcode);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        binding.lvInfo.setLayoutManager(MyLayoutManager);
        binding.lvInfo.setItemAnimator(new DefaultItemAnimator());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        MyLayoutManager.setAutoMeasureEnabled(true);
        binding.lvInfo.setNestedScrollingEnabled(false);
        binding.lvInfo.setHasFixedSize(false);
        GetteacherinfoTask();

        return binding.getRoot();
    }
    public void GetteacherinfoTask() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherProfileURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        infolist=new ArrayList<>();
                        classlist=new ArrayList<>();
                        try {
                            Log.d("RRRRRR", "teacherpersonalinfofragment  :  onResponse: "+response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            JSONObject obj=jsonObject.getJSONObject("data");
                            if (flag.equals("true")) {
                                teacherid=obj.getString("user_id");
                                username=obj.getString("username");
                                name=obj.getString("full_name");
                                rate=obj.getString("rating");
                                address=obj.getString("address");
                                city=obj.getString("city");
                                country=obj.getString("country");
                                zipcode=obj.getString("zipcode");
                                final String email = obj.getString("email");
                                final String contact_number = obj.getString("contact_number");
                                final JSONArray classes = obj.getJSONArray("class");
                                final String subject = obj.getString("subject_name");
                                final String availability_days = obj.getString("availability_days");
                                final String availability_morning = obj.getString("availability_morning");
                                final String availability_afternoon = obj.getString("availability_afternoon");
                                final String availability_evening = obj.getString("availability_evening");
                                JSONArray teaching_place = obj.getJSONArray("teaching_place");
                                final String duration = obj.getString("duration");
                                final String teacher_fees = obj.getString("teacher_fees");
                                final String institute_name = obj.getString("institute_name");
                                final String qualification = obj.getString("qualification");
                                final String year_of_passing = obj.getString("year_of_passing");
                                final String rating = obj.getString("rating");
                                final String nation=obj.getString("nationality");
                                if(availability_morning.equals("")){
                                    morning=getString(R.string.blankmor);
                                }else{
                                    String[] mor=obj.getString("availability_morning").split(",");
                                    String last_mor = obj.getString("availability_morning").substring(obj.getString("availability_morning").lastIndexOf(',') + 1);
                                    morning="Morning"+"("+mor[0]+" A.M "+" to "+" "+last_mor+" A.M "+")";
                                }
                                if(availability_afternoon.equals("")){
                                    afternoon=getString(R.string.blankafter);
                                }else{
                                    String[] after=obj.getString("availability_afternoon").split(",");
                                    String last_after = obj.getString("availability_afternoon").substring(obj.getString("availability_afternoon").lastIndexOf(',') + 1);
                                    afternoon="Afternoon"+"("+after[0]+" P.M "+" to "+" "+last_after+" P.M "+")";
                                }
                                if(availability_evening.equals("")){
                                    evening=getString(R.string.blankeve);
                                }else {
                                    String[] eve=obj.getString("availability_evening").split(",");
                                    String last_eve = obj.getString("availability_evening").substring(obj.getString("availability_evening").lastIndexOf(',') + 1);
                                    evening="Evening"+"("+eve[0]+" P.M "+" to "+" "+last_eve+" P.M "+")";
                                }
                                final String dayst=obj.getString("availability_days");
                                if(dayst.contains("Mon") || dayst.contains("Tue") || dayst.contains("Wed") || dayst.contains("Thu") || dayst.contains("Fri") || dayst.contains("Sat") || dayst.contains("Sun")){
                                    days=dayst.replace("Mon",getString(R.string.Mon));
                                    days=days.replace("Tue",getString(R.string.Tue));
                                    days=days.replace("Wed",getString(R.string.Wed));
                                    days=days.replace("Thu",getString(R.string.Thu));
                                    days=days.replace("Fri",getString(R.string.Fri));
                                    days=days.replace("Sat",getString(R.string.Sat));
                                    days=days.replace("Sun",getString(R.string.Sun));
                                }
                                else {
                                    days=obj.getString("availability_days");
                                }
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
                                infolist.add(new TeacherInfo(getString(R.string.fullname),name));
                                infolist.add(new TeacherInfo(getString(R.string.username),username));
                                infolist.add(new TeacherInfo(getString(R.string.email),email));
                                infolist.add(new TeacherInfo(getString(R.string.address),address+","+city+","+country+","+zipcode));
                                infolist.add(new TeacherInfo(getString(R.string.nationality),nation));
                                infolist.add(new TeacherInfo(getString(R.string.classes),classnamestr));
                                infolist.add(new TeacherInfo(getString(R.string.Subject),subject));
                                infolist.add(new TeacherInfo(getString(R.string.AvailabilityDays),days));
                                infolist.add(new TeacherInfo(getString(R.string.AvailabilityTime),morning+" "+afternoon+" "+evening));
                                infolist.add(new TeacherInfo(getString(R.string.duration),duration));
                                infolist.add(new TeacherInfo(getString(R.string.Fees),teacher_fees));
                                infolist.add(new TeacherInfo(getString(R.string.InstituteName),institute_name));
                                infolist.add(new TeacherInfo(getString(R.string.Qualification),qualification));
                                infolist.add(new TeacherInfo(getString(R.string.YearOfPassing),year_of_passing));
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
                                String strfromArrayList = sb.toString();
                                if (strfromArrayList.endsWith(",")) {
                                    strfromArrayList = strfromArrayList.substring(0,strfromArrayList.length() - 1);
                                }
                                Log.d("placeaaray",strfromArrayList);
                                infolist.add(new TeacherInfo(getString(R.string.StudyPlaces),strfromArrayList));
                                adapter=new TeacherInfoAdapter(infolist,getActivity());
                                binding.lvInfo.setAdapter(adapter);

                            }else if (flag.equals("false")){
                                AlertClass.alertDialogShow(getActivity(),msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRRR", "teacherpersonalinfofragment  :  exception: "+e);
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
                params.put("user_id",user_id);
                params.put("lang_code",langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
}
