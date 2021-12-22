package com.infowindtech.eguru.StudentPanel.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Adapter.Choose_Studyplace_Adapter;
import com.infowindtech.eguru.StudentPanel.Model.LatLngBean;
import com.infowindtech.eguru.StudentPanel.Model.TeacherlistData;
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.StudentPanel.TeacherProfileActivity;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.FragChooseStudyplace1Binding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Choose_StudyPlace_Fragment extends Fragment implements OnMapReadyCallback {
    FragChooseStudyplace1Binding binding;
    String message, user_id, placeid, teacher_user_id, latitude, longitude, profile_pic, address, city, country, zipcode, last_id;
    UserSessionManager userSessionManager;
    ArrayList<TeacherlistData> list;
    Choose_Studyplace_Adapter adapter;
    GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    double lati, lng;
    ArrayList<LatLngBean> listLatLng;
    SupportMapFragment mapFragment;
    ArrayList<LatLng> latLngArrayList;
    boolean mapflag = false;
    String statusmsg, user_id1, langcode;
    int scroll = 0;
    Snackbar bar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragChooseStudyplace1Binding.inflate(inflater, container, false);
        userSessionManager = new UserSessionManager(getActivity());
        if (userSessionManager.signIn())
            getActivity().finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        list = new ArrayList<>();
        user_id = user.get(userSessionManager.KEY_USERID);
        address = user.get(userSessionManager.KEY_ADDRESS);
        city = user.get(userSessionManager.KEY_CITY);
        country = user.get(userSessionManager.KEY_COUNTRY);
        zipcode = user.get(userSessionManager.KEY_ZIPCODE);
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(), langcode);
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        placeid = "2";
        last_id = "";
        binding.tvNoclass.setVisibility(View.VISIBLE);
        GetTeacherlistTask();

        binding.imDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StudentHomeActivity) getActivity()).callDrawer();
            }
        });

        binding.tvMyhome.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_roundedbox));
        binding.tvMyhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvWelcome.setText(getString(R.string.comeforclasses));
                list = new ArrayList<>();
                user_id = user.get(userSessionManager.KEY_USERID);
                placeid = "2";
                last_id = "";
                GetTeacherlistTask();
                binding.tvMyhome.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_roundedbox));
                binding.tvTeacherhome.setBackgroundDrawable(getResources().getDrawable(R.drawable.recangle_shape_purple));
            binding.tvOnline.setBackgroundDrawable(getResources().getDrawable(R.drawable.recangle_shape_purple));
                binding.tvPublicPlace.setBackgroundDrawable(getResources().getDrawable(R.drawable.recangle_shape_purple));
            }
        });
        binding.tvTeacherhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvWelcome.setText(getString(R.string.comeforclasses));
                list = new ArrayList<>();
                user_id = user.get(userSessionManager.KEY_USERID);
                placeid = "1";
                last_id = "";
                GetTeacherlistTask();
                binding.tvMyhome.setBackgroundDrawable(getResources().getDrawable(R.drawable.recangle_shape_purple));
                binding.tvOnline.setBackgroundDrawable(getResources().getDrawable(R.drawable.recangle_shape_purple));
                binding.tvTeacherhome.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_roundedbox));
                binding.tvPublicPlace.setBackgroundDrawable(getResources().getDrawable(R.drawable.recangle_shape_purple));
            }
        });
        binding.tvPublicPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvWelcome.setText(getString(R.string.taponmap));
                binding.tvMyhome.setBackgroundDrawable(getResources().getDrawable(R.drawable.recangle_shape_purple));
               binding.tvOnline.setBackgroundDrawable(getResources().getDrawable(R.drawable.recangle_shape_purple));
                binding.tvTeacherhome.setBackgroundDrawable(getResources().getDrawable(R.drawable.recangle_shape_purple));
                binding.tvPublicPlace.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_roundedbox));
                getLatLongFromPlace(address + " " + city + " " + country + " " + zipcode);
                longitude = String.valueOf(lng);
                latitude = String.valueOf(lati);
                GetTeacherlocationTask();

            }
        });

        binding.tvOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvWelcome.setText(getString(R.string.comeforclasses));
                list = new ArrayList<>();
                user_id = user.get(userSessionManager.KEY_USERID);
                placeid = "4";
                last_id = "";
                GetTeacherlistTask();
                binding.tvMyhome.setBackgroundDrawable(getResources().getDrawable(R.drawable.recangle_shape_purple));
                binding.tvTeacherhome.setBackgroundDrawable(getResources().getDrawable(R.drawable.recangle_shape_purple));
                binding.tvOnline.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_roundedbox));
                binding.tvPublicPlace.setBackgroundDrawable(getResources().getDrawable(R.drawable.recangle_shape_purple));
            }
        });


        binding.tvHeading.setText(getString(R.string.StudyPlaces));
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
                        if (binding.mapLayout.getVisibility() == View.GONE) {
                          //  bar = Snackbar.make(view, getString(R.string.pleasewait), Snackbar.LENGTH_LONG);
                           // ViewGroup contentLay = (ViewGroup) bar.getView().findViewById(R.id.snackbar_text).getParent();
                            //ProgressBar item = new ProgressBar(getActivity());
                          //  contentLay.addView(item, 0);
                           // bar.show();
                            if (binding.lvPlaces.getLastVisiblePosition() == adapter.getCount() - 1) {
                              //  binding.progressBar.setVisibility(View.GONE);
                                if (placeid.equals("1")) {
                                    user_id = user.get(userSessionManager.KEY_USERID);
                                    placeid = "1";
                                    last_id = user_id1;
                                    GetTeacherlistTask();
                                } else if (placeid.equals("2")) {
                                    user_id = user.get(userSessionManager.KEY_USERID);

                                    placeid = "2";
                                    last_id = user_id1;
                                    GetTeacherlistTask();

                                } else if (placeid.equals("4")) {
                                    user_id = user.get(userSessionManager.KEY_USERID);

                                    placeid = "4";
                                    last_id = user_id1;
                                    GetTeacherlistTask();

                                }
                            }
                        }


                    }
                }
            });
        }

        //TODO
