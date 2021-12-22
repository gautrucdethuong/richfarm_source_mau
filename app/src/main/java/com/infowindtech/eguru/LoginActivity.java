package com.infowindtech.eguru;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.infowindtech.eguru.StudentPanel.Model.TeacherInfo;
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.TeacherPanel.Fragments.Personal_Registration_Fragment;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.ActivityLoginReg1Binding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.infowindtech.eguru.R.mipmap.ic_arrow_right_white_24dp;

public class LoginActivity extends FragmentActivity {
    ActivityLoginReg1Binding binding;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin, savelang;
    private SharedPreferences.Editor loginPrefsEditor;
    UserSessionManager userSessionManager;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    RadioButton lang_en, lang_ar, lang_guest;
    TextView btn_ok, btn_cancel, tv_chose_lang;
    String message, username, password, str_sub, str_sub_ids, strfromArrayList, strfromArrayList1, classnamestr, classidstr, morning;
    ArrayList<TeacherInfo> teacherInfos;
    ArrayList<String> teachlist, subidlist, classlist;
    String user_id, appoinmtmentcount, langcode, afternoon, evening, days, language;
    List<String> appointdatelist;
    JSONArray datearray;
    String token="";

    String[] permissions = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_reg1);
        userSessionManager = new UserSessionManager(getApplicationContext());
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        savelang = loginPreferences.getBoolean("saveLang", false);
        loginPrefsEditor = loginPreferences.edit();
        if (savelang == true) {
            langcode = loginPreferences.getString("lang", "");
            userSessionManager.selectedlanguage(langcode);
            AlertClass.setLocale(LoginActivity.this, langcode);
        } else if (savelang == false) {
            langcode = "en";
            userSessionManager.selectedlanguage("en");
            AlertClass.setLocale(LoginActivity.this, langcode);
        }
        FirebaseApp.initializeApp(this);

         token = FirebaseInstanceId.getInstance().getToken();
        Log.d("CCDDDDDD", "onCreate: "+token);


        checkPermissions();

        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(LoginActivity.this, langcode);
        binding.imLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (langcode.equals("en")) {
                    langcode = "en";
                    userSessionManager.selectedlanguage("en");
                    setLocale("en");
                    loginPrefsEditor.putBoolean("saveLang", true);
                    loginPrefsEditor.putString("lang", langcode);
                    loginPrefsEditor.commit();
                    Intent in = new Intent(LoginActivity.this, LoginActivity.class);
                    startActivity(in);
                } else if (langcode.equals("ar")) {
                    langcode = "en";
                    userSessionManager.selectedlanguage("en");
                    setLocale("en");
                    loginPrefsEditor.putBoolean("saveLang", true);
                    loginPrefsEditor.putString("lang", langcode);
                    loginPrefsEditor.commit();
                    Intent in = new Intent(LoginActivity.this, LoginActivity.class);
                    startActivity(in);
                }
            }
        });

        getPurchaseToken(this);

