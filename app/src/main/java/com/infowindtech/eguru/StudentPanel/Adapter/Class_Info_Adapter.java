package com.infowindtech.eguru.StudentPanel.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import androidx.databinding.DataBindingUtil;

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
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.JitsiActivity;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.Fragments.Class_Info_Fragment;
import com.infowindtech.eguru.StudentPanel.Model.ClassInfoData;
import com.infowindtech.eguru.TeacherPanel.Adapters.UserTypeAdapter;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.TeacherPanel.Models.UserType;
import com.infowindtech.eguru.databinding.ClassStudentListitemBinding;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;


public class Class_Info_Adapter extends BaseAdapter {
    private List<ClassInfoData> mListenerList;
    Context mContext;
    ClassStudentListitemBinding binding;
    String status, user_id, teacher_id, str_msg, message, appoint_id, str_user_id, str_teacherid, str_appointid, selectedreson;
    Float f;
    UserSessionManager userSessionManager;
    Class_Info_Fragment fragment;
    ArrayList<UserType> resonlist;
    UserTypeAdapter resonadpter;
    Spinner respon_spn;
    String cancel_id, ratingstatus, langcode, pay_status, isOnline, roomId;
    String[] endtimearray, lastendarray;

    public Class_Info_Adapter(List<ClassInfoData> mListenerList, Context mContext, Class_Info_Fragment fragment) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        String language = Locale.getDefault().getDisplayLanguage();
       /* if(language.equals("English")){
            langcode="en";
        }else {
            langcode="ar";
        }*/
        userSessionManager = new UserSessionManager(mContext);
        if (userSessionManager.signIn())
            ((Activity) mContext).finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        teacher_id = mListenerList.get(i).getTeacher_id();
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(mContext, langcode);
        str_teacherid = teacher_id;
        str_user_id = user_id;
        if (view == null) {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(mContext),
                    R.layout.class_student_listitem, viewGroup, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (ClassStudentListitemBinding) view.getTag();
        }
        status = mListenerList.get(i).getId();
        ratingstatus = mListenerList.get(i).getRatingstauts();
        binding.tvClass.setText(mListenerList.get(i).getSubject());
        binding.tvDate.setText("" + mListenerList.get(i).getDate());
        binding.tvTime.setText("" + mListenerList.get(i).getTime());
        binding.tvFee.setText(mListenerList.get(i).getStatus());
        pay_status = mListenerList.get(i).getPayStatus();
        isOnline = mListenerList.get(i).getIs_online();


        Log.d("AAAAAAAAA", "getView: " + mListenerList.get(i).getStatus() + "   " + pay_status);



//     String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//     String app_date = mListenerList.get(i).getDate();
//        int date = app_date.compareTo(currentDate);
//        Log.d("DATE", "getView: "+date);

//        String valid_until = app_date;
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        Date strDate = null;
//        try {
//            strDate = sdf.parse(valid_until);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if(Calendar.getInstance().after(strDate)) {
//            past_date = "1";
//        }
//        else if (Calendar.getInstance().before(strDate))
//        {
//            past_date = "-1";
//        }
//        else if (new Date().equals(strDate))
//        {
//            past_date = "0";
//        }
//        else {
//            past_date = "";
//        }


//        switch (date) {
//            case -1: //date1<date2 = -1
//                 past_date = "-1";
//                 break;
////                binding.tvPaidclass.setText(mContext.getString(R.string.Confirm));
////                binding.actionLayout.setVisibility(View.VISIBLE);
////                binding.tvComplete.setVisibility(View.VISIBLE);
////                binding.tvComplete.setText("Complete");
////                binding.tvCancel.setVisibility(View.VISIBLE);
////                binding.tvRate.setVisibility(View.GONE);
////                binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
//            case 1: //date1>date2 = 1
//                past_date = "1";
//                break;
////                binding.tvPaidclass.setText(mContext.getString(R.string.Confirm));
////                binding.actionLayout.setVisibility(View.VISIBLE);
////                binding.tvComplete.setVisibility(View.VISIBLE);
////                binding.tvComplete.setText("Join Class");
////                binding.tvCancel.setVisibility(View.VISIBLE);
////                binding.tvRate.setVisibility(View.GONE);
////                binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
//            case 0: //date1==date2= 0
//                past_date = "0";
//                break;
////                binding.tvPaidclass.setText(mContext.getString(R.string.Confirm));
////                binding.actionLayout.setVisibility(View.VISIBLE);
////                binding.tvComplete.setVisibility(View.VISIBLE);
////                binding.tvComplete.setText("same date");
////                binding.tvCancel.setVisibility(View.VISIBLE);
////                binding.tvRate.setVisibility(View.GONE);
////                binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
//             default:
//                 past_date="";
////                        binding.tvPaidclass.setText(mContext.getString(R.string.Confirm));
////                        binding.actionLayout.setVisibility(View.VISIBLE);
////                        binding.tvComplete.setVisibility(View.VISIBLE);
////                        binding.tvComplete.setText("default");
////                        binding.tvCancel.setVisibility(View.VISIBLE);
////                        binding.tvRate.setVisibility(View.GONE);
////                        binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
//
//        }







//        String fee = mListenerList.get(i).getStatus();
//
//        Pattern mPattern = Pattern.compile("\\s^([0-9])");
//
//        Matcher matcher = mPattern.matcher(fee);
//        if(matcher.find())
//        {
//            String amount = matcher.group();
//            Log.d("CCSSSSS", "getView: "+amount);
//        }
//
//        Log.d("CCSSSSS", "getView: "+matcher+"   "+mPattern);


//
//
//        String fee = mListenerList.get(i).getStatus();
//        String[] numbers = fee.split(" ");
//        for (String s : numbers) {
//            Log.d("CCSSS", "getView: "+s);
//        }


