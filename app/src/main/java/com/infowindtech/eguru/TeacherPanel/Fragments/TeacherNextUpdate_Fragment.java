package com.infowindtech.eguru.TeacherPanel.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.GetByteArrayDecodeUri;
import com.infowindtech.eguru.MainActivity;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.TeacherPanel.Adapters.TeacherNexUpdateAdapter;
import com.infowindtech.eguru.TeacherPanel.Adapters.TimeSloteAdapter;
import com.infowindtech.eguru.TeacherPanel.Models.TimeSloteSpinnerData;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.TeacherPanel.Models.UserType;
import com.infowindtech.eguru.databinding.FragTeacherUpdateNext1Binding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TeacherNextUpdate_Fragment extends Fragment {
    FragTeacherUpdateNext1Binding binding;
    UserSessionManager userSessionManager;
    String user_id, gender_choice, fee="0 usd", timeslote, userdays, morning, afternoon, evening, fees, teaching_place, institute, qualification, passing_year, experience, duration, year, serverimage, serverimage1, idcopy, certificate, teachplaceid, days, strfromArrayList, strfromArrayList1, appointmentcount;
    int c, g, get_pos, get_pos1, get_pos2;
    byte[] byteimage;
    String userinstitute, unselected_str, usereducation, useryearofpassing, userexperience, message, responseText1, genderchoice, responseid, time = "", time1 = "", timef12 = "", timet6 = "", timef6 = "", timetend = "", teachingplace, genderchoicename, usermorning, userafternoon, userevening, userteachingplace, useridcopy, usercertificate, userfees, usergender, userduration;
    StringBuilder result, showdays;
    Button btn_submit;
    ListView lv_subject;
    ArrayList<UserType> subjectlist = new ArrayList<>(), classlist, currencylist;
    ArrayList<String> placelist = new ArrayList<>();
    String str_placeget = "", str_days = "";

    TeacherNexUpdateAdapter subjectAdapter, placeAdapter;
    String langcode = "", currency_str = "", timeduration = "", active_status = "", appoinmtmentcount = "", currency_price = "", currency_symbol = "", cc = "";
    ArrayList<TimeSloteSpinnerData> durationlist;
    List<String> appointdatelist;
    JSONArray datearray;
    String place_str = "";
    final ArrayList<TimeSloteSpinnerData> list12 = new ArrayList<>();
    final ArrayList<TimeSloteSpinnerData> list21 = new ArrayList<TimeSloteSpinnerData>();
    final ArrayList<TimeSloteSpinnerData> list31 = new ArrayList<TimeSloteSpinnerData>();
    ArrayList<String> List_class = new ArrayList<>();
    Boolean clickded = false, clickded1 = false, clickded2 = false;
    int int_flag = 0, int_flag1 = 0, int_flag2 = 0, int_flag3 = 0, int_flag4 = 0, int_flag5 = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragTeacherUpdateNext1Binding.inflate(inflater, container, false);
        binding.imBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        });
      //  binding.tvHeading.setText(getString(R.string.update));
        userSessionManager = new UserSessionManager(getActivity());
        if (userSessionManager.signIn())
            getActivity().finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        gender_choice = user.get(userSessionManager.KEY_GENDER_CHOICE);
        fee = user.get(userSessionManager.KEY_FEES);
        timeslote = user.get(userSessionManager.KEY_TIME_SLOT);
        days = user.get(userSessionManager.KEY_DAYS);
        morning = user.get(userSessionManager.KEY_MORNING);
        afternoon = user.get(userSessionManager.KEY_AFTRNOON);
        evening = user.get(userSessionManager.KEY_EVENING);
        teaching_place = user.get(userSessionManager.KEY_TEACHINGPLACE);
        institute = user.get(userSessionManager.KEY_INSTITUTE);
        qualification = user.get(userSessionManager.KEY_EDUCATION);
        passing_year = user.get(userSessionManager.KEY_PASSINGYEAR);
        experience = user.get(userSessionManager.KEY_EXPERIENCE);
        duration = user.get(userSessionManager.KEY_APPOINTMENT_DUR);
        idcopy = user.get(userSessionManager.KEY_IM_ID_COPY);
        certificate = user.get(userSessionManager.KEY_IM_CERTIFICATE);
        teachplaceid = user.get(userSessionManager.KEY_TEACHERPLACEID);
        genderchoicename = user.get(userSessionManager.KEY_GenderChoiceName);
        timeduration = user.get(userSessionManager.KEY_APPOINTMENT_DUR);
        active_status = user.get(userSessionManager.KEY_Activation);
        cc = user.get(userSessionManager.KEY_Currency_Code);
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(), langcode);
        Log.d("print_teaching_place", "" + teaching_place);
        Log.d("print_teaching_place", "" + teachplaceid);

        Log.d("TTTTTTT", "onCreateView: "+ user_id+"  "+timeslote+"  "+timeduration+"  "+days);

        try {
            if (active_status.equals("1")) {
                binding.etInstitute.setText(institute);
                binding.etQualification.setText(qualification);
                binding.etPassingYear.setText(passing_year);
                binding.etExperience.setText(experience);
                binding.choicegenderSpinner.setText(genderchoicename);
                binding.showtimeslote.setVisibility(View.VISIBLE);
                binding.showtimeslote.setText(days + "," + morning + "," + afternoon + "," + evening);
                String[] currency = fee.split(" ");
                String currency_price = currency[0];
                String currency_name = currency[1];
                currency_symbol = currency_price;
                Log.d("DDDDDDDD", "onCreateView: " + currency_name + "    " + currency_price + "   " + currency_str + "   " + currency_symbol);
                binding.etFee.setText(currency_name);
                currencylist = new ArrayList<>();
                // currencylist.add(new UserType(currency_price, cc, true));
                if (gender_choice.equals("1")) {
                    binding.choicegenderSpinner.setText(getString(R.string.male));
                } else if (gender_choice.equals("2")) {
                    binding.choicegenderSpinner.setText(getString(R.string.female));
                } else if (gender_choice.equals("1,2")) {
                    binding.choicegenderSpinner.setText(getString(R.string.male) + "," + getString(R.string.female));
                }


                Glide.with(getContext())

                        .asBitmap()
                        .load(idcopy)
                        .into(new SimpleTarget<Bitmap>() {

                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                binding.imIdCopy.setImageBitmap(resource);
                                byte[] byteimage = getBitmapAsByteArray(resource);
                                serverimage = Base64.encodeToString(byteimage, Base64.DEFAULT);
                            }


                        });
                Glide.with(getContext())
                        .asBitmap()
                        .load(certificate)

                        .into(new SimpleTarget<Bitmap>() {

                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                binding.imMdCertificate.setImageBitmap(resource);
                                byte[] byteimage = getBitmapAsByteArray(resource);
                                serverimage1 = Base64.encodeToString(byteimage, Base64.DEFAULT);
                            }

                        });

                genderchoice = gender_choice;
                userdays = days;
                str_days = days;
                usermorning = morning;
                userafternoon = afternoon;
                userevening = evening;
                userteachingplace = teachplaceid;
                useridcopy = serverimage;
                usercertificate = serverimage1;
                userduration = duration;
                userfees = fee;
            } else {
                currencylist = new ArrayList<>();
                //  currencylist.add(new UserType(currency_price, cc, true));
                //  CurrencyURL();
            }
        }catch (Exception ex)
        {
            Log.d("TAG", "onCreateView: "+ex);
        }
        CurrencyURL();
        ClassdurationURL();

        getplace();
