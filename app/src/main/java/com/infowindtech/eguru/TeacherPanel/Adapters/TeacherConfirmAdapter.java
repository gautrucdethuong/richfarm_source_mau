package com.infowindtech.eguru.TeacherPanel.Adapters;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.FirebaseChat.FirebaseChatMessage;
import com.infowindtech.eguru.JitsiActivity;
import com.infowindtech.eguru.MineVolleyGlobal;
import com.infowindtech.eguru.MineVolleyListener;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Model.ClassInfoData;
import com.infowindtech.eguru.TeacherPanel.Fragments.StudentProfile_Fragment;
import com.infowindtech.eguru.TeacherPanel.Fragments.Teacher_Appointment_Fragment;
import com.infowindtech.eguru.TeacherPanel.Fragments.Teacher_Confirm_Appointment;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.TeacherPanel.Models.UserType;
import com.infowindtech.eguru.databinding.AppointmentListitemBinding;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TeacherConfirmAdapter extends BaseAdapter {
    private List<ClassInfoData> mListenerList;
    Context mContext;
    AppointmentListitemBinding binding;
    String message, appoint_id, status, str_usertype, str_userId, str_msg, str_appointid, selectedreson, cancel_id, langcode;
    Teacher_Confirm_Appointment fragment;
    ArrayList<UserType> resonlist;
    UserTypeAdapter resonadpter;
    Spinner respon_spn;
    String senderId, senderName, senderUsername, senderPic;
    private ArrayList<ClassInfoData> arraylist;
    String[] endtimearray, lastendarray;
    UserSessionManager userSessionManager;
    String comfirmStatus, payStatus,teaching_place,roomId ;

    public TeacherConfirmAdapter(List<ClassInfoData> mListenerList, Context mContext, Teacher_Confirm_Appointment fragment) {
        this.mListenerList = mListenerList;
        this.mContext = mContext;
        this.fragment = fragment;
        this.arraylist = new ArrayList<ClassInfoData>();
        this.arraylist.addAll(mListenerList);
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        userSessionManager = new UserSessionManager(mContext);
        if (userSessionManager.signIn())
            ((Activity) mContext).finish();
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        str_usertype = user.get(userSessionManager.KEY_USERTYPE);
        str_userId = user.get(userSessionManager.KEY_USERID);

        teaching_place = user.get(userSessionManager.KEY_TEACHINGPLACE);

        AlertClass.setLocale(mContext, langcode);
        if (view == null) {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(mContext),
                    R.layout.appointment_listitem, viewGroup, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (AppointmentListitemBinding) view.getTag();
        }

        comfirmStatus = mListenerList.get(i).getStatus();
        payStatus = mListenerList.get(i).getPayStatus();


        if (comfirmStatus.equals("Confirm") && payStatus.equals("1")) {
            binding.tvConfirm.setText(mContext.getString(R.string.Pending));
            binding.tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    android.app.AlertDialog.Builder ab = new android.app.AlertDialog.Builder(mContext);
                    ab.setMessage(mContext.getString(R.string.paymentPending));
                    ab.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    android.app.AlertDialog alert = ab.create();
                    alert.show();
                }
            });

        }

        if (comfirmStatus.equals("Confirm") && payStatus.equals("2")) {
            if (teaching_place.equals("Online"))
            {
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

                binding.tvConfirm.setText(mContext.getString(R.string.Complete));
                binding.tvConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                        adb.setTitle(mContext.getString(R.string.askforcomplete));
                        adb.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                appoint_id = mListenerList.get(i).getAppointid();
                                status = "Completed";
                                CompleteOperation();

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


        binding.tvName.setText(mListenerList.get(i).getSubject());
        binding.tvDate.setText("" + mListenerList.get(i).getDate());
        binding.tvTime.setText("" + mListenerList.get(i).getTime());

        appoint_id = mListenerList.get(i).getAppointid();
        str_appointid = appoint_id;
        Glide.with(mContext).load(mListenerList.get(i).getUser_pic())
                .thumbnail(0.5f)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imProfile);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudentProfile_Fragment frag = new StudentProfile_Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("studentid", mListenerList.get(i).getId());
                bundle.putString("from", "Appointment");
                bundle.putString("appointment", mListenerList.get(i).getAppointid());
                frag.setArguments(bundle);
                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_layout, frag).commit();
            }
        });
        binding.imChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                senderId = mListenerList.get(i).getId();
                senderName = mListenerList.get(i).getSubject();
                senderUsername = mListenerList.get(i).getUsername();
                senderPic = mListenerList.get(i).getUser_pic();


                if (str_usertype.equals("1")) {

                    callAPII(str_userId, senderId);

                } else {
                    callAPII(senderId, str_userId);
                }

            }
        });
        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
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
                                str_appointid = appoint_id;
                                cancel_id = str_appointid;
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

        return binding.getRoot();
    }


    public void callAPII(String fromid, String toid) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_from_id", fromid);
        map.put("user_to_id", toid);

        new MineVolleyGlobal(mContext).parseVollyStringRequest(ServiceClass.ChatConnection, 1, map, new MineVolleyListener() {
            @Override
            public void onVolleyResponse(String response) {
                Log.d("RRRR", "onVolleyResponse: " + response);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    String flag = obj.getString("status");
                    String msg = obj.getString("message");
                    String chatId = obj.getString("chat_id");
                    Log.d("RRRR", "RRRR: " + chatId);
                    String room_id = chatId;
                    Log.d("RRRR", "onVolleyResponse: " + room_id);

                    Intent intent = new Intent(mContext, FirebaseChatMessage.class);
                    intent.putExtra("sender_id", senderId);
                    intent.putExtra("sender_name", senderName);
                    intent.putExtra("sender_username", senderUsername);
                    intent.putExtra("sender_pic", senderPic);
                    intent.putExtra("room_id", room_id);
                    mContext.startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public void CompleteOperation() {
        final ProgressDialog dialogCom = new ProgressDialog(mContext);
        dialogCom.setMessage(mContext.getString(R.string.pleasewait));
        dialogCom.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherConfimationURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialogCom.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "teachercomfirmadapter  :  onResponse: " + response);

                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                android.app.AlertDialog.Builder ab = new android.app.AlertDialog.Builder(mContext);
                                ab.setMessage(msg);
                                ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
//                                        Teacher_Appointment_Fragment fragment = new Teacher_Appointment_Fragment();
//                                        Bundle bundle = new Bundle();
//                                        bundle.putString("flag", "1");
//                                        fragment.setArguments(bundle);
//                                        ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.appointment, fragment).commit();
//

                                        fragment.Appointmentlist();

                                    }
                                });
                                android.app.AlertDialog alert = ab.create();
                                alert.show();
                                /*   if(msg.equals(R.string.futuredate)){
                                    android.app.AlertDialog.Builder ab = new android.app.AlertDialog.Builder(mContext);
                                    ab.setMessage(msg);
                                    ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                                    android.app.AlertDialog alert = ab.create();
                                    alert.show();
                                }else{
                                    Teacher_Appointment_Fragment fragment = new Teacher_Appointment_Fragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("flag", "1");
                                    fragment.setArguments(bundle);
                                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.appointment, fragment).commit();
                                }*/
                            } else if (flag.equals("false")) {
                                dialogCom.dismiss();
                                AlertClass.alertDialogShow(mContext, msg);
                            }
                        } catch (Exception e) {
                            dialogCom.dismiss();
                            Log.d("RRRRR", "teachercomfirmadapter  :  exception: " + e);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialogCom.dismiss();
                        if (error instanceof NetworkError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof ServerError) {
                            message = mContext.getString(R.string.servererror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof AuthFailureError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof ParseError) {
                            message = mContext.getString(R.string.parsingerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof NoConnectionError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof TimeoutError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appointment_id", appoint_id);
                params.put("status", status);
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public void CancelOperation() {
        final ProgressDialog dialogopera = new ProgressDialog(mContext);
        dialogopera.setMessage(mContext.getString(R.string.pleasewait));
        dialogopera.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherConfimationURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialogopera.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "teachercomfirmadapter  :  onResponse: " + response);

                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                android.app.AlertDialog.Builder ab = new android.app.AlertDialog.Builder(mContext);
                                ab.setMessage(msg);
                                ab.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Teacher_Appointment_Fragment fragment = new Teacher_Appointment_Fragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("flag", "2");
                                        fragment.setArguments(bundle);
                                        ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.appointment, fragment).commit();
                                    }
                                });
                                android.app.AlertDialog alert = ab.create();
                                alert.show();
                            } else if (flag.equals("false")) {
                                dialogopera.dismiss();
                                AlertClass.alertDialogShow(mContext, msg);
                            }
                        } catch (Exception e) {
                            dialogopera.dismiss();
                            Log.d("RRRRR", "teachercomfirmadapter  :  exception: " + e);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialogopera.dismiss();
                        if (error instanceof NetworkError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof ServerError) {
                            message = mContext.getString(R.string.servererror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof AuthFailureError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof ParseError) {
                            message = mContext.getString(R.string.parsingerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof NoConnectionError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof TimeoutError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appointment_id", cancel_id);
                params.put("status", status);
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public void CancelReson() {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage(mContext.getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.GiveCancellationReasonURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "teachercomfirmadapter  :  onResponse: " + response);

                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                cancel_id = str_appointid;
                                status = "Cancel";
                                CancelOperation();
                                dialog.dismiss();
                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(mContext, msg);
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "teachercomfirmadapter  :  exception: " + e);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.cancel();
                        if (error instanceof NetworkError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof ServerError) {
                            message = mContext.getString(R.string.servererror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof AuthFailureError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof ParseError) {
                            message = mContext.getString(R.string.parsingerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof NoConnectionError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof TimeoutError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appointment_id", str_appointid);
                params.put("cancel_reason", str_msg);
                params.put("cancel_reason_id", selectedreson);
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public void Reasonsforcancellation() {
        final ProgressDialog dialogreason = new ProgressDialog(mContext);
        dialogreason.setMessage(mContext.getString(R.string.pleasewait));
        dialogreason.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.CancelReasonURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialogreason.dismiss();
                        JSONObject jsonObject = null;
                        resonlist = new ArrayList<>();
                        try {
                            Log.d("RRRRR", "teachercomfirmadapter  :  onResponse: " + response);

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
                            Log.d("RRRRR", "teachercomfirmadapter  :  exception: " + e);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialogreason.dismiss();
                        if (error instanceof NetworkError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof ServerError) {
                            message = mContext.getString(R.string.servererror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof AuthFailureError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof ParseError) {
                            message = mContext.getString(R.string.parsingerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof NoConnectionError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
                        } else if (error instanceof TimeoutError) {
                            message = mContext.getString(R.string.networkerror);
                            AlertClass.DispToast(mContext, message);
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
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mListenerList.clear();
        if (charText.length() == 0) {
            mListenerList.addAll(arraylist);
        } else {
            for (ClassInfoData wp : arraylist) {
                if (wp.getDate().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mListenerList.add(wp);
                } else if (wp.getSubject().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mListenerList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}