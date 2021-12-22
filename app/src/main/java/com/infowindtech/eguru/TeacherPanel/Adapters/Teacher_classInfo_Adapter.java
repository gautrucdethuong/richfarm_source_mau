package com.infowindtech.eguru.TeacherPanel.Adapters;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.bumptech.glide.Glide;
import com.infowindtech.eguru.JitsiActivity;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Model.ClassInfoData;
import com.infowindtech.eguru.TeacherPanel.Fragments.StudentProfile_Fragment;
import com.infowindtech.eguru.TeacherPanel.Fragments.Teacher_Calender_Fragment;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.TeacherPanel.Models.UserType;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.ClassesInfoListitemBinding;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amplitude.api.DeviceInfo.TAG;


public class Teacher_classInfo_Adapter extends BaseAdapter {
    private List<ClassInfoData> mListenerList;
    Context mContext;
    ClassesInfoListitemBinding binding;
    String status, user_id, teacher_id, str_msg, message, appoint_id, str_user_id, str_teacherid, str_appointid, selectedreson;
    Float f;
    UserSessionManager userSessionManager;
    Teacher_Calender_Fragment fragment;
    ArrayList<UserType> resonlist;
    UserTypeAdapter resonadpter;
    Spinner respon_spn;
    String cancel_id,langcode, pay_status, teaching_place,roomId;
    String[] endtimearray,lastendarray;
    public Teacher_classInfo_Adapter(List<ClassInfoData> mListenerList, Context mContext, Teacher_Calender_Fragment fragment) {
        this.mListenerList = mListenerList;
        this.mContext = mContext;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return mListenerList.size();
    }

