package com.infowindtech.eguru.TeacherPanel.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.databinding.DataBindingUtil;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Fragments.Student_Next_Registation;
import com.infowindtech.eguru.TeacherPanel.Adapters.UserTypeAdapter;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.TeacherPanel.Models.UserType;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.FragPersonalRegistration1Binding;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Personal_Registration_Fragment extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {
    FragPersonalRegistration1Binding binding;
    ArrayList<UserType> userTypelist;
    UserTypeAdapter userTypeAdapter;
    String message, username, email, password, fullname, contactno, address, city, country, zipcode, usertype, profession, dob, latitude, longitude, backto, rgroup, selectdate, usernation;
    double lati, longi;
    UserSessionManager userSessionManager;
    ArrayList<String> countries;
    String country1, mapaddress, mapcity, mapcountry, mappostalCode, mapstate, selectedCountry, langcode;
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    TextView tv_address;
    double lat, lng;
    GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin, savelang;
    private SharedPreferences.Editor loginPrefsEditor;
    LocationManager manager;


    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.frag_personal_registration1);
        Intent in = getIntent();
        backto = in.getStringExtra("go");
        if (backto.equals("teacher")) {
            usertype = "1";
        } else if (backto.equals("student")) {
            usertype = "2";
        }
        userSessionManager = new UserSessionManager(getApplicationContext());
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        savelang = loginPreferences.getBoolean("saveLang", false);
        loginPrefsEditor = loginPreferences.edit();


        if (savelang == true) {
            langcode = loginPreferences.getString("lang", "");
            AlertClass.setLocale(Personal_Registration_Fragment.this, langcode);
        } else {
            langcode = "en";
            userSessionManager.selectedlanguage("en");
            AlertClass.setLocale(Personal_Registration_Fragment.this, langcode);
        }
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        Personal_Registration_Fragment.this.setFinishOnTouchOutside(true);
        manager = (LocationManager) Personal_Registration_Fragment.this.getSystemService(Context.LOCATION_SERVICE);
        Log.d("LLLLLLLL", "onCreate: "+manager);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(Personal_Registration_Fragment.this)) {
            enableLoc();
        }
        userSessionManager = new UserSessionManager(Personal_Registration_Fragment.this);
        userTypelist = new ArrayList<>();
        if (backto.equals("teacher")) {
            userTypelist.add(new UserType(getString(R.string.teacher), "1", true));
        } else if (backto.equals("both")) {
            userTypelist.add(new UserType(getString(R.string.Selecttype), "0", true));
            userTypelist.add(new UserType(getString(R.string.teacher), "1", true));
            userTypelist.add(new UserType(getString(R.string.student), "2", true));
        } else if (backto.equals("student")) {
            userTypelist.add(new UserType(getString(R.string.student), "2", true));
        }
        userTypeAdapter = new UserTypeAdapter(userTypelist, Personal_Registration_Fragment.this);
        binding.userTypeSpinner.setAdapter(userTypeAdapter);
        binding.userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                UserType type = userTypelist.get(i);
                usertype = type.getId().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        binding.etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                final Dialog dialog1 = new Dialog(Personal_Registration_Fragment.this);
                dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.datepickerdialog);
                dialog1.show();
                DatePicker simpleDatePicker = dialog1.findViewById(R.id.simpleDatePicker); // initiate a date picker
                TextView tv_ok = dialog1.findViewById(R.id.tv_ok);
                TextView tv_cancel = dialog1.findViewById(R.id.tv_cancel);
                simpleDatePicker.setSpinnersShown(false);
                simpleDatePicker.setMaxDate(calendar.getTimeInMillis());
                simpleDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
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
                        dialog1.cancel();
                    }
                });
            }
        });
        countries = new ArrayList<String>();
        final Dialog dialog = new Dialog(Personal_Registration_Fragment.this);
        dialog.setContentView(R.layout.map_dialog);
        final TextView tv_ok = dialog.findViewById(R.id.tv_ok);



        tv_address = dialog.findViewById(R.id.tv_address);

        /*   mapFragment = (SupportMapFragment) Personal_Registration_Fragment.this.getSupportFragmentManager()
                .findFragmentById(R.id.mapdialog);
        mapFragment.getMapAsync(this);*/

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapdialog);
        mapFragment.getMapAsync(this);

        binding.etAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tv_address.getText().toString().trim().equals("")) {
                            dialog.cancel();
                        } else {
                            binding.etAddress.setText(mapaddress);
                            binding.etCity.setText(mapcity);
                            binding.etZipcode.setText(mappostalCode);
                            Locale[] locale = Locale.getAvailableLocales();
                            countries = new ArrayList<String>();
                            countries.add(mapcountry);
                            for (Locale loc : locale) {
                                country1 = loc.getDisplayCountry();
                                if (country1.length() > 0 && !countries.contains(country1)) {
                                    countries.add(country1);
                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Personal_Registration_Fragment.this, android.R.layout.simple_spinner_item, countries);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.countryLayout.setAdapter(adapter);
                            dialog.cancel();
                        }
                    }
                });
            }
        });
        Locale[] locale = Locale.getAvailableLocales();
        for (Locale loc : locale) {
            country1 = loc.getDisplayCountry();
            if (country1.length() > 0 && !countries.contains(country1)) {
                countries.add(country1);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Personal_Registration_Fragment.this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.countryLayout.setAdapter(adapter);
        binding.countryLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                  country = countries.get(i).toString();
                Log.d("CCCCCCCCCCCC", "onItemSelected: "+country);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = binding.etUsername.getText().toString().trim();
                fullname = binding.etFullname.getText().toString().trim();
                email = binding.etEmail.getText().toString().trim();
                password = binding.etPassword.getText().toString().trim();
                int str_pass_length = password.length();

                contactno = binding.etPhone.getText().toString().trim();

                address = binding.etAddress.getText().toString().trim();
                city = binding.etCity.getText().toString().trim();
                zipcode = binding.etZipcode.getText().toString().trim();
                usernation = binding.etNation.getText().toString().trim();

                dob = selectdate;
                selectedCountry = country;
                profession = usertype;
                if (binding.male.isChecked()) {
                    rgroup = "1";
                } else if (binding.female.isChecked()) {
                    rgroup = "2";
                } else {
                    rgroup = "3";
                }
                getLatLongFromPlace(address + "," + city + "," + selectedCountry + "," + zipcode);
                latitude = String.valueOf(lati);
                longitude = String.valueOf(longi);
                if (username.equals("")) {
                    binding.etUsername.setError(getString(R.string.enterusername));
                    binding.scrollView.smoothScrollTo(0, 0);
                } else if (fullname.equals("")) {
                    binding.etFullname.setError(getString(R.string.enterfullname));
                    binding.scrollView.smoothScrollTo(0, 0);
                } else if (email.equals("")) {
                    binding.etEmail.setError(getString(R.string.enteremailid));
                    binding.scrollView.smoothScrollTo(0, 0);
                } else if (!isValidEmail(email)) {
                    binding.etEmail.setError(getString(R.string.entervalidemail));
                    binding.scrollView.smoothScrollTo(0, 0);
                } else if (password.equals("")) {
                    binding.etPassword.setError(getString(R.string.enterpassword));
                } else if (str_pass_length < 6) {
                    binding.etPassword.setError("Password Should be Equal or Less than 6 Character");
                } else if (contactno.equals("")) {
                    binding.etPhone.setError(getString(R.string.entercontactno));
                } else if (contactno.charAt(0) < 4) {
                    binding.etPhone.setError(getString(R.string.plzentervalidmobile));
                } else if (address.equals("")) {
                    binding.etAddress.setError(getString(R.string.enteraddress));
                } else if (city.equals("")) {
                    binding.etCity.setError(getString(R.string.entercity));
                } else if (zipcode.equals("")) {
                    binding.etZipcode.setError(getString(R.string.enterzipcode));
                } else if (usernation.equals("")) {
                    binding.etNation.setError(getString(R.string.enterusernation));
                } else if (binding.etDob.getText().toString().equals("")) {
                    AlertClass.alertDialogShow(Personal_Registration_Fragment.this, getString(R.string.enterdob));
                } else if (profession.equals("0")) {
                    AlertClass.alertDialogShow(Personal_Registration_Fragment.this, getString(R.string.enterprofession));
                } else {
                    RegistrationTask();
                    AlertClass.hideKeybord(Personal_Registration_Fragment.this, view);
                }
            }
        });
       // binding.tvHeading.setText(R.string.register);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Personal_Registration_Fragment.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);

                Location mobileLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (mobileLocation == null) {
                    mobileLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                onLocationChanged_function(mobileLocation);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            Location mobileLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (mobileLocation == null) {
                mobileLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            onLocationChanged_function(mobileLocation);
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(Personal_Registration_Fragment.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
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
                    AlertClass.alertDialogShow(Personal_Registration_Fragment.this, getString(R.string.plzselectotherplace));
                }
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(Personal_Registration_Fragment.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public void RegistrationTask() {
        final ProgressDialog dialog = new ProgressDialog(Personal_Registration_Fragment.this);
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.UserRegistrationCommonURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRR", "personalregistrationfragment: response"+response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                JSONObject obj = jsonObject.getJSONObject("data");
                                final String user_id = obj.getString("user_id");
                                final String user_type = obj.getString("user_type");

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Personal_Registration_Fragment.this);
                                alertDialog.setMessage(msg);
                                alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (user_type.equals("1")) {
                                            userSessionManager.reglogindata(username, password);
                                            SubjectClassRegistration frag = new SubjectClassRegistration();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("userid", user_id);
                                            bundle.putString("usertype", user_type);
                                            frag.setArguments(bundle);
                                            getFragmentManager().beginTransaction().replace(R.id.personal_reg, frag).commit();
                                        } else if (user_type.equals("2")) {
                                            userSessionManager.reglogindata(username, password);
                                            Student_Next_Registation frag = new Student_Next_Registation();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("userid", user_id);
                                            bundle.putString("usertype", user_type);
                                            frag.setArguments(bundle);
                                            getFragmentManager().beginTransaction().replace(R.id.personal_reg, frag).commit();
                                        }
                                    }
                                });
                                alertDialog.show();
                            } else if (flag.equals("false")) {
                                Toast.makeText(Personal_Registration_Fragment.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "personalregistrationfragment: exception"+e);

                            //AlertClass.alertDialogShow(Personal_Registration_Fragment.this,e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Log.d("RRRR", "personalregistrationfragment: error"+error);

                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(Personal_Registration_Fragment.this, message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("full_name", fullname);
                params.put("contact_number", contactno);
                params.put("address", address);
                params.put("city", city);
                params.put("country", selectedCountry);
                params.put("zipcode", zipcode);
                params.put("date_of_birth", dob);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("user_type", profession);
                params.put("gender", rgroup);
                params.put("nationality", usernation);
                params.put("lang_code", langcode);
                params.put("user_activation", "2");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Personal_Registration_Fragment.this);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void getLatLongFromPlace(String place) {
        try {
            Geocoder selected_place_geocoder = new Geocoder(Personal_Registration_Fragment.this);
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
        if (ContextCompat.checkSelfPermission(Personal_Registration_Fragment.this,
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
    }

    public void onLocationChanged_function(Location location) {
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


            lat = location.getLatitude();
            lng = location.getLongitude();
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(Personal_Registration_Fragment.this, Locale.getDefault());
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
                //String thirdvalue = addressaaray[2];
                mapaddress = firstvalue + "," + secondvalue;
                tv_address.setText(firstaddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(Personal_Registration_Fragment.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(Personal_Registration_Fragment.this, getString(R.string.permissiondenied), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
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
                    // Post again 16ms later.
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
        LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {
        Log.d("LLLLL", "enableLoc: "+googleApiClient);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(Personal_Registration_Fragment.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            Log.d("LLLL", "onConnected: ");
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            Log.d("LLLLL", "onConnectionSuspended: "+i);
                            googleApiClient.connect();
                            Log.d("LLLLL", "onConnectionSuspended: "+i);
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.d("LLLLL", "onConnectionFailed: "+connectionResult);
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
                    Log.d("LLLLL", "status: "+status+"    "+result);
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(Personal_Registration_Fragment.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                Log.d("LLLLL", "Excep: "+e);
                            }
                            break;
                    }
                }
            });
        }
    }
    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }*/




}
