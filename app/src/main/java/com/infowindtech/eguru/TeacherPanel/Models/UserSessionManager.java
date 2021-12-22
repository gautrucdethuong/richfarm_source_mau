package com.infowindtech.eguru.TeacherPanel.Models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.infowindtech.eguru.LoginActivity;

import org.json.JSONArray;

import java.util.HashMap;

public class UserSessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = "EGuru";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_PAYMENT_ID = "payment_id";
    public static final String KEY_USERID = "userid";
    public static final String KEY_USERTYPE = "usertype";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_PHONENUMBER = "phone";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_ZIPCODE = "zipcode";
    public static final String KEY_CLASSES = "classes";
    public static final String KEY_SUBJECT = "subjects";
    public static final String KEY_PROFILE = "profile";
    public static final String KEY_SubId = "sub_id";
    public static final String KEY_INSTITUTE = "institute";
    public static final String KEY_EDUCATION = "education";
    public static final String KEY_EXPERIENCE = "exp";
    public static final String KEY_TIME_SLOT = "timeslote";
    public static final String KEY_APPOINTMENT_DUR = "appointmentdur";
    public static final String KEY_IM_ID_COPY = "idc";
    public static final String KEY_IM_CERTIFICATE = "degree";
    public static final String KEY_MORNING = "morning";
    public static final String KEY_AFTRNOON = "afternoon";
    public static final String KEY_EVENING = "evening";
    public static final String KEY_DAYS = "days";
    public static final String KEY_TEACHINGPLACE = "teachplace";
    public static final String KEY_FEES = "fees";
    public static final String KEY_GENDER_CHOICE = "choice";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_DOB = "dob";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_PASSINGYEAR = "passingyear";
    public static final String KEY_TEACHERPLACEID = "teaching_place_id";
    public static final String KEY_Tagline = "tagline";
    public static final String KEY_GenderChoiceName = "choicename";
    public static final String KEY_USERPASS = "password";
    public static final String KEY_CLASSId = "classesid";
    public static final String KEY_Nation = "nation";
    public static final String KEY_HomeScreenCount = "count";
    public static final String KEY_Activation = "active";
    public static final JSONArray jsonarraydate = new JSONArray();
    public static final String KEY_ArrayList = "arraylist";
    public static final String KEY_Lang = "lang";
    public static final String KEY_Currency_Code = "cc";
    public static final String KEY_Future_Appointment = "futureAppointment";


    public static final Boolean KEY_CHECK = false;

    public UserSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void selectedlanguage(String lang) {
        editor.putString(KEY_Lang, lang);
        editor.commit();
    }
    public HashMap<String, String> getLanguage() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_Lang, pref.getString(KEY_Lang, null));
        return user;
    }

    public void reglogindata(String username, String password) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USERPASS, password);
        editor.commit();
    }

    public HashMap<String, String> getregDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_USERPASS, pref.getString(KEY_USERPASS, null));
        return user;
    }

    public void createUserLoginSession(String id, String type, String username, String email, String phone, String fullname, String address, String city, String country, String zipcode, String subject, String classes, String profile, String subid
            , String institute, String education, String exp, String timeslote, String duration, String idcopy, String certificate, String morning, String afternoon
            , String evening, String days, String teachingplace, String fees, String genderchoice, String latitude, String longitude, String dob, String gender, String passingyear, String teacherplaceid, String tagline, String choicename, String classid, String nation, String activation, String cc, String paypalEmail, String payment_id) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_USERID, id);
        editor.putString(KEY_USERTYPE, type);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONENUMBER, phone);
        editor.putString(KEY_FULLNAME, fullname);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_COUNTRY, country);
        editor.putString(KEY_ZIPCODE, zipcode);
        editor.putString(KEY_SUBJECT, subject);
        editor.putString(KEY_CLASSES, classes);
        editor.putString(KEY_PROFILE, profile);
        editor.putString(KEY_SubId, subid);
        editor.putString(KEY_INSTITUTE, institute);
        editor.putString(KEY_EDUCATION, education);
        editor.putString(KEY_EXPERIENCE, exp);
        editor.putString(KEY_TIME_SLOT, timeslote);
        editor.putString(KEY_APPOINTMENT_DUR, duration);
        editor.putString(KEY_IM_ID_COPY, idcopy);
        editor.putString(KEY_IM_CERTIFICATE, certificate);
        editor.putString(KEY_MORNING, morning);
        editor.putString(KEY_AFTRNOON, afternoon);
        editor.putString(KEY_EVENING, evening);
        editor.putString(KEY_DAYS, days);
        editor.putString(KEY_TEACHINGPLACE, teachingplace);
        editor.putString(KEY_FEES, fees);
        editor.putString(KEY_GENDER_CHOICE, genderchoice);
        editor.putString(KEY_LATITUDE, latitude);
        editor.putString(KEY_LONGITUDE, longitude);
        editor.putString(KEY_DOB, dob);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_PASSINGYEAR, passingyear);
        editor.putString(KEY_TEACHERPLACEID, teacherplaceid);
        editor.putString(KEY_Tagline, tagline);
        editor.putString(KEY_GenderChoiceName, choicename);
        editor.putString(KEY_CLASSId, classid);
        editor.putString(KEY_Nation, nation);
        editor.putString(KEY_Activation, activation);
        editor.putString(KEY_Currency_Code, cc);
        editor.putString(KEY_ACCOUNT, paypalEmail);
        editor.putString(KEY_PAYMENT_ID,payment_id);

        editor.commit();
    }

    public void createHomeScreen(String count) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_HomeScreenCount, count);
        editor.commit();
    }

    public void createAppointmentCalender(JSONArray jsonarraydate) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_ArrayList, jsonarraydate.toString());
        editor.commit();
    }

    public HashMap<String, String> getAppointmentCalender() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ArrayList, pref.getString(KEY_ArrayList, null));
        return user;
    }

    public HashMap<String, String> getAccountInfo() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ACCOUNT, pref.getString(KEY_ACCOUNT, null));
        user.put(KEY_PAYMENT_ID, pref.getString(KEY_PAYMENT_ID, null));
        return user;
    }

    public HashMap<String, String> getHomeScreen() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_HomeScreenCount, pref.getString(KEY_HomeScreenCount, null));
        return user;
    }

    public void UpdatecommonSession(String phone, String fullname, String address, String city, String country, String zipcode, String classes, String subid
            , String latitude, String longitude, String dob, String gender, String subject, String classid, String nation) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_PHONENUMBER, phone);
        editor.putString(KEY_FULLNAME, fullname);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_COUNTRY, country);
        editor.putString(KEY_ZIPCODE, zipcode);
        editor.putString(KEY_CLASSES, classes);
        editor.putString(KEY_SubId, subid);
        editor.putString(KEY_LATITUDE, latitude);
        editor.putString(KEY_LONGITUDE, longitude);
        editor.putString(KEY_DOB, dob);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_SUBJECT, subject);
        editor.putString(KEY_CLASSId, classid);
        editor.putString(KEY_Nation, nation);
        editor.commit();
    }

    public void UpdateTeacherNextScreenData(String genderchoice, String days, String morning, String afternoon, String evening, String place
            , String duration, String fees, String idcard, String certificate, String institute, String qualication, String passing, String experience, String choicename, String teacherplaceid, String activation, String cc) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_GENDER_CHOICE, genderchoice);
        editor.putString(KEY_DAYS, days);
        editor.putString(KEY_MORNING, morning);
        editor.putString(KEY_AFTRNOON, afternoon);
        editor.putString(KEY_EVENING, evening);
        editor.putString(KEY_TEACHINGPLACE, place);
        editor.putString(KEY_APPOINTMENT_DUR, duration);
        editor.putString(KEY_FEES, fees);
        editor.putString(KEY_IM_ID_COPY, idcard);
        editor.putString(KEY_IM_CERTIFICATE, certificate);
        editor.putString(KEY_INSTITUTE, institute);
        editor.putString(KEY_EDUCATION, qualication);
        editor.putString(KEY_PASSINGYEAR, passing);
        editor.putString(KEY_EXPERIENCE, experience);
        editor.putString(KEY_GenderChoiceName, choicename);
        editor.putString(KEY_TEACHERPLACEID, teacherplaceid);
        editor.putString(KEY_Activation, activation);
        editor.putString(KEY_Currency_Code, cc);
        editor.commit();
    }

    public void updateProfilePic(String profile) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_PROFILE, profile);
        editor.commit();
    }

    public void updateAccount(String account,String payment_id) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_ACCOUNT, account);
        editor.putString(KEY_PAYMENT_ID, payment_id);
        editor.commit();
    }


    public void updateTagline(String tagline) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_Tagline, tagline);
        editor.commit();
    }



    public boolean signIn() {
        if (!this.isUserLoggedIn()) {
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
            return true;
        }
        return false;
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));
        user.put(KEY_USERTYPE, pref.getString(KEY_USERTYPE, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_FULLNAME, pref.getString(KEY_FULLNAME, null));
        user.put(KEY_PHONENUMBER, pref.getString(KEY_PHONENUMBER, null));
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));
        user.put(KEY_CITY, pref.getString(KEY_CITY, null));
        user.put(KEY_COUNTRY, pref.getString(KEY_COUNTRY, null));
        user.put(KEY_CLASSES, pref.getString(KEY_CLASSES, null));
        user.put(KEY_SUBJECT, pref.getString(KEY_SUBJECT, null));
        user.put(KEY_ZIPCODE, pref.getString(KEY_ZIPCODE, null));
        user.put(KEY_PROFILE, pref.getString(KEY_PROFILE, null));
        user.put(KEY_SubId, pref.getString(KEY_SubId, null));
        user.put(KEY_INSTITUTE, pref.getString(KEY_INSTITUTE, null));
        user.put(KEY_EDUCATION, pref.getString(KEY_EDUCATION, null));
        user.put(KEY_EXPERIENCE, pref.getString(KEY_EXPERIENCE, null));
        user.put(KEY_TIME_SLOT, pref.getString(KEY_TIME_SLOT, null));
        user.put(KEY_APPOINTMENT_DUR, pref.getString(KEY_APPOINTMENT_DUR, null));
        user.put(KEY_IM_ID_COPY, pref.getString(KEY_IM_ID_COPY, null));
        user.put(KEY_IM_CERTIFICATE, pref.getString(KEY_IM_CERTIFICATE, null));
        user.put(KEY_MORNING, pref.getString(KEY_MORNING, null));
        user.put(KEY_AFTRNOON, pref.getString(KEY_AFTRNOON, null));
        user.put(KEY_EVENING, pref.getString(KEY_EVENING, null));
        user.put(KEY_DAYS, pref.getString(KEY_DAYS, null));
        user.put(KEY_TEACHINGPLACE, pref.getString(KEY_TEACHINGPLACE, null));
        user.put(KEY_FEES, pref.getString(KEY_FEES, null));
        user.put(KEY_GENDER_CHOICE, pref.getString(KEY_GENDER_CHOICE, null));
        user.put(KEY_LATITUDE, pref.getString(KEY_LATITUDE, null));
        user.put(KEY_LONGITUDE, pref.getString(KEY_LONGITUDE, null));
        user.put(KEY_DOB, pref.getString(KEY_DOB, null));
        user.put(KEY_GENDER, pref.getString(KEY_GENDER, null));
        user.put(KEY_PASSINGYEAR, pref.getString(KEY_PASSINGYEAR, null));
        user.put(KEY_TEACHERPLACEID, pref.getString(KEY_TEACHERPLACEID, null));
        user.put(KEY_Tagline, pref.getString(KEY_Tagline, null));
        user.put(KEY_GenderChoiceName, pref.getString(KEY_GenderChoiceName, null));
        user.put(KEY_CLASSId, pref.getString(KEY_CLASSId, null));
        user.put(KEY_Nation, pref.getString(KEY_Nation, null));
        user.put(KEY_Activation, pref.getString(KEY_Activation, null));
        user.put(KEY_Currency_Code, pref.getString(KEY_Currency_Code, null));
        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