    @Override
    public Object getItem(int i) {
        return mListenerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup){
        userSessionManager = new UserSessionManager(mContext);
        if (userSessionManager.signIn())
            ((Activity) mContext).finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        teacher_id = mListenerList.get(i).getTeacher_id();
        teaching_place = user.get(userSessionManager.KEY_TEACHINGPLACE);
        Log.d("place", "getView: "+teaching_place);
        HashMap<String,String> lang=userSessionManager.getLanguage();
        langcode=lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(mContext,langcode);
        str_teacherid = teacher_id;
        str_user_id = user_id;
        if (view == null){
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(mContext),
                    R.layout.classes_info_listitem, viewGroup, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (ClassesInfoListitemBinding) view.getTag();
        }
        status = mListenerList.get(i).getStatus();
        pay_status = mListenerList.get(i).getPayStatus();

      //  is_online = mListenerList.get(i).getIs_online();
        Log.d("AAAAAAAAA", "getView: "+status+"   "+pay_status);

        Glide.with(mContext)
                .load(mListenerList.get(i).getUser_pic())
                .into(binding.imProfile);
        binding.tvClass.setText(mListenerList.get(i).getSubject());
        binding.tvDate.setText("" + mListenerList.get(i).getDate());
        binding.tvTime.setText("" + mListenerList.get(i).getTime());
        binding.tvFee.setVisibility(View.INVISIBLE);
        if(status.equals("")){
            binding.tvPaidclass.setText(mContext.getString(R.string.nostatus));
            binding.tvPaidclass.setTextColor(mContext.getResources().getColor(R.color.oreange));
            binding.actionLayout.setVisibility(View.GONE);
            binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
        }else if(status.equals("Cancel")){
            binding.tvPaidclass.setText(mContext.getString(R.string.Canceled));
            binding.tvPaidclass.setTextColor(mContext.getResources().getColor(R.color.oreange));
            binding.actionLayout.setVisibility(View.GONE);
            binding.tvComplete.setVisibility(View.INVISIBLE);
            binding.tvCancel.setVisibility(View.INVISIBLE);
            binding.borderlayout.setBackgroundResource(R.drawable.red_rectangle_border);
        }else if(status.equals("Completed")){
            binding.tvPaidclass.setText(mContext.getString(R.string.Completed));
            binding.actionLayout.setVisibility(View.GONE);
            binding.tvPaidclass.setTextColor(mContext.getResources().getColor(R.color.green));
            binding.tvComplete.setVisibility(View.INVISIBLE);
            binding.tvCancel.setVisibility(View.INVISIBLE);
            binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
        }
        appoint_id = mListenerList.get(i).getAppointid();
        if (status.equals("Unconfirm")){
            binding.actionLayout.setVisibility(View.VISIBLE);
            binding.tvComplete.setVisibility(View.VISIBLE);
            binding.tvComplete.setText(mContext.getString(R.string.Confirm));
            binding.tvCancel.setVisibility(View.VISIBLE);
            binding.tvRate.setVisibility(View.GONE);
            binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
            binding.tvPaidclass.setText(mContext.getString(R.string.uconfirm));
            binding.tvComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                    adb.setTitle(mContext.getString(R.string.askforconfirm));
                    adb.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            appoint_id=mListenerList.get(i).getAppointid();
                            status="Confirm";
                            ConfirmOperation();
                            dialog.cancel();
                        } });
                    adb.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        } });
                    adb.show();

                }
            });
            binding.tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                    adb.setTitle(mContext.getString(R.string.askforcancel));
                    adb.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Reasonsforcancellation();
                            final Dialog dialog1 = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                            dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                            dialog1.getWindow().setGravity(Gravity.BOTTOM);
                            dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                            dialog1.setContentView(R.layout.cancel_reason_dialog);
                            dialog1.show();
                            final EditText et_msg = dialog1.findViewById(R.id.et_msg);
                            TextView btn_submit = dialog1.findViewById(R.id.tv_send);
                            ImageView back=dialog1.findViewById(R.id.im_back);
                            back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog1.cancel();
                                }
                            });
                            respon_spn = dialog1.findViewById(R.id.user);
                            respon_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    UserType type = resonlist.get(i);
                                    selectedreson = type.getId().toString();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            btn_submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    str_msg = et_msg.getText().toString();
                                    appoint_id = mListenerList.get(i).getAppointid();
                                    str_appointid = appoint_id;
                                    cancel_id=str_appointid;
                                    if (str_msg.equals("")) {
                                        Toast.makeText(mContext, mContext.getString(R.string.enterreason), Toast.LENGTH_SHORT).show();
                                    } else {
                                        AlertClass.hideKeybord(mContext, view);
                                        CancelReson();
                                        dialog1.cancel();
                                    }
                                }
                            });

                        }
                    });
                    adb.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    adb.show();
                }
            });
        }
        else if (status.equals("Confirm") && pay_status.equals("1") ){
            binding.actionLayout.setVisibility(View.VISIBLE);
            binding.tvComplete.setVisibility(View.VISIBLE);
            binding.tvComplete.setText(mContext.getString(R.string.Pending));  //pay
            binding.tvCancel.setVisibility(View.VISIBLE);
            binding.tvRate.setVisibility(View.GONE);
            binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
            binding.tvPaidclass.setText(mContext.getString(R.string.Confirm));
            binding.tvComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setMessage(mContext.getString(R.string.paymentPending));
                    ab.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = ab.create();
                    alert.show();

                }
            });
            binding.tvCancel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                    adb.setTitle(mContext.getString(R.string.askforcancel));
                    adb.setIcon(android.R.drawable.ic_dialog_alert);
                    adb.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Reasonsforcancellation();
                            final Dialog dialog1 = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                            dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                            dialog1.getWindow().setGravity(Gravity.BOTTOM);
                            dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                            dialog1.setContentView(R.layout.cancel_reason_dialog);
                            dialog1.show();
                            final EditText et_msg = dialog1.findViewById(R.id.et_msg);
                            TextView btn_submit = dialog1.findViewById(R.id.tv_send);
                            ImageView back=dialog1.findViewById(R.id.im_back);
                            back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog1.cancel();
                                }
                            });
                            respon_spn = dialog1.findViewById(R.id.user);
                            respon_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    UserType type = resonlist.get(i);
                                    selectedreson = type.getId().toString();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            btn_submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    str_msg = et_msg.getText().toString();
                                    appoint_id = mListenerList.get(i).getAppointid();
                                    str_appointid = appoint_id;
                                    if (str_msg.equals("")) {
                                        Toast.makeText(mContext, mContext.getString(R.string.enterreason), Toast.LENGTH_SHORT).show();
                                    } else{
                                        AlertClass.hideKeybord(mContext, view);
                                        CancelReson();
                                        dialog1.cancel();
                                    }
                                }
                            });
                        }
                    });
                    adb.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    adb.show();
                }
            });
        }
        else if (status.equals("Confirm") && pay_status.equals("2") ){

            if(teaching_place.equals("Online")) {

                binding.tvStartClass.setVisibility(View.VISIBLE);
                binding.tvStartClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


//                        Intent i = new Intent(mContext, JitsiActivity.class);
//                        mContext.startActivity(i);


                        // Initialize default options for Jitsi Meet conferences.
                        URL serverURL;
                        try {
                            serverURL = new URL("https://meet.jit.si");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            throw new RuntimeException("Invalid server URL!");
                        }
                        JitsiMeetConferenceOptions defaultOptions
                                = new JitsiMeetConferenceOptions.Builder()
                                .setServerURL(serverURL)
                                .setWelcomePageEnabled(false)
                                .build();
                        JitsiMeet.setDefaultConferenceOptions(defaultOptions);

                        roomId = mListenerList.get(i).getRoom_id();
                        Log.d("room", "onClick: " + roomId);

                        if (roomId.equals("") || roomId.equals(null)) {

                            Toast.makeText(mContext, "Doesn't have room Id", Toast.LENGTH_SHORT).show();

                        } else {

                            String text = roomId;

                            if (text.length() > 0) {
                                // Build options object for joining the conference. The SDK will merge the default
                                // one we set earlier and this one when joining.
                                JitsiMeetConferenceOptions options
                                        = new JitsiMeetConferenceOptions.Builder()
                                        .setRoom(text)
                                        .build();
                                // Launch the new activity with the given options. The launch() method takes care
                                // of creating the required Intent and passing the options.
                                JitsiMeetActivity.launch(mContext, options);

                            }
                        }
                    }
                });
            }
                binding.actionLayout.setVisibility(View.VISIBLE);
                binding.tvComplete.setVisibility(View.VISIBLE);
                binding.tvComplete.setText(mContext.getString(R.string.Complete));  //pay
                binding.tvCancel.setVisibility(View.VISIBLE);
                binding.tvRate.setVisibility(View.GONE);
                binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
                binding.tvPaidclass.setText(mContext.getString(R.string.Confirm));
                binding.tvComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                        adb.setTitle(mContext.getString(R.string.askforcomplete));
                        adb.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                appoint_id = mListenerList.get(i).getAppointid();
                                status = "Completed";
                                ConfirmOperation();
                                dialog.cancel();
                            }
                        });
                        adb.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        adb.show();

                    }
                });
                binding.tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                        adb.setTitle(mContext.getString(R.string.askforcancel));
                        adb.setIcon(android.R.drawable.ic_dialog_alert);
                        adb.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Reasonsforcancellation();
                                final Dialog dialog1 = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                                dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                                dialog1.getWindow().setGravity(Gravity.BOTTOM);
                                dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                dialog1.setContentView(R.layout.cancel_reason_dialog);
                                dialog1.show();
                                final EditText et_msg = dialog1.findViewById(R.id.et_msg);
                                TextView btn_submit = dialog1.findViewById(R.id.tv_send);
                                ImageView back = dialog1.findViewById(R.id.im_back);
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog1.cancel();
                                    }
                                });
                                respon_spn = dialog1.findViewById(R.id.user);
                                respon_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        UserType type = resonlist.get(i);
                                        selectedreson = type.getId().toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                                btn_submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        str_msg = et_msg.getText().toString();
                                        appoint_id = mListenerList.get(i).getAppointid();
                                        str_appointid = appoint_id;
                                        if (str_msg.equals("")) {
                                            Toast.makeText(mContext, mContext.getString(R.string.enterreason), Toast.LENGTH_SHORT).show();
                                        } else {
                                            AlertClass.hideKeybord(mContext, view);
                                            CancelReson();
                                            dialog1.cancel();
                                        }
                                    }
                                });
                            }
                        });
                        adb.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        adb.show();
                    }
                });
            }



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StudentProfile_Fragment frag=new StudentProfile_Fragment();
                Bundle bundle=new Bundle();
                bundle.putString("studentid",mListenerList.get(i).getId());
                bundle.putString("from","Appointment");
                bundle.putString("appointment",mListenerList.get(i).getAppointid());
                frag.setArguments(bundle);
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout,frag).commit();




            }
        });
        return binding.getRoot();
    }
    public void ConfirmOperation(){
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage(mContext.getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST,ServiceClass.TeacherConfimationURL,
                new Response.Listener<String>(){
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "teacherclassinfoadapter  :  onResponse: "+response);

                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                fragment.clearlist();
                                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                                ab.setMessage(msg);
                                ab.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        fragment.setdata();
                                    }
                                });
                                AlertDialog alert = ab.create();
                                alert.show();
                                dialog.dismiss();

                            } else if (flag.equals("false")) {
                                dialog.dismiss();
                                AlertClass.alertDialogShow(mContext, msg);
                            }
                        } catch (Exception e) {
                            dialog.dismiss();
                            Log.d("RRRRR", "teacherclassinfoadapter  :  exception: "+e);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof NetworkError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof ServerError) {
                            message =mContext.getString(R.string.servererror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof AuthFailureError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof ParseError) {
                            message =mContext.getString(R.string.parsingerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof NoConnectionError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof TimeoutError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appointment_id",appoint_id);
                params.put("status",status);
                params.put("lang_code",langcode);
                Log.d("RRRRRR", "getParams: "+params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
    public void CancelOperation() {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage(mContext.getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST,ServiceClass.TeacherConfimationURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "teacherclassinfoadapter  :  onResponse: "+response);

                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            final String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                                ab.setMessage(msg);
                                ab.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        fragment.setdata();
                                    }
                                });
                                AlertDialog alert = ab.create();
                                alert.show();
                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(mContext, msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "teacherclassinfoadapter  :  exception: "+e);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.cancel();
                        if (error instanceof NetworkError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof ServerError) {
                            message =mContext.getString(R.string.servererror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof AuthFailureError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof ParseError) {
                            message =mContext.getString(R.string.parsingerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof NoConnectionError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof TimeoutError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        }
                    }
                })
            {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appointment_id",cancel_id);
                params.put("status", "Cancel");
                params.put("lang_code",langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
    public void CancelReson(){
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage(mContext.getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST,ServiceClass.GiveCancellationReasonURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "teacherclassinfoadapter  :  onResponse: "+response);

                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                cancel_id= str_appointid;
                                status = "Cancel";
                                CancelOperation();
                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(mContext, msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "teacherclassinfoadapter  :  exception: "+e);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.cancel();
                        if (error instanceof NetworkError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof ServerError) {
                            message =mContext.getString(R.string.servererror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof AuthFailureError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof ParseError) {
                            message =mContext.getString(R.string.parsingerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof NoConnectionError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof TimeoutError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        }
                    }
                })
            {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appointment_id", str_appointid);
                params.put("cancel_reason", str_msg);
                params.put("cancel_reason_id",selectedreson);
                params.put("lang_code",langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
    public void Reasonsforcancellation(){
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.CancelReasonURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        resonlist = new ArrayList<>();
                        try {
                            Log.d("RRRRR", "teacherclassinfoadapter  :  onResponse: "+response);

                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String messageid = obj.getString("message_id");
                                    String message = obj.getString("message");
                                    resonlist.add(new UserType(message, messageid, true));
                                }
                                resonadpter = new UserTypeAdapter(resonlist, mContext);
                                respon_spn.setAdapter(resonadpter);
                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(mContext, msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "teacherclassinfoadapter  :  exception: "+e);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NetworkError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof ServerError) {
                            message =mContext.getString(R.string.servererror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof AuthFailureError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof ParseError) {
                            message =mContext.getString(R.string.parsingerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof NoConnectionError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        } else if (error instanceof TimeoutError) {
                            message =mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext,message);
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("lang_code",langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }
}
