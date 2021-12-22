package com.infowindtech.eguru.TeacherPanel.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.infowindtech.eguru.MainActivity;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Model.TeacherInfo;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.GetByteArrayDecodeUri;
import com.infowindtech.eguru.databinding.TeacherDocVerificationBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Teacher_Verification_Fragment extends Fragment {
    TeacherDocVerificationBinding binding;
    String usertype, qualification, collage_name, passing_year, serverimage, serverimage1, message, userid, teacheredu, userphotoid, usercertificate, year, experience, str_sub, str_sub_ids, strfromArrayList, strfromArrayList1;
    int c, g;
    byte[] byteimage;
    ArrayList<TeacherInfo> teacherInfos;
    ArrayList<String> teachlist, subidlist, classlist;
    String dayst, days, morning, afternoon, evening, username, password, loginusername, loginuserpass, classnamestr, classidstr, user_id, appoinmtmentcount, langcode;
    UserSessionManager userSessionManager;
    List<String> appointdatelist;
    JSONArray datearray;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    String token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TeacherDocVerificationBinding.inflate(inflater, container, false);
        userid = getArguments().getString("userid");
        usertype = getArguments().getString("usertype");
        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        userSessionManager = new UserSessionManager(getActivity());
        final HashMap<String, String> user = userSessionManager.getregDetails();
        username = user.get(userSessionManager.KEY_USERNAME);
        password = user.get(userSessionManager.KEY_USERPASS);
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        loginPrefsEditor.putBoolean("saveLang", true);
        loginPrefsEditor.putString("lang", langcode);
        loginPrefsEditor.commit();
        AlertClass.setLocale(getActivity(), langcode);

        FirebaseApp.initializeApp(getActivity());

        token = FirebaseInstanceId.getInstance().getToken();
        Log.d("CCDDDDDD", "onCreate: "+token);

        binding.tvPhotoid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = 0;
                g = 4;
                chosseCameraandGallery();
            }
        });
        binding.imIdCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = 0;
                g = 4;
                chosseCameraandGallery();
            }
        });
        binding.tvCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = 1;
                g = 5;
                chosseCameraandGallery();

            }
        });
        binding.imMdCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = 1;
                g = 5;
                chosseCameraandGallery();
            }
        });
        binding.etPassingYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int cyear = calendar.get(Calendar.YEAR);
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.year_dialog);
                NumberPicker np = dialog.findViewById(R.id.np);
                np.setMinValue(1950);
                np.setMaxValue(cyear);
                np.setWrapSelectorWheel(true);
                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        year = String.valueOf(i1);
                    }
                });
                TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
                TextView tv_ok = dialog.findViewById(R.id.tv_ok);
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.etPassingYear.setText(year);
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collage_name = binding.etInstitute.getText().toString().trim();
                passing_year = binding.etPassingYear.getText().toString().trim();
                userphotoid = serverimage;
                usercertificate = serverimage1;
                qualification = binding.etQualification.getText().toString().trim();
                experience = binding.etExperience.getText().toString().trim();
                if (collage_name.equals("")) {
                    binding.etInstitute.setError(getString(R.string.enterinstitute));
                } else if (passing_year.equals("")) {
                    binding.etPassingYear.setError(getString(R.string.enterpassingyear));
                } else if (qualification.equals("")) {
                    binding.etQualification.setError(getString(R.string.enterqualification));
                } else if (experience.equals("")) {
                    binding.etExperience.setError(getString(R.string.enterexp));
                } else if (qualification.equals(getString(R.string.selectqualification))) {
                    AlertClass.alertDialogShow(getActivity(), getString(R.string.plzselectqualification));
                } else if (binding.imIdCopy.getDrawable() == null) {
                    AlertClass.alertDialogShow(getActivity(), getString(R.string.attachdoc1));
                } else if (binding.imMdCertificate.getDrawable() == null) {
                    AlertClass.alertDialogShow(getActivity(), getString(R.string.attachdoc2));
                } else {
                    VerificationTask();
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Bitmap bp = (Bitmap) data.getExtras().get("data");
                        binding.imIdCopy.setImageBitmap(bp);
                        byteimage = GetByteArrayDecodeUri.getBitmapAsByteArray(bp);
                        serverimage = Base64.encodeToString(byteimage, Base64.DEFAULT);
                        binding.tvPhotoid.setVisibility(View.GONE);
                    } catch (Exception e) {
                        AlertClass.alertDialogShow(getActivity(), e.getMessage());
                    }
                }
                break;
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Bitmap bp1 = (Bitmap) data.getExtras().get("data");
                        binding.imMdCertificate.setImageBitmap(bp1);
                        byteimage = GetByteArrayDecodeUri.getBitmapAsByteArray(bp1);
                        serverimage1 = Base64.encodeToString(byteimage, Base64.DEFAULT);
                        binding.tvCertificate.setVisibility(View.GONE);
                    } catch (Exception e) {
                        AlertClass.alertDialogShow(getActivity(), e.getMessage());
                    }
                }
                break;
            case 4:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        cursor.close();
                        binding.imIdCopy.setImageURI(selectedImage);
                        Bitmap bp2 = GetByteArrayDecodeUri.decodeUri(getActivity(), selectedImage, 100);
                        byteimage = GetByteArrayDecodeUri.getBitmapAsByteArray(bp2);
                        serverimage = Base64.encodeToString(byteimage, Base64.DEFAULT);
                        binding.tvPhotoid.setVisibility(View.GONE);
                    } catch (Exception e) {
                        AlertClass.alertDialogShow(getActivity(), e.getMessage());
                    }
                }
                break;
            case 5:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        cursor.close();
                        binding.imMdCertificate.setImageURI(selectedImage);
                        Bitmap bp2 = GetByteArrayDecodeUri.decodeUri(getActivity(), selectedImage, 100);
                        byteimage = GetByteArrayDecodeUri.getBitmapAsByteArray(bp2);
                        serverimage1 = Base64.encodeToString(byteimage, Base64.DEFAULT);
                        binding.tvCertificate.setVisibility(View.GONE);
                    } catch (Exception e) {
                        AlertClass.alertDialogShow(getActivity(), e.getMessage());
                    }
                }
                break;
        }

    }

    public void chosseCameraandGallery() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.activity_capture_image);
        ImageView camera = dialog.findViewById(R.id.im_camera);
        ImageView gallary = dialog.findViewById(R.id.im_gallary);
        dialog.show();
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(in, c);
                dialog.cancel();
            }
        });
        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, g);
                dialog.cancel();
            }
        });

    }

    public void VerificationTask() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherVerificationRegistration2URL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRR", "teacherverificationfragment  verifi:  response " + response);
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
                                        HashMap<String, String> lang = userSessionManager.getLanguage();
                                        langcode = lang.get(userSessionManager.KEY_Lang);
                                        LoginTask();
                                    }
                                });
                                alertDialog.show();
                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(getActivity(), msg);
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            dialog.dismiss();
                            Log.d("RRRR", "teacherverficationfragment verifi :  exception" + e);
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
                params.put("user_id", userid);
                params.put("identity_card", userphotoid);
                params.put("certificate", usercertificate);
                params.put("institute_name", collage_name);
                params.put("qualification", qualification);
                params.put("year_of_passing", passing_year);
                params.put("teacher_experince", experience);
                params.put("lang_code", langcode);
                params.put("user_activation", "1");
                Log.d("RRRRRFFFFFFF", "getParams: "+params);
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
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            Log.d("RRRR", "teacherverficationfragment  login: response" + response);

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
                                final String fees = obj.getString("teacher_fees");
                                final String techer_gender_choice = obj.getString("teaching_gender_choice_id");
                                final String latitude = obj.getString("latitude");
                                final String longitude = obj.getString("longitude");
                                final String date_of_birth = obj.getString("date_of_birth");
                                final String gender = obj.getString("gender");
                                final String passingyear = obj.getString("year_of_passing");
                                final String teacherplaceid = obj.getString("teaching_place_id");
                                final String tagline = obj.getString("user_tagline");
                                final String nation = obj.getString("nationality");
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
                                final String timeslote = morningt + "," + afternoont + "," + eveningt;
                                final String active_status = obj.getString("status");
                                dayst = obj.getString("availability_days");
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
                                    sb.append(str).append(","); //separating contents using semi colon
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
                                if (user_type.equals("1")) {
                                    userSessionManager.createUserLoginSession(user_id, user_type, username1, email, contact_number, full_name, address, city, country, zipcode, str_sub, classnamestr, profile, subject, institute, education, exp, timeslote, appointment_dur, im_id_copy, im_certificate, morning, afternoon
                                            , evening, days, strfromArrayList, fees, techer_gender_choice, latitude, longitude, date_of_birth, gender, passingyear, teacherplaceid, tagline, strfromArrayList1, classidstr, nation, "1", currency_code,paypal_email,payment_method_id);



                                    TeacherHomeScreenURL();
                                    TeacherAppointmentURL();
                                }
                            } else if (flag.equals("false")) {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "teacherverficationfragment login :  exception" + e);
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
                params.put("email_user_name", username);
                params.put("password", password);
                params.put("lang_code", langcode);
                params.put("device_token",token);
                Log.d("SSSSS", "getParams: "+params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    private void TeacherHomeScreenURL() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherHomeScreenURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRR", "teacherverficationfragment home : response" + response);
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
                                    Intent in = new Intent(getActivity(), MainActivity.class);
                                    in.putExtra("count", appoinmtmentcount);
                                    startActivity(in);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "teacherverficationfragment home :  exception" + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRR", "teacherverficationfragment home :  error" + error);
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

    private void TeacherAppointmentURL() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherAppointmentDateURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRR", "teacherverficationfragment app: response" + response);
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
                                } else if (flag.equals("flase")) {
                                    //TODO
                                    datearray = new JSONArray(appointdatelist);
                                    userSessionManager.createAppointmentCalender(datearray);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "teacherverficationfragment app :  exception" + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRR", "teacherverficationfragment app :  error" + error);
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
}