//
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//
//                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });



        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        dialog.setContentView(R.layout.language_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        lang_en = dialog.findViewById(R.id.english);
        lang_ar = dialog.findViewById(R.id.arabic);
        lang_guest = dialog.findViewById(R.id.guest);
        btn_ok = dialog.findViewById(R.id.tv_ok);
        btn_cancel = dialog.findViewById(R.id.tv_cancel);
        tv_chose_lang = dialog.findViewById(R.id.tv_chose_lang);
        tv_chose_lang.setText(R.string.liketosign);
        lang_en.setText(getString(R.string.student));
        lang_ar.setText(getString(R.string.teacher));
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lang_en.isChecked()) {
                    dialog.cancel();
                } else if (lang_ar.isChecked()) {
                    dialog.cancel();
                } else if (lang_guest.isChecked()) {
                    dialog.cancel();
                    LoginTask();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.liketosign), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        binding.tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvSignin.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bothsidecorner));
                binding.tvSignin.setTextColor(Color.WHITE);
                int img1 = ic_arrow_right_white_24dp;
                binding.tvSignin.setCompoundDrawablesWithIntrinsicBounds ( 0, 0,img1,0 );

                int img = R.drawable.ic_arrow_forward_black_24dp;
                binding.tvSignup.setCompoundDrawablesWithIntrinsicBounds ( 0, 0,img,0 );
                binding.tvSignup.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bothsidecornerwhite));
                binding.tvSignup.setTextColor(Color.BLACK);
                Login_Fragment frag = new Login_Fragment();
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.loginreg, frag).commit();
            }
        });
        binding.tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvSignin.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bothsidecornerwhite));
                binding.tvSignin.setTextColor(Color.BLACK);
                int img = R.drawable.ic_arrow_forward_black_24dp;
                binding.tvSignin.setCompoundDrawablesWithIntrinsicBounds ( 0, 0,img,0 );

                int img1 = ic_arrow_right_white_24dp;
                binding.tvSignup.setCompoundDrawablesWithIntrinsicBounds ( 0, 0,img1,0 );
                binding.tvSignup.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bothsidecorner));
                binding.tvSignup.setTextColor(Color.WHITE);
                Intent in = new Intent(LoginActivity.this, Personal_Registration_Fragment.class);
                in.putExtra("go", "both");
                startActivity(in);
            }
        });

        this.setFinishOnTouchOutside(true);
        final LocationManager manager = (LocationManager) LoginActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(LoginActivity.this)) {
            enableLoc();
        }
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(LoginActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) (LoginActivity.this),
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }


    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {
        if (googleApiClient == null) {
            Log.d("LLLL", "enableLoc: "+googleApiClient);
            googleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();
//
//            LocationRequest locationRequest = LocationRequest.create();
//            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(LoginActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    private void getPurchaseToken(final Activity context) {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, ServiceClass.Purchase_Api,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("envato-valid-purchase", "onResponse: " + response);
                            if (null != response) {

                                }else {
                                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                alertDialog.setCancelable(false);
                                alertDialog.setTitle("Puchase Token");
                                alertDialog.setMessage("Invalid Purchase Token");
                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                                        "ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                userSessionManager.logoutUser();
                                                loginPrefsEditor.clear();
                                                loginPrefsEditor.commit();
                                            }
                                        });

                                alertDialog.show();
                            }

                        } catch (Exception e) {
                            Log.d("RRRRR", "forgetPassword :  exception: " + e);
                            Toast.makeText(context, "Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "forgetPassword :  error: " + error);
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setCancelable(false);
                        alertDialog.setTitle("Puchase Token");
                        alertDialog.setMessage("Invalid Purchase Token");
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                                "ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                            userSessionManager.logoutUser();
                                            loginPrefsEditor.clear();
                                            loginPrefsEditor.commit();
                                    }
                                });

                        alertDialog.show();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer W7oQeDRVDyanPBq6FGCYQjzpnx4q5Veo");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }



//    public boolean checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(LoginActivity.this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
//                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
//                ActivityCompat.requestPermissions(LoginActivity.this,
//                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            } else {
//                ActivityCompat.requestPermissions(LoginActivity.this,
//                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        conf.setLayoutDirection(new Locale(lang));
        res.updateConfiguration(conf, dm);
    }

    public void LoginTask() {
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.LoginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d("RRRRR", "loginactivity: response"+response);
                        JSONObject jsonObject = null;
                        teacherInfos = new ArrayList<>();
                        teachlist = new ArrayList<>();
                        subidlist = new ArrayList<>();
                        classlist = new ArrayList<>();
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
                                    userSessionManager.createHomeScreen("0");
                                    loginPrefsEditor.putBoolean("saveLogin", true);
                                    loginPrefsEditor.putString("mobile_no", "default");
                                    loginPrefsEditor.putString("password", "default@123");
                                    loginPrefsEditor.commit();
                                    Intent in = new Intent(LoginActivity.this, StudentHomeActivity.class);
                                    in.putExtra("count", "0");
                                    startActivity(in);
                                }
                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(LoginActivity.this, msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "loginactivity:  except " + e.getMessage());
                            AlertClass.alertDialogShow(LoginActivity.this, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Log.d("RRRR", "loginactivity:  error " + error);
                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(LoginActivity.this, message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_user_name", "default");
                params.put("password", "default@123");
                params.put("lang_code", "en");
                params.put("device_token",token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
}





















