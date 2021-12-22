package com.infowindtech.eguru.StudentPanel;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.MainActivity;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Adapter.Expande_Subjectlist_Adapter;
import com.infowindtech.eguru.StudentPanel.Model.ExpandedSubjectData;
import com.infowindtech.eguru.TeacherPanel.Adapters.UpdateSubjectClassAdapter;
import com.infowindtech.eguru.TeacherPanel.Adapters.UserTypeAdapter;
import com.infowindtech.eguru.TeacherPanel.Fragments.TeacherNextUpdate_Fragment;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.TeacherPanel.Models.UserType;
import com.infowindtech.eguru.databinding.FragStudentUpdate1Binding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class UpdateCommonActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {
    FragStudentUpdate1Binding binding;
    UserSessionManager userSessionManager;
    String activt_status, user_id, name, phone, responseid, address, city, country, zipcode, classes = "", subjects, profile, responseText1, message, fullname, contactno, useraddress, usercity, usercountry, userzipcode, usersubject, userclass, user_type, stu_class, stu_subj, serverimage, session_subject_id, dob, gender, latitude, longitude, userdob, usergender, userlatitude, userlongitude;
    ArrayList<UserType> classlist = new ArrayList<>();
    UpdateSubjectClassAdapter classteachadapter;
    UserTypeAdapter classAdapter;
    Button btn_submit;
    String selectdate, new_order_date, usersessionsubject, responseid1 = "", classid, classname, responseText2, usernation;
    double lati, longi;
    ArrayList<String> countries;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    String langcode, country1, mapaddress, mapcity, mapcountry, mappostalCode, mapstate, nation, fromid, appointmentcount, firstaddress, active_status;
    SupportMapFragment mMapFragment;
    GoogleMap mMap;
    TextView tv_address;
    Expande_Subjectlist_Adapter expandadapter;
    ArrayList<ExpandedSubjectData> parentlist = new ArrayList<>();
    ArrayList<UserType> childlist;
    ArrayList<UserType> childsublist;
    ExpandableListView expandableListView;
    ListView lv_subject;
    double lat, lng;
    GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    ArrayList<String> List_class = new ArrayList<>();
    ArrayList<String> List_class_ID = new ArrayList<>();
    ArrayList<String> List_tution_place = new ArrayList<>();
    ArrayList<String> List_gender = new ArrayList<>();
    HashMap<String, String> checkBoxData_1 = new HashMap<>();
    HashMap<String, String> checkBoxData_2 = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.frag_student_update1);
        userSessionManager = new UserSessionManager(UpdateCommonActivity.this);
        if (userSessionManager.signIn())
            UpdateCommonActivity.this.finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        name = user.get(userSessionManager.KEY_FULLNAME);
        phone = user.get(userSessionManager.KEY_PHONENUMBER);
        address = user.get(userSessionManager.KEY_ADDRESS);
        city = user.get(userSessionManager.KEY_CITY);
        country = user.get(userSessionManager.KEY_COUNTRY);
        zipcode = user.get(userSessionManager.KEY_ZIPCODE);
        stu_class = user.get(userSessionManager.KEY_CLASSES);
        stu_subj = user.get(userSessionManager.KEY_SUBJECT);
        profile = user.get(userSessionManager.KEY_PROFILE);
        user_type = user.get(userSessionManager.KEY_USERTYPE);
        session_subject_id = user.get(userSessionManager.KEY_SubId);
        dob = user.get(userSessionManager.KEY_DOB);
        gender = user.get(userSessionManager.KEY_GENDER);
        latitude = user.get(userSessionManager.KEY_LATITUDE);
        longitude = user.get(userSessionManager.KEY_LONGITUDE);
        classid = user.get(userSessionManager.KEY_CLASSId);
        nation = user.get(userSessionManager.KEY_Nation);
        active_status = user.get(userSessionManager.KEY_Activation);
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(UpdateCommonActivity.this, langcode);
        subjects = session_subject_id;
        binding.subjectSpinner.setText(stu_subj);
        binding.tvCountry.setText(country);
        usersessionsubject = stu_subj;
        this.setFinishOnTouchOutside(true);
        final LocationManager manager = (LocationManager) UpdateCommonActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(UpdateCommonActivity.this)) {
            enableLoc();
        }
        Log.d("LLLL", "onCreate: "+manager);
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date oneWayTripDate = null;
        try {
            oneWayTripDate = input.parse(dob);
            new_order_date = output.format(oneWayTripDate);
            binding.etDob.setText(new_order_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        selectdate = dob;
        if (gender.equals("1")) {
            binding.male.setChecked(true);
        } else if (gender.equals("2")) {
            binding.female.setChecked(true);
        } else if (gender.equals("3")) {
            binding.other.setChecked(true);
        }
        if (user_type.equals("1")) {
            classes = classid;
            classname = stu_class;
        }
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        binding.tvHeading.setText(R.string.update);
        binding.etFullname.setText(name);
        binding.etPhone.setText(phone);
        binding.etAddress.setText(address);
        binding.etCity.setText(city);
        binding.etZipcode.setText(zipcode);
        binding.etNation.setText(nation);
        Glide.with(UpdateCommonActivity.this).load(profile)
                .thumbnail(0.5f)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imPic);
        binding.etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int maxYear = c.get(Calendar.YEAR) - 20;
                int maxMonth = c.get(Calendar.MONTH);
                int maxDay = c.get(Calendar.DAY_OF_MONTH);
                final Dialog dialog1 = new Dialog(UpdateCommonActivity.this);
                dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.datepickerdialog);
                dialog1.show();
                DatePicker simpleDatePicker = dialog1.findViewById(R.id.simpleDatePicker); // initiate a date picker
                TextView tv_ok = dialog1.findViewById(R.id.tv_ok);
                TextView tv_cancel = dialog1.findViewById(R.id.tv_cancel);
                simpleDatePicker.setSpinnersShown(false);
                simpleDatePicker.setMaxDate(c.getTimeInMillis());
                simpleDatePicker.init(maxYear - 10, maxMonth, maxDay, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                        int newm = i1 + 1;
                        selectdate = i + "-" + newm + "-" + i2;
                        binding.etDob.setText(i2 + "-" + newm + "-" + i);
                    }
                });
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.cancel();
                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.etDob.setText(new_order_date);
                        dialog1.cancel();
                    }
                });
            }
        });
        binding.subjectSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(UpdateCommonActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
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

                Log.d("check_sublist_session", stu_subj);
                  stu_subj="";
                if (!stu_subj.equals("")) {

                    HashMap<String, String> map = new HashMap<String, String>();
                    String[] arr = stu_subj.split(",");
                    // String[] arStr = arr.split("=");
                    Log.d("array_size", String.valueOf(arr.length));

                    for (int k = 0; k < arr.length; k++) {
                        map.put(arr[k], arr[k]);
                    }
                    checkBoxData_1.putAll(map);
                }
                Log.d("checkBoxData_10", String.valueOf(checkBoxData_1.size()));

                if (parentlist.size() == 0) {
                    GetAllSubTask();
                } else {
                    expandadapter = new Expande_Subjectlist_Adapter(parentlist, UpdateCommonActivity.this, UpdateCommonActivity.this, checkBoxData_1);
                    expandableListView.setAdapter(expandadapter);
                }
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkBoxData_1 = expandadapter.getCheckBoxData();
                        Set<String> keys = checkBoxData_1.keySet();
                        StringBuilder sb = new StringBuilder();
                        StringBuilder sb1 = new StringBuilder();
                        for (String key : keys) {
                            sb.append(key).append(",");
                            sb1.append(checkBoxData_1.get(key)).append(",");
                        }

                        Log.d("SSSSS", "onClick: "+checkBoxData_1+"    "+checkBoxData_2+"     "+keys+"    "+sb+"   "+sb1);
                        responseText1 = sb.toString();
                        if (responseText1.endsWith(",")) {
                            responseText1 = responseText1.substring(0, responseText1.length() - 1);
                        }
                        responseid = sb1.toString();
                        if (responseid.endsWith(",")) {
                            responseid = responseid.substring(0, responseid.length() - 1);
                        }
                        binding.subjectSpinner.setText(responseText1);
                        usersessionsubject = responseText1;
                        subjects = responseid;
                        Log.d("SSSSSSSS", "onClick: "+responseid+"  "+responseText1+"  "+subjects+"    "+usersessionsubject);
                        dialog.cancel();
                    }
                });
            }
        });
        if (user_type.equals("2")) {
            binding.tvClasses.setVisibility(View.VISIBLE);
            binding.classSpinner.user.setVisibility(View.VISIBLE);
            binding.tvTeachClasses.setVisibility(View.GONE);
            binding.classTeachSpinner.setVisibility(View.GONE);

            //student
            if (classlist.size() == 0) {
                GetAllClassesTask();
            } else {
                classAdapter = new UserTypeAdapter(classlist, UpdateCommonActivity.this);
                binding.classSpinner.user.setAdapter(classAdapter);
            }
            binding.classSpinner.user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    UserType slote = classlist.get(i);
                    classes = slote.getId().toString();
                    classname = slote.getType().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } else if (user_type.equals("1")) {   //teacher
            binding.classTeachSpinner.setText(stu_class);
            binding.tvClasses.setVisibility(View.GONE);
            binding.classSpinner.user.setVisibility(View.GONE);
            binding.classSpinner.imgDown.setVisibility(View.GONE);
            binding.classSpinner.spinLayout.setVisibility(View.GONE);
            binding.tvTeachClasses.setVisibility(View.VISIBLE);
            binding.classTeachSpinner.setVisibility(View.VISIBLE);
            binding.classTeachSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(UpdateCommonActivity.this);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                    dialog.setContentView(R.layout.subjectclass_selection_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                    dialog.getWindow().setLayout(AppBarLayout.LayoutParams.WRAP_CONTENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
                    lv_subject = dialog.findViewById(R.id.lv_subject);
                    btn_submit = dialog.findViewById(R.id.btn_submit);
                    ImageView close = dialog.findViewById(R.id.close);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                    //Teacher class list
                    if (classlist.size() == 0) {
                        GetAllClassesForTeacherTask();
                    } else {
                        classteachadapter = new UpdateSubjectClassAdapter(classlist, UpdateCommonActivity.this, UpdateCommonActivity.this, List_class, List_class_ID);
                        lv_subject.setAdapter(classteachadapter);
                    }
                    dialog.show();
                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            StringBuffer responseText = new StringBuffer();
                            StringBuffer responseid = new StringBuffer();

                            List_class = classteachadapter.List_class;
                            List_class_ID = classteachadapter.List_class_ID;
                            HashSet hs = new HashSet();
                            HashSet hs_id = new HashSet();
                            hs.addAll(List_class);  // willl not add the duplicate values
                            List_class.clear();
                            List_class.addAll(hs);

                            hs_id.addAll(List_class_ID);  // willl not add the duplicate values
                            List_class_ID.clear();
                            List_class_ID.addAll(hs_id);

                            String str_class_list = List_class.toString();
                            str_class_list = str_class_list.substring(1, str_class_list.length() - 1);

                            String str_class_list_id = List_class_ID.toString();
                            str_class_list_id = str_class_list_id.substring(1, str_class_list_id.length() - 1);

                            binding.classTeachSpinner.setText(str_class_list);
                            classes = str_class_list_id;
                            classname = str_class_list;
                            dialog.cancel();
                        }
                    });
                }
            });
        }
        final Dialog mapdialog = new Dialog(UpdateCommonActivity.this);
        mapdialog.setContentView(R.layout.map_dialog);
        final TextView tv_ok = mapdialog.findViewById(R.id.tv_ok);
        tv_address = mapdialog.findViewById(R.id.tv_address);
        mMapFragment = (SupportMapFragment) UpdateCommonActivity.this.getSupportFragmentManager()
                .findFragmentById(R.id.mapdialog);
        mMapFragment.getMapAsync(this);
        binding.etAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapdialog.show();
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tv_address.getText().toString().trim().equals("")) {
                            mapdialog.cancel();
                        } else {
                            binding.etAddress.setText(mapaddress);
                            binding.etCity.setText(mapcity);
                            binding.etZipcode.setText(mappostalCode);
                            binding.tvCountry.setText(mapcountry);
                            mapdialog.cancel();
                        }

                    }
                });
            }
        });
        binding.tvCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(UpdateCommonActivity.this);
                dialog.setContentView(R.layout.country_dialog);
                dialog.show();
                Spinner countryspinner = dialog.findViewById(R.id.country_layout);
                TextView ok = dialog.findViewById(R.id.tv_ok);
                Locale[] locale = Locale.getAvailableLocales();
                countries = new ArrayList<String>();
                countries.add(getString(R.string.selectcountry));
                for (Locale loc : locale) {
                    country1 = loc.getDisplayCountry();
                    if (country1.length() > 0 && !countries.contains(country1)) {
                        countries.add(country1);
                    }
                }
                Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateCommonActivity.this, android.R.layout.simple_spinner_item, countries);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                countryspinner.setAdapter(adapter);
                countryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        country = countries.get(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.tvCountry.setText(country);
                        dialog.cancel();
                    }
                });
            }
        });
        if (user_type.equals("1")) {
            binding.tvNext.setVisibility(View.VISIBLE);
            binding.tvNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TeacherNextUpdate_Fragment frag = new TeacherNextUpdate_Fragment();
                    UpdateCommonActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.personal_reg, frag).addToBackStack(null).commit();
                }
            });
        }
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname = binding.etFullname.getText().toString().trim();
                contactno = binding.etPhone.getText().toString().trim();
                useraddress = binding.etAddress.getText().toString().trim();
                usercity = binding.etCity.getText().toString().trim();
                usercountry = binding.tvCountry.getText().toString().trim();
                userzipcode = binding.etZipcode.getText().toString().trim();
                usernation = binding.etNation.getText().toString().trim();
                usersubject = subjects;
                Log.d("SSSSSS", "onClick: "+usersubject);
                userclass = classes;
                userdob = selectdate;
                if (binding.male.isChecked()) {
                    usergender = "1";
                } else if (binding.female.isChecked()) {
                    usergender = "2";
                } else if (binding.other.isChecked()) {
                    usergender = "3";
                }
                getLatLongFromPlace(useraddress + "," + usercity + "," + usercountry + "," + userzipcode);
                userlatitude = String.valueOf(lati);
                userlongitude = String.valueOf(longi);
                if (fullname.equals("")) {
                    binding.etFullname.setError(getString(R.string.enterfullname));
                    binding.scrollView.smoothScrollTo(0, 0);
                } else if (contactno.equals("")) {
                    binding.etPhone.setError(getString(R.string.entercontactno));
                    binding.scrollView.smoothScrollTo(0, 0);
                } else if (binding.etPhone.getText().toString().trim().length() < 4) {
                    binding.etPhone.setError(getString(R.string.plzentervalidmobile));
                    binding.scrollView.smoothScrollTo(0, 0);
                } else if (useraddress.equals("")) {
                    binding.etAddress.setError(getString(R.string.enteraddress));
                } else if (usercity.equals("")) {
                    binding.etCity.setError(getString(R.string.entercity));
                } else if (userzipcode.equals("")) {
                    binding.etZipcode.setError(getString(R.string.enterzipcode));
                } else if (usernation.equals("")) {
                    binding.etNation.setError(getString(R.string.enterusernation));
                } else if (user_type.equals("2")) {
                    if (fullname.equals(name) && contactno.equals(phone) && useraddress.equals(address) && usercity.equals(city) && usercountry.equals(country) && userzipcode.equals(zipcode) && usernation.equals(nation) && usersubject.equals(session_subject_id) && userclass.equals(classid) && userdob.equals(dob) && usergender.equals(gender) && userlatitude.equals(latitude) && userlongitude.equals(longitude)) {
                        AlertClass.alertDialogShow(UpdateCommonActivity.this, getString(R.string.changeinfo));
                    } else {
                        UpdateTask();
                        AlertClass.hideKeybord(UpdateCommonActivity.this, view);
                    }
                } else {
                    UpdateTask();
                    AlertClass.hideKeybord(UpdateCommonActivity.this, view);
                }
            }
        });
        binding.imCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(UpdateCommonActivity.this);
                dialog.setContentView(R.layout.activity_capture_image);
                dialog.setTitle(getString(R.string.selectpic));
                ImageView camera = dialog.findViewById(R.id.im_camera);
                ImageView gallary = dialog.findViewById(R.id.im_gallary);
                dialog.show();
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(in, 0);
                        dialog.cancel();
                    }
                });
                gallary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, 1);
                        dialog.cancel();
                    }
                });
            }
        });
    }

    public void Click() {
        btn_submit.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Bitmap bp = (Bitmap) data.getExtras().get("data");
                        byte[] byteimage = getBitmapAsByteArray(bp);
                        serverimage = Base64.encodeToString(byteimage, Base64.DEFAULT);
                        UploadProfileTask();
                        binding.imPic.setImageBitmap(bp);
                    } catch (Exception e) {
                        AlertClass.alertDialogShow(UpdateCommonActivity.this, e.getMessage());
                    }
                }
                break;
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        final Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = UpdateCommonActivity.this.getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        cursor.close();
                        final Bitmap bp2 = decodeUri(UpdateCommonActivity.this, selectedImage, 100);
                        byte[] byteimage = getBitmapAsByteArray(bp2);
                        serverimage = Base64.encodeToString(byteimage, Base64.DEFAULT);
                        binding.imPic.setImageURI(selectedImage);
                        UploadProfileTask();
                    } catch (Exception e) {
                        AlertClass.alertDialogShow(UpdateCommonActivity.this, e.getMessage());
                    }
                }
                break;
        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }

    public void UpdateTask() {
        final ProgressDialog dialog = new ProgressDialog(UpdateCommonActivity.this);
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.UpdateUserProfileCommonURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "updatecommonactivity  :  onResponse: "+response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                if (user_type.equals("2")) {
                                    final HashMap<String, String> homecount = userSessionManager.getHomeScreen();
                                    appointmentcount = homecount.get(userSessionManager.KEY_HomeScreenCount);
                                    AlertDialog.Builder ab = new AlertDialog.Builder(UpdateCommonActivity.this);
                                    ab.setMessage(msg);
                                    ab.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            userSessionManager.UpdatecommonSession(contactno, fullname, useraddress, usercity, usercountry, userzipcode, classname, usersubject
                                                    , userlatitude, userlongitude, userdob, usergender, usersessionsubject, classes, usernation);
                                            Intent in = new Intent(UpdateCommonActivity.this, StudentHomeActivity.class);
                                            in.putExtra("count", appointmentcount);
                                            startActivity(in);
                                        }
                                    });
                                    AlertDialog alert = ab.create();
                                    alert.show();
                                } else if (user_type.equals("1")) {
                                    final HashMap<String, String> homecount = userSessionManager.getHomeScreen();
                                    appointmentcount = homecount.get(userSessionManager.KEY_HomeScreenCount);
                                    AlertDialog.Builder ab = new AlertDialog.Builder(UpdateCommonActivity.this);
                                    ab.setMessage(msg + " " + getString(R.string.updateother));
                                    ab.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            userSessionManager.UpdatecommonSession(contactno, fullname, useraddress, usercity, usercountry, userzipcode, classname, usersubject
                                                    , userlatitude, userlongitude, userdob, usergender, usersessionsubject, classes, usernation);
                                            TeacherNextUpdate_Fragment frag = new TeacherNextUpdate_Fragment();
                                            UpdateCommonActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.personal_reg, frag).addToBackStack(null).commit();
                                            dialog.cancel();
                                        }
                                    });
                                    ab.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            userSessionManager.UpdatecommonSession(contactno, fullname, useraddress, usercity, usercountry, userzipcode, classname, usersubject
                                                    , userlatitude, userlongitude, userdob, usergender, usersessionsubject, classes, usernation);
                                            dialogInterface.cancel();
                                            Intent in = new Intent(UpdateCommonActivity.this, MainActivity.class);
                                            in.putExtra("count", appointmentcount);
                                            startActivity(in);
                                        }
                                    });
                                    AlertDialog alert = ab.create();
                                    alert.show();
                                }

                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(UpdateCommonActivity.this, msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "updatecommonactivity  :  Exception: "+e);
                            AlertClass.alertDialogShow(UpdateCommonActivity.this, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Log.d("RRRRR", "updatecommonactivity  :  error: "+error);
                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("full_name", fullname);
                params.put("contact_number", contactno);
                params.put("address", useraddress);
                params.put("city", usercity);
                params.put("country", usercountry);
                params.put("zipcode", userzipcode);
                params.put("subject", usersubject);
                params.put("user_id", user_id);
                params.put("class", userclass);
                params.put("date_of_birth", userdob);
                params.put("gender", usergender);
                params.put("latitude", userlatitude);
                params.put("longitude", userlongitude);
                params.put("nationality", usernation);
                params.put("lang_code", langcode);
                Log.d("SSSSS", "getParams: "+params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(UpdateCommonActivity.this);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public void GetAllSubTask() {
        final ProgressDialog dialog = new ProgressDialog(UpdateCommonActivity.this);
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.AllSubjectURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d("RRRRR", "updatecommonactivity  :  response: "+response);
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
                                    expandadapter = new Expande_Subjectlist_Adapter(parentlist, UpdateCommonActivity.this, UpdateCommonActivity.this, checkBoxData_1);
                                    expandableListView.setAdapter(expandadapter);
                                }

                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(UpdateCommonActivity.this, msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "updatecommonactivity  :  Exception: "+e);
                            AlertClass.alertDialogShow(UpdateCommonActivity.this, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        } else if (error instanceof ServerError) {
                            message = getString(R.string.servererror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        } else if (error instanceof AuthFailureError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        } else if (error instanceof ParseError) {
                            message = getString(R.string.parsingerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        } else if (error instanceof NoConnectionError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        } else if (error instanceof TimeoutError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
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
        RequestQueue requestQueue = Volley.newRequestQueue(UpdateCommonActivity.this);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public void GetAllClassesTask() {
        final ProgressDialog dialog = new ProgressDialog(UpdateCommonActivity.this);
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
                            Log.d("RRRRR", "updatecommonactivity  :  response: "+response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (flag.equals("true")) {
                                classlist = new ArrayList<>();
                                UserType subj2 = new UserType();
                                subj2.setType(stu_class);
                                subj2.setId(classid);
                                classlist.add(subj2);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String class_id = obj.getString("class_id");
                                    String class_name = obj.getString("class_name");
                                    UserType subj1 = new UserType();
                                    subj1.setType(class_name);
                                    subj1.setId(class_id);
                                    classlist.add(subj1);
                                    classAdapter = new UserTypeAdapter(classlist, UpdateCommonActivity.this);
                                    binding.classSpinner.user.setAdapter(classAdapter);
                                }
                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(UpdateCommonActivity.this, msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "updatecommonactivity  :  Exception: "+e);
                            AlertClass.alertDialogShow(UpdateCommonActivity.this, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        } else if (error instanceof ServerError) {
                            message = getString(R.string.servererror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        } else if (error instanceof AuthFailureError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        } else if (error instanceof ParseError) {
                            message = getString(R.string.parsingerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        } else if (error instanceof NoConnectionError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        } else if (error instanceof TimeoutError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
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
        RequestQueue requestQueue = Volley.newRequestQueue(UpdateCommonActivity.this);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public void GetAllClassesForTeacherTask() {
        final ProgressDialog dialog = new ProgressDialog(UpdateCommonActivity.this);
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
                            Log.d("RRRRR", "updatecommonactivity  :  response: "+response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (flag.equals("true")) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String class_id = obj.getString("class_id");
                                    String class_name = obj.getString("class_name");
                                    UserType subj1 = new UserType();
                                    subj1.setType(class_name);
                                    subj1.setId(class_id);
                                    classlist.add(subj1);
                                    classteachadapter = new UpdateSubjectClassAdapter(classlist, UpdateCommonActivity.this, UpdateCommonActivity.this, List_class, List_class_ID);
                                    lv_subject.setAdapter(classteachadapter);
                                }
                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(UpdateCommonActivity.this, msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "updatecommonactivity  :  Exception: "+e);
                            AlertClass.alertDialogShow(UpdateCommonActivity.this, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
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
        RequestQueue requestQueue = Volley.newRequestQueue(UpdateCommonActivity.this);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    private void UploadProfileTask() {
        final ProgressDialog dialog = new ProgressDialog(UpdateCommonActivity.this);
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.ProfilePicUpdateURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("RRRRR", "updatecommonactivity  :  response: "+response);
                            JSONObject obj = new JSONObject(response);
                            String flag = obj.getString("status").toString();
                            String msg = obj.getString("message");
                            if (flag.equals("true")) {
                                dialog.dismiss();
                                String profile_pic = obj.getString("image");
                                userSessionManager.updateProfilePic(profile_pic);
                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateCommonActivity.this);
                                alertDialog.setMessage(msg);
                                alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        final HashMap<String, String> homecount = userSessionManager.getHomeScreen();
                                        appointmentcount = homecount.get(userSessionManager.KEY_HomeScreenCount);
                                        if (user_type.equals("2")) {
                                            Intent in = new Intent(UpdateCommonActivity.this, StudentHomeActivity.class);
                                            in.putExtra("count", appointmentcount);
                                            startActivity(in);
                                        } else if (user_type.equals("1")) {
                                            Intent in = new Intent(UpdateCommonActivity.this, MainActivity.class);
                                            in.putExtra("count", appointmentcount);
                                            startActivity(in);
                                        }
                                    }
                                });
                                alertDialog.show();
                            } else {
                                AlertClass.alertDialogShow(UpdateCommonActivity.this, msg);
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            Log.d("RRRRR", "updatecommonactivity  :  Exception: "+e);
                            AlertClass.alertDialogShow(UpdateCommonActivity.this, e.getMessage());
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        } else if (error instanceof ServerError) {
                            message = getString(R.string.servererror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        } else if (error instanceof AuthFailureError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        } else if (error instanceof ParseError) {
                            message = getString(R.string.parsingerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        } else if (error instanceof NoConnectionError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        } else if (error instanceof TimeoutError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(UpdateCommonActivity.this, message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("profile_image", serverimage);
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(UpdateCommonActivity.this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    public void getLatLongFromPlace(String place) {
        try {
            Geocoder selected_place_geocoder = new Geocoder(UpdateCommonActivity.this);
            List<Address> address;
            address = selected_place_geocoder.getFromLocationName(place, 5);
            if (address == null) {
            } else {
                Address location = address.get(0);
                lati = location.getLatitude();
                longi = location.getLongitude();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(UpdateCommonActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        if (mLastLocation != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            animateMarker(mCurrLocationMarker, latLng, false);
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
            lat = location.getLatitude();
            lng = location.getLongitude();
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(UpdateCommonActivity.this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1);
                String firstaddress = addresses.get(0).getAddressLine(0);
                mapcity = addresses.get(0).getLocality();
                mapstate = addresses.get(0).getAdminArea();
                mapcountry = addresses.get(0).getCountryName();
                mappostalCode = addresses.get(0).getPostalCode();
                String[] addressaaray = firstaddress.split(",");
                String firstvalue = addressaaray[0];
                String secondvalue = addressaaray[1];
                // String thirdvalue = addressaaray[2];
                mapaddress = firstvalue + "," + secondvalue;
                tv_address.setText(firstaddress);
            } catch (IOException e) {
                AlertClass.alertDialogShow(UpdateCommonActivity.this, getString(R.string.plzselectotherplace));
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(UpdateCommonActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
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
            googleApiClient = new GoogleApiClient.Builder(UpdateCommonActivity.this)
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
                        }
                    }).build();
            googleApiClient.connect();
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
                                status.startResolutionForResult(UpdateCommonActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(UpdateCommonActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(UpdateCommonActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                    firstaddress = addresses.get(0).getAddressLine(0);
                    mapcity = addresses.get(0).getLocality();
                    mapstate = addresses.get(0).getAdminArea();
                    mapcountry = addresses.get(0).getCountryName();
                    mappostalCode = addresses.get(0).getPostalCode();
                    String[] addressaaray = firstaddress.split(",");
                    String firstvalue = addressaaray[0];
                    String secondvalue = addressaaray[1];
                    // String thirdvalue = addressaaray[2];
                    mapaddress = firstvalue + "," + secondvalue;
                    tv_address.setText(firstaddress);
                } catch (IOException e) {
                    AlertClass.alertDialogShow(UpdateCommonActivity.this, getString(R.string.plzselectotherplace));
                }
            }
        });
    }
}

