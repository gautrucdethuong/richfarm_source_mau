package com.infowindtech.eguru.TeacherPanel.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

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
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Model.ExpandedSubjectData;
import com.infowindtech.eguru.TeacherPanel.Adapters.SubjectClassSelectionAdapter;
import com.infowindtech.eguru.TeacherPanel.Adapters.Subject_Registration_ExpandAdapter;
import com.infowindtech.eguru.TeacherPanel.Adapters.TimeSloteAdapter;
import com.infowindtech.eguru.TeacherPanel.Adapters.UserTypeAdapter;
import com.infowindtech.eguru.TeacherPanel.Models.TimeSloteSpinnerData;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.TeacherPanel.Models.UserType;
import com.infowindtech.eguru.databinding.FragNextRegistration1Binding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SubjectClassRegistration extends Fragment {
    FragNextRegistration1Binding binding;
    String item, userid, usertype, message, subjects, classes, selsubject, selclass, responseText1, responseid1, responseText2, currency_str;
    ArrayList<UserType> classlist = new ArrayList<>(), genderlist = new ArrayList<>();
    ArrayList<String> placelist = new ArrayList<>();
    SubjectClassSelectionAdapter classAdapter, placeAdapter, genderchoiceadapter;
    UserTypeAdapter userTypeAdapter;
    String langcode, unselected_str, teachergenderchoice, genderchoice, fee, responseid, time, time1, days, timef12, timet6, timef6, timetend, morning, afternoon, evening, duration, teachplace;
    StringBuilder result, showdays;
    Dialog dialog_sub = null;
    Button btn_submit;
    //Button btn_submit_sub;

    ListView lv_subject, lv_class;
    int int_flag = 0, int_flag1 = 0, int_flag2 = 0, int_flag3 = 0, int_flag4 = 0, int_flag5 = 0;

    Subject_Registration_ExpandAdapter expandAdapter;
    ArrayList<ExpandedSubjectData> parentlist = new ArrayList<>();
    ArrayList<UserType> childlist;
    ArrayList<UserType> childsublist, currencylist;
    ExpandableListView expandableListView;
    ArrayList<TimeSloteSpinnerData> durationlist;
    ArrayList<String> checkedData;
    String str_userid, str_days, str_morning, str_afternoon, str_evening, str_place, str_duration, currency_price, currency_symbol;
    UserSessionManager userSessionManager;
    HashMap<String, String> checkBoxData_1 = new HashMap<>();

    ArrayList<String> List_class = new ArrayList<>();
    ArrayList<String> List_id = new ArrayList<>();
    ArrayList<String> List_class_ID = new ArrayList<>();
    ArrayList<String> List_tution_place = new ArrayList<>();
    ArrayList<String> List_gender = new ArrayList<>();
    final ArrayList<TimeSloteSpinnerData> list12 = new ArrayList<>();
    final ArrayList<TimeSloteSpinnerData> list21 = new ArrayList<TimeSloteSpinnerData>();
    final ArrayList<TimeSloteSpinnerData> list3 = new ArrayList<TimeSloteSpinnerData>();
    int get_pos, get_pos1, get_pos2;
    Boolean clickded = false, clickded1 = false, clickded2 = false;
    ArrayList<String> listclass = new ArrayList<>();
    ArrayList<String> listid = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragNextRegistration1Binding.inflate(inflater, container, false);
        userid = getArguments().getString("userid");
        usertype = getArguments().getString("usertype");
        userSessionManager = new UserSessionManager(getActivity());
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(), langcode);

        binding.choicegenderSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderlist.clear();
                List_class.clear();
                List_gender.clear();
                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                dialog.setContentView(R.layout.subjectclass_selection_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                dialog.getWindow().setLayout(AppBarLayout.LayoutParams.WRAP_CONTENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
                lv_subject = dialog.findViewById(R.id.lv_subject);
                lv_subject = dialog.findViewById(R.id.lv_subject);
                btn_submit = dialog.findViewById(R.id.btn_submit);
                ImageView close = dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();

                //gender
                if (genderlist.size() == 0) {
                    UserType t1 = new UserType();
                    t1.setId("1");
                    t1.setType(getString(R.string.male));
                    genderlist.add(t1);
                    UserType t2 = new UserType();
                    t2.setId("2");
                    t2.setType(getString(R.string.female));
                    genderlist.add(t2);
                    genderchoiceadapter = new SubjectClassSelectionAdapter(genderlist, getActivity(), SubjectClassRegistration.this, List_class, List_gender, List_tution_place, List_id);
                    lv_subject.setAdapter(genderchoiceadapter);
                } else {
                    genderchoiceadapter = new SubjectClassSelectionAdapter(genderlist, getActivity(), SubjectClassRegistration.this, List_class, List_gender, List_tution_place, List_id);
                    lv_subject.setAdapter(genderchoiceadapter);
                }
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List_gender = genderchoiceadapter.List_class;
                        ArrayList<String> arrayList1 = new ArrayList<>();
                        arrayList1.addAll(List_gender);
                        HashSet hs = new HashSet();
                        hs.addAll(arrayList1);  // willl not add the duplicate values
                        arrayList1.clear();
                        arrayList1.addAll(hs);
                        String str_gender = arrayList1.toString();
                        str_gender = str_gender.substring(1, str_gender.length() - 1);
                        binding.choicegenderSpinner.setText(str_gender);
                        genderchoice = str_gender;
                        dialog.cancel();
                    }
                });
            }
        });



        CurrencyURL();
        getplace();

        binding.subjectSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_sub = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
                dialog_sub.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                dialog_sub.setContentView(R.layout.expanded_subject_dialog);
                dialog_sub.show();
                expandableListView = dialog_sub.findViewById(R.id.expanded_list);
                btn_submit = dialog_sub.findViewById(R.id.btn_submit);
                ImageView close = dialog_sub.findViewById(R.id.close);
                // checkBoxData_1.clear();
                //checkBoxData_putData=new HashMap<>();

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_sub.cancel();
                    }
                });

                if (parentlist.size() == 0) {
                    GetAllSubTask();
                } else {
                    // Click();
                    expandAdapter = new Subject_Registration_ExpandAdapter(parentlist, getActivity(), SubjectClassRegistration.this, checkBoxData_1);
                    expandableListView.setAdapter(expandAdapter);
                }

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        checkBoxData_1 = expandAdapter.getCheckBoxData();
                        //checkBoxData_putData.putAll(checkBoxData_1);

                        Set<String> keys = checkBoxData_1.keySet();
                        StringBuilder sb = new StringBuilder();
                        StringBuilder sb1 = new StringBuilder();
                        for (String key : keys) {
                            sb.append(key).append(",");
                            sb1.append(checkBoxData_1.get(key)).append(",");
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
                        dialog_sub.cancel();
                    }
                });
            }
        });

        binding.classSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                dialog.setContentView(R.layout.subjectclass_selection_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                dialog.getWindow().setLayout(AppBarLayout.LayoutParams.WRAP_CONTENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
                lv_subject = dialog.findViewById(R.id.lv_subject);
                lv_class = dialog.findViewById(R.id.lv_subject);
                btn_submit = dialog.findViewById(R.id.btn_submit);
                ImageView close = dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();

                //classList
                if (classlist.size() == 0) {
                    GetAllClassesForTeacherTask();
                } else {
                    classAdapter = new SubjectClassSelectionAdapter(classlist, getActivity(), SubjectClassRegistration.this, List_class, List_gender, List_tution_place, List_id);
                    // classAdapter.list_clear();
                    lv_class.setAdapter(classAdapter);
                }

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringBuffer responseText = new StringBuffer();
                        StringBuffer responseid = new StringBuffer();
                        List_class = classAdapter.List_class;
                        List_id = classAdapter.List_ID;

                        // listclass.clear();

                       /* for (int k = 0; k < List_class.size(); k++) {
                            String get_data = List_class.get(k);
                            get_data = get_data.substring(0, get_data.length() - 2);
                            Log.d("jdhfdfdsfjgsd", get_data);

                            listclass.add(get_data);
                        }
                        // listclass.addAll(List_class);*/

                        HashSet hs1 = new HashSet();
                        hs1.addAll(List_class);  // will not add the duplicate values
                        listclass.clear();
                        listclass.addAll(hs1);


                        HashSet hs2 = new HashSet();
                        hs2.addAll(List_id);  // will not add the duplicate values
                        listid.clear();
                        listid.addAll(hs2);

                        String str_class_list = listclass.toString();
                        str_class_list = str_class_list.substring(1, str_class_list.length() - 1);

                        String str_class_id = listid.toString();
                        str_class_id = str_class_id.substring(1, str_class_id.length() - 1);

                        binding.classSpinner.setText(str_class_list);
                        classes = str_class_id;
                        Log.d("class_name", classes);
                        dialog.cancel();
                    }
                });
            }
        });
        binding.tvTimeslot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

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
                final Spinner ato = dialog.findViewById(R.id.to_six);
                final Spinner efrom = dialog.findViewById(R.id.from_six);
                final Spinner eto = dialog.findViewById(R.id.to_last);
                final Button btn = dialog.findViewById(R.id.button);
                TextView tv_heading = dialog.findViewById(R.id.tv_heading);
                ImageView back = dialog.findViewById(R.id.im_back);
                back.setVisibility(View.VISIBLE);
                tv_heading.setText(getString(R.string.Addtimeslote));
                back.setOnClickListener(new View.OnClickListener() {
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


                if (duration.equals("Select Duration") || duration.equals(null) || duration.equals("")) {
                    Toast.makeText(getActivity(), "Please Select Duration", Toast.LENGTH_SHORT).show();
                    dialog.cancel();


                }


                from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (int_flag4 != 0) {
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

                TimeSloteAdapter adapter3 = new TimeSloteAdapter(list3, getActivity());
                eto.setAdapter(adapter3);

                TimeSloteSpinnerData slote5 = list13.get(0);
                timef6 = slote5.getTimeslote().toString();

                TimeSloteSpinnerData slote6 = list3.get(0);
                timetend = slote6.getTimeslote().toString();

                efrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (int_flag != 0) {
                            TimeSloteSpinnerData slote = list13.get(i);
                            timef6 = slote.getTimeslote().toString();
                            // timef6="00:00 P.M";

                            get_pos2 = i;
                            getArraylist31();

                            if (clickded2) {
                                //  Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                                for (int j = 0; j <= get_pos2 + 1; j++) {
                                    list3.remove(0);
                                }
                            } else {
                                for (int j = 0; j <= get_pos2; j++)
                                    list3.remove(0);
                            }
                        } else {
                            int_flag = 1;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                eto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (int_flag1 != 0) {
                            TimeSloteSpinnerData slote = list3.get(i);
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

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (mon.isChecked()) {
                            result.append("Mon");
                            // showdays.append(getString(R.string.Mon));
                        }
                        if (tues.isChecked()) {
                            result.append(" Tue");
                            //showdays.append(getString(R.string.Tue));
                        }
                        if (wed.isChecked()) {
                            result.append(" Wed");
                            //showdays.append(getString(R.string.Wed));
                        }
                        if (ths.isChecked()) {
                            result.append(" Thu");
                            // showdays.append(getString(R.string.Thu));
                        }
                        if (fri.isChecked()) {
                            result.append(" Fri");
                            // showdays.append(getString(R.string.Fri));
                        }
                        if (sat.isChecked()) {
                            result.append(" Sat");
                            //  showdays.append(getString(R.string.Sat));
                        }
                        if (sun.isChecked()) {
                            result.append(" Sun");
                            // showdays.append(getString(R.string.Sun));
                        }
                        days = result.toString();
                        if (days.equals("")) {
                            AlertClass.alertDialogShow(getActivity(), getString(R.string.selectavailability));
                        } else if (time.equals("00:00 A.M") && timef12.equals("00:00 P.M") && timef6.equals("00:00 P.M")) {
                            AlertClass.alertDialogShow(getActivity(), getString(R.string.selectavailability));
                        } else if (!time.equals("00:00 A.M") && time.equals(time1)) {
                            Toast.makeText(getActivity(), "Please select proper morning time slot", Toast.LENGTH_SHORT).show();
                        } else if (!timef12.equals("00:00 P.M") && timef12.equals(timet6)) {
                            Toast.makeText(getActivity(), "Please select proper afternoon time slot", Toast.LENGTH_SHORT).show();
                        }
                        /*
                            else if (!timef6.equals("00:00 P.M") && timef6.equals(timetend)) {
                            Toast.makeText(getActivity(), "Please select proper evening time slot", Toast.LENGTH_SHORT).show();
                        }*/

                        else if (checktime(time, time1, Integer.parseInt(duration), getActivity()).equals("0")) {
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

        ClassdurationURL();
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int_flag = 0;
                int_flag1 = 0;
                int_flag2 = 0;
                int_flag3 = 0;
                int_flag4 = 0;
                int_flag5 = 0;

                str_userid = userid;
                str_days = days;
                Log.d("DDDDD", "onClick: " + str_days + "     " + days);

                str_morning = morning;
                str_afternoon = afternoon;
                str_evening = evening;
                str_duration = duration;

                String place_str = "";
                if (item.equals(getString(R.string.TeacherHome))) {
                    place_str = "1";
                } else if (item.equals(getString(R.string.StudentHome))) {
                    place_str = "2";
                } else if (item.equals(getString(R.string.PublicPlace))) {
                    place_str = "3";
                }
                else if (item.equals("Online")) {
                    place_str = "4";
                }
                selsubject = subjects;
                selclass = classes;
                fee = currency_symbol + " " + binding.etFee.getText().toString().trim();
                Log.d("CCCC", "onItemSelected: " + currency_symbol + "   " + currency_str);

                teachergenderchoice = genderchoice;

                if (genderchoice.equals(getString(R.string.male))) {
                    teachergenderchoice = "1";
                } else if (genderchoice.equals(getString(R.string.female))) {
                    teachergenderchoice = "2";
                } else if ((genderchoice.equals("Female, Male")) || (genderchoice.equals("Male, Female"))) {
                    teachergenderchoice = "1,2";
                }
                currency_price = binding.etFee.getText().toString().trim();
                if (binding.subjectSpinner.getText().toString().trim().equals(getString(R.string.Selectsubject))) {
                    AlertClass.alertDialogShow(getActivity(), getString(R.string.Selectsubject));
                } else if (binding.classSpinner.getText().toString().trim().equals(getString(R.string.Selectclass))) {
                    AlertClass.alertDialogShow(getActivity(), getString(R.string.Selectclass));
                } else if (binding.choicegenderSpinner.getText().toString().trim().equals(getString(R.string.SelectGender))) {
                    AlertClass.alertDialogShow(getActivity(), getString(R.string.SelectGender));
                } else if (binding.etFee.getText().toString().trim().equals("")) {
                    binding.etFee.setError(getString(R.string.enterfee));
                } else if (currency_price.equals("0") || currency_price.equals("00") || currency_price.equals("000") || currency_price.equals("0000")) {
                    binding.etFee.setError(getString(R.string.enterfee));
                } else if (fee.equals("")) {
                    binding.etFee.setError(getString(R.string.enterfee));
                } else if (binding.showtimeslote.getVisibility() == View.GONE) {
                    //Toast.makeText(getActivity(), getString(R.string.selecttimeslot),Toast.LENGTH_SHORT).show();
                    AlertClass.alertDialogShow(getActivity(), getString(R.string.selecttimeslot));
                } else if (unselected_str.equals(getString(R.string.SelectTeachingPlace))) {
                    AlertClass.alertDialogShow(getActivity(), getString(R.string.SelectTeachingPlace));
                } else {
                    RegistrationTask(place_str);
                }
            }
        });
        return binding.getRoot();
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
        list3.clear();
        list3.add(new TimeSloteSpinnerData("00:00 P.M"));
        list3.add(new TimeSloteSpinnerData("00:00 P.M"));
        list3.add(new TimeSloteSpinnerData("06:00 P.M"));
        list3.add(new TimeSloteSpinnerData("06:30 P.M"));
        list3.add(new TimeSloteSpinnerData("07:00 P.M"));
        list3.add(new TimeSloteSpinnerData("07:30 P.M"));
        list3.add(new TimeSloteSpinnerData("08:00 P.M"));
        list3.add(new TimeSloteSpinnerData("08:30 P.M"));
        list3.add(new TimeSloteSpinnerData("09:00 P.M"));
        list3.add(new TimeSloteSpinnerData("09:30 P.M"));
        list3.add(new TimeSloteSpinnerData("10:00 P.M"));
        list3.add(new TimeSloteSpinnerData("10:30 P.M"));
        list3.add(new TimeSloteSpinnerData("11:00 P.M"));
        list3.add(new TimeSloteSpinnerData("11:30 P.M"));
        list3.add(new TimeSloteSpinnerData("12:00 A.M"));
    }

    public void Click() {
        btn_submit.setVisibility(View.VISIBLE);
    }

    public void RegistrationTask(final String place_str) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherRegistration2URL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRR", "subjectclassregistration: response" + response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                Log.d("afternoon", afternoon);
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                alertDialog.setMessage(msg);
                                alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Teacher_Verification_Fragment frag = new Teacher_Verification_Fragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("userid", userid);
                                        bundle.putString("usertype", usertype);
                                        frag.setArguments(bundle);
                                        getActivity().getFragmentManager().beginTransaction().replace(R.id.personal_reg, frag).commit();
                                    }
                                });
                                alertDialog.show();
                                dialog.dismiss();
                            } else if (flag.equals("false")) {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "subjectclassregistration: exception" + e);
                            dialog.dismiss();
                            //AlertClass.alertDialogShow(getActivity(), e.getMessage());
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
                params.put("user_id", str_userid);
                params.put("subject", selsubject);
                params.put("class", selclass);
                params.put("availability_days", str_days);
                params.put("availability_morning", str_morning);
                params.put("availability_afternoon", str_afternoon);
                params.put("availability_evening", str_evening);
                params.put("teaching_place", place_str);
                params.put("teaching_gender_choice", teachergenderchoice);
                params.put("duration", str_duration);
                params.put("teacher_fees", fee);
                params.put("lang_code", langcode);
                params.put("user_activation", "2");
                params.put("currency_code", currency_symbol);
                params.put("currency_price", currency_price);
                Log.d("PPPP", "getParams: " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
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
                        dialog.dismiss();

                        childsublist = new ArrayList<>();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRR", "subjectclassregistration  class: response" + response);
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
                                    expandAdapter = new Subject_Registration_ExpandAdapter(parentlist, getActivity(), SubjectClassRegistration.this, checkBoxData_1);
                                    expandableListView.setAdapter(expandAdapter);
                                }
                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(getActivity(), msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "subjectclassregistration: exception" + e);
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

    public void GetAllClassesForTeacherTask() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.ClassesListURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        childsublist = new ArrayList<>();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRR", "subjectclassregistration  class: response" + response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (flag.equals("true")) {
                                //classlist = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String class_id = obj.getString("class_id");
                                    String class_name = obj.getString("class_name");
                                    UserType subj1 = new UserType();
                                    subj1.setType(class_name);
                                    subj1.setId(class_id);
                                    classlist.add(subj1);
                                    classAdapter = new SubjectClassSelectionAdapter(classlist, getActivity(), SubjectClassRegistration.this, List_class, List_gender, List_tution_place, List_id);
                                    classAdapter.list_clear();
                                    lv_class.setAdapter(classAdapter);
                                }
                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(getActivity(), msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "subjectclassregistration: exception" + e);
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

    private void CurrencyURL() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, ServiceClass.AdminCurrencyURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRR", "subjectclassregistration: response " + response);
                                currencylist = new ArrayList<>();
                                //  currencylist.add(new UserType("SR", "SAR", true));
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                //String msg = obj.getString("message").toString();
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
//                                        currencylist.add(new UserType(cc, symbol, true));
//                                        UserTypeAdapter adapter = new UserTypeAdapter(currencylist, getActivity());
//                                        binding.currencyDropdown.user.setAdapter(adapter);
//                                        binding.currencyDropdown.user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                            @Override
//                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                                UserType currency = currencylist.get(i);
//                                                currency_str = currency.getId().toString();
//                                                currency_symbol = currency.getType().toString();
//                                                Log.d("CCCC", "onItemSelected: " + currency_symbol + "   " + currency_str);
//                                            }
//
//                                            @Override
//                                            public void onNothingSelected(AdapterView<?> adapterView) {
//                                            }
//                                        });
//                                    }

                                } else if (flag.equals("false")) {

                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "subjectclassregistration: exception" + e);
                            //  Log.d("exceptionPrint", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    public void getplace() {

       /* placelist.add(new UserType("Select Place", "0", true));
        placelist.add(new UserType(getString(R.string.TeacherHome), "1", true));
        placelist.add(new UserType(getString(R.string.StudentHome), "2", true));
        placelist.add(new UserType(getString(R.string.PublicPlace), "3", true));*/

        placelist.add(0, getString(R.string.SelectTeachingPlace));

        placelist.add(1, getString(R.string.TeacherHome));
        placelist.add(2, getString(R.string.StudentHome));
        placelist.add(3, getString(R.string.PublicPlace));
        placelist.add(4, "Online");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, placelist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.teachingPlace.user.setAdapter(dataAdapter);

        binding.teachingPlace.user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unselected_str = adapterView.getItemAtPosition(i).toString();
                item = binding.teachingPlace.user.getSelectedItem().toString();
                //   binding.teachingPlace.user.get

                int ink = placelist.size();

/*
                Log.d("positionSelctedItem", item);
                Log.d("sizeofList", String.valueOf(ink));
                Log.d("selected_item", abc);*/


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void ClassdurationURL() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.ClassdurationURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                durationlist = new ArrayList<>();
                                durationlist.add(0, new TimeSloteSpinnerData("Select Duration"));
                                Log.d("RRRR", "subjectclassregistration: response" + response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                JSONArray data = obj.getJSONArray("data");
                                if (flag.equals("true")) {
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
                            Log.d("RRRR", "subjectclassregistration: exception" + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRR", "subjectclassregistration: error" + error);
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
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
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


