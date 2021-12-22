package com.infowindtech.eguru.TeacherPanel.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.infowindtech.eguru.MainActivity;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.databinding.FragTeacherdrawerProfile1Binding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Teacher_Profile_Fragment extends Fragment{
    FragTeacherdrawerProfile1Binding binding;
    UserSessionManager userSessionManager;
    String user_id,phone,email,usertype,address,city,country,zipcode,classes,subjects,name,profile,tagline,days,morning,afternoon,evening,appointmentcount;
    String dayst,morningt,afternoont,eveningt,langcode;
    EditText et_msg;
     ProgressDialog pdialog ;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragTeacherdrawerProfile1Binding.inflate(inflater,container,false);
        binding.tvHeading.setText(getString(R.string.Profile));
        userSessionManager = new UserSessionManager(getActivity());

       // pdialog = new ProgressDialog(getContext());

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

        if (userSessionManager.signIn())
            getActivity().finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id=user.get(userSessionManager.KEY_USERID);
        name=user.get(userSessionManager.KEY_FULLNAME);
        phone=user.get(userSessionManager.KEY_PHONENUMBER);
        email=user.get(userSessionManager.KEY_EMAIL);
        usertype=user.get(userSessionManager.KEY_USERTYPE);
        address=user.get(userSessionManager.KEY_ADDRESS);
        city=user.get(userSessionManager.KEY_CITY);
        country=user.get(userSessionManager.KEY_COUNTRY);
        zipcode=user.get(userSessionManager.KEY_ZIPCODE);
        classes=user.get(userSessionManager.KEY_CLASSES);
        subjects=user.get(userSessionManager.KEY_SUBJECT);
        profile=user.get(userSessionManager.KEY_PROFILE);
        tagline=user.get(userSessionManager.KEY_Tagline);
        dayst=user.get(userSessionManager.KEY_DAYS);
        morningt=user.get(userSessionManager.KEY_MORNING);
        afternoont=user.get(userSessionManager.KEY_AFTRNOON);
        eveningt=user.get(userSessionManager.KEY_EVENING);
        HashMap<String,String> lang=userSessionManager.getLanguage();
        langcode=lang.get(userSessionManager.KEY_Lang);
        Log.d("AAAAA", "onCreate:www "+subjects+" "+classes+"    "+dayst);
        AlertClass.setLocale(getActivity(),langcode);
        if(dayst.contains("Mon") || dayst.contains("Tue") || dayst.contains("Wed") || dayst.contains("Thu") || dayst.contains("Fri") || dayst.contains("Sat") || dayst.contains("Sun")){
            days=dayst.replace("Mon",getString(R.string.Mon));
            days=days.replace("Tue",getString(R.string.Tue));
            days=days.replace("Wed",getString(R.string.Wed));
            days=days.replace("Thu",getString(R.string.Thu));
            days=days.replace("Fri",getString(R.string.Fri));
            days=days.replace("Sat",getString(R.string.Sat));
            days=days.replace("Sun",getString(R.string.Sun));
        }else{
            days=dayst;
        }
        morning=morningt;
        afternoon=afternoont;
        evening=eveningt;
       /* if(morningt.contains("Morning")){
            morning=morningt.replace("Morning",getString(R.string.Morning));
            afternoon=afternoont.replace("Afternoon",getString(R.string.Aftrnoon));
            evening=eveningt.replace("Evening",getString(R.string.Evening));
        }else{
            morning=morningt;
            afternoon=afternoont;
            evening=eveningt;
        }*/
        binding.tvName.setText(toTitleCase(name));
        binding.tvEmail.setText(email);
        binding.tvPhone.setText(phone);
        binding.tvUserName.setText(toTitleCase(name));
        binding.tvEmail1.setText(email);
        binding.tvMobile.setText(phone);
        binding.tvDays.setText(days);
        binding.tvTime.setText(morning+"\n"+afternoon+"\n"+evening);
        binding.tvSubjects.setText(subjects);
        binding.tvClasses.setText(classes);
        if(usertype.equals("1")){
            binding.tvType.setText(getString(R.string.teacher));
        }else{
            binding.tvType.setText(getString(R.string.student));
        }
        binding.tvAddress.setText(address);
        final HashMap<String, String> homecount = userSessionManager.getHomeScreen();
        appointmentcount = homecount.get(userSessionManager.KEY_HomeScreenCount);
        if(tagline.equals("")){
        }else{
            binding.tvTagline.setText(tagline);
        }
        Glide.with(getActivity()).load(profile)
                .thumbnail(0.5f)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imProfile);
           binding.editTagline.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Dialog dialog1 = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
                dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                //dialog1.getWindow().setGravity(Gravity.BOTTOM);
                //dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.tagline_dialog);
                dialog1.show();
                AlertClass.hideKeybord(getActivity(),view);
                et_msg = dialog1.findViewById(R.id.et_msg);
                et_msg.setText(tagline);
                TextView btn_submit = dialog1.findViewById(R.id.tv_send);
                TextView tv_heading= dialog1.findViewById(R.id.tv_heading);
                ImageView imback = dialog1.findViewById(R.id.im_back);
                tv_heading.setText(getString(R.string.Tagline));
                imback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.cancel();
                    }
                });
                tagline=et_msg.getText().toString().trim();
                btn_submit.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {


                        AlertClass.hideKeybord(getActivity(),view);
                        tagline=et_msg.getText().toString().trim();
                        user_id=user.get(userSessionManager.KEY_USERID);
                        if(tagline.equals("")){
                           // pdialog.dismiss();
                            Toast.makeText(getActivity(), getString(R.string.entertagline), Toast.LENGTH_SHORT).show();
                        }else{
                            dialog1.cancel();
                            EditTagline();
                        }
                    }
                });
            }
        });
        return binding.getRoot();
    }
    private void EditTagline(){
//        pdialog.setMessage("Loading....");
//        pdialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.EditTaglineURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("RRRRR", "teacherprofilefragment: response"+response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                if (flag.equals("true")) {

                                    AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                                    ab.setMessage(msg);
                                    ab.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            userSessionManager.updateTagline(tagline);
                                            binding.tvTagline.setText(tagline);

                                        }
                                    });
                                   // pdialog.dismiss();
                                    AlertDialog alert = ab.create();
                                    alert.show();
                                } else {
                                   // pdialog.dismiss();
                                    AlertClass.alertDialogShow(getActivity(), msg);
                                }
                            }
                        } catch (Exception e){
                          //  pdialog.dismiss();
                            Log.d("RRRRR", "teacherprofilefragment: exception"+e);

                            AlertClass.alertDialogShow(getActivity(), e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // pdialog.dismiss();
                        Log.d("RRRRR", "teacherprofilefragment: error"+error);

                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
            {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id);
                params.put("user_tag_line",tagline);
                params.put("lang_code",langcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
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