//        if (teachplaceid.equals("1")) {
//            str_placeget = getString(R.string.TeacherHome);
//        } else if (teachplaceid.equals("2")) {
//            str_placeget = getString(R.string.StudentHome);
//        } else if (teachplaceid.equals("3")) {
//            str_placeget = getString(R.string.PublicPlace);
//        }
//        else if (teachplaceid.equals("4")) {
//            str_placeget = "Online";
//        }
//        if (!str_placeget.equals("")) {
//            //placelist.add(0, str_placeget);
//        }
//        else
//            placelist.add(0, getString(R.string.SelectTeachingPlace));

        binding.btnUploadImidcopy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.activity_capture_image);
                ImageView camera = dialog.findViewById(R.id.im_camera);
                ImageView gallary = dialog.findViewById(R.id.im_gallary);
                dialog.show();
                camera.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(in, 0);
                        dialog.cancel();
                    }
                });
                gallary.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, 4);
                        dialog.cancel();
                    }
                });
            }
        });
        binding.btnUploadImmdcertificate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.activity_capture_image);
                ImageView camera = dialog.findViewById(R.id.im_camera);
                ImageView gallary = dialog.findViewById(R.id.im_gallary);
                dialog.show();
                camera.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(in, 1);
                        dialog.cancel();
                    }
                });
                gallary.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, 5);
                        dialog.cancel();
                    }
                });
            }
        });
        binding.etPassingYear.setOnClickListener(new OnClickListener() {
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
                tv_cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                tv_ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.etPassingYear.setText(year);
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        binding.choicegenderSpinner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                dialog.setContentView(R.layout.subjectclass_selection_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                dialog.getWindow().setLayout(AppBarLayout.LayoutParams.WRAP_CONTENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
                lv_subject = dialog.findViewById(R.id.lv_subject);
                btn_submit = dialog.findViewById(R.id.btn_submit);
                ImageView close = dialog.findViewById(R.id.close);
                close.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();

                if (subjectlist.size() == 0) {
                    UserType t1 = new UserType();
                    t1.setId("1");
                    t1.setType(getString(R.string.male));
                    subjectlist.add(t1);
                    UserType t2 = new UserType();
                    t2.setId("2");
                    t2.setType(getString(R.string.female));
                    subjectlist.add(t2);
                    subjectAdapter = new TeacherNexUpdateAdapter(subjectlist, getActivity(), TeacherNextUpdate_Fragment.this, List_class);
                    subjectAdapter.claerList();
                    lv_subject.setAdapter(subjectAdapter);
                } else {
                    subjectAdapter = new TeacherNexUpdateAdapter(subjectlist, getActivity(), TeacherNextUpdate_Fragment.this, List_class);
                    lv_subject.setAdapter(subjectAdapter);
                }

                btn_submit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringBuffer responseText = new StringBuffer();
                        StringBuffer responseText2 = new StringBuffer();

                        ArrayList<String> List = subjectAdapter.List_class;
                        ArrayList<String> arrayList1 = new ArrayList<>();
                        arrayList1.addAll(List);
                        HashSet hs = new HashSet();
                        hs.addAll(arrayList1);  // willl not add the duplicate values
                        arrayList1.clear();
                        arrayList1.addAll(hs);

                        String str_place = arrayList1.toString();
                        str_place = str_place.substring(1, str_place.length() - 1);
                        teachingplace = str_place;


                        binding.choicegenderSpinner.setText(str_place);
                        if (str_place.equals(getString(R.string.male))) {
                            genderchoice = "1";
                        } else if (str_place.equals(getString(R.string.female))) {
                            genderchoice = "2";
                        } else if ((str_place.equals("Female, Male")) || (str_place.equals("Male, Female"))) {
                            genderchoice = "1,2";
                        }
                        //genderchoice = str_place;
                        dialog.cancel();
                    }
                });

            }
        });

        binding.tvTimeslot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                dialog.setContentView(R.layout.addtimeslote_dialog);
                dialog.show();
                final CheckBox mon = dialog.findViewById(R.id.monday);
                final CheckBox tues = dialog.findViewById(R.id.tuesday);
                final CheckBox wed = dialog.findViewById(R.id.wednesday);
                final CheckBox ths = dialog.findViewById(R.id.thusday);
                final CheckBox fri = dialog.findViewById(R.id.friday);
                final CheckBox sat = dialog.findViewById(R.id.saturday);
                final CheckBox sun = dialog.findViewById(R.id.sunday);
                final Spinner from = dialog.findViewById(R.id.user);
                final Spinner to = dialog.findViewById(R.id.user1);
                Spinner afrom = dialog.findViewById(R.id.from_twelve);
                Spinner ato = dialog.findViewById(R.id.to_six);
                Spinner efrom = dialog.findViewById(R.id.from_six);
                Spinner eto = dialog.findViewById(R.id.to_last);
                Button btn = dialog.findViewById(R.id.button);
                TextView tv_heading = dialog.findViewById(R.id.tv_heading);
                ImageView back = dialog.findViewById(R.id.im_back);
                back.setVisibility(View.VISIBLE);
                tv_heading.setText(getString(R.string.Addtimeslote));
                back.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                result = new StringBuilder();

                getArrayList2();
                final ArrayList<TimeSloteSpinnerData> list1 = new ArrayList<>();
                list1.add(new TimeSloteSpinnerData("00:00 A.M"));
                list1.add(new TimeSloteSpinnerData("07:00 A.M"));
                list1.add(new TimeSloteSpinnerData("07:30 A.M"));
                list1.add(new TimeSloteSpinnerData("08:00 A.M"));
                list1.add(new TimeSloteSpinnerData("08:30 A.M"));
                list1.add(new TimeSloteSpinnerData("09:00 A.M"));
                list1.add(new TimeSloteSpinnerData("09:30 A.M"));
                list1.add(new TimeSloteSpinnerData("10:00 A.M"));
                list1.add(new TimeSloteSpinnerData("10:30 A.M"));
                list1.add(new TimeSloteSpinnerData("11:00 A.M"));
                list1.add(new TimeSloteSpinnerData("11:30 A.M"));
                TimeSloteAdapter adapter1 = new TimeSloteAdapter(list1, getActivity());
                from.setAdapter(adapter1);

                TimeSloteSpinnerData slote = list1.get(0);
                time = slote.getTimeslote().toString();
                TimeSloteSpinnerData slote1 = list12.get(0);
                time1 = slote1.getTimeslote().toString();

                from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        // TimeSloteSpinnerData slote = list1.get(i);
                        if (int_flag4 != 0) {
                            Log.d("flag_log", String.valueOf(i));

                            TimeSloteSpinnerData slote = list1.get(i);
                            time = slote.getTimeslote().toString();

                            get_pos1 = i;
                            getArrayList2();
                            if (clickded1) {
                                for (int j = 0; j <= get_pos1 + 1; j++) {
                                    list12.remove(0);
                                }
                            } else
                                for (int j = 0; j <= get_pos1; j++)
                                    list12.remove(0);
                        } else {
                            int_flag4 = 1;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                TimeSloteAdapter adapter12 = new TimeSloteAdapter(list12, getActivity());
                to.setAdapter(adapter12);
                to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (int_flag4 != 0) {
                            TimeSloteSpinnerData slote = list12.get(i);
                            time1 = slote.getTimeslote().toString();
                            // time1="00:00 A.M";
                            clickded1 = true;
                        } else {
                            int_flag4 = 1;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                getArratList21();
                final ArrayList<TimeSloteSpinnerData> list2 = new ArrayList<TimeSloteSpinnerData>();
                list2.add(new TimeSloteSpinnerData("00:00 P.M"));
                list2.add(new TimeSloteSpinnerData("12:00 P.M"));
                list2.add(new TimeSloteSpinnerData("12:30 P.M"));
                list2.add(new TimeSloteSpinnerData("01:00 P.M"));
                list2.add(new TimeSloteSpinnerData("01:30 P.M"));
                list2.add(new TimeSloteSpinnerData("02:00 P.M"));
                list2.add(new TimeSloteSpinnerData("02:30 P.M"));
                list2.add(new TimeSloteSpinnerData("03:00 P.M"));
                list2.add(new TimeSloteSpinnerData("03:30 P.M"));
                list2.add(new TimeSloteSpinnerData("04:00 P.M"));
                list2.add(new TimeSloteSpinnerData("04:30 P.M"));
                list2.add(new TimeSloteSpinnerData("05:00 P.M"));
                list2.add(new TimeSloteSpinnerData("05:30 P.M"));
                TimeSloteAdapter adapter2 = new TimeSloteAdapter(list2, getActivity());
                afrom.setAdapter(adapter2);


                TimeSloteSpinnerData slote2 = list2.get(0);
                timef12 = slote2.getTimeslote().toString();

                TimeSloteSpinnerData slote3 = list21.get(0);
                timet6 = slote3.getTimeslote().toString();


                afrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (int_flag2 != 0) {

                            TimeSloteSpinnerData slote = list2.get(i);
                            timef12 = slote.getTimeslote().toString();
                            //timef12="00:00 A.M";
                            get_pos2 = i;

                            getArratList21();

                            if (clickded) {
                                for (int j = 0; j <= get_pos2 + 1; j++) {
                                    list21.remove(0);
                                }
                            } else {
                                for (int j = 0; j <= get_pos2; j++)
                                    list21.remove(0);
                            }
                        } else {
                            int_flag2 = 1;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                TimeSloteAdapter adapter21 = new TimeSloteAdapter(list21, getActivity());
                ato.setAdapter(adapter21);
                ato.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                       /* TimeSloteSpinnerData slote = list21.get(i);
                        timet6 = slote.getTimeslote().toString();*/
                        if (int_flag3 != 0) {
                            TimeSloteSpinnerData slote = list21.get(i);
                            timet6 = slote.getTimeslote().toString();
                            // timet6="00:00 A.M";
                            clickded = true;
                        } else {
                            int_flag3 = 1;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                getArraylist31();

                final ArrayList<TimeSloteSpinnerData> list13 = new ArrayList<TimeSloteSpinnerData>();
                list13.add(new TimeSloteSpinnerData("00:00 P.M"));
                list13.add(new TimeSloteSpinnerData("06:00 P.M"));
                list13.add(new TimeSloteSpinnerData("06:30 P.M"));
                list13.add(new TimeSloteSpinnerData("07:00 P.M"));
                list13.add(new TimeSloteSpinnerData("07:30 P.M"));
                list13.add(new TimeSloteSpinnerData("08:00 P.M"));
                list13.add(new TimeSloteSpinnerData("08:30 P.M"));
                list13.add(new TimeSloteSpinnerData("09:00 P.M"));
                list13.add(new TimeSloteSpinnerData("09:30 P.M"));
                list13.add(new TimeSloteSpinnerData("10:00 P.M"));
                list13.add(new TimeSloteSpinnerData("10:30 P.M"));
                list13.add(new TimeSloteSpinnerData("11:00 P.M"));
                list13.add(new TimeSloteSpinnerData("11:30 P.M"));
                list13.add(new TimeSloteSpinnerData("12:00 A.M"));
                TimeSloteAdapter adapter13 = new TimeSloteAdapter(list13, getActivity());
                efrom.setAdapter(adapter13);


                TimeSloteSpinnerData slote5 = list13.get(0);
                timef6 = slote5.getTimeslote().toString();

                TimeSloteSpinnerData slote6 = list31.get(0);
                timetend = slote6.getTimeslote().toString();

                efrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                       /* TimeSloteSpinnerData slote = list13.get(i);
                        timef6 = slote.getTimeslote().toString();
                        get_pos2 = i;
                        getArraylist31();
                        for (int j = 0; j <= get_pos2; j++) {
                            list31.remove(0);
                        }*/


                        if (int_flag != 0) {
                            TimeSloteSpinnerData slote = list13.get(i);
                            timef6 = slote.getTimeslote().toString();
                            // timef6="00:00 P.M";

                            get_pos2 = i;
                            getArraylist31();

                            if (clickded2) {
                                for (int j = 0; j <= get_pos2 + 1; j++) {
                                    list31.remove(0);
                                }
                            } else {
                                for (int j = 0; j <= get_pos2; j++)
                                    list31.remove(0);
                            }
                        } else {
                            int_flag = 1;
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                TimeSloteAdapter adapter3 = new TimeSloteAdapter(list31, getActivity());
                eto.setAdapter(adapter3);
                eto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        //TimeSloteSpinnerData slote = list31.get(i);
                        // timetend = slote.getTimeslote().toString();

                        if (int_flag1 != 0) {
                            TimeSloteSpinnerData slote = list31.get(i);
                            timetend = slote.getTimeslote().toString();
                            // timetend="00:00 P.M";

                            clickded2 = true;
                        } else {
                            int_flag1 = 1;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mon.isChecked()) {
                            result.append("Mon");
                        }
                        if (tues.isChecked()) {
                            result.append(" Tue");
                        }
                        if (wed.isChecked()) {
                            result.append(" Wed");
                            //showdays.append(getString(R.string.Wed));
                        }
                        if (ths.isChecked()) {
                            result.append(" Thu");
                            //showdays.append(getString(R.string.Thu));
                        }
                        if (fri.isChecked()) {
                            result.append(" Fri");
                            //showdays.append(getString(R.string.Fri));
                        }
                        if (sat.isChecked()) {
                            result.append(" Sat");
                            //showdays.append(getString(R.string.Sat));
                        }
                        if (sun.isChecked()) {
                            result.append(" Sun");
                            //showdays.append(getString(R.string.Sun));
                        }
                        days = result.toString();
                        if (days.contains(" ")) {
                            str_days = days.replace(" ", ",");
                            Log.d("str_days", str_days);
                        }

                        if (days.equals("")) {
                            AlertClass.alertDialogShow(getActivity(), getString(R.string.selectavailability));
                        } else if (!time.equals("00:00 A.M") && time.equals(time1)) {
                            Toast.makeText(getActivity(), "Please select proper morning time slot", Toast.LENGTH_SHORT).show();
                        } else if (!timef6.equals("00:00 P.M") && timef6.equals(timetend)) {
                            Toast.makeText(getActivity(), "Please select proper evening time slot", Toast.LENGTH_SHORT).show();
                        } else if (checktime(time, time1, Integer.parseInt(duration), getActivity()).equals("0")) {
                            Toast.makeText(getActivity(), "Please select proper morning time slot", Toast.LENGTH_SHORT).show();
                        } else if (checktime(timef12, timet6, Integer.parseInt(duration), getActivity()).equals("0")) {
                            Toast.makeText(getActivity(), "Please select proper afternoon time slot", Toast.LENGTH_SHORT).show();
                        } else if (checktime(timef6, timetend, Integer.parseInt(duration), getActivity()).equals("0")) {
                            Toast.makeText(getActivity(), "Please select proper evening time slot", Toast.LENGTH_SHORT).show();
                        } else {
                            morning = "Morning(" + time + " to " + time1 + ")";
                            afternoon = "Afternoon(" + timef12 + " to " + timet6 + ")";
                            evening = "Evening(" + timef6 + " to " + timetend + ")";
                            binding.showtimeslote.setVisibility(View.VISIBLE);
                            binding.showtimeslote.setText(days + "," + "Morning" + "(" + time + " to " + time1 + ")" + "," + "Afternoon" + "(" + timef12 + " to " + timet6 + ")" + "," + "Evening" + "(" + timef6 + " to " + timetend + ")");
                            dialog.cancel();
                        }
                    }
                });
            }
        });
        binding.btnUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                userinstitute = binding.etInstitute.getText().toString().trim();
                usereducation = binding.etQualification.getText().toString().trim();
                useryearofpassing = binding.etPassingYear.getText().toString().trim();
                userexperience = binding.etExperience.getText().toString().trim();
                userdays = days;
                usermorning = morning;
                userafternoon = afternoon;
                userevening = evening;
                userteachingplace = teachplaceid;
                useridcopy = serverimage;
                usercertificate = serverimage1;
                userfees = currency_symbol + " " + binding.etFee.getText().toString().trim();
                Log.d("DDDDDDDD", "onCreateView:   "+currency_price+"   "+currency_str+"   "+currency_symbol);

                usergender = genderchoice;
                userduration = duration;
                currency_price = binding.etFee.getText().toString().trim();
                if (userinstitute.equals("")) {
                    binding.etInstitute.setError(getString(R.string.enterinstitute));
                    binding.scrollView.smoothScrollTo(0, 0);
                } else if (useryearofpassing.equals("")) {
                    binding.etPassingYear.setError(getString(R.string.enterpassingyear));
                    binding.scrollView.smoothScrollTo(0, 0);
                } else if (usereducation.equals("")) {
                    binding.etQualification.setError(getString(R.string.plzselectqualification));
                    binding.scrollView.smoothScrollTo(0, 0);
                } else if (userexperience.equals("")) {
                    binding.etExperience.setError(getString(R.string.enterexp));
                } else if (binding.etFee.getText().toString().trim().equals("")) {
                    binding.etFee.setError(getString(R.string.enterfee));
                } else if (userdays.equals("")) {
                    AlertClass.alertDialogShow(getActivity(), getString(R.string.selecttimeslot));
                } else if (unselected_str.equals(getString(R.string.SelectTeachingPlace))) {
                    AlertClass.alertDialogShow(getActivity(), getString(R.string.SelectTeachingPlace));
                } else if (binding.imIdCopy.getDrawable() == null) {
                    AlertClass.alertDialogShow(getActivity(), getString(R.string.choseid));
                } else if (binding.imMdCertificate.getDrawable() == null) {
                    AlertClass.alertDialogShow(getActivity(), getString(R.string.chosecert));
                } else {
                    UpdateTask();
                    AlertClass.hideKeybord(getActivity(), view);
                }
            }
        });

        return binding.getRoot();
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public void getArrayList2() {
        list12.clear();
        list12.add(new TimeSloteSpinnerData("00:00 A.M"));
        list12.add(new TimeSloteSpinnerData("00:00 A.M"));
        list12.add(new TimeSloteSpinnerData("07:00 A.M"));
        list12.add(new TimeSloteSpinnerData("07:30 A.M"));
        list12.add(new TimeSloteSpinnerData("08:00 A.M"));
        list12.add(new TimeSloteSpinnerData("08:30 A.M"));
        list12.add(new TimeSloteSpinnerData("09:00 A.M"));
        list12.add(new TimeSloteSpinnerData("09:30 A.M"));
        list12.add(new TimeSloteSpinnerData("10:00 A.M"));
        list12.add(new TimeSloteSpinnerData("10:30 A.M"));
        list12.add(new TimeSloteSpinnerData("11:00 A.M"));
        list12.add(new TimeSloteSpinnerData("11:30 A.M"));
        list12.add(new TimeSloteSpinnerData("12:00 P.M"));
    }

    public void getArratList21() {
        list21.clear();
        list21.add(new TimeSloteSpinnerData("00:00 P.M"));
        list21.add(new TimeSloteSpinnerData("00:00 P.M"));
        list21.add(new TimeSloteSpinnerData("12:00 P.M"));
        list21.add(new TimeSloteSpinnerData("12:30 P.M"));
        list21.add(new TimeSloteSpinnerData("01:00 P.M"));
        list21.add(new TimeSloteSpinnerData("01:30 P.M"));
        list21.add(new TimeSloteSpinnerData("02:00 P.M"));
        list21.add(new TimeSloteSpinnerData("02:30 P.M"));
        list21.add(new TimeSloteSpinnerData("03:00 P.M"));
        list21.add(new TimeSloteSpinnerData("03:30 P.M"));
        list21.add(new TimeSloteSpinnerData("04:00 P.M"));
        list21.add(new TimeSloteSpinnerData("04:30 P.M"));
        list21.add(new TimeSloteSpinnerData("05:00 P.M"));
        list21.add(new TimeSloteSpinnerData("05:30 P.M"));
        list21.add(new TimeSloteSpinnerData("06:00 P.M"));
    }

    public void getArraylist31() {
        list31.clear();
        list31.add(new TimeSloteSpinnerData("00:00 P.M"));
        list31.add(new TimeSloteSpinnerData("00:00 P.M"));
        list31.add(new TimeSloteSpinnerData("06:00 P.M"));
        list31.add(new TimeSloteSpinnerData("06:30 P.M"));
        list31.add(new TimeSloteSpinnerData("07:00 P.M"));
        list31.add(new TimeSloteSpinnerData("07:30 P.M"));
        list31.add(new TimeSloteSpinnerData("08:00 P.M"));
        list31.add(new TimeSloteSpinnerData("08:30 P.M"));
        list31.add(new TimeSloteSpinnerData("09:00 P.M"));
        list31.add(new TimeSloteSpinnerData("09:30 P.M"));
        list31.add(new TimeSloteSpinnerData("10:00 P.M"));
        list31.add(new TimeSloteSpinnerData("10:30 P.M"));
        list31.add(new TimeSloteSpinnerData("11:00 P.M"));
        list31.add(new TimeSloteSpinnerData("11:30 P.M"));
        list31.add(new TimeSloteSpinnerData("12:00 A.M"));
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
                        binding.imIdCopy.setImageBitmap(bp);
                        byteimage = GetByteArrayDecodeUri.getBitmapAsByteArray(bp);
                        serverimage = Base64.encodeToString(byteimage, Base64.DEFAULT);

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
                    } catch (Exception e) {
                        AlertClass.alertDialogShow(getActivity(), e.getMessage());
                    }
                }
                break;
        }
    }

    public void UpdateTask() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherVerificationRegistrationURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        final HashMap<String, String> homecount = userSessionManager.getHomeScreen();
                        appointmentcount = homecount.get(userSessionManager.KEY_HomeScreenCount);
                        try {
                            jsonObject = new JSONObject(response);
                            Log.d("RRRR", "teachernextupdatefragment update :  response"+response);

                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                String user_id = data.getString("user_id");
                                final String teaching_gender_choice = data.getString("teaching_gender_choice_str");
                                final String availability_days = data.getString("availability_days");
                                final String availability_morning = data.getString("availability_morning");
                                final String availability_afternoon = data.getString("availability_afternoon");
                                final String availability_evening = data.getString("availability_evening");
                                final String teaching_place = data.getString("teaching_place_str");
                                duration = data.getString("duration");
                                final String teacher_fees = data.getString("teacher_fees");
                                final String identity_card = data.getString("identity_card");
                                final String certificate = data.getString("certificate");
                                final String institute_name = data.getString("institute_name");
                                final String qualification = data.getString("qualification");
                                final String year_of_passing = data.getString("year_of_passing");
                                final String teacher_experince = data.getString("teacher_experince");
                                JSONArray teaching_placearray = data.getJSONArray("teaching_place");
                                final String currency_code = data.getString("teacher_currency_code");
                                if (availability_morning.equals("")) {
                                    morning = getString(R.string.blankmor);
                                } else {
                                    String[] mor = data.getString("availability_morning").split(",");
                                    String last_mor = data.getString("availability_morning").substring(data.getString("availability_morning").lastIndexOf(',') + 1);
                                    morning = "Morning" + "(" + mor[0] + " A.M " + " to " + " " + last_mor + " A.M " + ")";

                                }
                                if (availability_afternoon.equals("")) {
                                    afternoon = getString(R.string.blankafter);
                                } else {
                                    String[] after = data.getString("availability_afternoon").split(",");
                                    String last_after = data.getString("availability_afternoon").substring(data.getString("availability_afternoon").lastIndexOf(',') + 1);
                                    afternoon = "Afternoon" + "(" + after[0] + " P.M " + " to " + " " + last_after + " P.M " + ")";
                                }
                                if (availability_evening.equals("")) {
                                    evening = getString(R.string.blankeve);
                                } else {
                                    String[] eve = data.getString("availability_evening").split(",");
                                    String last_eve = data.getString("availability_evening").substring(data.getString("availability_evening").lastIndexOf(',') + 1);
                                    evening = "Evening" + "(" + eve[0] + " P.M " + " to " + " " + last_eve + " P.M " + ")";
                                }
                                final String dayst = data.getString("availability_days");
                                if (dayst.contains("Mon") || dayst.contains("Tue") || dayst.contains("Wed") || dayst.contains("Thu") || dayst.contains("Fri") || dayst.contains("Sat") || dayst.contains("Sun")) {
                                    days = dayst.replace("Mon", getString(R.string.Mon));
                                    days = days.replace("Tue", getString(R.string.Tue));
                                    days = days.replace("Wed", getString(R.string.Wed));
                                    days = days.replace("Thu", getString(R.string.Thu));
                                    days = days.replace("Fri", getString(R.string.Fri));
                                    days = days.replace("Sat", getString(R.string.Sat));
                                    days = days.replace("Sun", getString(R.string.Sun));
                                } else {
                                    days = data.getString("availability_days");
                                }
                                ArrayList<String> list = new ArrayList<>();
                                for (int i = 0; i < teaching_placearray.length(); i++) {
                                    JSONObject placejson = teaching_placearray.getJSONObject(i);
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
                                JSONArray genderchoicearray = data.getJSONArray("teaching_gender_choice");
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
                                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                                ab.setMessage(msg);
                                ab.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        userSessionManager.UpdateTeacherNextScreenData(teaching_gender_choice, days, morning, afternoon, evening, strfromArrayList
                                                , duration, teacher_fees, identity_card, certificate, institute_name, qualification, year_of_passing, teacher_experince, strfromArrayList1, teaching_place, "1", currency_code);
                                        TeacherAppointmentURL();
                                        TeacherHomeScreenURL();
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert = ab.create();
                                alert.show();
                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(getActivity(), msg);
                            }
                        } catch (Exception e) {
                            AlertClass.alertDialogShow(getActivity(), e.getMessage());
                            Log.d("RRRR", "teachernextupdatefragment  :  exception"+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Log.d("RRRR", "teachernextupdatefragment  :  error"+error);
                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(), message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("teaching_gender_choice", usergender);
                params.put("availability_days", str_days);
                params.put("availability_morning", usermorning);
                params.put("availability_afternoon", userafternoon);
                params.put("availability_evening", userevening);
                params.put("teaching_place", userteachingplace);
                params.put("teacher_fees", userfees);
                params.put("institute_name", userinstitute);
                params.put("qualification", usereducation);
                params.put("year_of_passing", useryearofpassing);
                params.put("teacher_experince", userexperience);
                params.put("identity_card", useridcopy);
                params.put("certificate_card", usercertificate);
                params.put("duration", userduration);
                params.put("user_activation", "1");
                params.put("lang_code", langcode);
                params.put("currency_code", currency_symbol);
                params.put("currency_price", currency_price);

                Log.d("print_params",   " "+ userafternoon+"  "+ userevening +" "+userteachingplace+"  "
                        +userfees+"  "+userinstitute+"  "+usereducation+"  "+useryearofpassing+" "+userexperience);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(150000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private void TeacherHomeScreenURL() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherHomeScreenURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRR", "teachernextupdatefragment  home :  response"+response);

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
                            Log.d("RRRR", "teachernextupdatefragment  :  exception"+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRR", "teachernextupdatefragment  :  error"+error);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(150000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void TeacherAppointmentURL() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherAppointmentDateURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRR", "teachernextupdatefragment  app:  response"+response);
                                appointdatelist = new ArrayList<>();
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                JSONArray data = obj.getJSONArray("data");
                                if (flag.equals("true")) {
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject object = data.getJSONObject(i);
                                        String date = object.getString("date");
                                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
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
                            Log.d("RRRR", "teachernextupdatefragment  :  exception"+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRR", "teachernextupdatefragment  :  error"+error);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(150000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    private void CurrencyURL() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, ServiceClass.AdminCurrencyURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                Log.d("RRRRR", "teachernextupdatefragment curr :  response" + response);
                                JSONObject data = obj.getJSONObject("data");
                                if (flag.equals("true")) {
                                    String admin_currency = data.getString("admin_currency");
                                    binding.currencyDropdown.setText(admin_currency);
                                    currency_symbol = admin_currency;
//                                    for (int i = 0; i < data.length(); i++) {
//                                        JSONObject object = data.getJSONObject(i);
//                                        String cc = object.getString("cc");
//                                        String cname = object.getString("name");
//                                        String symbol = object.getString("symbol");
//                                        currencylist.add(new UserType(cc,symbol, true));
//                                        UserTypeAdapter adapter = new UserTypeAdapter(currencylist, getActivity());
//                                        binding.currencyDropdown.user.setAdapter(adapter);
//
//                                        binding.currencyDropdown.user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                            @Override
//                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                                UserType currency = currencylist.get(i);
//                                                currency_str = currency.getId().toString();
//                                                currency_symbol = currency.getType().toString();
//                                                Log.d("SSSS", "onItemSelected: "+currency_str+"   "+currency_symbol);
//                                            }
//
//                                            @Override
//                                            public void onNothingSelected(AdapterView<?> adapterView) {
//
//                                            }
//                                        });
//                                    }
                                } else if (flag.equals("false")) {

                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "teachernextupdatefragment  :  exception"+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRR", "teachernextupdatefragment  :  error"+error);
                    }
                }) {
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(150000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void ClassdurationURL() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.ClassdurationURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRRR", "teachernextupdatefragment  class:  response" + response);
                                durationlist = new ArrayList<>();
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                JSONArray data = obj.getJSONArray("data");
                                if (flag.equals("true")) {
                                    if (!duration.equals("")) {
                                        TimeSloteSpinnerData subj2 = new TimeSloteSpinnerData();
                                        subj2.setTimeslote(duration);
                                        durationlist.add(subj2);
                                    } else {
                                        durationlist.add(0, new TimeSloteSpinnerData("Select Duration"));
                                    }

                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject object = data.getJSONObject(i);
                                        String durationid = object.getString("classes_duration_id");
                                        String duration = object.getString("duration");
                                        durationlist.add(new TimeSloteSpinnerData(duration));
                                    }
                                    TimeSloteAdapter adapter2 = new TimeSloteAdapter(durationlist, getActivity());
                                    binding.timeSpinner.user.setAdapter(adapter2);
                                    binding.timeSpinner.user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            TimeSloteSpinnerData slote = durationlist.get(i);
                                            duration = slote.getTimeslote().toString();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                } else if (flag.equals("false")) {

                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "teachernextupdatefragment  :  exception"+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRR", "teachernextupdatefragment  :  error"+error);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(150000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void getplace() {

        //  placelist.remove(0);
        //  placelist.clear();
        // ArrayList<String> placelist = new ArrayList<>();

        // placelist.add(0, getString(R.string.SelectTeachingPlace));
        placelist.add(0, getString(R.string.TeacherHome));
        placelist.add(1, getString(R.string.StudentHome));
        placelist.add(2, getString(R.string.PublicPlace));
        placelist.add(3, "Online");

        Log.d("listget", placelist.toString());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, placelist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.placeSpinnerUpdate.user.setAdapter(dataAdapter);

        int spinPlace = dataAdapter.getPosition(teaching_place);
        binding.placeSpinnerUpdate.user.setSelection(spinPlace);

        binding.placeSpinnerUpdate.user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
              //  unselected_str = binding.placeSpinnerUpdate.user.getSelectedItem().toString();
                unselected_str = adapterView.getItemAtPosition(i).toString();
                if (unselected_str.equals(getString(R.string.TeacherHome))) {
                    teachplaceid = "1";
                    userteachingplace = teachplaceid;
                } else if (unselected_str.equals(getString(R.string.StudentHome))) {
                    teachplaceid = "2";
                    userteachingplace = teachplaceid;
                } else if (unselected_str.equals(getString(R.string.PublicPlace))) {
                    teachplaceid = "3";
                    userteachingplace = teachplaceid;
                }
                else if (unselected_str.equals("Online")) {
                    teachplaceid = "4";
                    userteachingplace = teachplaceid;
                }
                Log.d("unselected_str", "" + unselected_str);
                //  teachplaceid= String.valueOf(adapterView.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public static String checktime(String starttime, String endtime, int duration, Context context) {
        String result = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date startDate = simpleDateFormat.parse(starttime);
            Date endDate = simpleDateFormat.parse(endtime);
            if (endDate.getTime() < startDate.getTime()) {
                result = "0";
            } else {
                result = "1";
            }
        } catch (Exception e) {

        }
        return result;
    }

}


