package com.infowindtech.eguru;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.Forgotpassword1Binding;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends Fragment {
    Forgotpassword1Binding binding;
    String email,msg,langcode;
    UserSessionManager userSessionManager;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding=Forgotpassword1Binding.inflate(inflater,container,false);
        userSessionManager = new UserSessionManager(getActivity());
        HashMap<String,String> lang=userSessionManager.getLanguage();
        langcode=lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(),langcode);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        binding.imBack.setVisibility(View.VISIBLE);

        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login_Fragment frag=new Login_Fragment();
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.forgot_pwd,frag).commit();
            }
        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               email=binding.etEmail.getText().toString().trim();
                if(email.equals("")){
                    binding.etEmail.setError(getString(R.string.enteremailid));
                }else if(!isValidEmail(email)){
                    binding.etEmail.setError(getString(R.string.entervalidemail));
                }else {
                    Forgotpasswordtask();
                }
            }
        });
        return binding.getRoot();
    }
    private void Forgotpasswordtask() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.ForgotPasswordURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRRR", "forgetPassword:  response "+response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                msg = obj.getString("message").toString();
                                if (flag.equals("true")) {
                                    AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                                    ab.setMessage(getString(R.string.passwordsent));
                                    ab.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent in = new Intent(getActivity(),LoginActivity.class);
                                            startActivity(in);
                                            dialog.cancel();
                                            binding.etEmail.setText("");
                                        }
                                    });
                                    AlertDialog alert = ab.create();
                                    alert.show();
                                }
                                else {
                                    AlertClass.alertDialogShow(getActivity(), msg);
                                }
                            }
                        }
                        catch (Exception e) {
                            Log.d("RRRRR", "forgetPassword :  exception: "+e);
                            AlertClass.alertDialogShow(getActivity(), getString(R.string.unableresponse));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RRRRR", "forgetPassword :  error: "+error);
                        Toast.makeText(getActivity(),  getString(R.string.unableresponse), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("lang_code",langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