        binding.classInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        Glide.with(mContext)
                .load(mListenerList.get(i).getUser_pic())
                .into(binding.imProfile);

        appoint_id = mListenerList.get(i).getAppointid();
        if (status.equals("Cancel")) {
            binding.tvPaidclass.setText(mContext.getString(R.string.Canceled));
            binding.tvPaidclass.setTextColor(mContext.getResources().getColor(R.color.oreange));
            binding.actionLayout.setVisibility(View.GONE);
            binding.borderlayout.setBackgroundResource(R.drawable.red_rectangle_border);
        } else if (status.equals("Completed") && ratingstatus.equals("Rated")) {
            binding.tvPaidclass.setText(mContext.getString(R.string.Completed));
            binding.tvPaidclass.setTextColor(mContext.getResources().getColor(R.color.green));
            binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
            binding.actionLayout.setVisibility(View.GONE);
        } else if (status.equals("Unconfirm")) {
            binding.tvPaidclass.setText(mContext.getString(R.string.uconfirm));

            binding.actionLayout.setVisibility(View.VISIBLE);
            binding.tvComplete.setVisibility(View.INVISIBLE);
            binding.tvCancel.setVisibility(View.VISIBLE);
            binding.tvRate.setVisibility(View.GONE);
            binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
            binding.tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                    adb.setTitle(mContext.getString(R.string.askforcancel));
                    adb.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Reasonsforcancellation();
                            final Dialog dialog1 = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
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
        } else if (status.equals("Completed") && ratingstatus.equals("Not Rated")) {
            binding.actionLayout.setVisibility(View.VISIBLE);
            binding.tvComplete.setVisibility(View.INVISIBLE);
            binding.tvCancel.setVisibility(View.INVISIBLE);
            binding.tvRate.setVisibility(View.VISIBLE);
            binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
            binding.tvRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                    dialog.getWindow().setGravity(Gravity.BOTTOM);
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.frag_rating_dialog);
                    dialog.show();
                    AlertClass.hideKeybord(mContext, view);
                    ImageView imback = dialog.findViewById(R.id.im_back);
                    imback.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                    final MaterialRatingBar ratingBar = dialog.findViewById(R.id.rating);
                    final EditText et_msg = dialog.findViewById(R.id.et_msg);
                    TextView btn_submit = dialog.findViewById(R.id.tv_send);
                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            appoint_id = mListenerList.get(i).getAppointid();
                            f = ratingBar.getRating();
                            str_msg = et_msg.getText().toString();
                            user_id = user.get(userSessionManager.KEY_USERID);
                            str_teacherid = mListenerList.get(i).getTeacher_id();
                            if (f == 0.0) {
                                Toast.makeText(mContext, mContext.getString(R.string.giverate), Toast.LENGTH_SHORT).show();
                            } else if (str_msg.equals("")) {
                                Toast.makeText(mContext, mContext.getString(R.string.nomsg), Toast.LENGTH_SHORT).show();
                            } else {
                                ratingtask();
                                dialog.cancel();
                            }

                        }
                    });
                }
            });
        } else if (status.equals("Confirm") && pay_status.equals("1")) {
            binding.tvPaidclass.setText(mContext.getString(R.string.Confirm));
            binding.actionLayout.setVisibility(View.VISIBLE);
            binding.tvComplete.setVisibility(View.VISIBLE);
            binding.tvComplete.setText("Pay");
            binding.tvCancel.setVisibility(View.VISIBLE);
            binding.tvRate.setVisibility(View.GONE);
            binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
            binding.tvComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("Ppopopo", "onClick: " + i);
                    fragment.onItemClick(mListenerList.get(i).getAppointid(), mListenerList.get(i).getStatus(), mListenerList.get(i).getSubject());

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

        } else if (status.equals("Confirm") && pay_status.equals("2")) {
            if (isOnline.equals("1")) {
//
////                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
////
////                int date = mListenerList.get(i).getDate().compareTo(currentDate);
////                Log.d("DATE", "getView: "+date);
////                switch (date) {
////                    case -1: //date1<date2 = -1
////                        binding.tvPaidclass.setText(mContext.getString(R.string.Confirm));
////                        binding.actionLayout.setVisibility(View.VISIBLE);
////                        binding.tvComplete.setVisibility(View.VISIBLE);
////                        binding.tvComplete.setText("Complete");
////                        binding.tvCancel.setVisibility(View.VISIBLE);
////                        binding.tvRate.setVisibility(View.GONE);
////                        binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
////                    case 1: //date1>date2 = 1
////                        binding.tvPaidclass.setText(mContext.getString(R.string.Confirm));
////                        binding.actionLayout.setVisibility(View.VISIBLE);
////                        binding.tvComplete.setVisibility(View.VISIBLE);
////                        binding.tvComplete.setText("Join Class");
////                        binding.tvCancel.setVisibility(View.VISIBLE);
////                        binding.tvRate.setVisibility(View.GONE);
////                        binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
////                    case 0: //date1==date2= 0
////                        binding.tvPaidclass.setText(mContext.getString(R.string.Confirm));
////                        binding.actionLayout.setVisibility(View.VISIBLE);
////                        binding.tvComplete.setVisibility(View.VISIBLE);
////                        binding.tvComplete.setText("same date");
////                        binding.tvCancel.setVisibility(View.VISIBLE);
////                        binding.tvRate.setVisibility(View.GONE);
////                        binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
//////                    default:
//////                        binding.tvPaidclass.setText(mContext.getString(R.string.Confirm));
//////                        binding.actionLayout.setVisibility(View.VISIBLE);
//////                        binding.tvComplete.setVisibility(View.VISIBLE);
//////                        binding.tvComplete.setText("default");
//////                        binding.tvCancel.setVisibility(View.VISIBLE);
//////                        binding.tvRate.setVisibility(View.GONE);
//////                        binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
////
////                }
//
////                if(past_date.equals("-1")) {
////
////                    binding.tvPaidclass.setText(mContext.getString(R.string.Confirm));
////                    binding.actionLayout.setVisibility(View.VISIBLE);
////                    binding.tvComplete.setVisibility(View.VISIBLE);
////                    binding.tvComplete.setText("Complete");
////                    binding.tvCancel.setVisibility(View.VISIBLE);
////                    binding.tvRate.setVisibility(View.GONE);
////                    binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
////
////                    binding.tvComplete.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View view) {
////
////                            androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(mContext);
////                            adb.setTitle(mContext.getString(R.string.askforcomplete));
////                            adb.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
////                                public void onClick(DialogInterface dialog, int which) {
////                                    appoint_id = mListenerList.get(i).getAppointid();
////                                    //  status ="Complete";
////                                    ConfirmOperation();
////                                    dialog.cancel();
////                                }
////                            });
////                            adb.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
////                                public void onClick(DialogInterface dialog, int which) {
////                                    dialog.cancel();
////                                }
////                            });
////                            adb.show();
////
////                        }
////                    });
////
////
////
////
////                    binding.tvCancel.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View view) {
////                            androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(mContext);
////                            adb.setTitle(mContext.getString(R.string.askforcancel));
////                            adb.setIcon(android.R.drawable.ic_dialog_alert);
////                            adb.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
////                                public void onClick(DialogInterface dialog, int which) {
////                                    dialog.cancel();
////                                    Reasonsforcancellation();
////                                    final Dialog dialog1 = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
////                                    dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
////                                    dialog1.getWindow().setGravity(Gravity.BOTTOM);
////                                    dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
////                                    dialog1.setContentView(R.layout.cancel_reason_dialog);
////                                    dialog1.show();
////                                    final EditText et_msg = dialog1.findViewById(R.id.et_msg);
////                                    TextView btn_submit = dialog1.findViewById(R.id.tv_send);
////                                    ImageView back = dialog1.findViewById(R.id.im_back);
////                                    back.setOnClickListener(new View.OnClickListener() {
////                                        @Override
////                                        public void onClick(View view) {
////                                            dialog1.cancel();
////                                        }
////                                    });
////                                    respon_spn = dialog1.findViewById(R.id.user);
////                                    respon_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////                                        @Override
////                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                                            UserType type = resonlist.get(i);
////                                            selectedreson = type.getId().toString();
////                                        }
////
////                                        @Override
////                                        public void onNothingSelected(AdapterView<?> adapterView) {
////
////                                        }
////                                    });
////                                    btn_submit.setOnClickListener(new View.OnClickListener() {
////                                        @Override
////                                        public void onClick(View view) {
////                                            str_msg = et_msg.getText().toString();
////                                            appoint_id = mListenerList.get(i).getAppointid();
////                                            str_appointid = appoint_id;
////                                            if (str_msg.equals("")) {
////                                                Toast.makeText(mContext, mContext.getString(R.string.enterreason), Toast.LENGTH_SHORT).show();
////                                            } else {
////                                                AlertClass.hideKeybord(mContext, view);
////                                                CancelReson();
////                                                dialog1.cancel();
////                                            }
////                                        }
////                                    });
////                                }
////                            });
////                            adb.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
////                                public void onClick(DialogInterface dialog, int which) {
////                                    dialog.cancel();
////                                }
////                            });
////                            adb.show();
////                        }
////                    });
////
////                }
////                else if(past_date.equals("1")){
//
//
//                binding.tvPaidclass.setText(mContext.getString(R.string.Confirm));
//                binding.actionLayout.setVisibility(View.VISIBLE);
//                binding.tvComplete.setVisibility(View.VISIBLE);
//                binding.tvComplete.setText("Join Class");
//                binding.tvCancel.setVisibility(View.VISIBLE);
//                binding.tvRate.setVisibility(View.GONE);
//                binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
//                binding.tvComplete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        roomId = mListenerList.get(i).getRoom_id();
//
////                        Intent i = new Intent(mContext, JitsiActivity.class);
////                        mContext.startActivity(i);
//
//                        // Initialize default options for Jitsi Meet conferences.
//                        URL serverURL;
//                        try {
//                            serverURL = new URL("https://meet.jit.si");
//                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
//                            throw new RuntimeException("Invalid server URL!");
//                        }
//                        JitsiMeetConferenceOptions defaultOptions
//                                = new JitsiMeetConferenceOptions.Builder()
//                                .setServerURL(serverURL)
//                                .setWelcomePageEnabled(false)
//                                .build();
//                        JitsiMeet.setDefaultConferenceOptions(defaultOptions);
//
//                        String text = roomId;
//
//                        if (text.length() > 0) {
//                            // Build options object for joining the conference. The SDK will merge the default
//                            // one we set earlier and this one when joining.
//                            JitsiMeetConferenceOptions options
//                                    = new JitsiMeetConferenceOptions.Builder()
//                                    .setRoom(text)
//                                    .build();
//                            // Launch the new activity with the given options. The launch() method takes care
//                            // of creating the required Intent and passing the options.
//                            JitsiMeetActivity.launch(mContext, options);
//
//                        }
//
//
//
//                    }
//                });
//
//
//                binding.tvCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(mContext);
//                        adb.setTitle(mContext.getString(R.string.askforcancel));
//                        adb.setIcon(android.R.drawable.ic_dialog_alert);
//                        adb.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                                Reasonsforcancellation();
//                                final Dialog dialog1 = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
//                                dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
//                                dialog1.getWindow().setGravity(Gravity.BOTTOM);
//                                dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//                                dialog1.setContentView(R.layout.cancel_reason_dialog);
//                                dialog1.show();
//                                final EditText et_msg = dialog1.findViewById(R.id.et_msg);
//                                TextView btn_submit = dialog1.findViewById(R.id.tv_send);
//                                ImageView back = dialog1.findViewById(R.id.im_back);
//                                back.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        dialog1.cancel();
//                                    }
//                                });
//                                respon_spn = dialog1.findViewById(R.id.user);
//                                respon_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                    @Override
//                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                        UserType type = resonlist.get(i);
//                                        selectedreson = type.getId().toString();
//                                    }
//
//                                    @Override
//                                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                                    }
//                                });
//                                btn_submit.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        str_msg = et_msg.getText().toString();
//                                        appoint_id = mListenerList.get(i).getAppointid();
//                                        str_appointid = appoint_id;
//                                        if (str_msg.equals("")) {
//                                            Toast.makeText(mContext, mContext.getString(R.string.enterreason), Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            AlertClass.hideKeybord(mContext, view);
//                                            CancelReson();
//                                            dialog1.cancel();
//                                        }
//                                    }
//                                });
//                            }
//                        });
//                        adb.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                        adb.show();
//                    }
//                });


                binding.tvJoinClass.setVisibility(View.VISIBLE);
                binding.tvJoinClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        roomId = mListenerList.get(i).getRoom_id();

                        try {
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

                            String text = roomId;

                            if (text.length() > 0) {
                                // Build options object for joining the conference. The SDK will merge the default
                                // one we set earlier and this one when joining.
                                JitsiMeetConferenceOptions options
                                        = new JitsiMeetConferenceOptions.Builder()
                                        .setRoom(text)
                                        .setFeatureFlag("call-integration.enabled", false)
                                        .build();
                                // Launch the new activity with the given options. The launch() method takes care
                                // of creating the required Intent and passing the options.
                                JitsiMeetActivity.launch(mContext, options);

                            }

                        }catch (Exception e)
                        {

                        }

                    }
                });


            }
                binding.tvPaidclass.setText(mContext.getString(R.string.Confirm));
                binding.actionLayout.setVisibility(View.VISIBLE);
                binding.tvComplete.setVisibility(View.VISIBLE);
                binding.tvComplete.setText("Complete");
                binding.tvCancel.setVisibility(View.VISIBLE);
                binding.tvRate.setVisibility(View.GONE);
                binding.borderlayout.setBackgroundResource(R.drawable.recangle_shape);
                binding.tvComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                        adb.setTitle(mContext.getString(R.string.askforcomplete));
                        adb.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                appoint_id = mListenerList.get(i).getAppointid();
                                //  status ="Complete";
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

        return binding.getRoot();
    }


    private void ratingtask() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherRatingURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRRR", "classinfoadapter  :  response: " + response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                if (flag.equals("true")) {
                                    fragment.clearlist();
                                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                                    ab.setMessage(msg);
                                    ab.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            fragment.setdata();
                                            dialog.cancel();
                                        }
                                    });
                                    AlertDialog alert = ab.create();
                                    alert.show();
                                } else {
                                    AlertClass.alertDialogShow(mContext, msg);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "classinfoadapter  :  exception: " + e);
                            AlertClass.alertDialogShow(mContext, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "classinfoadapter  :  error: " + error);
                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appointment_id", appoint_id);
                params.put("student_id", str_user_id);
                params.put("teacher_id", str_teacherid);
                params.put("teacher_rating", String.valueOf(f));
                params.put("rating_feedback", str_msg);
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public void ConfirmOperation() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherConfimationURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "classinfoadapter  :  response: " + response);
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
                                        //fragment.AllAppointmentlist();
                                        fragment.setdata();
                                    }
                                });
                                AlertDialog alert = ab.create();
                                alert.show();

                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(mContext, msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "classinfoadapter  :  exception: " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                params.put("status", "Completed");
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }

    public void CancelOperation() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherConfimationURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "classinfoadapter  :  response: " + response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            final String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                fragment.clearlist();
                                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                                ab.setMessage(msg);
                                ab.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        //fragment.AllAppointmentlist();
                                        fragment.setdata();
                                    }
                                });
                                AlertDialog alert = ab.create();
                                alert.show();
                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(mContext, msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "classinfoadapter  :  exception: " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                params.put("status", "Cancel");
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
                        dialog.cancel();
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "classinfoadapter  :  response: " + response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                cancel_id = str_appointid;
                                status = "Cancel";
                                CancelOperation();
                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(mContext, msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "classinfoadapter  :  exception: " + e);
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
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.CancelReasonURL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        resonlist = new ArrayList<>();
                        try {
                            Log.d("RRRRR", "classinfoadapter  :  response: " + response);
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
                            Log.d("RRRRR", "classinfoadapter  :  exception: " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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


}

