package com.infowindtech.eguru.StudentPanel.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.infowindtech.eguru.MainActivity;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Adapter.Student_Subject_Registration_Adapter;
import com.infowindtech.eguru.StudentPanel.Model.ExpandedSubjectData;
import com.infowindtech.eguru.StudentPanel.Model.TeacherInfo;
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.TeacherPanel.Adapters.UserTypeAdapter;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.TeacherPanel.Models.UserType;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.FragStudentNextRegistrationBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class Student_Next_Registation extends Fragment {
    FragStudentNextRegistrationBinding binding;
    String userid, usertype, message, subjects, classes, selsubject, selclass, responseText1, responseid, str_sub, str_sub_ids, strfromArrayList, strfromArrayList1;
    ArrayList<UserType> classlistshow;
    Button btn_submit;
    UserTypeAdapter classAdapter;
    ArrayList<TeacherInfo> teacherInfos;
    ArrayList<String> teachlist, subidlist, classlist;
    String username, password, loginusername, loginuserpass, classnamestr, classidstr, user_id, appoinmtmentcount, langcode;
    UserSessionManager userSessionManager;
    Student_Subject_Registration_Adapter expandadapter;
    ArrayList<ExpandedSubjectData> parentlist;
    ArrayList<UserType> childlist;
    ArrayList<UserType> childsublist;
    ExpandableListView expandableListView;
    List<String> appointdatelist;
    JSONArray datearray;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin, savelang;
    HashMap<String, String> checkBoxData = new HashMap<>();
    String token;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragStudentNextRegistrationBinding.inflate(inflater, container, false);
        userid = getArguments().getString("userid");
        usertype = getArguments().getString("usertype");
        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        savelang = loginPreferences.getBoolean("saveLang", false);
        selclass = "";
        userSessionManager = new UserSessionManager(getActivity());
        final HashMap<String, String> user = userSessionManager.getregDetails();
        username = user.get(userSessionManager.KEY_USERNAME);
        password = user.get(userSessionManager.KEY_USERPASS);
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(), langcode);

        FirebaseApp.initializeApp(getActivity());
        token = FirebaseInstanceId.getInstance().getToken();
        Log.d("CCDDDDDD", "onCreate: "+token);




        binding.subjectSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                dialog.setContentView(R.layout.expanded_subject_dialog);
                dialog.show();
                expandableListView = dialog.findViewById(R.id.expanded_list);
                btn_submit = dialog.findViewById(R.id.btn_submit);
                ImageView close = dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                if (parentlist.size() == 0) {
                    GetAllSubTask();
                } else {

                    expandadapter = new Student_Subject_Registration_Adapter(parentlist, getActivity(), Student_Next_Registation.this, checkBoxData);
                    expandableListView.setAdapter(expandadapter);
                }

                //GetAllSubTask();
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkBoxData = expandadapter.getCheckBoxData();

                        Set<String> keys = checkBoxData.keySet();
                        StringBuilder sb = new StringBuilder();
                        StringBuilder sb1 = new StringBuilder();
                        for (String key : keys) {
                            //Log.d("LOG", key + "=" + checkBoxData.get(key));
                            sb.append(key).append(",");
                            sb1.append(checkBoxData.get(key)).append(",");
                        }
                        responseText1 = sb.toString();
                        if (responseText1.endsWith(",")) {
                            responseText1 = responseText1.substring(0, responseText1.length() - 1);
                        }
                        responseid = sb1.toString();
                        if (responseid.endsWith(",")) {
                            responseid = responseid.substring(0, responseid.length() - 1);
                        }
                        binding.subjectSpinner.setText(responseText1);
                        subjects = responseid;
                        dialog.cancel();
                    }
                });
            }
        });
        GetAllClassesTask();
        binding.classSpinner.user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                UserType slote = classlistshow.get(i);
                classes = slote.getId().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selsubject = subjects;
                selclass = classes;
                if (binding.subjectSpinner.getText().toString().trim().equals(getString(R.string.Selectsubject))) {
                    AlertClass.alertDialogShow(getActivity(), getString(R.string.Selectsubject));
                } else if (selclass.equals("")) {
                    AlertClass.alertDialogShow(getActivity(), getString(R.string.Selectclass));
                } else {
                    RegistrationTask();
                }

            }
        });
        return binding.getRoot();
    }

    public void Click() {
        btn_submit.setVisibility(View.VISIBLE);
    }

    public void GetAllSubTask() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.AllSubjectURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        Log.d("RRRRRR", "studentnextregistration subject :  onResponse: "+response);
                        dialog.dismiss();
                        parentlist = new ArrayList<>();
                        childsublist = new ArrayList<>();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (flag.equals("true")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    childlist = new ArrayList<>();
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String category_id = obj.getString("Category_id");
                                    String category_name = obj.getString("Category_name");
                                    String category_image = obj.getString("category_image");
                                    JSONArray subarray = obj.getJSONArray("Subject");
                                    for (int j = 0; j < subarray.length(); j++) {
                                        JSONObject subobj = subarray.getJSONObject(j);
                                        String subject_id = subobj.getString("Subject_id");
                                        String subject_name = subobj.getString("Sub_subject");
                                        String subject_image = subobj.getString("Subject_image");
                                        UserType subj = new UserType();
                                        subj.setId(subject_id);
                                        subj.setType(subject_name);
                                        childlist.add(subj);
                                        childsublist.add(subj);
                                    }
                                    ExpandedSubjectData parentdata = new ExpandedSubjectData();
                                    parentdata.setName(category_name);
                                    parentdata.setId(category_id);
                                    parentdata.setImage(category_image);
                                    parentdata.setChildList(childlist);
                                    parentlist.add(parentdata);
                                    expandadapter = new Student_Subject_Registration_Adapter(parentlist, getActivity(), Student_Next_Registation.this, checkBoxData);
                                    expandableListView.setAdapter(expandadapter);
                                }

                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(getActivity(), msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRRR", "studentnextregistration subject :  exception: "+e);
                            AlertClass.alertDialogShow(getActivity(), e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
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
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public void GetAllClassesTask() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.ClassesListURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        parentlist = new ArrayList<>();
                        childsublist = new ArrayList<>();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRRR", "studentnextregistration  class :  onResponse: "+response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (flag.equals("true")) {
                                classlistshow = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String class_id = obj.getString("class_id");
                                    String class_name = obj.getString("class_name");
                                    UserType subj1 = new UserType();
                                    subj1.setType(class_name);
                                    subj1.setId(class_id);
                                    classlistshow.add(subj1);
                                    classAdapter = new UserTypeAdapter(classlistshow, getActivity());
                                    binding.classSpinner.user.setAdapter(classAdapter);
                                }

                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(getActivity(), msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRRR", "studentnextregistration  class  :  exception: "+e);
                            AlertClass.alertDialogShow(getActivity(), e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
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
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public void RegistrationTask() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.StudentNextRegistrationURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRRR", "studentnextregistration  next  :  onResponse: "+response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                alertDialog.setMessage(msg);
                                alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        loginusername = username;
                                        loginuserpass = password;
                                        LoginTask();
                                    }
                                });
                                alertDialog.show();
                                dialog.dismiss();
                            } else if (flag.equals("false")) {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Log.d("RRRRRR", "studentnextregistration next :  exception: "+e);
                            dialog.dismiss();
                            //AlertClass.alertDialogShow(getActivity(),e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
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
                params.put("user_id", userid);
                params.put("class", selclass);
                params.put("subject", selsubject);
                params.put("lang_code", langcode);
                Log.d("RRRR", "getParams: "+params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public void LoginTask() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.LoginURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        teacherInfos = new ArrayList<>();
                        teachlist = new ArrayList<>();
                        subidlist = new ArrayList<>();
                        classlist = new ArrayList<>();
                        try {
                            Log.d("RRRRRR", "studentnextregistration login :  onResponse: "+response);
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
                                final String morning = obj.getString("availability_morning");
                                final String afternoon = obj.getString("availability_afternoon");
                                final String evening = obj.getString("availability_evening");
                                final String days = obj.getString("availability_days");
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
                                final String currency_code = obj.getString("teacher_currency_code");
                                final String paypal_email=obj.getString("paypal_email");
                                final String payment_method_id=obj.getString("payment_method_id");

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
                                JSONArray genderchoicearray = obj.getJSONArray("teacher_gender_choice");
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
                                if (user_type.equals("2")) {
                                    userSessionManager.createUserLoginSession(user_id, user_type, username1, email, contact_number, full_name, address, city, country, zipcode, str_sub, classnamestr, profile, subject, institute, education, exp, timeslote, appointment_dur, im_id_copy, im_certificate, morning, afternoon
                                            , evening, days, strfromArrayList, fees, techer_gender_choice, latitude, longitude, date_of_birth, gender, passingyear, teacherplaceid, tagline, strfromArrayList1, classidstr, nation, "1", currency_code,paypal_email,payment_method_id);
                                    HomeScreenURL();
                                    StudentAppointmentURL();
                                }
                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(getActivity(), msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRRR", "studentnextregistration login :  exception: "+e);
                            AlertClass.alertDialogShow(getActivity(), e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
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
                params.put("email_user_name", username);
                params.put("password", password);
                params.put("lang_code", langcode);
                params.put("device_token",token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                                Log.d("RRRRRR", "studentnextregistration home :  onResponse: "+response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                if (flag.equals("true")) {
                                    appoinmtmentcount = obj.getString("data").toString();
                                    userSessionManager.createHomeScreen(appoinmtmentcount);
                                    loginPrefsEditor.putBoolean("saveLogin", true);
                                    loginPrefsEditor.putString("mobile_no", username);
                                    loginPrefsEditor.putString("password", password);
                                    loginPrefsEditor.commit();
                                    Intent in = new Intent(getActivity(), StudentHomeActivity.class);
                                    in.putExtra("count", appoinmtmentcount);
                                    startActivity(in);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRRR", "studentnextregistration home :  exception: "+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRRR", "studentnextregistration home :  error: "+error);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                                Log.d("RRRRRR", "studentnextregistration homescreen :  onResponse: "+response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                if (flag.equals("true")) {
                                    appoinmtmentcount = obj.getString("data").toString();
                                    userSessionManager.createHomeScreen(appoinmtmentcount);
                                    Intent in = new Intent(getActivity(), MainActivity.class);
                                    in.putExtra("count", appoinmtmentcount);
                                    startActivity(in);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRRR", "studentnextregistration homescreen :  exception: "+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRRR", "studentnextregistration homescreen :  error: "+error);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                                Log.d("RRRRRR", "studentnextregistration appoint :  onResponse: "+response);
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
                                    //TODO
                                    datearray = new JSONArray(appointdatelist);
                                    userSessionManager.createAppointmentCalender(datearray);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRRR", "studentnextregistration  appoint:  exception: "+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRRR", "studentnextregistration appoint :  error: "+error);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
}