//        binding.appbar.setExpanded(true);
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
//                    isShow = true;
//                } else if (isShow) {
//                    isShow = false;
//                }
//            }
//        });

        return binding.getRoot();
    }

    public void GetTeacherlistTask() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeachingPlaceFilterURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RRRRR", "choosestudyplacefragment  :  response: "+response);
                        dialog.cancel();
                       // binding.progressBar.setVisibility(View.GONE);
                        JSONObject jsonObject = null;
                        binding.lvPlaces.setVisibility(View.VISIBLE);
                        binding.mapLayout.setVisibility(View.GONE);
                        try {
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            Log.d("print_REsponse", response);

                            if (flag.equals("true")) {
                                binding.tvNoclass.setVisibility(View.GONE);
                                statusmsg = msg;
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    user_id1 = obj.getString("user_id");
                                    String name = obj.getString("full_name");
                                    String email = obj.getString("email");
                                    String contact_number = obj.getString("contact_number");
                                    String teacher_fees = obj.getString("teacher_fees");
                                    String rating = obj.getString("rating");
                                    String profile_image = obj.getString("profile_image");
                                    String teacher_quality = obj.getString("user_teacher_quality");
                                    String tagline = "";
                                    list.add(new TeacherlistData(user_id1,"", name, email, contact_number, profile_image, teacher_fees, rating, tagline, teacher_quality));
                                    adapter = new Choose_Studyplace_Adapter(list, getActivity());
                                    binding.lvPlaces.setAdapter(adapter);
                                    setListViewHeightBasedOnChildren(binding.lvPlaces);
                                    if (list.size() >= 10) {
                                        binding.lvPlaces.setSelection(list.size() - 10);
                                    }
                                }
                                last_id = user_id1;


                            } else if (flag.equals("false")) {
                                statusmsg = msg;
                             //   binding.progressBar.setVisibility(View.GONE);
                                binding.mapLayout.setVisibility(View.GONE);
                               // binding.tvNoclass.setVisibility(View.VISIBLE);
                                if (list.size()!=0) {
                                    Toast.makeText(getActivity(), getString(R.string.nomore), Toast.LENGTH_SHORT).show();
                                } else {
                                    binding.tvNoclass.setVisibility(View.VISIBLE);
                                    binding.lvPlaces.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                }



//                            } else if (flag.equals("false")) {
//                                statusmsg = msg;
//                                binding.progressBar.setVisibility(View.GONE);
//                                binding.mapLayout.setVisibility(View.GONE);
//                                binding.lvPlaces.setVisibility(View.GONE);
//                                if (list.size() == 0) {
//                                    binding.tvNoclass.setVisibility(View.VISIBLE);
//                                } else {
//                                    bar.dismiss();
//                                    Toast.makeText(getActivity(), getString(R.string.nomore), Toast.LENGTH_SHORT).show();
//                                }
                            }
                        } catch (Exception e) {

                            dialog.cancel();
                            Log.d("RRRRR", "choosestudyplacefragment  :  exception: "+e);
                          //  binding.progressBar.setVisibility(View.GONE);
                            binding.mapLayout.setVisibility(View.GONE);
                            binding.tvNoclass.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.cancel();
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
                params.put("teaching_place", placeid);
                params.put("last_id", last_id);
                params.put("city", city);
                params.put("lang_code", langcode);
                Log.d("dsdsdsdsdsdsdsdsds", "getParams: "+params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public void GetTeacherlocationTask() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherMarkerLocationURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d("RRRRR", "choosestudyplacefragment  :  response: "+response);
                        listLatLng = new ArrayList<>();
                        latLngArrayList = new ArrayList<>();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                binding.tvNoclass.setVisibility(View.GONE);
                                binding.lvPlaces.setVisibility(View.GONE);
                                binding.mapLayout.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    teacher_user_id = obj.getString("user_id");
                                    String name = obj.getString("full_name");
                                    String latitude = obj.getString("latitude");
                                    String longitude = obj.getString("longitude");
                                    final String profile_pic = obj.getString("profile_image");
                                    lati = Double.parseDouble(latitude);
                                    lng = Double.parseDouble(longitude);
                                    LatLng point = new LatLng(lati, lng);
                                    LatLngBean latLngBean = new LatLngBean();
                                    latLngBean.setTitle(name);
                                    latLngBean.setLatitude(latitude);
                                    latLngBean.setLongitude(longitude);
                                    latLngBean.setUser_id(teacher_user_id);
                                    latLngBean.setLatlong(point);
                                    listLatLng.add(latLngBean);
                                    latLngArrayList.add(point);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 14.0f));
                                    final Marker marker1 = mMap.addMarker(new MarkerOptions().position(point).snippet(name));
                                    marker1.setTitle(teacher_user_id);
                                    marker1.showInfoWindow();
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 14.0f));
                                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(Marker marker) {
                                            marker.showInfoWindow();
                                            return false;
                                        }
                                    });
                                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                        @Override
                                        public View getInfoWindow(Marker marker) {
                                            return null;
                                        }

                                        @Override
                                        public View getInfoContents(Marker marker) {
                                            View v = LayoutInflater.from(getActivity()).inflate(R.layout.map_infowindow, null);
                                            LatLng latLng = marker.getPosition();
                                            TextView tvLat = v.findViewById(R.id.tv_name);
                                            TextView tvLng = v.findViewById(R.id.tv_id);
                                            CircleImageView im_pic = v.findViewById(R.id.im_pic);
                                            tvLat.setText(marker.getSnippet());
                                            tvLng.setText(marker.getTitle());
                                            Glide.with(getActivity()).load(profile_pic)
                                                    .thumbnail(0.5f)

                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .into(im_pic);
                                            return v;
                                        }
                                    });
                                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {
                                            mapflag = true;
                                            String id = marker.getTitle();
                                            Intent in = new Intent(getActivity(), TeacherProfileActivity.class);
                                            in.putExtra("id", id);
                                            startActivity(in);
                                        }
                                    });
                                }
                            } else if (flag.equals("false")) {
                                binding.mapLayout.setVisibility(View.VISIBLE);
                                binding.tvNoclass.setVisibility(View.GONE);
                                binding.lvPlaces.setVisibility(View.GONE);
                               // AlertClass.alertDialogShow(getActivity(), getString(R.string.noteacheravailable));
                               // binding.tvNoclass.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "choosestudyplacefragment  :  exception: "+e);
                            AlertClass.alertDialogShow(getActivity(), getString(R.string.noteacheravailable));
                            binding.tvNoclass.setVisibility(View.GONE);
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
                params.put("longitude", longitude);
                params.put("latitude", latitude);
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title(""));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                latitude = String.valueOf(point.latitude);
                longitude = String.valueOf(point.longitude);
                GetTeacherlocationTask();
/*                Public_Place_Fragment frag=new Public_Place_Fragment();
                Bundle bundle=new Bundle();
                bundle.putString("latitude",latitude);
                bundle.putString("longitude",longitude);
                frag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout,frag).commit();*/
            }
        });
    }

    public void getLatLongFromPlace(String place) {
        try {
            Geocoder selected_place_geocoder = new Geocoder(getActivity());
            List<Address> address;
            address = selected_place_geocoder.getFromLocationName(place, 5);
            if (address == null) {
            } else {
                Address location = address.get(0);
                lati = location.getLatitude();
                lng = location.getLongitude();
            }
        } catch (Exception e) {
            e.printStackTrace();

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
}
