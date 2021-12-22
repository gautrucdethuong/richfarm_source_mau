package com.infowindtech.eguru.StudentPanel.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.infowindtech.eguru.LoginActivity;
import com.infowindtech.eguru.MainActivity;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.SplashActivity;
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.StudentPanel.UpdateCommonActivity;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.FragSetting1Binding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Setting_Fragment extends Fragment {
    FragSetting1Binding binding;
    UserSessionManager userSessionManager;
    String user_id, profile, name, langcode;
    String opass, npass, cpass, UserId, appointmentcount, user_type;
    UserSessionManager session;
    Dialog dialog1;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin, savelang;
    private SharedPreferences.Editor loginPrefsEditor;
    RadioButton lang_en, lang_ar, lang_guest;
    TextView btn_ok, btn_cancel, tv_chose_lang;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragSetting1Binding.inflate(inflater, container, false);
        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        savelang = loginPreferences.getBoolean("saveLang", false);
        loginPrefsEditor = loginPreferences.edit();

        binding.imDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    ((MainActivity)getActivity()).callDrawer();

                }catch (Exception ex){
                    ((StudentHomeActivity)getActivity()).callDrawer();
                }

            }
        });


        binding.tvHeading.setText(getString(R.string.Settings));
        userSessionManager = new UserSessionManager(getActivity());
        if (userSessionManager.signIn())
            getActivity().finish();
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(), langcode);
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        profile = user.get(userSessionManager.KEY_PROFILE);
        name = user.get(userSessionManager.KEY_FULLNAME);
        user_type = user.get(userSessionManager.KEY_USERTYPE);
        HashMap<String, String> homecount = userSessionManager.getHomeScreen();
        appointmentcount = homecount.get(userSessionManager.KEY_HomeScreenCount);
        binding.tvName.setText(toTitleCase(name));

        Glide.with(getActivity()).load(profile)
                .thumbnail(0.5f)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imProfile);

        binding.relPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), UpdateCommonActivity.class);
                startActivity(in);
            }
        });

        binding.relChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1 = new Dialog(getActivity());
                dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                dialog1.getWindow().setGravity(Gravity.BOTTOM);
                dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.change_password_dialog1);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                dialog1.getWindow().setLayout(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.MATCH_PARENT);
                dialog1.show();
                final EditText etCurrentpass = dialog1.findViewById(R.id.et_currentpass);
                final EditText etNewpass = dialog1.findViewById(R.id.et_newpass);
                final EditText etConfirmpass = dialog1.findViewById(R.id.et_confirmpass);
                Button btn_submit = dialog1.findViewById(R.id.btn_submit);
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        opass = etCurrentpass.getText().toString().trim();
                        npass = etNewpass.getText().toString().trim();
                        cpass = etConfirmpass.getText().toString().trim();
                        session = new UserSessionManager(getActivity());
                        if (opass.equals("")) {
                            etCurrentpass.setError(getString(R.string.entercurrentpass));
                        } else if (npass.equals("")) {
                            etNewpass.setError(getString(R.string.enternewpass));
                        } else if (cpass.equals("")) {
                            etConfirmpass.setError(getString(R.string.enterconfirmpass));
                        } else if (!npass.equals(cpass)) {
                            AlertClass.alertDialogShow(getActivity(), getString(R.string.passnotmatched));
                        } else {
                            if (session.signIn())
                                getActivity().finish();
                            HashMap<String, String> user = session.getUserDetails();
                            UserId = user.get(UserSessionManager.KEY_USERID);
                            ChangePasswordTask();
                        }

                        // Toast.makeText(getContext(),"Change Password functionality is not available",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

//        binding.relLanguage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Dialog dialog = new Dialog(getActivity());
//                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
//                dialog.setContentView(R.layout.language_dialog);
//                dialog.show();
//                dialog.setTitle(R.string.chooselang);
//                lang_en = dialog.findViewById(R.id.english);
//                lang_ar = dialog.findViewById(R.id.arabic);
//                lang_guest = dialog.findViewById(R.id.guest);
//                btn_ok = dialog.findViewById(R.id.tv_ok);
//                btn_cancel = dialog.findViewById(R.id.tv_cancel);
//                tv_chose_lang = dialog.findViewById(R.id.tv_chose_lang);
//                lang_guest.setVisibility(View.GONE);
//                tv_chose_lang.setText(R.string.chooselang);
//                lang_en.setText("English");
//                lang_ar.setText("Arabic");
//                if (langcode.equals("en")) {
//                    lang_en.setVisibility(View.GONE);
//                } else {
//                    lang_ar.setVisibility(View.GONE);
//                }
//                btn_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.cancel();
//                    }
//                });
//                btn_ok.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (lang_en.isChecked()) {
//                            langcode = "en";
//                            userSessionManager.selectedlanguage("en");
//                            setLocale("en");
//                            loginPrefsEditor.putBoolean("saveLang", true);
//                            loginPrefsEditor.putString("lang", langcode);
//                            loginPrefsEditor.commit();
//                            dialog.cancel();
//                            Intent in = new Intent(getActivity(), SplashActivity.class);
//                            startActivity(in);
//                        } else if (lang_ar.isChecked()) {
//                            langcode = "en";
//                            userSessionManager.selectedlanguage("en");
//                            setLocale("ar");
//                            loginPrefsEditor.putBoolean("saveLang", true);
//                            loginPrefsEditor.putString("lang", langcode);
//                            loginPrefsEditor.commit();
//                            dialog.cancel();
//                            Intent in = new Intent(getActivity(), SplashActivity.class);
//                            startActivity(in);
//                        } else {
//                            Toast.makeText(getActivity(), getString(R.string.chooselang), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
        return binding.getRoot();
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        conf.setLayoutDirection(new Locale(lang));
        res.updateConfiguration(conf, dm);
    }

    private void ChangePasswordTask() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.pleasewait));
        pDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.ChangePasswordURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRRRR", "setting fragment  :  onResponse: "+response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String str_msg = obj.getString("message");
                                if (flag.equals("true")) {
                                    dialog1.cancel();
                                    pDialog.hide();
                                    AlertClass.alertDialogShow(getActivity(), str_msg);
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    AlertClass.alertDialogShow(getActivity(), str_msg);
                                    pDialog.hide();
                                }
                            }
                        } catch (Exception e) {
                            Log.d("RRRRRR", "setting fragment  :  exception: "+e);
                            AlertClass.alertDialogShow(getActivity(), getString(R.string.unableresponse));
                            pDialog.hide();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRRR", "setting fragment  :  error: "+error);
                        Toast.makeText(getActivity(), getString(R.string.unableresponse), Toast.LENGTH_LONG).show();
                        pDialog.hide();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("old_password", opass);
                params.put("new_password", npass);
                params.put("lang_code", langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}


